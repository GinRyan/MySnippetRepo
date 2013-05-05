package com.example.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class layout2Activity extends Activity {
	private Button _btnBack=null;
	private String _pass1=null;
	private TextView _label_note=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.layout2);
		
		Bundle bundle=getIntent().getBundleExtra("key1");
		_pass1=bundle.getString("key1");
		_label_note=(TextView)findViewById(com.example.R.id.layout2_label_note);
		_label_note.setText(_pass1);
		
		_btnBack=(Button)findViewById(com.example.R.id.layout2_btnBack);
		_btnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(layout2Activity.this,
						com.example.UITutorial_V2Activity.class);
				startActivity(intent);
				layout2Activity.this.finish();
			}
			
		});
	}
}
