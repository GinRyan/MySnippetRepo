package com.example.expression;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class SelectExpressionDialog extends Dialog {

	public SelectExpressionDialog(Context context ) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public SelectExpressionDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expressions_available);
	}
	
}
