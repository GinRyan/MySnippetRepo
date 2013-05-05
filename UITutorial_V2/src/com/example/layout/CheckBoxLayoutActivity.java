package com.example.layout;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CheckBoxLayoutActivity extends Activity {
	List<String> m_checkedIds;
	Button m_btnOk;
	TextView m_lblResult;
	CheckBox m_chk1;
	CheckBox m_chk2;
	CheckBox m_chk3;
	CheckBox m_chk4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.checkboxlayout);

		m_btnOk = (Button) findViewById(com.example.R.id.checkboxlayout_btnOk);
		m_btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (m_checkedIds == null || m_checkedIds.isEmpty() ) {
					m_lblResult.setText("你没有选择任何项");
				} else {
					StringBuffer sb=new StringBuffer();
					for(String id :m_checkedIds){
						sb.append(id);
						sb.append(",");
						sb.append(((CheckBox) findViewById(Integer.parseInt(id))).getText());
						sb.append(",");
					}
					sb.deleteCharAt(sb.lastIndexOf(","));
					m_lblResult.setText(sb.toString());
				}
			}

		});
		m_chk1 = (CheckBox) findViewById(com.example.R.id.checkboxlayout_chk1);
		m_chk1.setOnCheckedChangeListener(checkboxChecked());
		m_chk2 = (CheckBox) findViewById(com.example.R.id.checkboxlayout_chk2);
		m_chk2.setOnCheckedChangeListener(checkboxChecked());
		m_chk3 = (CheckBox) findViewById(com.example.R.id.checkboxlayout_chk3);
		m_chk3.setOnCheckedChangeListener(checkboxChecked());
		m_chk4 = (CheckBox) findViewById(com.example.R.id.checkboxlayout_chk4);
		m_chk4.setOnCheckedChangeListener(checkboxChecked());
		m_lblResult = (TextView) findViewById(com.example.R.id.checkboxlayout_lbleResult);

	}

	private OnCheckedChangeListener checkboxChecked() {
		return new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					if (m_checkedIds == null)
						m_checkedIds = new ArrayList<String>();
					m_checkedIds.add(String.valueOf( buttonView.getId()));
				} else {
					if (m_checkedIds != null
							&&m_checkedIds.isEmpty()==false
							&& m_checkedIds.contains(String.valueOf(buttonView.getId())))
						m_checkedIds.remove(String.valueOf(buttonView.getId()));
				}
			}
		};
	}
}
