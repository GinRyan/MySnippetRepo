package com.ljp.tools.gif;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.style.DynamicDrawableSpan;
import android.view.View;

public class MyDrawableSpan extends DynamicDrawableSpan {

	private View view;
	private BitmapDrawable bitmap;
	private List<HashMap<String, Object>> e;
	private int i;
	boolean isStop = false;
	private Drawable localDrawable;

	public MyDrawableSpan(int paramInt, View view, Context context,
			List<HashMap<String, Object>> paramArrayOfam) {
		super(paramInt);
		this.view = view;
		this.e = paramArrayOfam;
		i = 0;
	}

	public MyDrawableSpan(View view, Context context,
			List<HashMap<String, Object>> paramArrayOfam) {
		this(1, view, context, paramArrayOfam);
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end,
			float x, int top, int y, int bottom, Paint paint) {
		// TODO Auto-generated method stub
		localDrawable = getDrawable();

		canvas.save();

		int transY = bottom - localDrawable.getBounds().bottom;
		if (mVerticalAlignment == 33) {
			transY -= paint.getFontMetricsInt().descent;
		}
		canvas.translate(x, transY);
		localDrawable.draw(canvas);
		canvas.restore();
	}
	private Handler redrawHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (i == e.size() - 1) {
				i = 0;
			}
			view.invalidate();
			i++;
			isStop = false;
		}
	};

	@Override
	public Drawable getDrawable() {
		// TODO Auto-generated method stub
		// DisplayMetrics c = new DisplayMetrics();
		// DisplayMetrics dm= new DisplayMetrics();

		// getWindowManager().getDefaultDisplay().getMetrics(dm);

		bitmap = new BitmapDrawable((Bitmap) e.get(i).get("image"));
		// bitmap.setTargetDensity(mContext.c.densityDpi);
		// int width = bitmap.getIntrinsicWidth();
		// int heigh = bitmap.getIntrinsicHeight();
		bitmap.setBounds(0, 0, 27, 27);

		if (!isStop) {
			new Thread() {
				public void run() {
					isStop = true;
					Message msg = redrawHandler.obtainMessage();
					redrawHandler.sendMessageDelayed(msg, (Integer) e.get(i)
							.get("delay"));
				}
			}.start();
		}
		return bitmap;
	}

}
