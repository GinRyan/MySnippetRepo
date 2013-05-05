package com.example.animation;

import android.app.Activity;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class AlphaAnimationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.com_example_animation_alpha);

		ImageView img=(ImageView)findViewById(com.example.R.id.com_example_animation_alpha_imageView1);
		
		Animation anim =new AlphaAnimation(1,	0);
		anim.setDuration(2000);
		anim.setRepeatCount(-1);
	anim.setInterpolator(getApplicationContext(), android.R.anim.accelerate_interpolator);
		img.setAnimation(anim);
		anim.startNow();
	}
}
