package com.renren.api.connect.android.pay.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.renren.api.connect.android.R;
import com.renren.api.connect.android.pay.bean.PayOrder;
import com.renren.api.connect.android.pay.impl.RenrenPay;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayRepaiListAdapter extends BaseAdapter {

	private ArrayList<RepairListItem> items = new ArrayList<RepairListItem>();

	@Override
	public int getCount() {
		if (items != null) {
			return items.size();
		}
		return 0;
	}

	public PayRepaiListAdapter(List<PayOrder> orders) {
		super();
		if (orders != null) {
			for (PayOrder o : orders) {
				items.add(new RepairListItem(o));
			}
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (items != null && position >= 0 && position < items.size()) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		if (items != null && position >= 0 && position < items.size()) {
			View itemView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.renren_sdk_pay_repair_item, null);
			final RepairListItem item = items.get(position);

			TextView orderNumView = (TextView) itemView
					.findViewById(R.id.renren_sdk_pay_repair_item_order_num);
			TextView desView = (TextView) itemView
					.findViewById(R.id.renren_sdk_pay_repair_item_des);
			TextView bidView = (TextView) itemView
					.findViewById(R.id.renren_sdk_pay_repair_item_bid);
			TextView timeView = (TextView) itemView
					.findViewById(R.id.renren_sdk_pay_repair_item_time);
			TextView amountView = (TextView) itemView
					.findViewById(R.id.renren_sdk_pay_repair_item_amount);
			TextView statusView = (TextView) itemView
					.findViewById(R.id.renren_sdk_pay_repair_item_status);
			Button repairBtn = (Button) itemView
					.findViewById(R.id.renren_sdk_pay_repair_item_repair_button);
			orderNumView.setText(item.getOrderNumber());
			desView.setText(item.getOrderDes());
			bidView.setText(item.getOrderBid());
			timeView.setText(item.getOrderTime());
			amountView.setText(item.getOrderAmount());
			statusView.setText(item.getOrderStatus());
			//如果订单失败，则设置红色醒目
			if(!item.getOrder().isSuccess())
			{
				statusView.setTextColor(Color.RED);
			}

			repairBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						RenrenPay.getInstance().repairOrder(parent.getContext(),
								item.getOrder());
					} catch (Exception e) {
						e.printStackTrace();// do nothing
					}
				}
			});
			convertView = itemView;

		}
		return convertView;
	}

	class RepairListItem {
		public String getOrderNumber() {
			return orderNumber;
		}

		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}

		public String getOrderDes() {
			return orderDes;
		}

		public void setOrderDes(String orderDes) {
			this.orderDes = orderDes;
		}

		public String getOrderBid() {
			return orderBid;
		}

		public void setOrderBid(String orderBid) {
			this.orderBid = orderBid;
		}

		public String getOrderAmount() {
			return orderAmount;
		}

		public void setOrderAmount(String orderAmount) {
			this.orderAmount = orderAmount;
		}

		public String getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}

		public String getOrderTime() {
			return orderTime;
		}

		public void setOrderTime(String orderTime) {
			this.orderTime = orderTime;
		}

		private String orderNumber;
		private String orderDes;
		private String orderBid;
		private String orderAmount;
		private String orderStatus;
		private String orderTime;
		private PayOrder order;

		public PayOrder getOrder() {
			return order;
		}

		public RepairListItem(PayOrder order) {
			this.order = order;
			orderNumber = order.getOrderNumber();
			orderDes = order.getDescr();
			if ((orderBid = order.getBid()) == null || orderBid.equals("")) {
				orderBid = "未获得流水号";
			}
			orderAmount = String.valueOf(order.getAmount());
			orderStatus = order.isSuccess() ? "订单成功" : "订单失败";
			orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getOrderTime());
		}
	}
}
