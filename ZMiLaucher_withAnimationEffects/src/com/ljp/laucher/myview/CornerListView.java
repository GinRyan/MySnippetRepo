package com.ljp.laucher.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ljp.laucher.R;

/** * Բ��ListView */
public class CornerListView extends ListView {
	int itemnum;
	public CornerListView(Context context) {
		this(context, null);
	}

	public CornerListView(Context context, AttributeSet attrs) {
		super(context, attrs); // ���listview��Բ�Ǳ���
		//this.setBackgroundResource(R.drawable.set_backdrop);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			itemnum = pointToPosition(x, y);
			if (itemnum == AdapterView.INVALID_POSITION)
				break;
			else {
				if (itemnum == 0) {
					if (itemnum == (getAdapter().getCount() - 1)) {
						setSelector(R.drawable.selector_list_corner_round);
					} else {
						setSelector(R.drawable.selector_list_corner_top);
					}
				} else if (itemnum == (getAdapter().getCount() - 1))
					setSelector(R.drawable.selector_list_corner_bottom);
				else {
					setSelector(R.drawable.selector_list_corner_center);
				}
				break;
			}
		case MotionEvent.ACTION_UP:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
