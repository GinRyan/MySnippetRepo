package com.example.varlables;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;

public class LabelGenerator {
	public static Button newInstance(Context context, String str) {
		Button tv = new Button(context);
		MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(15, 15, 15, 15);
		tv.setLayoutParams(lp);
		tv.setPadding(20, 20, 20, 20);
		tv.setText(str);
		System.out.println();
		return tv;
	}
}
