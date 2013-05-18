package com.liuxiaofei.test11;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class testAdapter extends BaseAdapter {

	private List<String> strs;
	private Context context;
	public Button xzbutton;

	public testAdapter(Context context, List<String> strs) {
		super();
		this.context = context;
		this.strs = strs;
	}

	public int getCount() {
		return strs == null ? 0 : strs.size();
	}

	public Object getItem(int arg0) {
		return strs == null ? null : strs.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final AlphaAnimation fadein = new AlphaAnimation(0.0f, 1.0f);
		final AlphaAnimation fadeout = new AlphaAnimation(1.0f, 0.0f);
		fadeout.setDuration(1000);
		fadein.setDuration(1000);
		View v = arg1;
		v = LayoutInflater.from(context).inflate(R.layout.lv_list, null);
		TextView tv = (TextView) v.findViewById(R.id.tv);
		final Button bt2 = (Button) v.findViewById(R.id.bt2);
		final int position = arg0;
		tv.setText(strs.get(arg0));

		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, "点击的是第" + strs.get(position) + "条",
						3000).show();
				
			}
		});
		
		v.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				bt2.setText("我是"+strs.get(position));
				if (xzbutton == bt2) {
					xzbutton.startAnimation(fadeout);
					xzbutton.setVisibility(View.GONE);
					xzbutton = null;
				} else {

					if (xzbutton != null) {
						xzbutton.startAnimation(fadeout);
						
						xzbutton.setVisibility(View.GONE);
					}
					xzbutton = bt2;
					xzbutton.startAnimation(fadein);
					xzbutton.setVisibility(View.VISIBLE);
				}
			}
		});
		return v;
	}
}
