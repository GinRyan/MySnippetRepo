package com.renren.api.connect.android.pay.bean;

import com.renren.api.connect.android.pay.IPayListener;

/**
 * <p>提交订单对象。给开发者使用。
 * <p>beginPay之前请先构建该对象。
 * <p>orderNumber字段一定要设置
 * <p>description字段会提示给用户
 * <p>payment字段是留给开发者设置的字段
 * 人人服务器会原封不动的传回来或者缀在回调应用服务器的参数后面
 * 供开发者验证确认是哪个订单使用
 * <p>iPayListener 务必要实例化该接口，否则支付成功后将没有任何方法通知您的程序
 * <p>返回订单使用 PayOrder对象
 * @see PayOrder *
 */
public class Payment {
	private IPayListener iPayListener;
	//这三个字段可以为空""但是不能为null
	private String payment = "";
	private String description = "";
	/**
	 * 订单号开发者可以不设置，这种情况下会去开发者设置的服务器地址上抓取一个订单号
	 * 为了兼容之前的人人豆的支付流程
	 * 目前详见一个比较旧的版本：http://wiki.dev.renren.com/wiki/%E6%A0%A1%E5%86%85%E8%B1%86
	 */
	private String orderNumber = "";
	private int amount = 0;
	/**
	 * 带参数的构造函数
	 * @param orderNumber
	 * @param amount
	 * @param description
	 * @param payment
	 * @param listner
	 */
	public Payment(String orderNumber,int amount,String description,String payment,IPayListener listner)
	{
		this.setAmount(amount);
		this.setDescription(description);
		this.setOrderNumber(orderNumber);
		this.setPayListener(listner);
		this.setPayment(payment);
	}
	public void setPayment(String payment) {
		if(payment != null)
		this.payment = payment;
	}

	public void setDescription(String description) {
		if(description != null)
		this.description = description;
	}

	public void setOrderNumber(String orderNumber) {
		if(orderNumber != null)
		this.orderNumber = orderNumber;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setPayListener(IPayListener iPayListener) {
		if(iPayListener != null)
		this.iPayListener = iPayListener;
	}

	public IPayListener getPayListener() {
		return iPayListener;
	}

	public int getAmount() {
		return amount;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public String getDescription() {
		return description;
	}

	public String getPayment() {
		return payment;
	}

	public void addListener(IPayListener listner) {
		setPayListener(listner);
	}
	//默认构造函数
	public Payment()
	{}
	
}
