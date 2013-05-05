package cn.m15.xys;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

public class Font extends Activity {
    public int mScreenWidth = 0;
    public int mScreenHeight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	setContentView(new FontView(this));
	// 获取屏幕宽高
	Display display = getWindowManager().getDefaultDisplay();
	mScreenWidth  = display.getWidth();
	mScreenHeight = display.getHeight();
	super.onCreate(savedInstanceState);

    }

    class FontView extends View {
        public final static String STR_WIDTH = "获取字符串宽为："; 
        public final static String STR_HEIGHT = "获取字体高度为："; 
        Paint mPaint = null;
        
	public FontView(Context context) {
	    super(context);
	    mPaint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    //设置字符串颜色
	    mPaint.setColor(Color.WHITE);
	    canvas.drawText("当前屏幕宽" + mScreenWidth, 0, 30, mPaint);
	    canvas.drawText("当前屏幕高"+ mScreenHeight, 0, 60, mPaint);
	    //设置字体大小
	    mPaint.setColor(Color.RED);
	    mPaint.setTextSize(18);
	    canvas.drawText("字体大小为18", 0, 90, mPaint);
	    //消除字体锯齿
	    mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	    canvas.drawText("消除字体锯齿后", 0, 120, mPaint);
	    //获取字符串宽度
	    canvas.drawText(STR_WIDTH + getStringWidth(STR_WIDTH), 0, 150, mPaint);
	    //获取字体高度
	    canvas.drawText(STR_HEIGHT + getFontHeight(), 0, 180, mPaint);
	    //从string.xml读取字符串绘制
	    mPaint.setColor(Color.YELLOW);
	    canvas.drawText(getResources().getString(R.string.string_font), 0, 210, mPaint);
	    super.onDraw(canvas);
	}
	
	/**
	 * 获取字符串宽
	 * @param str
	 * @return
	 */
	private int getStringWidth(String str) {
	    return (int) mPaint.measureText(STR_WIDTH); 
	}
	/*
	 * 获取字体高度
	 */
	private int getFontHeight() {
	    FontMetrics fm = mPaint.getFontMetrics();
	    return (int)Math.ceil(fm.descent - fm.top) + 2;
	}
    }
}
