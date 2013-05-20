package com.ljp.laucher.util;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class ImgAsync_Weibo {



	public Bitmap loadDrawable(final Context context,final String imageUrl, final ImageCallback_DW imageCallback,final int position,final ProgressBar pb) {
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap bitmap = null;
				File cacheDir;
				 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"night_girls/weibos");
			    }else{
			        cacheDir=context.getCacheDir();
			    }
				if (!cacheDir.exists()) {
					cacheDir.mkdirs();
				}
				File file = new File(cacheDir+ "/" + imageUrl.hashCode());
				boolean isLoad = false;
				if (!file.isFile()) {
					isLoad = ImageOperation.loadImageFromURL(context, imageUrl,"night_girls/weibos",pb);
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
						isLoad =ImageOperation.loadImageFromURL(context, imageUrl,"night_girls/weibos",pb);
					}
				}
				if (isLoad)
					bitmap = null;
				else
					bitmap = BitmapFactory.decodeFile(cacheDir+ "/" + imageUrl.hashCode());
				Configure.DetailWeiboImages[position>=Configure.DetailWeiboImages.length?Configure.DetailWeiboImages.length-1:position] = bitmap;
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public interface ImageCallback_DW{
		public void imageLoaded(Bitmap imageDrawable, String imageUrl);
	}

}