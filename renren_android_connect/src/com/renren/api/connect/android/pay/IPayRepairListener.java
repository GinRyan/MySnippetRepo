package com.renren.api.connect.android.pay;

import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.pay.bean.PayOrder;
/**
 * 给修复订单回调使用的listener
 */
public interface IPayRepairListener {
	
	/**
	 * 修复订单成功时调用的方法 处理逻辑可以跟 {@link IPayListener#onComplete(PayOrder)} 一样
	 */
	public void onRepairComplete(PayOrder order);

	/**
	 * 修复订单失败时调用 处理逻辑可以跟 {@link IPayListener#onError(RenrenError) } 一样
	 * @param error
	 */
	public void onRepairError(RenrenError error);
}
