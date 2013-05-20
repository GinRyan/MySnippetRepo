package com.ljp.laucher.path;

import java.util.Random;

import weibo4android.Comment;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ljp.laucher.R;
import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.ToastAndDialog;

public class FeedbackActivity extends Activity {

	private Button sentbut;ProgressDialog progressDialog;EditText content;Comment c;Weibo weibo;
	//private EditText content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		System.gc();
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_more_freeback);

		if(Configure.N_USER_NAME==null)
			weibo = Configure.getSysWeibo(new Random().nextInt(7));
		else
			weibo = Configure.getUserWeibo(FeedbackActivity.this);
		content = (EditText) findViewById(R.id.edit_feed);
		Button back = (Button) findViewById(R.id.freeback_cancle);
		back.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_OK, getIntent());
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}
		});
		//content = (EditText)findViewById(R.id.edit_feed);
		sentbut = (Button)findViewById(R.id.freeback_sent);
		sentbut.setOnClickListener(new sentlisten());
	}
	
	class sentlisten implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//01-10 14:28:10.619: I/System.out(16481): 3400293528508833
			progressDialog = ProgressDialog.show(FeedbackActivity.this, "请稍等片刻...",
					"小夜正在努力的为您与服务器通信", true, true);
			new Thread(){
				public void run(){
					try {
						c=weibo.updateComment(content.getText().toString(),3400293528508833l+"",null);
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = Handler.obtainMessage();
					Handler.sendMessage(msg);
				}
			}.start();
		}
		
	}
	private Handler Handler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if(c !=null && (c.getId()>0)){
				Toast.makeText(FeedbackActivity.this, "反馈成功，谢谢您的意见。", 3000).show();
				finish();
			}
			else
				ToastAndDialog.Toast(FeedbackActivity.this, "网络通讯出现了一点小问题噢。", 3000);
		}
	};	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			return false;
		}
		return false;
	}
}
