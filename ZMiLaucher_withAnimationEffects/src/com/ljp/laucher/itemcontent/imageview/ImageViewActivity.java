package com.ljp.laucher.itemcontent.imageview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.ljp.laucher.R;
import com.ljp.laucher.myview.ScrollZoomImageView;
import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.ImageOperation;
import com.ljp.laucher.util.IntentData;
import com.ljp.laucher.util.ToastAndDialog;

public class ImageViewActivity extends Activity {

	private ScrollZoomImageView imageview_MTIV;
	private LinearLayout return_LL;
	private LinearLayout save_LL;

	private String imgUrl;ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_weibo_imageview);
		imgUrl = getIntent().getStringExtra("imgUrl");
		initComponent();
		bindEvent();
		if(imgUrl.endsWith("gif")){
			Intent intent = new Intent();
			intent.setClass(ImageViewActivity.this, GifImageViewActivity.class);
			intent.putExtra("imgUrl", imgUrl);
			startActivity(intent);
			finish();
		}else{
		imageview_MTIV
				.setImageBitmap(Configure.DetailWeiboImages[Configure._position]);
		}
	}

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
		imageview_MTIV = (ScrollZoomImageView) findViewById(R.id.imageviewer_multitouchimageview);
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
					 ToastAndDialog.Toast(ImageViewActivity.this,
								"请插入SDcard", 3000);
				}else if (ImageOperation.isSave("/love_girls/", imgUrl)) {
					ToastAndDialog.Toast(ImageViewActivity.this,
							"已经保存在love_girls文件夹哦", 3000);
				} else {
					progressDialog = ProgressDialog.show(ImageViewActivity.this, "请稍等片刻...",
							"小夜正在努力的为您保存图片", true, true);
					new Thread(){
						public void run(){
							if(IntentData.PicFrom==0)
								ImageOperation.saveFile("/love_girls/", imgUrl);
							else if(IntentData.PicFrom==1) 
								ImageOperation.saveFileFromAssets(ImageViewActivity.this,"/love_girls/", imgUrl);
							else 
								ImageOperation.saveFileFromVIP("/love_girls/", imgUrl);
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
			ToastAndDialog.Toast(ImageViewActivity.this,
					"已经为您保存于love_girls文件夹之下", 3000);
		}
	};
}
