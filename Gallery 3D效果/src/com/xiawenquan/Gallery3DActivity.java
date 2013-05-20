package com.xiawenquan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Gallery3DActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);   
        
        Integer[] images = { R.drawable.aa, R.drawable.bb,   
                R.drawable.cc, R.drawable.dd, R.drawable.ee,   
                R.drawable.ff, /* R.drawable.gg,R.drawable.hh ,
                R.drawable.jj, R.drawable.kk, R.drawable.ll*/};   
           
        ImageAdapter adapter = new ImageAdapter(this, images);   
        adapter.createReflectedImages();//创建倒影效果   
        GalleryFlow galleryFlow = (GalleryFlow) Gallery3DActivity.this.findViewById(R.id.Gallery01);   
        galleryFlow.setFadingEdgeLength(0);   
        galleryFlow.setSpacing(-50); //图片之间的间距   
        galleryFlow.setAdapter(adapter);   
           
        galleryFlow.setOnItemClickListener(new OnItemClickListener() {   
            public void onItemClick(AdapterView<?> parent, View view,   
                    int position, long id) {   
                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();   
            }   
               
        });   
        galleryFlow.setSelection(0);   
    }   
}
