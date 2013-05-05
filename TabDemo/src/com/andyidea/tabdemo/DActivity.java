package com.andyidea.tabdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class DActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView tv = new TextView(this);
		tv.setText("This is D Activity!");
		tv.setGravity(Gravity.CENTER);
		setContentView(tv);
	}
	
}
