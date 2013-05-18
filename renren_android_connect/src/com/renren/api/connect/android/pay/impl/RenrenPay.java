package com.renren.api.connect.android.pay.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.RequestListener;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.pay.IPayListener;
import com.renren.api.connect.android.pay.IPayRepairListener;
import com.renren.api.connect.android.pay.IRenrenPay;
import com.renren.api.connect.android.pay.bean.AppState;
import com.renren.api.connect.android.pay.bean.PayOrder;
import com.renren.api.connect.android.pay.bean.Payment;
import com.renren.api.connect.android.pay.util.PayStoreHelper;
import com.renren.api.connect.android.pay.util.PayUtil;
import com.renren.api.connect.android.pay.view.PayDialogListener;
import com.renren.api.connect.android.view.RenrenAuthListener;
import com.renren.api.connect.android.view.RenrenDialog;

/**
 * 主要职责是接受数据、检查参数、检查App。 将打开Dialog。进行消费。 消费信息的返回值处理全部交给
 * {@link PayDialogListener}
 */
public class RenrenPay implements IRenrenPay {

	private static RenrenPay instance;
	private static PayStoreHelper payStoreHelper;
	// 线程池，目前仅作异步调用网络使用，以后可能还有别的用处
	private Executor mPool = Executors.newFixedThreadPool(2);

	private Renren mRenren = null;
	private AppState mAppState = null;
	private boolean isInitalized = false;
	private boolean is4Test = false;
	private IPayRepairListener repairListener;

	@Override
	public String generateOrderNumber() {
		StringBuilder sb = new StringBuilder();
		sb.append('2');
		sb.append(mRenren.getAppId().substring(0, 4));
		Format format = new SimpleDateFormat("yyMMddHHmm");
		sb.append(format.format(new Date()));
		sb.append(String.format("%04d", new Random().nextInt(9999)));
		return sb.toString();
	}

	/**
	 * 查询app的状态，如果通过审核则可以调用下一步。 每次创建pay对象时会调用一遍。 一般来说调用pay4Test接口不会受appState的限制
	 * 
	 * @return
	 */
	private boolean asynCheckApp() {
		isInitalized = false;
		Bundle params = new Bundle();
		params.putString("app_id", mRenren.getAppId());
		String time = String.valueOf(new Date().getTime());
		params.putString("time", time);
		params.putString("app_encode", PayUtil.getAppCode(mRenren.getAppId(),
				"", mRenren.getSecret(), time));
		RequestListener l = new RequestListener() {
			@Override
			public void onFault(Throwable e) {
				// DO NOTHING
			}

			@Override
			public void onComplete(String resString) {
				// 这里可以认为是线程安全的，因为主线程并不涉及写操作，只是可能单纯的在读
				mAppState = PayUtil.parseAppState(resString);
				isInitalized = true;
			}

			@Override
			public void onRenrenError(RenrenError renrenError) {
				// DO NOTHING
			}
		};
		PayUtil.request(mPool, params, PayUtil.PAY_CHECK_APP_URL, l);
		return true;
	}

	/**
	 * 设置Renren和IPayRepairListener，通过调用这个方法可以主动的修改renren对象跟IPayRepairListener
	 */
	@Override
	public void init(Renren renren, IPayRepairListener repairListener) {
		if (null == renren) {
			throw new RuntimeException("实例化过的renren对象必须提供");
		}
		if (null == repairListener) {
			this.enableStore(false);
			// throw new RuntimeException("实例化过的IPayRepairListener对象必须提供");
		}
		mRenren = renren;
		this.repairListener = repairListener;

	}

	/**
	 * 读取本地存储的订单，供修复订单使用
	 * 
	 * @param c
	 * @return
	 */
	public List<PayOrder> getStoredPayOrders(Context c) {

		return payStoreHelper.getPayOrder(Integer.parseInt(mRenren.getAppId()),
				(int) mRenren.getCurrentUid());
	}

	@Override
	public void removeAllLocalInfo(Context context) {
		payStoreHelper.removeAllByUid((int) mRenren.getCurrentUid());
	}

	/**
	 * 初始化本地存储
	 * 
	 * @param Context
	 */
	private void initLocalStore(Context c) {
		payStoreHelper = PayStoreHelper.getInstance(c);
	}

	private RenrenPay(Context context, Renren renren,
			IPayRepairListener listener) {
		if (null == context || null == renren || null == listener) {
			throw new RuntimeException(
					"context|renren|IPayRepairListener cannot be null!!");
		}
		initLocalStore(context);
		init(renren, listener);
		asynCheckApp();
	}

	/**
	 * 是否完成初始化
	 */
	public boolean isInitalized() {
		return isInitalized;
	}

	private boolean checkLogin(final Activity act, final RenrenAuthListener ll) {
		if (!mRenren.isAccessTokenValid()) {
			mRenren.authorize(act, ll);
			return false;
		}
		return true;
	}

	@Override
	public boolean beginPay4Test(final Activity act, final Payment payment) {
		is4Test = true;
		return pay(act, payment);
	}

	@Override
	public boolean beginPay(final Activity act, final Payment payment) {
		is4Test = false;
		return pay(act, payment);
	}

