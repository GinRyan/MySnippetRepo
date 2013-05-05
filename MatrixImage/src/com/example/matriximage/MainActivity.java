package com.example.matriximage;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends MulitPointTouchActivity {

	private ImageView iv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView)super.findViewById(R.id.bigpicture);
        iv.setOnTouchListener(this);
    }
    
}
