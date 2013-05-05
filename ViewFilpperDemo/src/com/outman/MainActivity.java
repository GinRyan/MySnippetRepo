package com.outman;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.outman.tools.DataRes;

public class MainActivity extends Activity implements OnGestureListener {

	static final int FLING_MIN_DISTANCE = 120;

	private String[][] subTitles = { { "法律相关信息", "getEngineInfo" },
			{ "车速和油耗", "getSpeedInfo" } };
	//ViewFlipper实例
	private ViewFlipper flipper;
	//监听手势
	private GestureDetector detector;

	private List<Car> cars;

	{
		cars = new ArrayList<Car>();
		Car car = new Car();
		car.setType("2011年8月12日， 最高人民法院召开新闻发布会，通报适用《最高人民法院关于适用<中华人民共和国婚姻法>若干问题的解释（三）》有关情况。解释指出，以个人财产支付首付款并在银行贷款，婚后用夫妻共同财产还贷，不动产登记于首付款支付方名下的，人民法院可以判决该不动产归产权登记一方。《最高人民法院关于适用〈中华人民共和国婚姻法〉若干问题的解释（三）》已于2011年7月4日由最高人民法院审判委员会第1525次会议通过，自2011年8月13日起施行。");
		cars.add(car);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//初始化GestureDetector
		detector = new GestureDetector(this);
		//初始化ViewFlipper
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);
			 Car car = cars.get(0);
			// 添加布局
			LinearLayout frame = (LinearLayout) this.getLayoutInflater()
					.inflate(R.layout.frame, null);
			TextView textView = (TextView) frame.findViewById(R.id.TextView01);
			textView.setText(car.getType());
			//添加子页面到ViewFlipper
			flipper.addView(frame);

		LinearLayout frame1 = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.main02, null);
		ListView listView = (ListView) frame1.findViewById(R.id.myList);
		//ListView适配器
		SimpleAdapter adapter = new SimpleAdapter(this, DataRes.getData(),
				R.layout.listview, new String[] { "title", "info", "image",
						"image1", "text" }, new int[] { R.id.title, R.id.info,
						R.id.image, R.id.image1, R.id.text });
		listView.setAdapter(adapter);
		//添加子页面到ViewFlipper
		flipper.addView(frame1);
	}

	/**
	 * 这里没用onTouchEvent(MotionEvent ev)，因为是要用到ListView控件,ListView已经实现
	 * 了手势监听，所以冲突。
	 */
	
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		return detector.onTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	/**
	 * 监听手势
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		ViewFlipper flipper2 = (ViewFlipper) this.flipper.getCurrentView()
				.findViewWithTag("flipper2");
		//向左
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			this.flipper.showNext();
			return true;
			//向右
		} else if (e1.getX() - e2.getX() < -FLING_MIN_DISTANCE) {
			//设置动画效果
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			this.flipper.showPrevious();
			return true;
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

}
