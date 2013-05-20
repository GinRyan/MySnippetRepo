package com.ljp.laucher;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljp.laucher.databean.ContentItem;
import com.ljp.laucher.util.ImgAsync_List;

public class DragGridAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ContentItem> lstDate;
	private TextView txt;private ImageView img;private RelativeLayout relate;
	ImgAsync_List asyncImageLoader_Weibo;
	@SuppressWarnings("unused")
	private GridView listview;

	public DragGridAdapter(Context mContext,GridView listview, ArrayList<ContentItem> list) {
		this.context = mContext;
		lstDate = list;
		this.listview = listview;
		asyncImageLoader_Weibo = new ImgAsync_List();
	}

	@Override
	public int getCount() {
		return lstDate.size();
	}

	@Override
	public Object getItem(int position) {
		return lstDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void exchange(int startPosition, int endPosition) {
		Object endObject = getItem(endPosition);
		Object startObject = getItem(startPosition);
		lstDate.add(startPosition, (ContentItem) endObject);
		lstDate.remove(startPosition + 1);
		lstDate.add(endPosition, (ContentItem) startObject);
		lstDate.remove(endPosition + 1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.griditem_milaucher, null);
		txt = (TextView) convertView.findViewById(R.id.item_text);
		img = (ImageView) convertView.findViewById(R.id.item_img);
		relate = (RelativeLayout) convertView.findViewById(R.id.item_relate);
		ContentItem map = lstDate.get(position);
		
		if(map!=null && map.getText()==null){
			txt.setText("");
			img.setImageBitmap(null);
			relate.setBackgroundResource(R.drawable.red_add);
		}
		else if(map!=null &&map.getText().equals("none")){
			txt.setText("");
			img.setImageBitmap(null);
			relate.setBackgroundDrawable(null);
		}else {
			txt.setText(map.getText().toString());
			int url = map.getIcon();
			if(url!=-1)
				img.setBackgroundResource(url);
		/*	Drawable cachedImage = asyncImageLoader_Weibo.loadDrawable(context,
					url, new ImageCallback_LW() {
						public void imageLoaded(Drawable imageDrawable,
								String imageUrls) {
							ImageView imageViewByTag = (ImageView) listview.findViewWithTag(imageUrls);
							if (imageViewByTag == null || imageDrawable == null) 
								img.setBackgroundDrawable(imageDrawable);
							else 
								imageViewByTag.setBackgroundDrawable(imageDrawable);
						}
					});
			if (cachedImage == null) {
				img.setBackgroundDrawable(null);
			} else {
				img.setBackgroundDrawable(cachedImage);
			}*/
			relate.setBackgroundResource(R.drawable.blue);
		}
		
		return convertView;
	}

}
