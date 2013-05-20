package com.android.switchbutton;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SwitchButton extends RelativeLayout implements OnGestureListener,
		OnTouchListener {
	private TextView mTextOn, mTextOff;
	private ImageView togglebtn, bg;
	private Drawable bg_drawable, image_toggle;
	private OnCheckedChangeListener mCheckedChangeListener;

	private static final int TEXTON_ID = 1;

	private static final int MIN_OFFSET = 5;

	private GestureDetector mGesturedetector;

	private LayoutParams toggle_params;

	private TranslateAnimation switch_on, switch_off, text_on, text_off;

	private boolean isChecked;

	public SwitchButton(Context context) {
		super(context);
		initView();
		initAnimation();
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initAnimation();
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		initAnimation();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		mGesturedetector = new GestureDetector(this);

		bg_drawable = getContext().getResources().getDrawable(R.drawable.bg);
		image_toggle = getContext().getResources().getDrawable(
				R.drawable.toggle);

		mTextOn = new TextView(getContext());
		mTextOn.setId(TEXTON_ID);
		mTextOn.setTextColor(getContext().getResources().getColor(
				R.color.add_text));
		mTextOn.setBackgroundResource(R.drawable.add_bg);

		LayoutParams texton_params = new LayoutParams(
				bg_drawable.getIntrinsicWidth(),
				bg_drawable.getIntrinsicHeight());
		mTextOn.setLayoutParams(texton_params);
		mTextOn.setPadding(0, 0, image_toggle.getIntrinsicWidth(), 0);
		mTextOn.setGravity(Gravity.CENTER);

		mTextOff = new TextView(getContext());
		mTextOff.setTextColor(getContext().getResources().getColor(
				R.color.cancel_text));
		mTextOff.setBackgroundResource(R.drawable.cancel_bg);

		LayoutParams textoff_params = new LayoutParams(
				bg_drawable.getIntrinsicWidth(),
				bg_drawable.getIntrinsicHeight());
		mTextOff.setLayoutParams(textoff_params);
		mTextOff.setPadding(image_toggle.getIntrinsicWidth(), 0, 0, 0);
		mTextOff.setGravity(Gravity.CENTER);
		if (isChecked) {
			mTextOff.setVisibility(View.GONE);
		} else {
			mTextOff.setVisibility(View.VISIBLE);
		}

		togglebtn = new ImageView(getContext());
		togglebtn.setBackgroundDrawable(image_toggle);

		toggle_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if (isChecked) {
			toggle_params.addRule(ALIGN_RIGHT, TEXTON_ID);
		} else {
			toggle_params.addRule(ALIGN_PARENT_LEFT);
		}
		togglebtn.setLayoutParams(toggle_params);

		bg = new ImageView(getContext());
		bg.setBackgroundDrawable(bg_drawable);

		togglebtn.setOnTouchListener(this);
		togglebtn.setLongClickable(true);

		addView(mTextOn);
		addView(mTextOff);
		addView(bg);
		addView(togglebtn);
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		switch_on = new TranslateAnimation(
				(bg_drawable.getIntrinsicWidth() - image_toggle.getIntrinsicWidth())
						* -1, 0, 0, 0);
		switch_on.setDuration(500);
		switch_on.setFillAfter(true);

		switch_off = new TranslateAnimation(bg_drawable.getIntrinsicWidth()
				- image_toggle.getIntrinsicWidth(), 0, 0, 0);
		switch_off.setDuration(500);
		switch_off.setFillAfter(true);

		text_on = new TranslateAnimation(0, bg_drawable.getIntrinsicWidth()
				- image_toggle.getIntrinsicWidth(), 0, 0);
		text_on.setDuration(500);
		text_on.setFillAfter(true);

		text_off = new TranslateAnimation(bg_drawable.getIntrinsicWidth()
				- image_toggle.getIntrinsicWidth(), 0, 0, 0);
		text_off.setDuration(500);
		text_off.setFillAfter(true);
	}

	/**
	 * 添加监听器
	 * 
	 * @param listener
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mCheckedChangeListener = listener;
	}

	/**
	 * 设置打开显示字
	 * 
	 * @param text
	 */
	public void setTextOn(CharSequence text) {
		mTextOn.setText(text);
	}

	/**
	 * 设置关闭显示字
	 * 
	 * @param text
	 */
	public void setTextOff(CharSequence text) {
		mTextOff.setText(text);
	}

	/**
	 * 获得打开显示字
	 * 
	 * @return CharSequence
	 */
	public CharSequence getTextOn() {
		return mTextOn.getText();
	}

	/**
	 * 获得关闭显示字
	 * 
	 * @return CharSequence
	 */
	public CharSequence getTextOff() {
		return mTextOff.getText();
	}

	/**
	 * 设置打开显示字体颜色
	 * 
	 * @param color
	 */
	public void setTextOnColor(int color) {
		mTextOn.setTextColor(color);
	}

	/**
	 * 设置关闭显示字体颜色
	 * 
	 * @param color
	 */
	public void setTextOffColor(int color) {
		mTextOff.setTextColor(color);
	}

	/**
	 * 设置打开时背景颜色从资源文件引入
	 * 
	 * @param res
	 */
	public void setTextOnBackGroundColor(int res) {
		mTextOn.setBackgroundResource(res);
	}

	/**
	 * 设置关闭时背景颜色从资源文件引入
	 * 
	 * @param res
	 */
	public void setTextOffBackGroundColor(int res) {
		mTextOff.setBackgroundResource(res);
	}

	/**
	 * 获取当前打开状态
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(bg_drawable.getIntrinsicWidth(),
				bg_drawable.getIntrinsicHeight());
	}

	public boolean onTouch(View v, MotionEvent event) {
		boolean handle = false;
		if (v == togglebtn) {
			handle = mGesturedetector.onTouchEvent(event);
		}
		return handle;
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if ((e1.getX() - e2.getX()) > MIN_OFFSET) {
			if (isChecked) {
				off();
			}
		} else if ((e1.getX() - e2.getX()) < -MIN_OFFSET) {
			if (isChecked == false) {
				on();
			}
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {
		if (isChecked) {
			off();
		} else {
			on();
		}
		return false;
	}

	/**
	 * 开关打开
	 */
	private void on() {
		toggle_params = null;
		toggle_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		toggle_params.addRule(ALIGN_RIGHT, TEXTON_ID);
		togglebtn.setLayoutParams(toggle_params);
		togglebtn.startAnimation(switch_on);
		mTextOff.startAnimation(text_on);
		mTextOff.setVisibility(View.GONE);
		isChecked = true;
		mCheckedChangeListener.CheckChanged(isChecked);
	}

	/**
	 * 开关关闭
	 */
	private void off() {
		toggle_params = null;
		toggle_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		toggle_params.addRule(ALIGN_PARENT_LEFT);
		togglebtn.setLayoutParams(toggle_params);
		togglebtn.startAnimation(switch_off);
		mTextOff.setVisibility(View.VISIBLE);
		mTextOff.startAnimation(text_off);
		isChecked = false;
		mCheckedChangeListener.CheckChanged(isChecked);
	}

	public interface OnCheckedChangeListener {
		public void CheckChanged(boolean isChecked);
	}
}
