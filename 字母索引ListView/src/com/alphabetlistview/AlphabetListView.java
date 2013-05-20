package com.alphabetlistview;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 右边带有字母查询的ListView
 * 
 * @author Davee
 */
public class AlphabetListView extends FrameLayout {
	private Context mContext;

	private ListView mListView;
	private LinearLayout alphabetLayout;
	private TextView mTextView;

	private AlphabetPositionListener positionListener;

	private float screenDensity;

	private Handler mHandler;
	private HideIndicator mHideIndicator = new HideIndicator();

	private int indicatorDuration = 1000;

	public void setIndicatorDuration(int duration) {
		this.indicatorDuration = duration;
	}

	private final class HideIndicator implements Runnable {
		@Override
		public void run() {
			mTextView.setVisibility(View.INVISIBLE);
		}
	}

	public AlphabetListView(Context context) {
		super(context);
		init(context);
	}

	public AlphabetListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;

		screenDensity = context.getResources().getDisplayMetrics().density;

		mHandler = new Handler();

		mListView = new ListView(mContext);

		initAlphabetLayout(mContext);

		mTextView = new TextView(mContext);
		mTextView.setTextSize(convertDIP2PX(50));
		mTextView.setTextColor(Color.argb(150, 255, 255, 255));
		mTextView.setBackgroundColor(Color.argb(200, 0, 0, 0));
		mTextView.setMinWidth(convertDIP2PX(70));
		mTextView.setMinHeight(convertDIP2PX(70));
		int pixels = convertDIP2PX(10);
		mTextView.setPadding(pixels, pixels, pixels, pixels);
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setVisibility(View.INVISIBLE);
		FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		textLayoutParams.gravity = Gravity.CENTER;
		// textLayoutParams.rightMargin = convertDIP2PX(10);
		mTextView.setLayoutParams(textLayoutParams);
	}

	public void setAdapter(ListAdapter adapter,
			AlphabetPositionListener positionListener) {
		if (positionListener == null)
			throw new IllegalArgumentException(
					"AlphabetPositionListener is required");

		mListView.setAdapter(adapter);

		this.positionListener = positionListener;

		this.addView(mListView);
		this.addView(alphabetLayout);
		this.addView(mTextView);
	}

	private void initAlphabetLayout(Context context) {
		// 创建字母布局
		alphabetLayout = new LinearLayout(context);
		alphabetLayout.setOrientation(LinearLayout.VERTICAL);
		FrameLayout.LayoutParams alphabetLayoutParams = new FrameLayout.LayoutParams(
				convertDIP2PX(30), ViewGroup.LayoutParams.FILL_PARENT);
		alphabetLayoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		alphabetLayoutParams.rightMargin = convertDIP2PX(10);
		alphabetLayout.setLayoutParams(alphabetLayoutParams);

		final String[] alphabet = new String[] { "A", "B", "C", "D", "E", "F",
				"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "L",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_HORIZONTAL;
		for (int i = 0, count = alphabet.length; i < count; i++) {
			TextView textView = new TextView(context);
			textView.setTextColor(Color.argb(150, 150, 150, 150));
			textView.setTextSize(convertDIP2PX(10));
			textView.setText(alphabet[i]);
			textView.setGravity(Gravity.CENTER);
			textView.setLayoutParams(params);
			textView.setTag(i + 1);
			alphabetLayout.addView(textView);
		}
		alphabetLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					alphabetLayout.setBackgroundColor(Color.argb(50, 100, 200,
							100));
					int l = (int) (event.getY() / (alphabetLayout.getHeight() / 26));
					if (l > 25)
						l = 25;
					else if (l < 0)
						l = 0;
					int pos = positionListener.getPosition(alphabet[l]);
					if (pos != -1) {
						mTextView.setText(alphabet[l]);
						mTextView.setVisibility(View.VISIBLE);
						mHandler.removeCallbacks(mHideIndicator);
						mHandler.postDelayed(mHideIndicator, indicatorDuration);
						// mListView.requestFocusFromTouch();
						mListView.setSelection(pos);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					l = (int) ((event.getY() + alphabetLayout.getHeight() / 26 / 2) / (alphabetLayout
							.getHeight() / 26));
					if (l > 25)
						l = 25;
					else if (l < 0)
						l = 0;
					pos = positionListener.getPosition(alphabet[l]);
					if (pos != -1) {
						mTextView.setText(alphabet[l]);
						mTextView.setVisibility(View.VISIBLE);
						mHandler.removeCallbacks(mHideIndicator);
						mHandler.postDelayed(mHideIndicator, indicatorDuration);
						// mListView.requestFocusFromTouch();
						mListView.setSelection(pos);
					}
					break;
				case MotionEvent.ACTION_UP:
					alphabetLayout.setBackgroundResource(0);
					break;
				}
				return true;
			}
		});
	}

	public int convertDIP2PX(float dip) {
		return (int) (dip * screenDensity + 0.5f * (dip >= 0 ? 1 : -1));
	}

	public static interface AlphabetPositionListener {
		public static final int UNKNOW = -1;

		public int getPosition(String letter);
	}
}