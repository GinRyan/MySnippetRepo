package com.javatiger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class QQTab extends Activity {
	private RelativeLayout layout;

	private RelativeLayout layout1;
	private RelativeLayout layout2;
	private RelativeLayout layout3;

	private ImageView tab1;
	private ImageView tab2;
	private ImageView tab3;

	private ImageView first;
	private int current = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qqtab);
		initUI();
	}

	private void initUI() {
		layout = (RelativeLayout) findViewById(R.id.root);

		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		layout3 = (RelativeLayout) findViewById(R.id.layout3);

		tab1 = (ImageView) findViewById(R.id.tab1);
		tab1.setOnClickListener(onClickListener);
		tab2 = (ImageView) findViewById(R.id.tab2);
		tab2.setOnClickListener(onClickListener);
		tab3 = (ImageView) findViewById(R.id.tab3);
		tab3.setOnClickListener(onClickListener);

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		rl.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

		first = new ImageView(this);

		first.setTag("first");
		first.setImageResource(R.drawable.topbar_select);

		switch (current) {
		case 1:
			layout1.addView(first, rl);
			current = R.id.tab1;
			break;
		case 2:
			layout2.addView(first, rl);
			current = R.id.tab2;
			break;
		case 3:
			layout3.addView(first, rl);
			current = R.id.tab3;
			break;
		default:
			break;
		}
	}

	private boolean isAdd = false;
	private int select_width; // top_select_width
	private int select_height; // top_select_height
	private int firstLeft;
	private int startLeft;

	private void replace() {
		switch (current) {
		case R.id.tab1:
			changeTop(layout1);
			break;
		case R.id.tab2:
			changeTop(layout2);
			break;
		case R.id.tab3:
			changeTop(layout3);
			break;
		default:
			break;
		}
	}

	private void changeTop(RelativeLayout relativeLayout) {
		ImageView old = (ImageView) relativeLayout.findViewWithTag("first");
		select_width = old.getWidth();
		select_height = old.getHeight();

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				select_width, select_height);
		rl.leftMargin = old.getLeft()
				+ ((RelativeLayout) old.getParent()).getLeft();
		rl.topMargin = old.getTop()
				+ ((RelativeLayout) old.getParent()).getTop();

		firstLeft = old.getLeft()
				+ ((RelativeLayout) old.getParent()).getLeft();

		ImageView iv = new ImageView(this);

		iv.setTag("move");
		iv.setImageResource(R.drawable.topbar_select);

		layout.addView(iv, rl);
		relativeLayout.removeView(old);
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isAdd) {
				replace();
				isAdd = true;
			}

			ImageView top_select = (ImageView) layout.findViewWithTag("move");
			int tabLeft;
			int endLeft = 0;

			boolean run = false;

			switch (v.getId()) {
			case R.id.tab1:
				if (current != R.id.tab1) {
					tabLeft = ((RelativeLayout) tab1.getParent()).getLeft()
							+ tab1.getLeft() + tab1.getWidth() / 2;
					endLeft = tabLeft - select_width / 2;
					current = R.id.tab1;
					run = true;
				}
				break;
			case R.id.tab2:
				if (current != R.id.tab2) {
					tabLeft = ((RelativeLayout) tab2.getParent()).getLeft()
							+ tab2.getLeft() + tab2.getWidth() / 2;
					endLeft = tabLeft - select_width / 2;
					current = R.id.tab2;
					run = true;
				}
				break;
			case R.id.tab3:
				if (current != R.id.tab3) {
					tabLeft = ((RelativeLayout) tab3.getParent()).getLeft()
							+ tab3.getLeft() + tab3.getWidth() / 2;
					endLeft = tabLeft - select_width / 2;
					current = R.id.tab3;
					run = true;
				}
				break;
			default:
				break;
			}

			if (run) {
				TranslateAnimation animation = new TranslateAnimation(
						startLeft, endLeft - firstLeft, 0, 0);
				startLeft = endLeft - firstLeft;
				animation.setDuration(400);
				animation.setFillAfter(true);
				top_select.bringToFront();
				top_select.startAnimation(animation);
			}
		}
	};
}