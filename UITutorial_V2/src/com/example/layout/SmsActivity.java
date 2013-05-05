package com.example.layout;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SmsActivity extends Activity {
	Button m_btnSend;
	EditText m_txtPhoneNum;
	EditText m_txtSmsContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.sms);
		
		m_txtPhoneNum=(EditText)findViewById(com.example.R.id.sms_txtPhoneNum);
		m_txtSmsContent=(EditText)findViewById(com.example.R.id.sms_txtSmsContent);
		m_btnSend=(Button)findViewById(com.example.R.id.sms_btnSendSms);
		
		m_btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				SmsManager smsManager=SmsManager.getDefault();
				List<String> contents=smsManager.divideMessage(m_txtSmsContent.getText().toString());
				for(String s :contents){
					smsManager.sendTextMessage(m_txtPhoneNum.getText().toString(),
							null,
							m_txtSmsContent.getText().toString(),
							null,
							null);
				}
				m_txtPhoneNum.clearComposingText();
				m_txtSmsContent.clearComposingText();
			}
		});
		
	}
}
