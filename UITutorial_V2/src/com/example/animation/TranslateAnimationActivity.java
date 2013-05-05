package com.example.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class TranslateAnimationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.com_example_animation_translate);

		ImageView img = (ImageView) findViewById(com.example.R.id.com_example_animation_translate_imageView1);

		Animation anim = new TranslateAnimation(10,230,10,240);
		anim.setDuration(2000);
		anim.setRepeatCount(-1);
		img.setAnimation(anim);
		anim.startNow();
	}
}
