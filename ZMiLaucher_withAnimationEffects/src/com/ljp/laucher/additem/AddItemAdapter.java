package com.ljp.laucher.additem;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ljp.laucher.R;
import com.ljp.laucher.databean.ContentItem;
import com.ljp.laucher.util.ImgAsync_List;

public class AddItemAdapter extends BaseAdapter {
	List<ContentItem> listData;
	ImgAsync_List asyncImageLoader_Weibo;
	Context context;ListView listview;

	// private String id;

	public AddItemAdapter(Context context,ListView listview, List<ContentItem> tdlist) {
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
					R.layout.listitem_additem, null);
		}
		ContentItem item = listData.get(position);
		final ListHolder holder = new ListHolder();
		holder.icon = (ImageView) convertView.findViewById(R.id.more_icon);
		holder.des = (TextView) convertView.findViewById(R.id.more_des);

		holder.des.setText((String) item.getFrom());
		int url = item.getFrom_icon();

		if(url!=-1)
			holder.icon.setBackgroundResource(url);
		else
			holder.icon.setBackgroundResource(R.drawable.icon);
/*		Drawable cachedImage = asyncImageLoader_Weibo.loadDrawable(context,
				url, new ImageCallback_LW() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrls) {
						ImageView imageViewByTag = (ImageView) listview.findViewWithTag(imageUrls);
						if (imageViewByTag == null || imageDrawable == null) 
							holder.icon.setBackgroundDrawable(imageDrawable);
						else 
							imageViewByTag.setBackgroundDrawable(imageDrawable);
					}
				});
		if (cachedImage == null) {
			holder.icon.setBackgroundResource(R.drawable.icon);
		} else {
			holder.icon.setBackgroundDrawable(cachedImage);
		}*/
		return convertView;
	}

	public class ListHolder {
		public TextView des;
		public ImageView icon;
	}
}
