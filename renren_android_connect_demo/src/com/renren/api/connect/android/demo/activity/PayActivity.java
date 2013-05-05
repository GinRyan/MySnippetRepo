package com.renren.api.connect.android.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.renren.api.connect.android.demo.R;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.pay.IPayListener;
import com.renren.api.connect.android.pay.IPayRepairListener;
import com.renren.api.connect.android.pay.IRenrenPay;
import com.renren.api.connect.android.pay.bean.PayOrder;
import com.renren.api.connect.android.pay.bean.Payment;
import com.renren.api.connect.android.pay.view.PayRepairActivity;

public class PayActivity extends BaseActivity implements IPayRepairListener,
		IPayListener {
	private EditText amountText;
	private EditText desText;
	private EditText orderNumberText;
	private EditText paymentText;
	private Button repairBtn;
	private Button payBtn;
	// private Button lsBtn;
	private IRenrenPay renrenPay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout payLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.pay, null);
		// baseActivity参数设置
		root.addView(payLayout);
		titlebarText.setText(getString(R.string.renren_demo_pay_title));

		orderNumberText = (EditText) findViewById(R.id.order_number_edit_text);
		amountText = (EditText) findViewById(R.id.order_amount_edit_text);
		desText = (EditText) findViewById(R.id.order_des_edit_text);
		paymentText = (EditText) findViewById(R.id.order_payment_edit_text);
		payBtn = (Button) findViewById(R.id.payBtn);
		repairBtn = (Button) findViewById(R.id.repairBtn);

		// 生成IRenrenPay对象
		renrenPay = renren.getRenrenPay(getApplicationContext(), this);
		renrenPay.enableStore(true);

		// 使用IRenrenPay的方法生成订单号，开发者也可以自己设置
		orderNumberText.setText(renrenPay.generateOrderNumber());

		repairBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PayActivity.this,
						PayRepairActivity.class);
				startActivity(intent);
			}
		});
		payBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 构建一个Payment对象
				Payment p = new Payment();
				//Payment p = new Payment("228282828282332",5,"desc","payment",PayActivity.this);
				// 将自身设置为消费回调的Listener
				p.addListener(PayActivity.this);
				p.setAmount(Integer.parseInt(amountText.getText().toString()
						.trim()));
				p.setDescription(desText.getText().toString().trim());
				p.setPayment(paymentText.getText().toString().trim());
				String orderNumber = orderNumberText.getText().toString()
						.trim();
				p.setOrderNumber(orderNumber);
				// 开始消费
				renrenPay.beginPay4Test(PayActivity.this, p);

			}
		});

	}

	@Override
	public void onRepairComplete(PayOrder order) {
		onComplete(order);

	}

	@Override
	public void onRepairError(RenrenError error) {
		onError(error);
	}

	@Override
	public void onStart(Payment p) {
		showMessageByToast("开始支付流程 orderNumber=" + p.getOrderNumber());
	}

	@Override
	public boolean onComplete(PayOrder o) {
		showMessageByToast("购买成功！请发送商品！流水号 bid=" + o.getBid());
		return false;
	}

	@Override
	public void onError(RenrenError e) {
		showMessageByToast("发生错误 " + e.getErrorCode() + e.getMessage());
	}

	private void showMessageByToast(final String message) {
		Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PayActivity.this, message, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}