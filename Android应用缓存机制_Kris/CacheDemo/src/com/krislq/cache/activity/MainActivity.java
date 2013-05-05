package com.krislq.cache.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.krislq.cache.Constants;
import com.krislq.cache.R;
import com.krislq.cache.util.Util;

/**
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 20, 2012
 * @version 1.0.0
 *
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button imageButton = (Button)findViewById(R.id.button_image);
        imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,ImageListActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        
        Button txtButton = (Button)findViewById(R.id.button_txt);
        txtButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,TextListActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        

        Button clearButton = (Button)findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.cleanFile(new File(Constants.CACHE_DIR), 0);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
