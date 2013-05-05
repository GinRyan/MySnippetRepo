package com.liuxiaofei.myprogress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyProgressActivity extends Activity {
	private View progressView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progressView = (View)findViewById(R.id.weibo_progress);
        //TODO
    }
}