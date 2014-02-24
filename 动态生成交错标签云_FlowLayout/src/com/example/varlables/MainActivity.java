package com.example.varlables;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	FlowLayout big_table;
	Context context = this;
	String[] words;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		words = getResources().getStringArray(R.array.lables);
		big_table = (FlowLayout) findViewById(R.id.big_table);
		for (int i = 0; i < words.length; i++) {
			big_table.addView(LabelGenerator.newInstance(context, words[i]));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
