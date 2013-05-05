package com.zgy.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	// 用来设置ImageView的风格
	int mGalleryItemBackground;
	private Context context;
	// 图片的资源ID
	private Integer[] mImageIds = { R.drawable.img1, R.drawable.img2,
			R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
			R.drawable.img7, R.drawable.img8 };

	// 构造函数
	public ImageAdapter(Context context) {
		this.context = context;
	}

	// 返回所有图片的个数
	@Override
	public int getCount() {
		return mImageIds.length;
	}

	// 返回图片在资源的位置
	@Override
	public Object getItem(int position) {
		return position;
	}

	// 返回图片在资源的位置
	@Override
	public long getItemId(int position) {
		return position;
	}

	// 此方法是最主要的，他设置好的ImageView对象返回给Gallery
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		// 通过索引获得图片并设置给ImageView
		imageView.setImageResource(mImageIds[position]);
		// 设置ImageView的伸缩规格，用了自带的属性值
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		// 设置布局参数
		imageView.setLayoutParams(new Gallery.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// 设置风格，此风格的配置是在xml中
		imageView.setBackgroundResource(mGalleryItemBackground);
		return imageView;
	}

	public int getmGalleryItemBackground() {
		return mGalleryItemBackground;
	}

	public void setmGalleryItemBackground(int mGalleryItemBackground) {
		this.mGalleryItemBackground = mGalleryItemBackground;
	}

}
