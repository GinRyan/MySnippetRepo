package com.icss.viewpagerdemos;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ViewPagerDemosActivity extends Activity {
    /** Called when the activity is first created. */
	private ViewPager viewPager ;
	private PagerTitleStrip pts; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewPager = (ViewPager)this.findViewById(R.id.viewPager);
        pts = (PagerTitleStrip) this.findViewById(R.id.pagerTitleStrip);
        
        LayoutInflater li = LayoutInflater.from(this);
//        li.inflate(R.layout.a, null);
//        li.inflate(R.layout.b, null);
//        li.inflate(R.layout.c, null);
        
       final ArrayList<View> arrayView = new ArrayList<View>();
        arrayView.add(li.inflate(R.layout.a, null));
        arrayView.add(li.inflate(R.layout.b, null));
        arrayView.add(li.inflate(R.layout.c, null));
        arrayView.add(li.inflate(R.layout.d, null));
        arrayView.add(li.inflate(R.layout.e, null));
        arrayView.add(li.inflate(R.layout.f, null));
        arrayView.add(li.inflate(R.layout.g, null));
        arrayView.add(li.inflate(R.layout.h, null));
        
        final ArrayList<String> titleArray = new ArrayList<String>();
        titleArray.add("美女");
        titleArray.add("萌女");
        titleArray.add("性感");
        titleArray.add("制服");
        titleArray.add("美眉");
        titleArray.add("妖娆");
        titleArray.add("清晰");
        titleArray.add("清纯");
     
        PagerAdapter apdter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return arrayView.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				((ViewPager)container).removeView(arrayView.get(position));
			}

			@Override
			public CharSequence getPageTitle(int position) {
				// TODO Auto-generated method stub
				return titleArray.get(position);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				((ViewPager)container).addView(arrayView.get(position));
				return arrayView.get(position);
			}
			
		};
		viewPager.setAdapter(apdter);
    }
}