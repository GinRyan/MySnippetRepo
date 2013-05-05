package com.example.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class RotateAnimationActivity extends Activity {
	ImageView m_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.com_example_animation_rotate);
		m_image = (ImageView) findViewById(com.example.R.id.com_example_animation_rotate_imageView1);
		
		//Animation anim=new ScaleAnimation(1,4,1,3 );
		Animation anim=new RotateAnimation(12, 184);
		anim.setDuration(2000);
		anim.setRepeatCount(-1);
		m_image.setAnimation(anim);
		anim.start();
	}
}
