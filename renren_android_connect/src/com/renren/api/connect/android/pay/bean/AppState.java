package com.renren.api.connect.android.pay.bean;

public class AppState {
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDes() {
		return statusDes;
	}
	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}
	private int statusCode = -1;
	private String statusDes = "";
	public static final int UNKNOWN = -1;
	public static final int OK = 100;
	public static final int APP_NOT_EXIST = 201;//app不存在
	public static final int APP_AUDIT_FAIL = 202;//app未通过审核
	public static final int APP_NOT_SUPPORT_PAY = 203;//app未开通支付
	public static final int APP_MAINTAIN = 204;//app维护
	public static final int APP_PAY_CHECK_FAIL = 205;//app身份认证失败
	public static final int APP_SERVER_CONNECT_FAIL = 206;//app服务端连接失败
	public static final int APP_SERVER_ORDERNO_FETCH_FAIL = 207;//app服务端回调获取订单号返回格式错误
	public static final int APP_SERVER_CALL_SUC = 208;//app服务端回调成功,返回数据正确
	public static final int APP_CALL_SERVER_DATA_FAIL = 209;//回调app服务端返回数据格式不正确
	public static final int APP_ORDER_MONEY_EXCEED= 210;//超过app单笔订单金额上限
	public static final int APP_CALL_DATA_FORMA_ERROR = 211;//app发送数据格式不正确
	public static final int APP_ORDERNO_FORMAT_ERROR = 212;//不正确的订单号
	public static final int APP_ORDERNO_REPEAT = 213;//重复的订单号
	public static final int APP_ORDER_AMOUNT_ZERO = 214;//订单金额为0

	AppState(int code, String des) {
		this.statusCode = code;
		this.statusDes = des;
	}
	@Override
	public String toString()
	{
		return ""+statusCode+statusDes+"";
	}
	public AppState(int code) {
		this.statusCode = code;
	}
}