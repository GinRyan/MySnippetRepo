package com.ljp.laucher.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljp.laucher.R;


public class ToastAndDialog {
	public static void Toast(Context context,String str, int time) {
		final TextView textControl = new TextView(context);
		textControl.setText(str);
		textControl.setBackgroundResource(R.drawable.tipline);
		textControl.setGravity(Gravity.CENTER_HORIZONTAL);
		textControl.setPadding(0, 8, 0, 0);
		textControl.setTextSize(18);
		textControl.setTextColor(Color.parseColor("#ffffff"));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		((Activity) context).addContentView(textControl, params);
		textControl.measure(0, 0);
		TranslateAnimation ta = new TranslateAnimation(0, 0,
				-textControl.getMeasuredHeight(),0);
		ta.setDuration(500);
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1);
		aa.setDuration(500);
		TranslateAnimation ta2 = new TranslateAnimation(0, 0,
				0,-textControl.getMeasuredHeight());
		ta2.setDuration(500);ta2.setStartOffset(1500);
		AlphaAnimation aa2 = new AlphaAnimation(1, 0.5f);
		aa2.setDuration(500);aa2.setStartOffset(1500);

		AnimationSet as = new AnimationSet(true);
		as.addAnimation(ta2);
		as.addAnimation(aa2);
		as.addAnimation(ta);
		as.addAnimation(aa);
		as.setFillAfter(true);
		as.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				textControl.setVisibility(8);
			}
		});
		textControl.startAnimation(as);
	}

}