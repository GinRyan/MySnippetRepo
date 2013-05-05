package com.liuxiaofei.test11;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

public class Test11Activity extends Activity {
	
	private ListView lv;
	private List<String> strs = new ArrayList<String>();
	private testAdapter ta;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lv = (ListView)findViewById(R.id.lv);
        strs.add("1");
        strs.add("2");
        strs.add("3");
        strs.add("4");
        strs.add("5");
        strs.add("6");
        ta = new testAdapter(this, strs);
        lv.setAdapter(ta);

    }
}