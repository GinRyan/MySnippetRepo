package com.renren.api.connect.android.pay;

import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.pay.bean.PayOrder;
import com.renren.api.connect.android.pay.bean.Payment;

/**
 * 支付功能的接口
 */
public interface IRenrenPay {
	/**
	 * 生成一个长20位的订单号。 生成订单号的规则： 
	 * <p>第一位 2.代表Android + appId的前四位 + 十位时间yyMMddHHmm +
	 * 随机四位数字
	 * @return String orderNumber 生成的订单号
	 */
	public String generateOrderNumber();
	/**
	 * 重新设置Renren类改变或者listener实例
	 * @param Renren
	 * @param IPayRepairListener
	 */
	public void init(Renren r,IPayRepairListener l);
	/**
	 * 开始支付，正常流程
	 * 测试请使用 {@link #beginPay4Test(Activity, Payment)}
	 * @param act 需要显示支付页面的activity
	 * @param payment 构建好的payment对象
	 * @see Payment
	 */
	public boolean beginPay(final Activity act, final Payment payment) ;
	/**
	 * 开始支付，测试流程
	 * 跟正常流程一样，但是不会扣除人人豆，开发者测试的时候使用，上线的时候请使用
	 * {@link #beginPay(Activity, Payment)}
	 * @param Activity
	 * @param payment
	 * @return
	 */
	public boolean beginPay4Test(final Activity act, final Payment payment);
	/**
	 * 修复订单
	 * @param context
	 * @param order
	 */
	public void repairOrder(final Context context,final PayOrder order);
	public void repairOrder4Test(Context context, PayOrder order);
	/**
	 * 是否使用本地存储，开发者可以关掉本地存储，也不会占储存空间
	 * @param enableStore
	 */
	public void enableStore(boolean enableStore);
	/**
	 * 按照orderNumber删除本地存储的订单
	 * @param orderNumber
	 */
	public void removeOrderByOrderNumber(final String orderNumber);
	/**
	 * 获取本地存储的订单
	 * @param c
	 * @return
	 */
	public List<PayOrder> getStoredPayOrders(final Context c);
	/**
	 * 删除所有本地存储的信息,只能删除当前用户的数据
	 * @param context
	 */
	public void removeAllLocalInfo(final Context context);
}
