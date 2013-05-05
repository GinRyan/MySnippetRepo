package com.example.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class layout1Activity extends Activity {
	private Button _btnBack=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(com.example.R.layout.layout1);
		
		_btnBack=(Button)findViewById(com.example.R.id.layout1_btnBack);
		
		_btnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(layout1Activity.this,
						com.example.UITutorial_V2Activity.class);
				startActivity(intent);
				layout1Activity.this.finish();
			}
			
			
		});
	}
}
