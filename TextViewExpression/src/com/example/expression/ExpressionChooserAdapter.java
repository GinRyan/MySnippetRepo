package com.example.expression;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ExpressionChooserAdapter extends BaseAdapter {

	private Context context;
	private int[] expressions_resoucesId;
	private String[] expressionName;
	public ExpressionChooserAdapter(Context context,int[] expressions_resoucesId,String[] expressionName) {
		this.context = context;
		this.expressions_resoucesId = expressions_resoucesId;
		this.expressionName = expressionName;
	}
	
	@Override
	public int getCount() {
		return expressionName.length;
	}
	
	@Override
	public long getItemId(int position) {
		return expressions_resoucesId[position];
	}

	@Override
	public Object getItem(int position) {
		return getCurrentString(position);
	}

	public String getCurrentString(int position){
		return expressionName[position];
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		v = LayoutInflater.from(context).inflate(R.layout.expressions_item, null);
		ImageView iv = (ImageView) v.findViewById(R.id.expression_single);
		iv.setImageResource(expressions_resoucesId[position]);
		return v;
	}

}
