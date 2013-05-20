package com.ljp.laucher.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ljp.laucher.R;

public class VersionCheck {
	Context context;boolean isLoading;public static boolean isVerLoading=false;
	ProgressBar pb;
	TextView tv;
	int fileSize;
	AlertDialog.Builder builder;
	int downLoadFileSize;
	String fileEx, fileNa, filename;

	public static int getVerCode(Context context,String _package) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					_package, 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"com.ljp.night", 0).versionName;
		} catch (NameNotFoundException e) {
		}
		return verName;
	}

	public VersionCheck(Context c,final Class<?> intentclass,boolean i) {
		this.context = c;
		this.isLoading=i;//首次页面检测更新与设置里面检测更新的区别
		LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.layout_loadapk, null);
		pb = (ProgressBar) ll.findViewById(R.id.down_pb);
		tv = (TextView) ll.findViewById(R.id.tv);
		builder = new Builder(context);
		builder.setView(ll);builder.setTitle("升级提示");
		builder.setNegativeButton("后台下载",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if(isLoading){
							Intent intent = new Intent();
							intent.setClass(context, intentclass);
							context.startActivity(intent);
							((Activity) context).finish();
						}
					}
				});
	}

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					pb.setMax(fileSize);
				case 1:
					pb.setProgress(downLoadFileSize);
					int result = downLoadFileSize * 100 / fileSize;
					tv.setText("已为您加载了：" + result + "%");
					break;
				case 2:
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "NightMan.apk")),
							"application/vnd.android.package-archive");
					context.startActivity(intent);
					break;
				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(context, error, 1).show();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	public void down_file(String url, String path) throws IOException {
		// 下载函数
		filename = url.substring(url.lastIndexOf("/") + 1);
		// 获取文件名
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		this.fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (this.fileSize <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (is == null)
			throw new RuntimeException("stream is null");
		FileOutputStream fos = new FileOutputStream(path + filename);
		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
		downLoadFileSize = 0;
		sendMsg(0);
		do {
			// 循环读取
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
			downLoadFileSize += numread;

			sendMsg(1);// 更新进度条
		} while (true);
		sendMsg(2);// 通知下载完成
		try {
			is.close();
		} catch (Exception ex) {
			Log.e("tag", "error: " + ex.getMessage(), ex);
		}

	}

	public void loadNewVersion(String url,String savePath) {
		builder.create().show();
		new Thread() {
			public void run() {
				try {
					down_file("http://1.nightman.sinaapp.com/NightMan.apk",
							"/sdcard/");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
}
