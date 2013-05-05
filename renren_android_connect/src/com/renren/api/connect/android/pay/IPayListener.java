package com.renren.api.connect.android.pay;

import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.pay.bean.PayOrder;
import com.renren.api.connect.android.pay.bean.Payment;

public interface IPayListener {
	public void onStart(Payment o);

	public boolean onComplete(PayOrder o);

	public void onError(RenrenError error);
}
