package com.example.listoflist;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListExpandedMoreAdapter extends BaseAdapter {
	Context context;
	List<String>list;
	
	public ListExpandedMoreAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = LayoutInflater.from(context).inflate(R.layout.ins_listitem, null);
		TextView tv =  (TextView) v.findViewById(R.id.tv);
		tv.setText(list.get(position));
		
		RelativeLayout rt= (RelativeLayout) v.findViewById(R.id.rt);
		
		if (InsListActivity.ID == position) {
			View vd = LayoutInflater.from(context).inflate(R.layout.insertcon, null);
			rt.addView(vd);
			Button btn = (Button) vd.findViewById(R.id.btn);
			btn.setText("Button:  "+(position+1));
		}
		return v;
	}
}
