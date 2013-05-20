package com.renren.api.connect.android.pay.view;

import java.util.Date;

import android.os.Bundle;
import android.util.Log;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.pay.IPayListener;
import com.renren.api.connect.android.pay.IPayRepairListener;
import com.renren.api.connect.android.pay.bean.PayOrder;
import com.renren.api.connect.android.pay.util.PayStoreHelper;
import com.renren.api.connect.android.pay.util.PayUtil;
import com.renren.api.connect.android.view.RenrenDialogListener;

/**
 * 负责处理webView的返回值。 解析错误或者支付成功的信息
 */
public class PayDialogListener implements RenrenDialogListener {

	private PayOrder mPayOrder;
	/*
	 * 根据Listener的类型调用不同的方法 实现的不是太好，有待优化
	 */
	private Object mListener;

	/**
	 * 这里实现用Object
	 * 
	 * @param payOrder
	 * @param listener
	 */
	public PayDialogListener(final PayOrder payOrder, final Object listener) {
		mPayOrder = payOrder;
		mListener = listener;
	}

	/**
	 * 处理错误信息
	 * 
	 * @param url
	 * @return
	 */
	private void DoWithError(String url) {
		Bundle value = PayUtil.parseUrl(url);
		RenrenError error = new RenrenError();
		if (value.containsKey("code")) {
			error = new RenrenError(Integer.parseInt(value.getString("code")),
					value.getString("description"), null);
		}
		/**
		 * 解析并本地存储
		 */
		generateAndStorePayOrder(value);
		if (null != mListener)
			if (mListener instanceof IPayListener) {
				((IPayListener) mListener).onError(error);
			} else if (mListener instanceof IPayRepairListener) {
				((IPayRepairListener) mListener).onRepairError(error);
			}
	}

	private void DoWithLocalError(String url) {
		RenrenError error = new RenrenError();
		Bundle value = PayUtil.parseUrl(url);
		if (value.containsKey("code")) {
			error = new RenrenError(RenrenError.ERROR_CODE_UNKNOWN_ERROR,
					value.getString("code"), null);
		}
		/**
		 * 解析并本地存储
		 */
		generateAndStorePayOrder(value);
		if (null != mListener)
			if (mListener instanceof IPayListener) {
				((IPayListener) mListener).onError(error);
			} else if (mListener instanceof IPayRepairListener) {
				((IPayRepairListener) mListener).onRepairError(error);
			}
	}

	/**
	 * 生成并且存储订单
	 * 
	 * @param value
	 * @return
	 */
	private PayOrder generateAndStorePayOrder(Bundle value) {
		PayOrder order = new PayOrder(mPayOrder);
		if (value.containsKey("app_id")) {
			order.setAppId(value.getString("app_id"));
		}
		if (value.containsKey("amount")) {
			order.setAmount(Integer.parseInt(value.getString("amount")));
		}
		if (value.containsKey("order_number")) {
			order.setOrderNumber(value.getString("order_number"));
		}
		if (value.containsKey("orderedTime")) {
			try {
				order.setDealTime(new Date(Long.parseLong(value
						.getString("orderedTime"))));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		if (value.containsKey("descr")) {
			order.setDescr(value.getString("descr"));
		}
		if (value.containsKey("payment")) {
			order.setPayment(value.getString("payment"));
		}
		if (value.containsKey("bid")) {
			order.setBid(value.getString("bid"));
		}
		if (value.containsKey("payResultEncode")) {
			order.setPayResultEncode(value.getString("payResultEncode"));
		}
		if (value.containsKey("payStatusCode")) {
			order.setPayStatusCode(Integer.parseInt(value
					.getString("payStatusCode")));
		}
		if (value.containsKey("sandBox")) {
			order.setSandBox(Boolean.parseBoolean(value.getString("sandBox")));
		}
		if (value.containsKey("serverStatus")) {
			if (value.getString("serverStatus").trim().equals("订单成功")) {
				order.setPayState(PayOrder.SUCCESS_STATE);
			} else {
				order.setPayState(PayOrder.ERROR_STATE);
			}
		}

		PayStoreHelper.getInstance(null).addOrUpdatePay(order);
		return order;
	}

	/**
	 * 处理rrpay://success的情况
	 * 
	 * @param url
	 */
	private void DoWithComplete(String url) {
		Log.e("success", url);
		Bundle value = PayUtil.parseUrl(url);
		PayOrder order = generateAndStorePayOrder(value);
		/**
		 * 通知开发者OnComplete
		 */
		if (null != mListener)
			if (mListener instanceof IPayListener) {
				((IPayListener) mListener).onComplete(order);
			} else if (mListener instanceof IPayRepairListener) {
				((IPayRepairListener) mListener).onRepairComplete(order);
			}
	}

	@Override
	public int onPageBegin(String url) {
		int retCode = RenrenDialogListener.ACTION_UNPROCCESS;
		// 如果是pay://开头则认为是接受处理结果。可以返回了。
		if (url != null && url.startsWith(PayUtil.PAY_URL_PREFIX)) {
			Log.e("onPageBegin", url);
			retCode = RenrenDialogListener.ACTION_PROCCESSED;
			// 充值正确的情况
			if (url.startsWith(PayUtil.PAY_SUC_URL_PREFIX)||url.startsWith(PayUtil.PAY_FIXORDER_SUC_URL_PREFIX)) {
				DoWithComplete(url);
			}
			// 出错的情况
			else if (url.startsWith(PayUtil.PAY_ERROR_URL_PREFIX)||url.startsWith(PayUtil.PAY_FIXORDER_ERROR_URL_PREFIX)) {
				DoWithError(url);
			} else {
				Util.logger("new PayError()");
				RenrenError error = new RenrenError(
						RenrenError.ERROR_CODE_UNKNOWN_ERROR, "PAY_FIXORDER_ERROR_URL_PREFIX", "");
				if (null != mListener)
					if (mListener instanceof IPayListener) {
						((IPayListener) mListener).onError(error);
					} else if (mListener instanceof IPayRepairListener) {
						((IPayRepairListener) mListener).onRepairError(error);
					}
			}
		}
		if (url != null && url.startsWith(Renren.DEFAULT_REDIRECT_URI)) {
			Util.logger("DoWithLocalError");
			DoWithLocalError(url);
		}
		return retCode;
	}

	@Override
	public void onPageFinished(String url) {
	}

	@Override
	public boolean onPageStart(String url) {
		return false;
	}

	@Override
	public void onReceivedError(int errorCode, String description,
			String failingUrl) {
		RenrenError error = new RenrenError(errorCode, description, failingUrl);
		if (null != mListener)
			if (mListener instanceof IPayListener) {
				((IPayListener) mListener).onError(error);
			} else if (mListener instanceof IPayRepairListener) {
				((IPayRepairListener) mListener).onRepairError(error);
			}

	}

};