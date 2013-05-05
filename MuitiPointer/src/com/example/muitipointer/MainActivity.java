package com.example.muitipointer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.v("MP", "触摸点数：" + event.getPointerCount());
		Log.v("MPLocX", "第一个点触摸坐标X：" + event.getX(0));
		Log.v("MPLocY", "第一个点触摸坐标Y：" + event.getY(0));
		return super.onTouchEvent(event);
	}

}
