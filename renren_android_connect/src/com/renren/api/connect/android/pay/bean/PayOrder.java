package com.renren.api.connect.android.pay.bean;

import java.util.Date;

import com.renren.api.connect.android.Util;

/**
 * 返回给开发者的订单对象，本地存储用的订单对象，跟人人服务器交互的对象
 * <p>
 * 该bean保留了服务器传回来的所有参数  在支付成功后该类会作为参数回调开发者 开发者可以获得这笔订单的任何信息
 * <p>
 * 作为开发者只需要关心orderNumber和payment是否正确 订单状态是否是success
 * <p>
 * 跟Payment不一样
 * 
 * @see Payment
 * 
 */
public class PayOrder {

	public static final int START_STATE = 0x00;
	public static final int ERROR_STATE = 0x01;
	public static final int SUCCESS_STATE = 0x02;
	/**
	 * appId
	 */
	private String appId = "";
	/**
	 * 订单号
	 */
	private String orderNumber = "";
	/**
	 * 开发者自定义字段，服务器原样返回
	 */
	private String payment = "";
	private boolean sandBox = false;
	private String redirect_url = "";
	private int serverState = START_STATE;

	/**
	 * 订单流水号，服务器生成
	 */
	private String bid = "";
	/**
	 * 消费用户的id
	 */
	private long userId = 0L;
	/**
	 * 消费金额
	 */
	private int amount = 0;

	/**
	 * 提交订单的时间
	 */
	private Date orderTime = new Date();

	/**
	 * 成交的时间
	 */
	private Date dealTime = new Date();

	/**
	 * 描述
	 */
	private String descr = "";
	/**
	 * 修复订单相关参数,验证使用
	 */
	private String payResultEncode = "";
	/**
	 * 修复订单相关参数
	 */
	private int payStatusCode = 0;

	// 判断订单状态是否是已经成功
	public boolean isSuccess() {
		return this.serverState == SUCCESS_STATE;
	}
	public String getLocalEncode() {
		return Util.md5(getAppId() + getOrderNumber()
				+ getPayResultEncode() + getAmount()
				+ getUserId());
	}
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		if (appId != null)
			this.appId = appId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		if (orderNumber != null)
			this.orderNumber = orderNumber;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		if (payment != null)
			this.payment = payment;
	}

	public int getServerState() {
		return serverState;
	}

	public void setServerState(int serverState) {
		this.serverState = serverState;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		if (bid != null)
			this.bid = bid;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		if (userId > 0)
			this.userId = userId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		if (orderTime != null)
			this.orderTime = orderTime;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		if (dealTime != null)
			this.dealTime = dealTime;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		if (descr != null)
			this.descr = descr;
	}

	public PayOrder() {
	}

	public PayOrder(String appId, long userId, Payment p) {
		Date orderTime = new Date();
		this.appId = appId;
		this.userId = userId;
		this.orderNumber = p.getOrderNumber();
		this.amount = p.getAmount();
		this.orderTime = orderTime;
		this.descr = p.getDescription();
		this.serverState = START_STATE;

	}

	public PayOrder(PayOrder payOrder) {
		this.appId = payOrder.getAppId();
		this.userId = payOrder.getUserId();
		this.orderNumber = payOrder.getOrderNumber();
		this.amount = payOrder.getAmount();
		this.orderTime = payOrder.getOrderTime();
		this.descr = payOrder.getDescr();
		this.dealTime = payOrder.getDealTime();
		this.bid = payOrder.getBid();
		this.serverState = payOrder.getServerState();
		this.payResultEncode = payOrder.getPayResultEncode();
		this.payment = payOrder.getPayment();
		this.payStatusCode = payOrder.getPayStatusCode();

	}

	public void setPayState(int payState) {
		this.serverState = payState;
	}

	public void setPayStatusCode(int payStatusCode) {
		this.payStatusCode = payStatusCode;
	}

	public int getPayStatusCode() {
		return payStatusCode;
	}

	public void setPayResultEncode(String payEncode) {
		this.payResultEncode = payEncode;
	}

	public String getPayResultEncode() {
		return payResultEncode;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setSandBox(boolean sandBox) {
		this.sandBox = sandBox;
	}

	public boolean isSandBox() {
		return sandBox;
	}

}