	/**
	 * 支付。之前涉及登录验证并尝试让用户登录 先设置登录的listener
	 * 
	 * @param act
	 * @param payment
	 * @return
	 */
	private boolean pay(final Activity act, final Payment payment) {
		if (null == payment) {
			throw new RuntimeException("实例化过的payment对象必须提供");
		}
		if (null == payment.getPayListener()) {
			throw new RuntimeException("实例化过的Payment.IPayListener对象必须提供");
		}
		final IPayListener l = payment.getPayListener();
		RenrenAuthListener authListener = new RenrenAuthListener() {
			@Override
			public void onComplete(Bundle values) {
				openDialogForPay(act, payment);
			}

			@Override
			public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
				l.onError(new RenrenError(RenrenError.ERROR_CODE_AUTH_FAILED,
						renrenAuthError.getErrorDescription(), renrenAuthError
								.getErrorUri()));
			}

			@Override
			public void onCancelLogin() {
				l.onError(new RenrenError(
						RenrenError.ERROR_CODE_AUTH_CANCELLED, "user_cancel",
						null));
			}

			@Override
			public void onCancelAuth(Bundle values) {
				l.onError(new RenrenError(
						RenrenError.ERROR_CODE_AUTH_CANCELLED,
						"user_cancel_auth", null));
			}
		};
		if (checkLogin(act, authListener)) {
			openDialogForPay(act, payment);
		}
		return true;

	}

	/**
	 * 打开webView，之后的所有返回值、判断都交给RRPayDialogListener来处理了 该方法只调用onStart。
	 * 该方法中根据是否初始化，app是否检查过.
	 * 
	 * @param context
	 */
	private void openDialogForPay(Context context, Payment payment) {
		if (!this.is4Test && this.isInitalized
				&& mAppState.getStatusCode() != AppState.OK) {
			// 如果初始化了，但是没有通过审核。而且还不是测试状态
			// 就没必要请求服务器了
			payment.getPayListener().onError(
					new RenrenError("app未通过审核，请使用Test方法或提交审核"));
			return;
		}
		/**
		 * 生成PayOrder 对象
		 */
		PayOrder payOrder = new PayOrder(mRenren.getAppId(),
				mRenren.getCurrentUid(), payment);
		String url = null;
		if (!this.is4Test) {
			url = PayUtil.PAY_SUBMIT_ORDER_URL;
			payOrder.setSandBox(false);
		} else {
			url = PayUtil.PAY_4TEST_URL;
			payOrder.setSandBox(true);
		}
		String data = PayUtil.generateOrderDatas(mRenren.getAppId(),
				mRenren.getSecret(), mRenren.getAccessToken(),
				payment.getOrderNumber(), payment.getAmount(),
				payment.getDescription(), payment.getPayment());
		RenrenDialog dialog = new RenrenDialog(context, url, data,
				new PayDialogListener(payOrder, payment.getPayListener()));
		payStoreHelper.addOrUpdatePay(payOrder);
		payment.getPayListener().onStart(payment);
		dialog.show();
	}

	/**
	 * 提供修复订单功能
	 * 
	 * @param activity
	 * @param order
	 */
	@Override
	public void repairOrder(final Context context, final PayOrder order) {
		this.is4Test = false;
		this.openDialogForRepair(context, order, this.repairListener);
	}
	/**
	 * 提供修复订单功能
	 * 
	 * @param activity
	 * @param order
	 */
	@Override
	public void repairOrder4Test(final Context context, final PayOrder order) {
		this.is4Test = true;
		this.openDialogForRepair(context, order, this.repairListener);
	}
	/**
	 * 打开修复订单的WebView。
	 * 
	 * @param context
	 */
	private void openDialogForRepair(Context context, PayOrder payOrder,
			IPayRepairListener listener) {
		String fixOrderUrl = null;
		// 如果不是test 状态，设置url
		if (!this.is4Test) {
			fixOrderUrl = PayUtil.PAY_FIXORDER_URL;
		} else {
			fixOrderUrl = PayUtil.PAY_FIXORDER_4TEST_URL;
		}
		String data = PayUtil.generateRepairDatas(payOrder.getBid(), payOrder
				.getAppId(), payOrder.getOrderNumber(), payOrder.getAmount(),
				mRenren.getSecret(), mRenren.getAccessToken(), payOrder
						.getUserId(), payOrder.getOrderTime().getTime());
		RenrenDialog dialog = new RenrenDialog(context, fixOrderUrl, data,
				new PayDialogListener(payOrder, listener));
		dialog.show();
		payStoreHelper.addOrUpdatePay(payOrder);
	}

	/**
	 * 请确保初始化之后才使用该方法
	 * 
	 * @return
	 */
	public static IRenrenPay getInstance() throws RuntimeException {
		if (null == instance) {
			throw new RuntimeException("IRenrenPay未实例化");
		}
		return instance;
	}

	/**
	 * 单例方法
	 * 
	 * @param Context
	 * @param Renren
	 * @param IPayRepairListener
	 *            初始化时请提供RepairListener
	 * @return
	 */
	public static synchronized IRenrenPay getInstance(Context c, Renren r,
			IPayRepairListener l) {
		if (null == instance) {
			instance = new RenrenPay(c, r, l);
		} else {
			instance.init(r, l);
		}
		return instance;
	}

	@Override
	public void enableStore(boolean enableStore) {
		if (null != payStoreHelper) {
			payStoreHelper.enableLocalStore(enableStore);
		}

	}

	@Override
	public void removeOrderByOrderNumber(String orderNumber) {
		// TODO Auto-generated method stub
		if (null != orderNumber && null != payStoreHelper) {
			payStoreHelper.removeByUidAndOrderNumber(
					(int) this.mRenren.getCurrentUid(), orderNumber);
		}
	}

}
