package com.ljp.laucher.itemcontent.imageview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ljp.laucher.R;
import com.ljp.laucher.util.ImageOperation;
import com.ljp.laucher.util.IntentData;
import com.ljp.laucher.util.ToastAndDialog;
import com.ljp.tools.gif.GifDrawable;

public class GifImageViewActivity extends Activity {

	private ImageView imageview_MTIV;
	private LinearLayout return_LL;
	private LinearLayout save_LL;

	private String imgUrl;ProgressDialog progressDialog;
	Drawable drawable;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_weibo_gifimageview);
		imgUrl = getIntent().getStringExtra("imgUrl");
		initComponent();
		bindEvent();
		progressDialog = ProgressDialog.show(GifImageViewActivity.this, "请稍等片刻...",
				"小夜正在努力的为您加载中", true, true);
		new Thread(){
			public void run(){
				try {
					if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
						drawable = new GifDrawable(new FileInputStream(new File(Environment.getExternalStorageDirectory()+ "/night_girls/weibos/" + imgUrl.hashCode())));
						}else {
						drawable = new GifDrawable(new FileInputStream(new File(GifImageViewActivity.this.getCacheDir()+ "/" + imgUrl.hashCode())));
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = UIHandler.obtainMessage();
				UIHandler.sendMessage(msg);
			}
		}.start();
		
	}
	private Handler UIHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			imageview_MTIV.setImageDrawable(drawable);
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private void initComponent() {
		imageview_MTIV = (ImageView) findViewById(R.id.imageviewer_multitouchimageview);
		return_LL = (LinearLayout) findViewById(R.id.imageviewer_linearlayout_return);
		save_LL = (LinearLayout) findViewById(R.id.imageviewer_linearlayout_save);
	}

	private void bindEvent() {
		return_LL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		save_LL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
					 ToastAndDialog.Toast(GifImageViewActivity.this,
								"请插入SDcard", 3000);
				}else if (ImageOperation.isSave("/love_girls/", imgUrl)) {
					ToastAndDialog.Toast(GifImageViewActivity.this,
							"已经保存在love_girls文件夹哦", 3000);
				} else {
					progressDialog = ProgressDialog.show(GifImageViewActivity.this, "请稍等片刻...",
							"小夜正在努力的为您保存图片", true, true);
					new Thread(){
						public void run(){
							if(IntentData.PicFrom==0)ImageOperation.saveFile("/love_girls/", imgUrl);
							else ImageOperation.saveFileFromAssets(GifImageViewActivity.this,"/love_girls/", imgUrl);
							Message msg = BroadcastHandler.obtainMessage();
							BroadcastHandler.sendMessage(msg);
						}
					}.start();
				}
			}
		});
	}
	private Handler BroadcastHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			ToastAndDialog.Toast(GifImageViewActivity.this,
					"已经为您保存于love_girls文件夹之下", 3000);
		}
	};
}
