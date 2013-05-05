package com.krislq.cache.adapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krislq.cache.ItemClickListener;
import com.krislq.cache.R;
import com.krislq.cache.manager.DownloadManager;
import com.krislq.cache.util.L;

/**
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 21, 2012
 * @version 1.0.0
 *
 */
public class ImageAdapter extends BaseAdapter{

	protected Context context;
	private DownloadManager downloadImageManager;
	private List<String> contentList;
	private List<String> imageList;
	private LayoutInflater mInflater;
	private Map<Integer, View> viewMap;
	
	private int count=0;
	private int maxCount = 0;
	
	public ImageAdapter(Context context, DownloadManager downloadManager) {
		this.context = context;
		this.downloadImageManager = downloadManager;
		mInflater = LayoutInflater.from(context);
		viewMap = new HashMap<Integer, View>();
		count = 20;
		String[] texts = context.getResources().getStringArray(R.array.texts);
		contentList = Arrays.asList(texts);
		L.e("contentList size:"+contentList.size());
		maxCount = contentList.size();

		String[] images = context.getResources().getStringArray(R.array.images);
		imageList = Arrays.asList(images);
		L.e("imageList size:"+imageList.size());
		int imageSize = imageList.size();
		if(imageSize< maxCount) {
			maxCount =imageSize;
		}
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
		ViewHolder hodler= null;
		if(position>=maxCount)
		{
			index = position%maxCount;
		}
		if(viewMap.containsKey(position)) {
			convertView  = viewMap.get(position);
			hodler = (ViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.image_item, null);
			hodler = new ViewHolder();
			hodler.nameView = (TextView)convertView.findViewById(R.id.tv_from_username);
			String name = "www.krislq.com";
			hodler.nameView.setText(name);
			hodler.nameView.setOnClickListener(new ItemClickListener(name) {
				
				@Override
				public void onClick(View v) {
					Uri uri = Uri.parse("http://"+getStrID());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(intent);
				}
			});
			
			hodler.messageView = (TextView) convertView.findViewById(R.id.tv_title);
			String content = contentList.get(index);
			L.e("name:"+content);
			hodler.messageView.setText(content);
			
			hodler.pictureView = (ImageView) convertView.findViewById(R.id.iv_picture);
			hodler.imageView = (ImageView) convertView.findViewById(R.id.iv_icon);
			convertView.setTag(hodler);
			viewMap.put(index, convertView);
			
		}
		String url = imageList.get(index);
		L.e("url:"+url);
		downloadImageManager.add(url, hodler.pictureView, R.drawable.default_user);

		downloadImageManager.add(url, hodler.imageView, R.drawable.default_image);
		return convertView;
	}
	class ViewHolder {
		TextView nameView;
		TextView messageView;
		ImageView pictureView;
		ImageView imageView;
	}
}
