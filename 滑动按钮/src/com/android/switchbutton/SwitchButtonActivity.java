package com.android.switchbutton;

import android.app.Activity;
import android.os.Bundle;

import com.android.switchbutton.SwitchButton.OnCheckedChangeListener;

public class SwitchButtonActivity extends Activity implements
		OnCheckedChangeListener {
	private SwitchButton switchButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		switchButton = (SwitchButton) findViewById(R.id.switchbtn);

		switchButton.setTextOn("添加");
		switchButton.setTextOff("取消");

		switchButton.setOnCheckedChangeListener(this);
	}

	public void CheckChanged(boolean isChecked) {
		System.out.println(isChecked);
	}
}