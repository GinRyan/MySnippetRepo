package com.renren.api.connect.android.pay.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.Executor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.renren.api.connect.android.RequestListener;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.pay.bean.AppState;

/**
 * 工具类 定义url等常量 提供拼装请求参数、解析参数方法
 * 
 */
public final class PayUtil {
	/** url 相关 **/
	public static String PAY_SUBMIT_ORDER_URL = "https://graph.renren.com/spay/android/submitOrder";
	public static String PAY_4TEST_URL = "https://graph.renren.com/spay/android/test/submitOrder";
	public static String PAY_FIXORDER_URL = "https://graph.renren.com/spay/android/fixOrder";
	public static String PAY_FIXORDER_4TEST_URL = "https://graph.renren.com/spay/android/test/fixOrder";
	public static String PAY_CHECK_APP_URL = "https://graph.renren.com/spay/appStatus";

	/** 服务端重定向的url前缀 **/
	public static String PAY_URL_PREFIX = "rrpay://";
	public static String PAY_ERROR_URL_PREFIX = "rrpay://error";
	public static String PAY_SUC_URL_PREFIX = "rrpay://success";
	public static String PAY_FIXORDER_ERROR_URL_PREFIX = "rrpay://repairerror";
	public static String PAY_FIXORDER_SUC_URL_PREFIX = "rrpay://repairsuccess";

	/** DB 相关 **/

	/**
	 * 生成组装支付订单的信息 发送post请求时使用
	 */
	public static String generateOrderDatas(String appId, String appSecert,
			String accessToken, String orderNumber, int amount, String desc,
			String payment) {
		Bundle params = new Bundle();
		String timeString = String.valueOf(new Date().getTime());

		params.putString("app_id", appId);
		params.putString("access_token", accessToken);
		params.putString("order_number", orderNumber);
		params.putString("descr", desc);
		params.putString("payment", payment);
		params.putString("amount", String.valueOf(amount));
		params.putString("app_encode",
				getAppCode(appId, orderNumber, appSecert, timeString));
		params.putString("submitTime", timeString);
		return Util.encodeUrl(params);
	}

	/**
	 * 生成组装修复订单的数据
	 * 
	 * @param bid
	 * @param appId
	 * @param orderNumber
	 * @param amount
	 * @param fixEncode
	 * @return
	 */
	public static String generateRepairDatas(String bid, String appId,
			String orderNumber, int amount, String appSecret,
			String accessToken, long userId, long orderTime) {
		Bundle params = new Bundle();
		params.putString("amount", String.valueOf(amount));
		params.putString("bid", bid);
		params.putString("access_token", accessToken);
		params.putString("app_id", appId);
		params.putString("user_id", String.valueOf(userId));
		params.putString("order_number", orderNumber);
		params.putString("fix_time", String.valueOf(orderTime));
		params.putString(
				"fix_encode",
				getRepairFixCode(userId, appId, orderNumber, amount, appSecret,
						orderTime));
		return Util.encodeUrl(params);

	}

	public static String getRepairFixCode(long userId, String appId,
			String orderNumber, int amount, String appSecret, long time) {
		return Util.md5("" + userId + appId + orderNumber + amount + time
				+ appSecret + "");
	}

	/**
	 * 返回调用pay服务器的验证字段
	 * 
	 * @param appId
	 * @param appSecert
	 * @param time
	 * @return
	 */
	public static String getAppCode(String appId, String orderNumber,
			String appSecert, String time) {
		return Util.md5("" + appId + orderNumber + time + appSecert + "");
	}

	/**
	 * 解析服务器返回的app状态
	 * 
	 * @param responseJSON
	 * @return
	 */
	public static AppState parseAppState(String responseJSON) {
		try {
			JSONObject json = new JSONObject(responseJSON);

			JSONArray errorCode = json.getJSONArray("payStatusCodes");
			Log.e("fff", String.valueOf(errorCode));
			if (errorCode != null && !errorCode.isNull(0))
				return new AppState(errorCode.getInt(0));
			else
				return new AppState(AppState.UNKNOWN);

		} catch (JSONException e) {
			return new AppState(AppState.UNKNOWN);
		}
	}

	public static Bundle parseUrl(String url) {
		url = url.replace(PayUtil.PAY_URL_PREFIX, "http://");
		url = url.replace("#", "?");
		try {
			URL u = new URL(url);
			Bundle b = Util.decodeUrl(u.getQuery());
			b.putAll(Util.decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}

	/**
	 * 将url中的错误信息 转换为pay Error
	 * 
	 * @param url
	 */
	public static RenrenError parsePayError(String url) {
		Bundle b = parseUrl(url);
		RenrenError eb = null;
		if (b.containsKey("error")) {
			eb = new RenrenError(Integer.parseInt(b.getString("error")),
					b.getString("description"), url);
		}
		return eb;
	}

	/**
	 * 异步请求服务器
	 * 
	 * @param parameters
	 * @param listener
	 * @param requestUri
	 */
	public static void request(final Executor pool, final Bundle parameters,
			final String requestUri, final RequestListener l) {
		pool.execute(new Runnable() {

			@Override
			public void run() {
				try {
					String resp = Util.openUrl(requestUri, "POST", parameters);
					RenrenError rrError = parsePayError(resp);
					if (rrError != null) {
						l.onRenrenError(rrError);
					} else {
						l.onComplete(resp);
					}
				} catch (Throwable e) {
					l.onFault(e);
				}
			}
		});
	}
}
