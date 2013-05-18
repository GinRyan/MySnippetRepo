package com.ljp.laucher.itemcontent.usercomment;

import java.util.ArrayList;
import java.util.HashMap;

import weibo4android.Comment;
import weibo4android.Status;
import weibo4android.WeiboException;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ljp.laucher.R;
import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.MathOperation;
import com.ljp.laucher.util.ToastAndDialog;

public class UserCommentActivity extends Activity implements TextWatcher, OnClickListener{
	EditText edit;TextView wordCounterTextView;ImageButton btn_back,btn_at,btn_face,btn_topic;Button btn_send;
	Comment c;Status s;ProgressDialog	progressDialog;CheckBox checkbox;
	
	ArrayList<HashMap<String, String>> list;PopupWindow pw ;ListView fanslist;
	boolean isLoadingMore = false;boolean isStop=false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weibo_detail_comment);
		edit = (EditText) findViewById(R.id.c_edit);
		wordCounterTextView = (TextView) findViewById(R.id.c_textcount);
		checkbox = (CheckBox) findViewById(R.id.c_checkbx);
		
		btn_send = (Button) findViewById(R.id.detail_send);
		btn_back = (ImageButton) findViewById(R.id.detail_back);	btn_at = (ImageButton) findViewById(R.id.c_at);
		btn_face = (ImageButton) findViewById(R.id.c_face);btn_topic = (ImageButton) findViewById(R.id.c_topic);
		btn_back.setOnClickListener(this);btn_send.setOnClickListener(this);btn_at.setOnClickListener(this);
		btn_face.setOnClickListener(this);btn_topic.setOnClickListener(this);
		wordCounterTextView.setText("字数统计：0/140");
		edit.addTextChangedListener(this);
	}
	private Handler comHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if(!checkbox.isChecked()&&c !=null && (c.getId()>0)){
				Toast.makeText(UserCommentActivity.this, "评论成功。", 3000).show();
				finish();
				//overridePendingTransition(R.anim.leftshow_anim, R.anim.righthidden_anim);
			}
			else if(checkbox.isChecked()&&s !=null && (s.getId()>0)){
				Toast.makeText(UserCommentActivity.this, "转发并且评论该微博成功。", 3000).show();
				finish();
				//overridePendingTransition(R.anim.leftshow_anim, R.anim.righthidden_anim);
			}
			else
				ToastAndDialog.Toast(UserCommentActivity.this, "网络通讯出现了一点小问题噢。", 3000);
		}
	};

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String textContent = edit.getText().toString();
		int currentLength = MathOperation.count(textContent);
		if (currentLength <= 140) {
			wordCounterTextView.setTextColor(Color.GRAY);
			wordCounterTextView.setText("字数统计：" + MathOperation.count(textContent) + "/140");
		} else {
			wordCounterTextView.setTextColor(Color.RED);
			wordCounterTextView.setText("字数过多："+String.valueOf(140 - currentLength));
		}
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int index = edit.getSelectionStart();
		Editable edittext = edit.getEditableText();
		switch(v.getId()){
		case R.id.detail_back:
			finish();
			//overridePendingTransition(R.anim.leftshow_anim, R.anim.righthidden_anim);
			break;
		case R.id.detail_send:
			progressDialog = ProgressDialog.show(UserCommentActivity.this, "请稍等片刻...",
					"小夜正在努力的为您与服务器通信", true, true);
			new Thread(){
				public void run(){
					try {
						if(checkbox.isChecked()){
							s=Configure.getUserWeibo(UserCommentActivity.this).repost(getIntent().getStringExtra("w_id"), edit.getText().toString(), 1);
						}
						else{
							String c_id="";
							if(getIntent().hasExtra("c_id"))
									c_id=getIntent().getStringExtra("c_id");
							c=Configure.getUserWeibo(UserCommentActivity.this).updateComment(edit.getText().toString(),getIntent().getStringExtra("w_id"),c_id);
						}
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = comHandler.obtainMessage();
					comHandler.sendMessage(msg);
				}
			}.start();
			break;
		case R.id.c_at:
			if(Configure.getUserWeibo(UserCommentActivity.this)==null)
				ToastAndDialog.Toast(UserCommentActivity.this, "请先登录后再进行该操作。", 3000);
			else{
				edittext.insert(index," @");
			}
			break;
		case R.id.c_face:
				edittext.insert(index," -。-");
			break;
		case R.id.c_topic:
			if(Configure.getUserWeibo(UserCommentActivity.this)==null)
				ToastAndDialog.Toast(UserCommentActivity.this, "请先登录后再进行该操作。", 3000);
			else{
				edittext.insert(index," #");
			}
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







