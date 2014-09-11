package org.execise.make.viewgroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 子曰：如果有了轮子，就不要重新发明轮子。
 * 
 * 子又曰：只是说不要重新发明轮子，但是没有说不能仿造一个轮子
 * 
 * 我曰：那我就自己仿制一个轮子
 * 
 * 仿制一个ViewPager
 * 
 * @author Liang
 *
 */
public class PagedView extends FrameLayout {
	private static final String TAG = "TAG";

	public void log(String s) {
		Log.d(TAG, s);
	}

	GestureDetector mGestureDetector = null;
	ScaleGestureDetector mScaleGestureDetector = null;
	GestureImpl mGestureImpl = new GestureImpl();
	Scroller scroller;
	public boolean isFling = false;

	int mCurrentTabId = 0;

	public PagedView(Context context) {
		super(context);
		init();
	}

	public PagedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PagedView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mGestureDetector = new GestureDetector(getContext(), mGestureImpl);
		mScaleGestureDetector = new ScaleGestureDetector(getContext(), mGestureImpl);
		scroller = new Scroller(getContext(), new AccelerateInterpolator());
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (scroller.computeScrollOffset()) {
			// log("scroller.getCurrX() : " + scroller.getCurrX());
			scrollTo(scroller.getCurrX(), 0);
			invalidate();
		}
	}

	class GestureImpl implements OnGestureListener, OnScaleGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			log("onScroll: distanceX: " + distanceX);
			scrollBy((int) distanceX, 0);
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			isFling = true;
			if (velocityX > 0 && mCurrentTabId > 0) {
				mCurrentTabId--;
			} else if (velocityX < 0 && mCurrentTabId < getChildCount()) {
				mCurrentTabId++;
			}
			moveToDest(mCurrentTabId);
			return false;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			return false;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return false;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {

		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		mGestureDetector.onTouchEvent(event);
		// mScaleGestureDetector.onTouchEvent(event);
		int downX = 0;
		int upX = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			upX = (int) event.getX();
			if (!isFling) {
				int nextId = 0;
				if (upX - downX < getWidth() / 2) {// Slide to left
					nextId = mCurrentTabId + 1;
				} else if (upX - downX > getWidth() / 2) {// Slide to right
					nextId = mCurrentTabId - 1;
				} else {
					nextId = mCurrentTabId;
				}
				moveToDest(nextId);
			}
			isFling = false;
			// log("action_up:x:  " + upX);
			break;
		}
		return true;
	}

	private void moveToDest(int nextId) {
		mCurrentTabId = (nextId < getChildCount()) ? ((nextId >= 0) ? nextId : 0) : getChildCount() - 1;
		int distance = mCurrentTabId * getWidth() - getScrollX();
		log("tap to : " + mCurrentTabId);
		scroller.startScroll(getScrollX(), 0, distance, 0, Math.abs(distance));
		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (changed) {
			for (int i = 0; i < getChildCount(); i++) {
				View view = getChildAt(i);
				// 指明每个子View的所在位置
				view.layout(i * getWidth(), 0, getWidth() * (i + 1), getHeight());
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
