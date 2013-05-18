package com.renren.api.connect.android.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.demo.activity.ApiCreateAlbumActivity;
import com.renren.api.connect.android.demo.activity.ApiGetAlbumsActivity;
import com.renren.api.connect.android.demo.activity.ApiUploadPhotoActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * 封装renren_android_sdk中对照片、相册API的调用
 * 
 * @author Sun Ting (ting.sun@renren-inc.com)
 */
public class PhotoDemo {

	public static final String PHOTO_LOG_TAG = "renren_demo_photo";

	/**
	 * 调用系统界面上传照片<br>
	 * albumResponse
	 * 注意：用户需自己准备上传的文件(File对象)
	 * 
	 * @param activity
	 * @param renren
	 */
	public static void uploadPhotoWithActivity(final Activity activity,
			final Renren renren) {
		// 读取assets文件夹下的图片，保存在手机中
		String fileName = "renren.png";
		// 获取文件后缀，构造本地文件名
		int index = fileName.lastIndexOf('.');
		// 文件保存在/sdcard目录下，以renren_前缀加系统毫秒数构造文件名
		final String realName = "renren_" + System.currentTimeMillis()
				+ fileName.substring(index, fileName.length());
		try {
			InputStream is = activity.getResources().getAssets().open(fileName);
			BufferedOutputStream bos = new BufferedOutputStream(
					activity.openFileOutput(realName, Context.MODE_PRIVATE));
			int length = 0;
			byte[] buffer = new byte[1024];
			while ((length = is.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
			is.close();
			bos.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filePath = activity.getFilesDir().getAbsolutePath() + "/"
				+ realName;
		
		// 以上准备好了File参数
		// 下面调用SDK接口
		renren.publishPhoto(activity, new File(filePath), "传入的默认参数");
	}

	/**
	 * 调用自己的界面上传照片
	 * 
	 * @param activity
	 * @param renren
	 */
	public static void uploadPhoto(final Activity activity, final Renren renren) {
		// 读取assets文件夹下的图片，保存在手机中
		String fileName = "renren.png";
		// 获取文件后缀，构造本地文件名
		int index = fileName.lastIndexOf('.');
		// 文件保存在/sdcard目录下，以renren_前缀加系统毫秒数构造文件名
		final String realName = "renren_" + System.currentTimeMillis()
				+ fileName.substring(index, fileName.length());
		try {
			InputStream is = activity.getResources().getAssets().open(fileName);
			BufferedOutputStream bos = new BufferedOutputStream(
					activity.openFileOutput(realName, Context.MODE_PRIVATE));
			int length = 0;
			byte[] buffer = new byte[1024];
			while ((length = is.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
			is.close();
			bos.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filePath = activity.getFilesDir().getAbsolutePath() + "/"
				+ realName;

		// 准备好上传的文件对象后，跳转到上传界面
		Intent intent = new Intent(activity, ApiUploadPhotoActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		intent.putExtra("file", new File(filePath));
		activity.startActivity(intent);
	}

	/**
	 * 调用自己的界面创建相册
	 * 
	 * @param activity
	 * @param renren
	 */
	public static void createAlbum(final Activity activity, final Renren renren) {
		Intent intent = new Intent(activity, ApiCreateAlbumActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		activity.startActivity(intent);
	}

	/**
	 * 跳转到获取相册信息的界面
	 * 
	 * @param activity
	 * @param renren
	 */
	public static void getAlbums(final Activity activity, final Renren renren) {
		Intent intent = new Intent(activity, ApiGetAlbumsActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		activity.startActivity(intent);
	}

}
