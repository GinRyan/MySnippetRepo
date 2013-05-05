/**
 * $id$
 */
package com.renren.api.connect.android.demo.activity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.renren.api.connect.android.demo.LogHelper;
import com.renren.api.connect.android.demo.R;

/**
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class LogActivity extends BaseActivity{
	
	private static final int PROGRESS_DIALOG = 1;
	
	private static final String KEY_LOG = "log";
	
	EditText logText;
	
	Executor pool; 
	
	Handler handler;
	
	String log;
	
	LogHelper logHelper;
	
	ProgressDialog progress;
	
	private LinearLayout logLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		logLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.log, null);
		root.addView(logLayout);
				
		logText = (EditText) logLayout.findViewById(R.id.log);
		int screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		logText.setHeight(screenHeight - 70);
		
		titlebarRightButton.setText(getText(R.string.clear_log));
		titlebarRightButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				logText.setText("");
				log = null;
				logHelper.clear();
			}
		});
		
	    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
	    imm.hideSoftInputFromWindow(logText.getWindowToken(), 0);
		
		//加载日志
		logHelper = new LogHelper();
		if(savedInstanceState != null) {
			log = savedInstanceState.getString(KEY_LOG);
		}
		if(log == null) {
			showDialog(PROGRESS_DIALOG);
			handler = new Handler(getMainLooper());
		
			pool = Executors.newFixedThreadPool(1);
			pool.execute(new Runnable() {			
				@Override
				public void run() {
					final String logStr = logHelper.getLog();
					handler.post(new Runnable() {				
						@Override
						public void run() {
							removeDialog(PROGRESS_DIALOG);
							log = logStr;
							logText.setText(log);
							logText.setSelection(log.length());
						}
					});
				}
			});
		}		
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case PROGRESS_DIALOG:
			if(progress != null) {
				return null;
			}
			progress = new ProgressDialog(this);
			progress.setTitle("提示");
			progress.setMessage("正在加载日志，请稍候");
			return progress;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(KEY_LOG, log);
		super.onSaveInstanceState(outState);
	}	
}
