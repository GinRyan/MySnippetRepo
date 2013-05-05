package com.krislq.cache.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.krislq.cache.R;
import com.krislq.cache.util.L;

public class ScrollToFootRefreshListView extends ListView implements OnScrollListener{
	protected Context mContext;
	private View mFooterView;
	private OnRefreshListener mRefreshListener;
	private boolean isRefreshing = false;
	private LinearLayout layoutFooter;
	public ScrollToFootRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public ScrollToFootRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ScrollToFootRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private void init(Context context) {
		mFooterView = LayoutInflater.from(context).inflate(R.layout.scroll_to_refresh_footer, this, false);
		addFooterView(mFooterView, null, false);
		//mFooterView.setVisibility(View.INVISIBLE);
		layoutFooter = (LinearLayout) mFooterView.findViewById(R.id.scroll_to_refresh_footer);
		layoutFooter.setVisibility(View.GONE);
		setOnScrollListener(this);
	}
	
	public void setOnRefreshListner(ScrollToFootRefreshListView.OnRefreshListener onRefreshListener) {
		this.mRefreshListener = onRefreshListener;
	}
	
	/**
	 * call this method when refresh completed
	 */
	public void onRefreshCompleted() {
		isRefreshing = false;
		layoutFooter.setVisibility(View.GONE);
	}
	/**
	 * check whether it is refreshing
	 * @return
	 */
	public boolean isRefreshing() {
		return isRefreshing;
	}
	
	public void hideLineInFooter() {
		View line = mFooterView.findViewById(R.id.line);
		line.setVisibility(View.GONE);
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_FLING || scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				if(mRefreshListener != null && !isRefreshing) {
					layoutFooter.setVisibility(View.VISIBLE);
					isRefreshing = true;
					L.i("isrefreshing-->" + isRefreshing);
					mRefreshListener.onRefresh();
				}
			}
		}
	}
	
	/**
	 * 
	 * @author Oscar
	 *
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}
}
