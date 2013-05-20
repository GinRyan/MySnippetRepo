package org.cn.bean;

import org.cn.gv.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectDialog extends Dialog{
	Context context;

	private SelectDialog selectdialog;

	public SelectDialog(Context context) {
		super(context);
		this.context = context;
	}
	public SelectDialog(Context context, int theme) {
		super(context,theme);
		this.context = context;
	}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog);
    }
	
}