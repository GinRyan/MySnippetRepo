package com.example.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class ScaleAnimationActivity extends Activity {
	ImageView m_img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.com_example_animation_scale);
		
		m_img=(ImageView)findViewById(com.example.R.id.scaleanimation_imageView1);
		
	
		//Animation anim=new ScaleAnimation(1f,0.4f,1f,0.3f );
		//anim.setDuration(2000);
		//anim.setRepeatCount(100);
		//m_img.setAnimation(anim);
		//anim.start();
	
		Animation anim=AnimationUtils.loadAnimation(getApplicationContext(), com.example.R.anim.scale);
		m_img.startAnimation(anim);
	}
}
