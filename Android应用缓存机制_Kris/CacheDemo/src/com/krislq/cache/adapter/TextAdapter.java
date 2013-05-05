package com.krislq.cache.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krislq.cache.R;
import com.krislq.cache.util.L;

/**
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 21, 2012
 * @version 1.0.0
 *
 */
public class TextAdapter extends BaseAdapter{
	protected Context context;
	private List<String> contentList;
	private LayoutInflater mInflater;
	
	private int count=0;
	private int maxCount = 0;
	
	public TextAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		count = 20;
		String[] texts = context.getResources().getStringArray(R.array.texts);
		contentList = Arrays.asList(texts);
		L.e("contentList size:"+contentList.size());
		maxCount = contentList.size();
	}
	
	@Override
	public int getCount() {
		
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int index = position;
		ViewHolder holder = null;
		if(position>=maxCount) {
			index = position%maxCount;
		}
		if(convertView==null) {
			convertView = mInflater.inflate(R.layout.text_item, null);
			holder = new ViewHolder();
			holder.nameView = (TextView)convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}

		String content = contentList.get(index);
		L.e("name:"+content);
		holder.nameView.setText(content);
		return convertView;
	}
}
class ViewHolder {
	TextView nameView;
}
