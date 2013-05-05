package com.example.useviewflipper;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
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

		RelativeLayout iva = (RelativeLayout) findViewById(R.id.newers_guide_a);
		RelativeLayout ive = (RelativeLayout) findViewById(R.id.newers_guide_e);
		
		vfpr = (ViewFlipper) findViewById(R.id.flipper1);
		
		firstView = iva;
		finalView = ive;

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
