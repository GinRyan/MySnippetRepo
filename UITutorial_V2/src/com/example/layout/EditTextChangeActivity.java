package com.example.layout;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextChangeActivity extends Activity {
	EditText m_txtInput;
	TextView m_lblInput;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.edittext_textchange_layout);
		
		m_txtInput=( EditText)findViewById(com.example.R.id.edittext_textchange_layout_txtInput);
		m_txtInput.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				m_lblInput.setText(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				m_lblInput.setText(s);
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				m_lblInput.setText(s);
			}
		});
		m_lblInput=( TextView)findViewById(com.example.R.id.edittext_textchange_layout_lblInput);
	}
}
