package com.example.expression;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.SmileyParser;

public class ExpressionActivity extends Activity {

	private TextView tv;
	private EditText et;
	private Button bt;
	private Button bt2;
	String cacheSelectedExpression;// 模拟选择的表情名字

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expression);
		tv = (TextView) findViewById(R.id.text);
		et = (EditText) findViewById(R.id.edit);
		bt = (Button) findViewById(R.id.button);
		bt2 = (Button) findViewById(R.id.button2);

		final SelectExpressionDialog sed = new SelectExpressionDialog(ExpressionActivity.this,R.style.ExpressionDialog);
		sed.setCanceledOnTouchOutside(true);
		Window window = sed.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.y = Gravity.BOTTOM;
		window.setAttributes(lp);
		final String[] stringarray = getResources().getStringArray(SmileyParser.DEFAULT_SMILEY_TEXTS);
		final int[] expressions = SmileyParser.DEFAULT_SMILEY_RES_IDS;
		
		
		
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sed.show();
				ExpressionChooserAdapter eca = new ExpressionChooserAdapter(ExpressionActivity.this, expressions, stringarray);
				GridView gv = (GridView) sed.findViewById(R.id.expressions_chooser);
				gv.setAdapter(eca);
				gv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> apv, View v,
							int position, long id) {
						
						GridView gv = (GridView) apv;
						String str = (String) gv.getItemAtPosition(position);
						SmileyParser smileparser = new SmileyParser(ExpressionActivity.this);
						et.setText(smileparser.replace(et.getText().append(str)));
						et.setSelection(et.getText().length());
						cacheSelectedExpression = et.getText().toString();
						sed.dismiss();
						
					}
				});
			}
		});
		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				tv.setText(cacheSelectedExpression);
			}
		});
		
		
	}
}
