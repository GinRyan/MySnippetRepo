package com.ljp.laucher.util;

import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class ImgAsync_List {
	private HashMap<String, SoftReference<Drawable>> imageCache;

	public ImgAsync_List() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}
	
	public Drawable loadDrawable(final Context context,final String imageUrl, final ImageCallback_LW imageCallback) {
		if(imageUrl==null|| imageUrl.equals("")) return null;
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap bitmap = null;
				File cacheDir;
				 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"night_girls/icons");
			    }else{
			        cacheDir=context.getCacheDir();
			    }
				if (!cacheDir.exists()) {
					cacheDir.mkdirs();
				}
				File file = new File(cacheDir+ "/" +imageUrl.hashCode());
				boolean isLoad = false;
				if (!file.isFile()) {
					isLoad = ImageOperation.loadImageFromUrl(context, imageUrl,"night_girls/icons");
				} else {
					FileInputStream fis;
					int size = 0;
					try {
						fis = new FileInputStream(file);
						size = (fis.available() / 1000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("异常-》图片文件大小判断");
					}
					if (size <= 0) {
						file.delete();
						isLoad =ImageOperation.loadImageFromUrl(context, imageUrl,"night_girls/icons");
					}
				}
				if (isLoad)
					bitmap = null;
				else
					bitmap = BitmapFactory.decodeFile(cacheDir+ "/" +imageUrl.hashCode());
				Drawable drawable = new BitmapDrawable(bitmap);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public interface ImageCallback_LW{
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

}