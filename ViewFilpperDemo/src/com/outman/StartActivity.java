package com.outman;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
/**
 * 欢迎页面
 * @author Administrator
 *
 */
public class StartActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("请等待加载数据 ...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					//模拟数据加载，显示加载进度条
					Thread.sleep(2*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();

				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent();
				intent.setClass(StartActivity.this, MainActivity.class);
				startActivity(intent);
				StartActivity.this.finish();
			}
		});
		t.start();
	}

}