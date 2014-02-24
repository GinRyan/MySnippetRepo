package mnc.com.cn;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class testViewPager extends Activity {
	private ViewPager myViewPager;

	private MyPagerAdapter myAdapter;

	private LayoutInflater mInflater;
	private List<View> mListViews;
	private View layout1 = null;
	private View layout2 = null;
	private View layout3 = null;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Message msg;
	private BtnHandler btnhandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_layout);
		myAdapter = new MyPagerAdapter();
		myViewPager = (ViewPager) findViewById(R.id.viewpagerLayout);
		myViewPager.setAdapter(myAdapter);
		btnhandler = new BtnHandler();
		
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn1.setBackgroundResource(Color.TRANSPARENT);
//		btn2.setBackgroundResource(Color.TRANSPARENT);
		btn2.setBackgroundResource(R.color.red);
		btn3.setBackgroundResource(Color.TRANSPARENT);

		mListViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		layout1 = mInflater.inflate(R.layout.layout1, null);
		layout2 = mInflater.inflate(R.layout.layout2, null);
		layout3 = mInflater.inflate(R.layout.layout3, null);

		mListViews.add(layout1);
		mListViews.add(layout2);
		mListViews.add(layout3);

		// 初始化当前显示的view
		myViewPager.setCurrentItem(1);

		// 初始化第二个view的信息
		EditText v2EditText = (EditText) layout2.findViewById(R.id.editText1);
		v2EditText.setText("动态设置第二个view的值");

		myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				Log.d("k", "onPageSelected - " + arg0);
				// activity从1到2滑动，2被加载后掉用此方法
				View v = mListViews.get(arg0);
				EditText editText = (EditText) v.findViewById(R.id.editText1);
				editText.setText("动态设置#" + arg0 + "edittext控件的值");
				switch (arg0) {
				case 0:
					testViewPager.this.msg = testViewPager.this.btnhandler.obtainMessage();
					testViewPager.this.msg.arg1=0;
					testViewPager.this.msg.sendToTarget();
					break;
				case 1:
					testViewPager.this.msg = testViewPager.this.btnhandler.obtainMessage();
					testViewPager.this.msg.arg1=1;
					testViewPager.this.msg.sendToTarget();
					break;
				case 2:
					testViewPager.this.msg = testViewPager.this.btnhandler.obtainMessage();
					testViewPager.this.msg.arg1=2;
					testViewPager.this.msg.sendToTarget();
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.d("k", "onPageScrolled - " + arg0);
				// 从1到2滑动，在1滑动前调用
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				Log.d("k", "onPageScrollStateChanged - " + arg0);
				// 状态有三个0空闲，1是增在滑行中，2目标加载完毕
				/**
				 * Indicates that the pager is in an idle, settled state. The
				 * current page is fully in view and no animation is in
				 * progress.
				 */
				// public static final int SCROLL_STATE_IDLE = 0;
				/**
				 * Indicates that the pager is currently being dragged by the
				 * user.
				 */
				// public static final int SCROLL_STATE_DRAGGING = 1;
				/**
				 * Indicates that the pager is in the process of settling to a
				 * final position.
				 */
				// public static final int SCROLL_STATE_SETTLING = 2;

			}
		});

	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			Log.d("k", "destroyItem");
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
			Log.d("k", "finishUpdate");
		}

		@Override
		public int getCount() {
			Log.d("k", "getCount");
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			Log.d("k", "instantiateItem");
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			Log.d("k", "isViewFromObject");
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			Log.d("k", "restoreState");
		}

		@Override
		public Parcelable saveState() {
			Log.d("k", "saveState");
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			Log.d("k", "startUpdate");
		}

	}
	
	private class BtnHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 0:
				btn1.setBackgroundResource(R.color.red);
				btn2.setBackgroundResource(R.color.transparent);
				btn3.setBackgroundResource(R.color.transparent);
//				Toast.makeText(testViewPager.this, "0", 0).show();
				break;
			case 1:
				btn2.setBackgroundResource(R.color.red);
				btn1.setBackgroundResource(R.color.transparent);
				btn3.setBackgroundResource(R.color.transparent);
				break;
			case 2:
				btn3.setBackgroundResource(R.color.red);
				btn2.setBackgroundResource(R.color.transparent);
				btn1.setBackgroundResource(R.color.transparent);
				break;

			default:
				break;
			}
		}
		
	}

}