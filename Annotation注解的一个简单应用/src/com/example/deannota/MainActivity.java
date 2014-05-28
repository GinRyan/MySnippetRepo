package com.example.deannota;

import com.example.deannota.anot.ViewClick;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AnnotationActivity {
	@ViewClick(targetId = 112334) TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.tv);
		inject();
	}
}
