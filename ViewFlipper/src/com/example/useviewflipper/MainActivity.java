package com.example.useviewflipper;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnGestureListener {

	private ViewFlipper vfpr;
	private GestureDetector gdetector;
	private View finalView;
	private View firstView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		ImageView iva = new ImageView(this);
		iva.setLayoutParams(lp);
		iva.setImageResource(R.drawable.newers_guide_a);
		ImageView ivb = new ImageView(this);
		ivb.setLayoutParams(lp);
		ivb.setImageResource(R.drawable.newers_guide_b);
		ImageView ivc = new ImageView(this);
		ivc.setLayoutParams(lp);
		ivc.setImageResource(R.drawable.newers_guide_c);
		ImageView ivd = new ImageView(this);
		ivd.setLayoutParams(lp);
		ivd.setImageResource(R.drawable.newers_guide_d);
		ImageView ive = new ImageView(this);
		ive.setLayoutParams(lp);
		ive.setImageResource(R.drawable.newers_guide_e);
		vfpr = (ViewFlipper) findViewById(R.id.flipper1);
		firstView = iva;
		finalView = ive;
		vfpr.addView(iva);
		vfpr.addView(ivb);
		vfpr.addView(ivc);
		vfpr.addView(ivd);
		vfpr.addView(ive);
		gdetector = new GestureDetector(this);

	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		if (e1.getX() - e2.getX() > 120) {
			if (vfpr.getCurrentView() != finalView) {

				vfpr.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.left_in));
				vfpr.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.left_out));
				vfpr.showNext();
				System.out.println("-------->");
				return true;
			}
		} else if (e2.getX() - e1.getX() > 120) {
			if (vfpr.getCurrentView() != firstView  ) {

				vfpr.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.right_in));
				vfpr.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.right_out));
				vfpr.showPrevious();
				System.out.println("<--------");
				return true;
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gdetector.onTouchEvent(event);
	}
}
