package com.mindtherobot.samples.asyncmvc;

import static com.mindtherobot.samples.asyncmvc.controller.ControllerProtocol.*;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindtherobot.samples.asyncmvc.controller.Controller;
import com.mindtherobot.samples.asyncmvc.model.Model;
import com.mindtherobot.samples.asyncmvc.model.ModelData;

public class DemoActivity extends Activity implements Handler.Callback, OnClickListener {
	
	private static final String TAG = DemoActivity.class.getSimpleName();
	
	private Controller controller;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        ((Button) findViewById(R.id.update)).setOnClickListener(this);
        ((Button) findViewById(R.id.quit)).setOnClickListener(this);
        
        controller = new Controller(new Model());
        controller.addOutboxHandler(new Handler(this)); // messages will go to .handleMessage()
        
        controller.getInboxHandler().sendEmptyMessage(V_REQUEST_DATA); // request initial data
    }

	@Override
	protected void onDestroy() {
		// I think it is a good idea to not fail in onDestroy()
		try {
			controller.dispose();
		} catch (Throwable t) {
			Log.e(TAG, "Failed to destroy the controller", t);
		} 

		super.onDestroy();
	}

	@Override
	public boolean handleMessage(Message msg) {
		Log.d(TAG, "Received message: " + msg);
		 
		switch (msg.what) {
		case C_QUIT:
			onQuit();
			return true;
		case C_DATA:
			onData((ModelData) msg.obj);
			return true;
		case C_UPDATE_STARTED:
			onUpdateStarted();
			return true;
		case C_UPDATE_FINISHED:
			onUpdateFinished();
			return true;
		}
		return false;
	}

	private void onUpdateStarted() {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		progressBar.setVisibility(View.VISIBLE);
	}

	private void onUpdateFinished() {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		progressBar.setVisibility(View.GONE);
		
		// request the updated data
		controller.getInboxHandler().sendEmptyMessage(V_REQUEST_DATA);
	}

	private void onData(ModelData data) {
		TextView dataView = (TextView) findViewById(R.id.data_view);
		dataView.setText("The answer is "+ data.getAnswer());
	}

	private void onQuit() {
		Log.d(TAG, "Activity quitting");
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update:
			controller.getInboxHandler().sendEmptyMessage(V_REQUEST_UPDATE);
			break;
		case R.id.quit:
			controller.getInboxHandler().sendEmptyMessage(V_REQUEST_QUIT);
			break;
		}
	}
}