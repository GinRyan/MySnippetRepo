package com.ljp.laucher.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljp.laucher.R;

public class PullToRefreshListView extends LinearLayout{

	public static final int STATE_IDLE = 0;
	public static final int STATE_PULL = 1;
	public static final int STATE_RELEASE = 2;
	public static final int STATE_LOADING = 3;
	private int REFRESH_VIEW_HEIGHT = 60;
	private int mCurRefreshViewHeight = 40;
	private boolean mCurReleaseState;
	private RotateAnimation mFlipAnimation;
	private float mInterceptY;
	private int mLastMotionY;
	private ListView mList;
	private RelativeLayout mRefreshView;
	private ImageView mRefreshViewImage;
	private TextView mRefreshViewText;
	private RotateAnimation mReverseFlipAnimation;
	private boolean mScrollingList = true;
	private int mState;
	private ProgressBar mRefreshProgress;
	private OnChangeStateListener mOnChangeStateListener;
	private int now;
	private Context mContext;

	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void applyHeaderHeight(MotionEvent ev) {
		final int historySize = ev.getHistorySize();

		if (historySize > 0) {
			for (int h = 0; h < historySize; h++) {
				int historicalY = (int) (ev.getHistoricalY(h));
				updateRefreshView((historicalY - mLastMotionY) / 2);
			}

		} else {
			int historicalY = (int) ev.getY();
			updateRefreshView((historicalY - mLastMotionY) / 2);
			now = (historicalY - mLastMotionY) / 2;
		}

	}

	private void changeRefreshView(boolean paramBoolean)
	  {
	    if (mCurReleaseState != paramBoolean)
	    {
	      mRefreshViewImage.setImageResource(R.drawable.arrow_down);
	      mRefreshViewImage.clearAnimation();
	      mRefreshViewImage.startAnimation(mReverseFlipAnimation);
	    }else{
	    
		  mCurReleaseState = paramBoolean;   
		  mRefreshViewImage.clearAnimation();
	      mRefreshViewImage.startAnimation(mFlipAnimation);
	      
	    }
	   
	  }
	
	private void changeState(int state) {
		switch (state) {
		case STATE_IDLE:
			
			setRefreshViewHeight(1);
			break;
		case STATE_PULL:
			mRefreshViewImage.setVisibility(View.VISIBLE);
			mRefreshProgress.setVisibility(View.GONE);
			 mRefreshViewText.setText("继续下拉刷新");
			changeRefreshView(true);
			break;
		case STATE_RELEASE:
			mRefreshViewImage.setVisibility(View.VISIBLE);
			mRefreshProgress.setVisibility(View.GONE);
			 mRefreshViewText.setText("松手开始刷新");
			changeRefreshView(false);
			break;
		case STATE_LOADING:
			mRefreshViewImage.setVisibility(View.GONE);
			mRefreshProgress.setVisibility(View.VISIBLE);
			mRefreshViewText.setText("正在努力的为你加载数据");
			setRefreshViewHeight(80);
			break;
		}

		mState = state;

		notifyStateChanged();
	}

