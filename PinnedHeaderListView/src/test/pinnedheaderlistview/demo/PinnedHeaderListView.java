package test.pinnedheaderlistview.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 支持ListView置顶功能
 */
public class PinnedHeaderListView extends ListView {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private View mHeaderView;
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private boolean mDrawFlag = true;
	private PinnedHeaderAdapter mPinnedHeaderAdapter;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public PinnedHeaderListView(Context context) {
		super(context);
	}
	
	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PinnedHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * 设置置顶的Header View
	 * 
	 * @param pHeader
	 */
	public void setPinnedHeader(View pHeader) {
		mHeaderView = pHeader;
		
		requestLayout();
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		
		mPinnedHeaderAdapter = (PinnedHeaderAdapter) adapter;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// 三个覆写方法负责在当前窗口显示inflate创建的Header View
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if (null != mHeaderView) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mMeasuredWidth = mHeaderView.getMeasuredWidth();
			mMeasuredHeight = mHeaderView.getMeasuredHeight();
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
		if (null != mHeaderView) {
			mHeaderView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
			controlPinnedHeader(getFirstVisiblePosition());
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		
		if (null != mHeaderView && mDrawFlag) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}
	
	
	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * HeaderView三种状态的具体处理
	 * 
	 * @param position
	 */
	public void controlPinnedHeader(int position) {
		if (null == mHeaderView) {
			return;
		}
		
		int pinnedHeaderState = mPinnedHeaderAdapter.getPinnedHeaderState(position);
		switch (pinnedHeaderState) {
		case PinnedHeaderAdapter.PINNED_HEADER_GONE:
			mDrawFlag = false;
			break;

		case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE:
			mPinnedHeaderAdapter.configurePinnedHeader(mHeaderView, position, 0);
			mDrawFlag = true;
			mHeaderView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
			break;
			
		case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP:
			mPinnedHeaderAdapter.configurePinnedHeader(mHeaderView, position, 0);
			mDrawFlag = true;
			
			// 移动位置
			View topItem = getChildAt(0);
			
			if (null != topItem) {
				int bottom = topItem.getBottom();
				int height = mHeaderView.getHeight();
				
				int y;
				if (bottom < height) {
					y = bottom - height;
				}else {
					y = 0;
				} 

				if (mHeaderView.getTop() != y) {
					mHeaderView.layout(0, y, mMeasuredWidth, mMeasuredHeight + y);
				}
	
			}			
			break;
		}
		
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public interface PinnedHeaderAdapter {
		
		public static final int PINNED_HEADER_GONE = 0;
		
		public static final int PINNED_HEADER_VISIBLE = 1;
		
		public static final int PINNED_HEADER_PUSHED_UP = 2;
		
		int getPinnedHeaderState(int position);
		
		void configurePinnedHeader(View headerView, int position, int alpaha);
	}


	

}
