package com.renren.api.connect.android.pay.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.renren.api.connect.android.R;
import com.renren.api.connect.android.pay.bean.PayOrder;
import com.renren.api.connect.android.pay.impl.RenrenPay;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PayRepairActivity extends Activity {
	ListView lv = null;
	Button repairBtn = null;
	Button removeBtn = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.renren_sdk_pay_repair);
		lv = (ListView) findViewById(R.id.renren_sdk_pay_repair_order_list);
		removeBtn = (Button) findViewById(R.id.renren_sdk_pay_repair_remove_all_button);
		removeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RenrenPay.getInstance().removeAllLocalInfo(PayRepairActivity.this);

			}
		});
		List<PayOrder> p = RenrenPay.getInstance().getStoredPayOrders(this);
		PayRepaiListAdapter adapter = new PayRepaiListAdapter( p);
		lv.setAdapter(adapter);
	}

}
