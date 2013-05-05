package com.lenovo.roll;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class RollActivity extends Activity {
	private View view;
	private Button btn;
	private PopupWindow mPopupWindow;
	private View[] btns;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//		LinearLayout layout=(LinearLayout) view.findViewById(R.id.layout_main);
//		//设置背景图片旋转180
//		Bitmap mBitmap=setRotate(R.drawable.bg_kuang);
//		BitmapDrawable drawable=new BitmapDrawable(mBitmap);
//		layout.setBackgroundDrawable(drawable);
        
        btn=(Button) this.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow(btn);
			}
        	
        });
        
        initPopupWindow(R.layout.popwindow);

    }
    
	private void initPopupWindow(int resId){
		LayoutInflater mLayoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
	    view = mLayoutInflater.inflate(resId, null);
	        
		mPopupWindow = new PopupWindow(view, 400,LayoutParams.WRAP_CONTENT);
//		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//必须设置background才能消失
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_frame));
		mPopupWindow.setOutsideTouchable(true);
		
		//自定义动画
//		mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		//使用系统动画
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		
		btns=new View[3];
		btns[0]=view.findViewById(R.id.btn_0);
		btns[1]=view.findViewById(R.id.btn_1);
		btns[2]=view.findViewById(R.id.btn_2);
		btns[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
			}
		});
		btns[1].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
			}
		});
		btns[2].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doSomething
			}
		});
	}
	private void showPopupWindow(View view) {
		if(!mPopupWindow.isShowing()){
//			mPopupWindow.showAsDropDown(view,0,0);
			mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		}
	}
	public Bitmap setRotate(int resId) {
		Matrix mFgMatrix = new Matrix();
		Bitmap mFgBitmap = BitmapFactory.decodeResource(getResources(), resId);
		mFgMatrix.setRotate(180f);
		return mFgBitmap=Bitmap.createBitmap(mFgBitmap, 0, 0, 
				mFgBitmap.getWidth(), mFgBitmap.getHeight(), mFgMatrix, true);
	}
}