package com.testmemoryadapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class TestAdapter extends BaseAdapter {

	private ArrayList list;
	private LayoutInflater mInflater;
	private Context context;
	//这个用来保存 imageview 的引用
	private ArrayList viewList = new ArrayList();
	//这个用来 保存 bitmap
	private ArrayList bitmapList = new ArrayList();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public TestAdapter(Context context, ArrayList list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		convertView = mInflater.inflate(R.layout.test_list_row, null);

		ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);
		
		
		//用try catch 块包围住
		try {
			setImage(iv);
		} catch (OutOfMemoryError e) {
			// 这里就是当内存泄露时 需要做的事情
			e.printStackTrace();

			Log.d("memory", "out");
			
			//释放内存资源
			recycleMemory();
			
			//将刚才 发生异常没有执行的 代码 再重新执行一次
			setImage(iv);

		}

		return convertView;
	}

	
	//这里是关键
	private void recycleMemory() {
		//一屏显示多少行 这里就设置为多少。不设也行 主要是用户体验好 不会将用户看到的图片设为默认图片
		int showCount = 10;
		
		//
		for (int i = 0; i < viewList.size()-showCount; i++) {
			ImageView iv = (ImageView) viewList.get(i);
			/***
			 *  这里是关键！ 将 imageview 设置一张默认的图片 ，
			 *  用于解决当释放bitmap的时候 还有其他 控件对他保持引用
			 *  就不会发生trying to use a recycled bitmap异常了
			 */
			iv.setImageResource(R.drawable.default_cover);
			//从list中去除
			viewList.remove(i);
		}

//		viewList = new ArrayList();

		for (int i = 0; i < bitmapList.size()-10; i++) {

			Bitmap bitmap = (Bitmap) bitmapList.get(i);
			//这里就开始释放bitmap 所占的内存了
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				System.out.println("recycle ");
			}
			//从list中去除
			bitmapList.remove(i);
		}

//		bitmapList = new ArrayList();
	}
	
	private void setImage(ImageView iv){
		/***
		 * 从sdcard获取 图片  这张图片 只要不超过  android对于图片大小的限制即可 
		 * 我用了 一张比较大的图片 也通过测试
		 */
		Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/test/1.jpg");

		iv.setImageBitmap(bitmap);
		
		//将这个控件 添加到 list里
		viewList.add(iv);
		//将要 释放的 bitmap也添加到list里
		bitmapList.add(bitmap);
	}

}
