package com.ljp.laucher.itemcontent.userforward;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljp.laucher.R;
import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.MathOperation;

public class UserForwardActivity extends Activity implements  OnClickListener {

	private EditText edit;
	private Button share, share_back;
	private TextView user_id, wordCounterTextView;
	private ImageView image, share_guan;
	private ProgressDialog progressDialog;
	
	
	boolean isLoadingMore = false;boolean isStop=false;
	boolean isWrong=false;
	String text,imgUrl;int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weibo_detail_share);
		text = getIntent().getStringExtra("text");
		imgUrl = getIntent().getStringExtra("image");
		position = getIntent().getIntExtra("position", 0);

		if (MathOperation.count(text) < 130) {
			text = text + "@赖剑平";
		}
		init();

		user_id.setText(Configure.N_USER_NAME);
		wordCounterTextView.setText("" + MathOperation.count(text) + "/140");
		int index = edit.getSelectionStart();
		Editable edittext = edit.getEditableText();
		if (index < 0 || index >= edittext.length()) {
			edittext.append(getIntent().getStringExtra("text"));
		} else {
			edittext.insert(index, text);
		}
		
		image.setOnClickListener(this);share_guan.setOnClickListener(this);
		share.setOnClickListener(this);share_back.setOnClickListener(this);
		
		edit.addTextChangedListener(watcher);

	}
	public void init(){
		edit = (EditText) findViewById(R.id.share_content);
		share = (Button) findViewById(R.id.share);
		share_back = (Button) findViewById(R.id.share_back);
		image = (ImageView) findViewById(R.id.share_image);
		share_guan = (ImageView) findViewById(R.id.share_guang);
		user_id = (TextView) findViewById(R.id.user_id);
		wordCounterTextView = (TextView) findViewById(R.id.share_word_counter);
	}
	TextWatcher watcher = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before,int count) {}
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
		@Override
		public void afterTextChanged(Editable s) {
			String textContent = edit.getText().toString();
			int currentLength = MathOperation.count(textContent);
			if (currentLength <= 140) {
				wordCounterTextView.setTextColor(Color.GRAY);
				wordCounterTextView.setText("" + MathOperation.count(textContent) + "/140");
			} else {
				wordCounterTextView.setTextColor(Color.RED);
				wordCounterTextView.setText(String.valueOf(140 - currentLength));
			}
		}
	};

	Handler shareHandler = new Handler(){
			public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if(isWrong){
				Toast.makeText(getApplicationContext(), "请求失败，原因为图片太大或文字太多！",Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "转发成功",Toast.LENGTH_SHORT).show();
				finish();
			//	overridePendingTransition(R.anim.leftshow_anim, R.anim.righthidden_anim);
			}
		}
	};

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.share_image:
			if (Configure.DetailWeiboImages[position] == null) {
				File cacheDir;
				 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"night_girls/weibos");
			    }else{
			        cacheDir=UserForwardActivity.this.getCacheDir();
			    }
				Configure.DetailWeiboImages[position] = BitmapFactory.decodeFile(cacheDir+ "/" + imgUrl.hashCode());
			}
			image.setImageBitmap(Configure.DetailWeiboImages[position]);
			break;
		case R.id.share_guang:
			int index = edit.getSelectionStart();
			Editable edittext = edit.getEditableText();
			edittext.insert(index," @");
			break;
		case R.id.share:
			if (edit.getText().toString().trim().equals("")) {
				Toast.makeText(getApplicationContext(), "请输入内容",
						Toast.LENGTH_LONG).show();
			} else if (MathOperation.count(edit.getText().toString()) > 140) {
				Toast.makeText(getApplicationContext(), "内容长度超过140",
						Toast.LENGTH_LONG).show();
			} else {
				progressDialog = ProgressDialog.show(UserForwardActivity.this, "请稍等片刻...",
						"正在努力的为您与服务器通信", true, true);
				new Thread(){
					public void run(){
						try {
							File cacheDir;
							 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
						        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"night_girls/weibos");
						    }else{
						        cacheDir=UserForwardActivity.this.getCacheDir();
						    }
							File myCaptureFile = new File(cacheDir+ "/" + imgUrl.hashCode());
							String content = edit.getText().toString();
							if(imgUrl==null || imgUrl.equals(""))
								Configure.getUserWeibo(UserForwardActivity.this).updateStatus(content);
							else
								Configure.getUserWeibo(UserForwardActivity.this).uploadStatus(content, myCaptureFile);
						} catch (Exception e) {
							e.printStackTrace();
							isWrong = true;
						}
						Message msg = shareHandler.obtainMessage();
						shareHandler.sendMessage(msg);
					}
				}.start();
			}
			break;
		case R.id.share_back:
			finish();
			//overridePendingTransition(R.anim.leftshow_anim, R.anim.righthidden_anim);
			break;
		
		}
	}
	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			//overridePendingTransition(R.anim.leftshow_anim, R.anim.righthidden_anim);
			return false;
		}
		return false;
	}
}



