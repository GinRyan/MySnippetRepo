package com.android.MyFlipper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MyFlipperUsage extends Activity {

	public final static int VIEW_TEXT = 0;
	public final static int VIEW_IMAGE = 1;
	
    Button previous,next;
    
    ViewFlipper flipper;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initiaView();
        
        flipper.addView(addTextByText("HelloAndroid"));
        flipper.addView(addImageById(R.drawable.beijing_003_mb5ucom));
        flipper.addView(addTextByText("eoe.Android"));
        flipper.addView(addImageById(R.drawable.beijing_004_mb5ucom));
        flipper.addView(addTextByText("Gryphone"));
        
    }
    
    
    private void initiaView(){
    	previous = (Button) findViewById(R.id.previousButton);
    	next = (Button) findViewById(R.id.nextButton);
    	
    	flipper = (ViewFlipper) findViewById(R.id.flipper);
    	flipper.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
        
        previous.setOnClickListener(listener);
        next.setOnClickListener(listener);
    }
    
    private OnClickListener listener = new OnClickListener(){
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.previousButton:
				flipper.showPrevious();
				break;
				
			case R.id.nextButton:
				flipper.showNext();
				break;
			}//switch
		}//onClick
    };//OnClickListener
    
    public View addTextByText(String text){
    		TextView tv = new TextView(this);
    		tv.setText(text);
    		tv.setGravity(1);
    		//tv.setPadding(80, 80, 80, 80);
    		return tv;
    }
    
    public View addImageById(int id){
		ImageView iv = new ImageView(this);
		iv.setImageResource(id);
		
		return iv;
    }
}