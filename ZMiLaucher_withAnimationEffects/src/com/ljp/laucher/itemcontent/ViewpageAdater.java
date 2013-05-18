package com.ljp.laucher.itemcontent;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewpageAdater extends PagerAdapter {

	
	private List<View> views;
	
	public ViewpageAdater(List<View> views){
		this.views=views;
	}
	
	@Override
	public void destroyItem(View view, int position, Object arg2) {
		// TODO Auto-generated method stub
		( (ViewPager) view).removeView(views.get(position));
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public Object instantiateItem(View view, int position) {
		// TODO Auto-generated method stub
		
		//System.out.println("instantiate:"+position);

			( (ViewPager) view).addView(views.get(position), 0);
			return views.get(position);
//		( (ViewPager) view).addView(views.get(position), 0);
//		return views.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}


}
