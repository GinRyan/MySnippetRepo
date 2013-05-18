package com.ljp.laucher.additem;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ljp.laucher.R;
import com.ljp.laucher.databean.ContentItem;
import com.ljp.laucher.util.ImgAsync_List;

public class ItemContentAdapter extends BaseAdapter {
	List<ContentItem> listData;
	ImgAsync_List asyncImageLoader_Weibo;
	Context context;ListView listview;

	// private String id;

	public ItemContentAdapter(Context context,ListView listview, List<ContentItem> tdlist) {
		super();
		this.listData = tdlist;
		this.context = context;
		this.listview =listview;
		asyncImageLoader_Weibo = new ImgAsync_List();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitem_additem_content, null);
		}
		ContentItem item = listData.get(position);
		final ListHolder holder = new ListHolder();
		holder.des = (TextView) convertView.findViewById(R.id.content_text);
		holder.choice = (TextView) convertView.findViewById(R.id.content_choice);

		holder.des.setText((String) item.getText());
		holder.choice.setVisibility(item.isChoice()?0:8);
		return convertView;
	}

	public class ListHolder {
		public TextView des;
		public TextView choice;
	}
}
