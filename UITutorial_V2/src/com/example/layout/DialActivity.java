package com.example.layout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DialActivity extends Activity {
	Button m_btnDial;
	EditText m_txtPhoneNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.dial);
		
		m_txtPhoneNum=(EditText)findViewById(com.example.R.id.dial_txtPhoneNum);
		m_btnDial=(Button)findViewById(com.example.R.id.dial_btnDial);
		m_btnDial.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+m_txtPhoneNum.getText().toString()));
				startActivity(intent);
				
			}
		});
		
	}
}
