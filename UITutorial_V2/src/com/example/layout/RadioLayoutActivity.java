package com.example.layout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class RadioLayoutActivity extends Activity {
	RadioGroup m_radioGroup;
	TextView m_result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.radiolayout);
		
		m_result=(TextView)findViewById(com.example.R.id.radiolayout_lable_selectitem);
		m_radioGroup=(RadioGroup)findViewById(com.example.R.id.radiolayout_radioGroup1);
		m_radioGroup.check(com.example.R.id.radiolayout_radio1);
		m_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton btn= (RadioButton)findViewById(group.getCheckedRadioButtonId());
				m_result.setText(btn.getText());
				Log.i("бЁжа", btn.getText().toString());
			}
			
		});
	}
}