	private void init(Context context) {
		mState = STATE_IDLE;
		this.mContext=context;
		float densityFactor = context.getResources().getDisplayMetrics().density;
		REFRESH_VIEW_HEIGHT *= densityFactor;

		setVerticalFadingEdgeEnabled(false);
		setVerticalScrollBarEnabled(false);

		mFlipAnimation = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
		mFlipAnimation.setDuration(250L);mFlipAnimation.setFillAfter(true);
		
		mReverseFlipAnimation = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1,0.5F);
		mReverseFlipAnimation.setDuration(250L);mReverseFlipAnimation.setFillAfter(true);
	}

	public void setRefreshViewHeight(int height) {

		if (height == 1) {
			mRefreshView.setLayoutParams(new LayoutParams(1, 1));
		} else {
			mCurRefreshViewHeight = height;
			mRefreshView.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, height));
		}

	}

	private void updateRefreshView(int height) {
		if (height <= 0) {
			return;
		}

		if ((REFRESH_VIEW_HEIGHT / 4 <= mCurRefreshViewHeight)
				&& (mCurRefreshViewHeight < REFRESH_VIEW_HEIGHT)) {
			setRefreshViewHeight(height);
			
			if(mState == STATE_IDLE){
				 mRefreshViewText.setText("继续下拉刷新");
				 mState = STATE_PULL;
				 mRefreshViewImage.setVisibility(View.VISIBLE);
				 mRefreshProgress.setVisibility(View.GONE);
				 notifyStateChanged();
			}else if(mState!=STATE_PULL&&mState != STATE_IDLE)
			changeState(STATE_PULL);
				
		

		} else if (mCurRefreshViewHeight > REFRESH_VIEW_HEIGHT) {
			if (height > REFRESH_VIEW_HEIGHT) {
				height = (int) (REFRESH_VIEW_HEIGHT + (height - REFRESH_VIEW_HEIGHT)
						* REFRESH_VIEW_HEIGHT * 2.45f / height);
			}
			
			setRefreshViewHeight(height);
			if(mState!=STATE_RELEASE)
				changeState(STATE_RELEASE);
		} else {
			setRefreshViewHeight(height);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		float oldLastY = mInterceptY;
		mInterceptY = ev.getY();
		if (mState == STATE_LOADING) {
			return false;
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mList.getFirstVisiblePosition() == 0
					&& (mList.getChildCount() == 0 || mList.getChildAt(0)
							.getTop() == 0)) {
				if ((mInterceptY - oldLastY > 5) || (mState == STATE_PULL)
						|| (mState == STATE_RELEASE)) {
					mScrollingList=true;
					break;
				} else {
					break;
				}
			}else {
				break;
			}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		if(mScrollingList){
			mScrollingList=!mScrollingList;
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			applyHeaderHeight(ev);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (mState == STATE_RELEASE) {
				TranslateAnimation am = new TranslateAnimation(0, 0,mCurRefreshViewHeight/mContext.getResources().getDisplayMetrics().density,0);
				am.setDuration(300l);
				startAnimation(am);
				mList.setFocusable(false);
				mList.setSelected(false);
				mRefreshViewImage.clearAnimation();
				refresh();
			}else if(mState==STATE_PULL){
				TranslateAnimation am = new TranslateAnimation(0, 0, now, 0);
				am.setDuration(300l);
				am.setInterpolator(AnimationUtils.loadInterpolator(
						getContext(),
						android.R.anim.accelerate_decelerate_interpolator));
				startAnimation(am);
				changeState(STATE_IDLE);
			}
			else{
				changeState(STATE_IDLE);
			}
			break;
		}
		return true;
	}


	public ListView getList() {
		return mList;
	}

	protected void onFinishInflate() {
		mRefreshView = (RelativeLayout) findViewById(R.id.refresh_view);
		mRefreshViewText = (TextView) findViewById(R.id.pull_to_refresh_text);
		mRefreshViewImage = (ImageView) findViewById(R.id.pull_to_refresh_image);
		mRefreshProgress = (ProgressBar) findViewById(R.id.pull_to_refresh_progress);
		mRefreshViewImage.setImageResource(R.drawable.arrow_down);
		mList = (ListView) findViewById(R.id.lv_weibo);
		mList.setSelected(false);
		mList.setFocusable(false);
		
	}

	private void notifyStateChanged() {
		if (mOnChangeStateListener != null) {
			mOnChangeStateListener.onChangeState(this, mState);
		}
	}

	public void onRefreshComplete() {
		mList.setFocusable(true);
		mList.setSelected(true);

		changeState(STATE_IDLE);
		TranslateAnimation am = new TranslateAnimation(0, 0, 80, 1);
		am.setDuration(600l);
		am.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
				android.R.anim.accelerate_decelerate_interpolator));
		startAnimation(am);
		
	}

	public void refresh() {
		changeState(STATE_LOADING);
	}

	public void clickrefresh() {
		TranslateAnimation am = new TranslateAnimation(0, 0, -80, 0);
		am.setDuration(600l);
		am.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
				android.R.anim.accelerate_decelerate_interpolator));
		startAnimation(am);
		changeState(STATE_LOADING);
	}

	public void setOnChangeStateListener(OnChangeStateListener listener) {
		mOnChangeStateListener = listener;
	}

	public interface OnChangeStateListener {
		public void onChangeState(PullToRefreshListView container, int state);
	}


}
