/*     */ package android.support.v4.view;
/*     */ 
/*     */ import java.util.ArrayList;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
/*     */ 
/*     */ public class ViewPager extends ViewGroup
/*     */ {
/*       private static final String TAG = "ViewPager";
  private static final boolean DEBUG = false;
/*       private static final boolean USE_CACHE = false;*/ 
/*  58 */   private final ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();
/*     */   private PagerAdapter mAdapter;
/*     */   private int mCurItem;
/*  62 */   private int mRestoredCurItem = -1;
/*  63 */   private Parcelable mRestoredAdapterState = null;
/*  64 */   private ClassLoader mRestoredClassLoader = null;
/*     */   private Scroller mScroller;
/*     */   private PagerAdapter.DataSetObserver mObserver;
/*     */   private int mChildWidthMeasureSpec;
/*     */   private int mChildHeightMeasureSpec;
/*     */   private boolean mInLayout;
/*     */   private boolean mScrollingCacheEnabled;
/*     */   private boolean mPopulatePending;
/*     */   private boolean mScrolling;
/*     */   private boolean mIsBeingDragged;
/*     */   private boolean mIsUnableToDrag;
/*     */   private int mTouchSlop;
/*     */   private float mInitialMotionX;
/*     */   private float mLastMotionX;
/*     */   private float mLastMotionY;
/*  90 */   private int mActivePointerId = -1;
/*       private static final int INVALID_POINTER = -1;*/ 
/*     */   private VelocityTracker mVelocityTracker;
/*     */   private int mMinimumVelocity;
/*     */   private int mMaximumVelocity;
/*     */   private OnPageChangeListener mOnPageChangeListener;
/*     */   public static final int SCROLL_STATE_IDLE = 0;
/*     */   public static final int SCROLL_STATE_DRAGGING = 1;
/*     */   public static final int SCROLL_STATE_SETTLING = 2;
/* 122 */   private int mScrollState = 0;
			private int mduration = 888;
/*     */ 
/*     */   public ViewPager(Context context)
/*     */   {
/* 184 */     super(context);
/* 185 */     initViewPager();
/*     */   }
/*     */ 
/*     */   public ViewPager(Context context, AttributeSet attrs) {
/* 189 */     super(context, attrs);
/* 190 */     initViewPager();
/*     */   }
/*     */ 
/*     */   void initViewPager() {
/* 194 */     setWillNotDraw(false);
/* 195 */     this.mScroller = new Scroller(getContext());
/* 196 */     ViewConfiguration configuration = ViewConfiguration.get(getContext());
/* 197 */     this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
/* 198 */     this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
/* 199 */     this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
/*     */   }
/*     */ 
/*     */   private void setScrollState(int newState) {
/* 203 */     if (this.mScrollState == newState) {
/* 204 */       return;
/*     */     }
/*     */ 
/* 207 */     this.mScrollState = newState;
/* 208 */     if (this.mOnPageChangeListener != null)
/* 209 */       this.mOnPageChangeListener.onPageScrollStateChanged(newState);
/*     */   }
/*     */ 	public void setDuration(int time){
				this.mduration =time;
			}
/*     */   public void setAdapter(PagerAdapter adapter)
/*     */   {
/* 214 */     if (this.mAdapter != null) {
/* 215 */       this.mAdapter.setDataSetObserver(null);
/*     */     }
/*     */ 
/* 218 */     this.mAdapter = adapter;
/*     */ 
/* 220 */     if (this.mAdapter != null) {
/* 221 */       if (this.mObserver == null) {
/* 222 */         this.mObserver = new DataSetObserver();
/*     */       }
/* 224 */       this.mAdapter.setDataSetObserver(this.mObserver);
/* 225 */       this.mPopulatePending = false;
/* 226 */       if (this.mRestoredCurItem >= 0) {
/* 227 */         this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
/* 228 */         setCurrentItemInternal(this.mRestoredCurItem, false, true);
/* 229 */         this.mRestoredCurItem = -1;
/* 230 */         this.mRestoredAdapterState = null;
/* 231 */         this.mRestoredClassLoader = null;
/*     */       } else {
/* 233 */         populate();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public PagerAdapter getAdapter() {
/* 239 */     return this.mAdapter;
/*     */   }
/*     */ 
/*     */   public void setCurrentItem(int item) {
/* 243 */     this.mPopulatePending = false;
/* 244 */     setCurrentItemInternal(item, true, false);
/*     */   }
/*     */ 
/*     */   void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
/* 248 */     if ((this.mAdapter == null) || (this.mAdapter.getCount() <= 0)) {
/* 249 */       setScrollingCacheEnabled(false);
/* 250 */       return;
/*     */     }
/* 252 */     if ((!always) && (this.mCurItem == item) && (this.mItems.size() != 0)) {
/* 253 */       setScrollingCacheEnabled(false);
/* 254 */       return;
/*     */     }
/* 256 */     if (item < 0)
/* 257 */       item = 0;
/* 258 */     else if (item >= this.mAdapter.getCount()) {
/* 259 */       item = this.mAdapter.getCount() - 1;
/*     */     }
/* 261 */     if ((item > this.mCurItem + 1) || (item < this.mCurItem - 1))
/*     */     {
/* 265 */       for (int i = 0; i < this.mItems.size(); i++) {
/* 266 */         ((ItemInfo)this.mItems.get(i)).scrolling = true;
/*     */       }
/*     */     }
/* 269 */     boolean dispatchSelected = this.mCurItem != item;
/* 270 */     this.mCurItem = item;
/* 271 */     populate();
/* 272 */     if (smoothScroll) {
/* 273 */       smoothScrollTo(getWidth() * item, 0);
/* 274 */       if ((dispatchSelected) && (this.mOnPageChangeListener != null))
/* 275 */         this.mOnPageChangeListener.onPageSelected(item);
/*     */     }
/*     */     else {
/* 278 */       if ((dispatchSelected) && (this.mOnPageChangeListener != null)) {
/* 279 */         this.mOnPageChangeListener.onPageSelected(item);
/*     */       }
/* 281 */       completeScroll();
/* 282 */       scrollTo(getWidth() * item, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setOnPageChangeListener(OnPageChangeListener listener) {
/* 287 */     this.mOnPageChangeListener = listener;
/*     */   }
/*     */ 
/*     */   void smoothScrollTo(int x, int y)
/*     */   {
/* 297 */     if (getChildCount() == 0)
/*     */     {
/* 299 */       setScrollingCacheEnabled(false);
/* 300 */       return;
/*     */     }
/* 302 */     int sx = getScrollX();
/* 303 */     int sy = getScrollY();
/* 304 */     int dx = x - sx;
/* 305 */     int dy = y - sy;
/* 306 */     if ((dx == 0) && (dy == 0)) {
/* 307 */       completeScroll();
/* 308 */       return;
/*     */     }
/*     */ 
/* 311 */     setScrollingCacheEnabled(true);
/* 312 */     this.mScrolling = true;
/* 313 */     setScrollState(2);
/* 314 */     this.mScroller.startScroll(sx, sy, dx, dy,mduration);
/* 315 */     invalidate();
/*     */   }
/*     */ 
/*     */   void addNewItem(int position, int index) {
/* 319 */     ItemInfo ii = new ItemInfo();
/* 320 */     ii.position = position;
/* 321 */     ii.object = this.mAdapter.instantiateItem(this, position);
/* 322 */     if (index < 0)
/* 323 */       this.mItems.add(ii);
/*     */     else
/* 325 */       this.mItems.add(index, ii);
/*     */   }
/*     */ 
/*     */   void dataSetChanged()
/*     */   {
/* 332 */     boolean needPopulate = (this.mItems.isEmpty()) && (this.mAdapter.getCount() > 0);
/* 333 */     int newCurrItem = -1;
/*     */ 
/* 335 */     for (int i = 0; i < this.mItems.size(); i++) {
/* 336 */       ItemInfo ii = (ItemInfo)this.mItems.get(i);
/* 337 */       int newPos = this.mAdapter.getItemPosition(ii.object);
/*     */ 
/* 339 */       if (newPos == -1)
/*     */       {
/*     */         continue;
/*     */       }
/* 343 */       if (newPos == -2) {
/* 344 */         this.mItems.remove(i);
/* 345 */         i--;
/* 346 */         this.mAdapter.destroyItem(this, ii.position, ii.object);
/* 347 */         needPopulate = true;
/*     */ 
/* 349 */         if (this.mCurItem != ii.position)
/*     */           continue;
/* 351 */         newCurrItem = Math.max(0, Math.min(this.mCurItem, this.mAdapter.getCount() - 1));
/*     */       }
/* 356 */       else if (ii.position != newPos) {
/* 357 */         if (ii.position == this.mCurItem)
/*     */         {
/* 359 */           newCurrItem = newPos;
/*     */         }
/*     */ 
/* 362 */         ii.position = newPos;
/* 363 */         needPopulate = true;
/*     */       }
/*     */     }
/*     */ 
/* 367 */     if (newCurrItem >= 0)
/*     */     {
/* 369 */       setCurrentItemInternal(newCurrItem, false, true);
/* 370 */       needPopulate = true;
/*     */     }
/* 372 */     if (needPopulate) {
/* 373 */       populate();
/* 374 */       requestLayout();
/*     */     }
/*     */   }
/*     */ 
/*     */   void populate() {
/* 379 */     if (this.mAdapter == null) {
/* 380 */       return;
/*     */     }
/*     */ 
/* 387 */     if (this.mPopulatePending)
/*     */     {
/* 389 */       return;
/*     */     }
/*     */ 
/* 395 */     if (getWindowToken() == null) {
/* 396 */       return;
/*     */     }
/*     */ 
/* 399 */     this.mAdapter.startUpdate(this);
/*     */ 
/* 401 */     int startPos = this.mCurItem > 0 ? this.mCurItem - 1 : this.mCurItem;
/* 402 */     int N = this.mAdapter.getCount();
/* 403 */     int endPos = this.mCurItem < N - 1 ? this.mCurItem + 1 : N - 1;
/*     */ 
/* 408 */     int lastPos = -1;
/* 409 */     for (int i = 0; i < this.mItems.size(); i++) {
/* 410 */       ItemInfo ii = (ItemInfo)this.mItems.get(i);
/* 411 */       if (((ii.position < startPos) || (ii.position > endPos)) && (!ii.scrolling))
/*     */       {
/* 413 */         this.mItems.remove(i);
/* 414 */         i--;
/* 415 */         this.mAdapter.destroyItem(this, ii.position, ii.object);
/* 416 */       } else if ((lastPos < endPos) && (ii.position > startPos))
/*     */       {
/* 420 */         lastPos++;
/* 421 */         if (lastPos < startPos) {
/* 422 */           lastPos = startPos;
/*     */         }
/* 424 */         while ((lastPos <= endPos) && (lastPos < ii.position))
/*     */         {
/* 426 */           addNewItem(lastPos, i);
/* 427 */           lastPos++;
/* 428 */           i++;
/*     */         }
/*     */       }
/* 431 */       lastPos = ii.position;
/*     */     }
/*     */ 
/* 435 */     lastPos = this.mItems.size() > 0 ? ((ItemInfo)this.mItems.get(this.mItems.size() - 1)).position : -1;
/* 436 */     if (lastPos < endPos) {
/* 437 */       lastPos++;
/* 438 */       lastPos = lastPos > startPos ? lastPos : startPos;
/* 439 */       while (lastPos <= endPos)
/*     */       {
/* 441 */         addNewItem(lastPos, -1);
/* 442 */         lastPos++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 453 */     this.mAdapter.finishUpdate(this);
/*     */   }
/*     */ 
/*     */   public Parcelable onSaveInstanceState()
/*     */   {
/* 504 */     Parcelable superState = super.onSaveInstanceState();
/* 505 */     SavedState ss = new SavedState(superState);
/* 506 */     ss.position = this.mCurItem;
/* 507 */    if(this.mAdapter!=null) ss.adapterState = this.mAdapter.saveState();
/* 508 */     return ss;
/*     */   }
/*     */ 
/*     */   public void onRestoreInstanceState(Parcelable state)
/*     */   {
/* 513 */     if (!(state instanceof SavedState)) {
/* 514 */       super.onRestoreInstanceState(state);
/* 515 */       return;
/*     */     }
/*     */ 
/* 518 */     SavedState ss = (SavedState)state;
/* 519 */     super.onRestoreInstanceState(ss.getSuperState());
/*     */ 
/* 521 */     if (this.mAdapter != null) {
/* 522 */       this.mAdapter.restoreState(ss.adapterState, ss.loader);
/* 523 */       setCurrentItemInternal(ss.position, false, true);
/*     */     } else {
/* 525 */       this.mRestoredCurItem = ss.position;
/* 526 */       this.mRestoredAdapterState = ss.adapterState;
/* 527 */       this.mRestoredClassLoader = ss.loader;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addView(View child, int index, ViewGroup.LayoutParams params)
/*     */   {
/* 533 */     if (this.mInLayout) {
/* 534 */       addViewInLayout(child, index, params);
/* 535 */       child.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
/*     */     } else {
/* 537 */       super.addView(child, index, params);
/*     */     }
/*     */   }
/*     */ 
/*     */   ItemInfo infoForChild(View child)
/*     */   {
/* 550 */     for (int i = 0; i < this.mItems.size(); i++) {
/* 551 */       ItemInfo ii = (ItemInfo)this.mItems.get(i);
/* 552 */       if (this.mAdapter.isViewFromObject(child, ii.object)) {
/* 553 */         return ii;
/*     */       }
/*     */     }
/* 556 */     return null;
/*     */   }
/*     */ 
/*     */   protected void onAttachedToWindow()
/*     */   {
/* 561 */     super.onAttachedToWindow();
/* 562 */     if (this.mAdapter != null)
/* 563 */       populate();
/*     */   }
/*     */ 
/*     */   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
/*     */   {
/* 574 */     setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
/*     */ 
/* 578 */     this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), 1073741824);
/*     */ 
/* 580 */     this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), 1073741824);
/*     */ 
/* 584 */     this.mInLayout = true;
/* 585 */     populate();
/* 586 */     this.mInLayout = false;
/*     */ 
/* 589 */     int size = getChildCount();
/* 590 */     for (int i = 0; i < size; i++) {
/* 591 */       View child = getChildAt(i);
/* 592 */       if (child.getVisibility() == 8) {
/*     */         continue;
/*     */       }
/* 595 */       child.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void onSizeChanged(int w, int h, int oldw, int oldh)
/*     */   {
/* 602 */     super.onSizeChanged(w, h, oldw, oldh);
/*     */ 
/* 605 */     int scrollPos = this.mCurItem * w;
/* 606 */     if (scrollPos != getScrollX()) {
/* 607 */       completeScroll();
/* 608 */       scrollTo(scrollPos, getScrollY());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void onLayout(boolean changed, int l, int t, int r, int b)
/*     */   {
/* 614 */     this.mInLayout = true;
/* 615 */     populate();
/* 616 */     this.mInLayout = false;
/*     */ 
/* 618 */     int count = getChildCount();
/* 619 */     int width = r - l;
/*     */ 
/* 621 */     for (int i = 0; i < count; i++) {
/* 622 */       View child = getChildAt(i);
/*     */       ItemInfo ii;
/* 624 */       if ((child.getVisibility() != 8) && ((ii = infoForChild(child)) != null)) {
/* 625 */         int loff = width * ii.position;
/* 626 */         int childLeft = getPaddingLeft() + loff;
/* 627 */         int childTop = getPaddingTop();
/*     */ 
/* 631 */         child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void computeScroll()
/*     */   {
/* 641 */     if ((!this.mScroller.isFinished()) && 
/* 642 */       (this.mScroller.computeScrollOffset()))
/*     */     {
/* 644 */       int oldX = getScrollX();
/* 645 */       int oldY = getScrollY();
/* 646 */       int x = this.mScroller.getCurrX();
/* 647 */       int y = this.mScroller.getCurrY();
/*     */ 
/* 649 */       if ((oldX != x) || (oldY != y)) {
/* 650 */         scrollTo(x, y);
/*     */       }
/*     */ 
/* 653 */       if (this.mOnPageChangeListener != null) {
/* 654 */         int width = getWidth();
/* 655 */         int position = x / width;
/* 656 */         int offsetPixels = x % width;
/* 657 */         float offset = offsetPixels / width;
/* 658 */         this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
/*     */       }
/*     */ 
/* 662 */       invalidate();
/* 663 */       return;
/*     */     }
/*     */ 
/* 668 */     completeScroll();
/*     */   }
/*     */ 
/*     */   private void completeScroll()
/*     */   {
/*     */     boolean needPopulate;
/* 673 */     if ((needPopulate = this.mScrolling))
/*     */     {
/* 675 */       setScrollingCacheEnabled(false);
/* 676 */       this.mScroller.abortAnimation();
/* 677 */       int oldX = getScrollX();
/* 678 */       int oldY = getScrollY();
/* 679 */       int x = this.mScroller.getCurrX();
/* 680 */       int y = this.mScroller.getCurrY();
/* 681 */       if ((oldX != x) || (oldY != y)) {
/* 682 */         scrollTo(x, y);
/*     */       }
/* 684 */       setScrollState(0);
/*     */     }
/* 686 */     this.mPopulatePending = false;
/* 687 */     this.mScrolling = false;
/* 688 */     for (int i = 0; i < this.mItems.size(); i++) {
/* 689 */       ItemInfo ii = (ItemInfo)this.mItems.get(i);
/* 690 */       if (ii.scrolling) {
/* 691 */         needPopulate = true;
/* 692 */         ii.scrolling = false;
/*     */       }
/*     */     }
/* 695 */     if (needPopulate)
/* 696 */       populate();
/*     */   }
/*     */ 
/*     */   public boolean onInterceptTouchEvent(MotionEvent ev)
/*     */   {
/* 708 */     int action = ev.getAction() & 0xFF;
/*     */ 
/* 711 */     if ((action == 3) || (action == 1))
/*     */     {
/* 714 */       this.mIsBeingDragged = false;
/* 715 */       this.mIsUnableToDrag = false;
/* 716 */       this.mActivePointerId = -1;
/* 717 */       return false;
/*     */     }
/*     */ 
/* 722 */     if (action != 0) {
/* 723 */       if (this.mIsBeingDragged)
/*     */       {
/* 725 */         return true;
/*     */       }
/* 727 */       if (this.mIsUnableToDrag)
/*     */       {
/* 729 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 733 */     switch (action)
/*     */     {
/*     */     case 2:
/* 744 */       int activePointerId = this.mActivePointerId;
/* 745 */       if (activePointerId == -1)
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 750 */       int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
/* 751 */       float x = MotionEventCompat.getX(ev, pointerIndex);
/* 752 */       float dx = x - this.mLastMotionX;
/* 753 */       float xDiff = Math.abs(dx);
/* 754 */       float y = MotionEventCompat.getY(ev, pointerIndex);
/* 755 */       float yDiff = Math.abs(y - this.mLastMotionY);
/*     */ 
/* 758 */       if ((xDiff > this.mTouchSlop) && (xDiff > yDiff))
/*     */       {
/* 760 */         this.mIsBeingDragged = true;
/* 761 */         setScrollState(1);
/* 762 */         this.mLastMotionX = x;
/* 763 */         setScrollingCacheEnabled(true);
/*     */       } else {
/* 765 */         if (yDiff <= this.mTouchSlop)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 771 */         this.mIsUnableToDrag = true; } break;
/*     */     case 0:
/* 782 */       this.mLastMotionX = (this.mInitialMotionX = ev.getX());
/* 783 */       this.mLastMotionY = ev.getY();
/* 784 */       this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
/*     */ 
/* 786 */       if (this.mScrollState == 2)
/*     */       {
/* 788 */         this.mIsBeingDragged = true;
/* 789 */         this.mIsUnableToDrag = false;
/* 790 */         setScrollState(1);
/*     */       } else {
/* 792 */         completeScroll();
/* 793 */         this.mIsBeingDragged = false;
/* 794 */         this.mIsUnableToDrag = false;
/*     */       }
/*     */ 
/* 800 */       break;
/*     */     case 6:
/* 804 */       onSecondaryPointerUp(ev);
/*     */     }
/*     */ 
/* 812 */     return this.mIsBeingDragged;
/*     */   }
/*     */ 
/*     */   public boolean onTouchEvent(MotionEvent ev)
/*     */   {
/* 818 */     if ((ev.getAction() == 0) && (ev.getEdgeFlags() != 0))
/*     */     {
/* 821 */       return false;
/*     */     }
/*     */ 
/* 824 */     if ((this.mAdapter == null) || (this.mAdapter.getCount() == 0))
/*     */     {
/* 826 */       return false;
/*     */     }
/*     */ 
/* 829 */     if (this.mVelocityTracker == null) {
/* 830 */       this.mVelocityTracker = VelocityTracker.obtain();
/*     */     }
/* 832 */     this.mVelocityTracker.addMovement(ev);
/*     */ 
/* 834 */     int action = ev.getAction();
/*     */ 
/* 836 */     switch (action & 0xFF)
/*     */     {
/*     */     case 0:
/* 842 */       completeScroll();
/*     */ 
/* 845 */       this.mLastMotionX = (this.mInitialMotionX = ev.getX());
/* 846 */       this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
/* 847 */       break;
/*     */     case 2:
/* 850 */       if (!this.mIsBeingDragged) {
/* 851 */         int pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
/* 852 */         float x = MotionEventCompat.getX(ev, pointerIndex);
/* 853 */         float xDiff = Math.abs(x - this.mLastMotionX);
/* 854 */         float y = MotionEventCompat.getY(ev, pointerIndex);
/* 855 */         float yDiff = Math.abs(y - this.mLastMotionY);
/*     */ 
/* 857 */         if ((xDiff > this.mTouchSlop) && (xDiff > yDiff))
/*     */         {
/* 859 */           this.mIsBeingDragged = true;
/* 860 */           this.mLastMotionX = x;
/* 861 */           setScrollState(1);
/* 862 */           setScrollingCacheEnabled(true);
/*     */         }
/*     */       }
/* 865 */       if (!this.mIsBeingDragged)
/*     */         break;
/* 867 */       int activePointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
/*     */ 
/* 869 */       float x = MotionEventCompat.getX(ev, activePointerIndex);
/* 870 */       float deltaX = this.mLastMotionX - x;
/* 871 */       this.mLastMotionX = x;
/* 872 */       float scrollX = getScrollX() + deltaX;
/* 873 */       int width = getWidth();
/*     */ 
/* 875 */       float leftBound = Math.max(0, (this.mCurItem - 1) * width);
/* 876 */       float rightBound = Math.min(this.mCurItem + 1, this.mAdapter.getCount() - 1) * width;
/*     */ 
/* 878 */       if (scrollX < leftBound)
/* 879 */         scrollX = leftBound;
/* 880 */       else if (scrollX > rightBound) {
/* 881 */         scrollX = rightBound;
/*     */       }
/*     */ 
/* 884 */       this.mLastMotionX += scrollX - (int)scrollX;
/* 885 */       scrollTo((int)scrollX, getScrollY());
/* 886 */       if (this.mOnPageChangeListener != null) {
/* 887 */         int position = (int)scrollX / width;
/* 888 */         int positionOffsetPixels = (int)scrollX % width;
/* 889 */         float positionOffset = positionOffsetPixels / width;
/* 890 */         this.mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
/*     */       }
/*     */ 
/* 893 */       break;
/*     */     case 1:
/* 896 */       if (!this.mIsBeingDragged) break;
/* 897 */       VelocityTracker velocityTracker = this.mVelocityTracker;
/* 898 */       velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
/* 899 */       int initialVelocity = (int)VelocityTrackerCompat.getYVelocity(velocityTracker, this.mActivePointerId);
/*     */ 
/* 901 */       this.mPopulatePending = true;
/* 902 */       if ((Math.abs(initialVelocity) > this.mMinimumVelocity) || (Math.abs(this.mInitialMotionX - this.mLastMotionX) >= getWidth() / 3))
/*     */       {
/* 904 */         if (this.mLastMotionX > this.mInitialMotionX)
/* 905 */           setCurrentItemInternal(this.mCurItem - 1, true, true);
/*     */         else
/* 907 */           setCurrentItemInternal(this.mCurItem + 1, true, true);
/*     */       }
/*     */       else {
/* 910 */         setCurrentItemInternal(this.mCurItem, true, true);
/*     */       }
/*     */ 
/* 913 */       this.mActivePointerId = -1;
/* 914 */       endDrag();
/* 915 */       break;
/*     */     case 3:
/* 918 */       if (!this.mIsBeingDragged) break;
/* 919 */       setCurrentItemInternal(this.mCurItem, true, true);
/* 920 */       this.mActivePointerId = -1;
/* 921 */       endDrag(); break;
/*     */     case 5:
/* 925 */       int index = MotionEventCompat.getActionIndex(ev);
/* 926 */       float x2 = MotionEventCompat.getX(ev, index);
/* 927 */       this.mLastMotionX = x2;
/* 928 */       this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
/* 929 */       break;
/*     */     case 6:
/* 932 */       onSecondaryPointerUp(ev);
/* 933 */       this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
/*     */     case 4:
/*     */     }
/*     */ 
/* 937 */     return true;
/*     */   }
/*     */ 
/*     */   private void onSecondaryPointerUp(MotionEvent ev) {
/* 941 */     int pointerIndex = MotionEventCompat.getActionIndex(ev);
/* 942 */     int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
/* 943 */     if (pointerId == this.mActivePointerId)
/*     */     {
/* 946 */       int newPointerIndex = pointerIndex == 0 ? 1 : 0;
/* 947 */       this.mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
/* 948 */       this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
/* 949 */       if (this.mVelocityTracker != null)
/* 950 */         this.mVelocityTracker.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void endDrag()
/*     */   {
/* 956 */     this.mIsBeingDragged = false;
/* 957 */     this.mIsUnableToDrag = false;
/*     */ 
/* 959 */     if (this.mVelocityTracker != null) {
/* 960 */       this.mVelocityTracker.recycle();
/* 961 */       this.mVelocityTracker = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setScrollingCacheEnabled(boolean enabled) {
/* 966 */     if (this.mScrollingCacheEnabled != enabled)
/* 967 */       this.mScrollingCacheEnabled = enabled;
/*     */   }
/*     */ 
/*     */   private class DataSetObserver
/*     */     implements PagerAdapter.DataSetObserver
/*     */   {
/*     */     private DataSetObserver()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void onDataSetChanged()
/*     */     {
/* 983 */       ViewPager.this.dataSetChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SavedState extends View.BaseSavedState
/*     */   {
/*     */     int position;
/*     */     Parcelable adapterState;
/*     */     ClassLoader loader;
public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>()
/*     */     {
/*     */       public ViewPager.SavedState createFromParcel(Parcel in, ClassLoader loader)
/*     */       {
/* 483 */         return new ViewPager.SavedState(in, loader);
/*     */       }
/*     */ 
/*     */       public ViewPager.SavedState[] newArray(int size) {
/* 487 */         return new ViewPager.SavedState[size];
/*     */       }
/*     */     });
/*     */ 
/*     */     public SavedState(Parcelable superState)
/*     */     {
/* 462 */       super(superState);
/*     */     }
/*     */ 
/*     */     public void writeToParcel(Parcel out, int flags)
/*     */     {
/* 467 */       super.writeToParcel(out, flags);
/* 468 */       out.writeInt(this.position);
/* 469 */       out.writeParcelable(this.adapterState, flags);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 474 */       return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
/*     */     }
/*     */ 
/*     */     SavedState(Parcel in, ClassLoader loader)
/*     */     {
/* 492 */       super(in);
/* 493 */       if (loader == null) {
/* 494 */         loader = getClass().getClassLoader();
/*     */       }
/* 496 */       this.position = in.readInt();
/* 497 */       this.adapterState = in.readParcelable(loader);
/* 498 */       this.loader = loader;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SimpleOnPageChangeListener
/*     */     implements ViewPager.OnPageChangeListener
/*     */   {
/*     */     public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void onPageSelected(int position)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void onPageScrollStateChanged(int state)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface OnPageChangeListener
/*     */   {
/*     */     public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);
/*     */ 
/*     */     public abstract void onPageSelected(int paramInt);
/*     */ 
/*     */     public abstract void onPageScrollStateChanged(int paramInt);
/*     */   }
/*     */ 
/*     */   static class ItemInfo
/*     */   {
/*     */     Object object;
/*     */     int position;
/*     */     boolean scrolling;
/*     */   }
/*     */ }