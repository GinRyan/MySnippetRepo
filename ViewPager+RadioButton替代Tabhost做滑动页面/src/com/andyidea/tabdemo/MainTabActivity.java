package com.andyidea.tabdemo;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainTabActivity extends FragmentActivity implements OnCheckedChangeListener {
	RadioGroup main_radio;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintabs);
		main_radio = (RadioGroup) findViewById(R.id.main_radio);
		((RadioButton) findViewById(R.id.radio_button0)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button1)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button2)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button3)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button4)).setOnCheckedChangeListener(this);
		vPager = (ViewPager) findViewById(R.id.pager);
	}

	@Override
	protected void onStart() {
		super.onStart();

		Fragment1 f1 = new Fragment1();
		Fragment2 f2 = new Fragment2();
		Fragment3 f3 = new Fragment3();
		Fragment4 f4 = new Fragment4();
		Fragment5 f5 = new Fragment5();
		fragments.add(f1);
		fragments.add(f2);
		fragments.add(f3);
		fragments.add(f4);
		fragments.add(f5);

		vPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), fragments));
		vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				System.out.println(arg0);
				switch (arg0) {
				case 0:
					((RadioButton) findViewById(R.id.radio_button0)).setChecked(true);
					break;
				case 1:
					((RadioButton) findViewById(R.id.radio_button1)).setChecked(true);
					break;
				case 2:
					((RadioButton) findViewById(R.id.radio_button2)).setChecked(true);
					break;
				case 3:
					((RadioButton) findViewById(R.id.radio_button3)).setChecked(true);
					break;
				case 4:
					((RadioButton) findViewById(R.id.radio_button4)).setChecked(true);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	ViewPager vPager;

	/**
	 * set Ò³Ãæ1~5
	 * 
	 * @param i
	 */
	public void setPager(int i) {
		vPager.setCurrentItem(i - 1);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_button0:
				setPager(1);
				break;
			case R.id.radio_button1:
				setPager(2);
				break;
			case R.id.radio_button2:
				setPager(3);
				break;
			case R.id.radio_button3:
				setPager(4);
				break;
			case R.id.radio_button4:
				setPager(5);
				break;
			}
		}
	}

	class TabPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> fragments;

		public TabPagerAdapter(FragmentManager fm, ArrayList<Fragment> frags) {
			super(fm);
			this.fragments = frags;
		}

		@Override
		public Fragment getItem(int pos) {
			return fragments.get(pos);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}

	/**
	 * Æ¬¶Î»ùÀà
	 * 
	 * @author Liang
	 * 
	 */
	public static class BaseFragment extends Fragment {

	}

	public static class Fragment1 extends BaseFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.main, container, false);
			TextView text = (TextView) v.findViewById(R.id.text);
			text.setText("1");
			return v;
		}

	}

	public static class Fragment2 extends BaseFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.main, container, false);
			TextView text = (TextView) v.findViewById(R.id.text);
			text.setText("2");
			return v;
		}
	}

	public static class Fragment3 extends BaseFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.main, container, false);
			TextView text = (TextView) v.findViewById(R.id.text);
			text.setText("3");
			return v;
		}
	}

	public static class Fragment4 extends BaseFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.main, container, false);
			TextView text = (TextView) v.findViewById(R.id.text);
			text.setText("4");
			return v;
		}
	}

	public static class Fragment5 extends BaseFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.main, container, false);
			TextView text = (TextView) v.findViewById(R.id.text);
			text.setText("5");
			return v;
		}

	}
}