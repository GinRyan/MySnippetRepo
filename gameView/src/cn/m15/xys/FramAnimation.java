package cn.m15.xys;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class FramAnimation extends Activity {
    public final static int ANIM_COUNT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	setContentView(new FramView(this));
	super.onCreate(savedInstanceState);

    }

    class FramView extends View {
	Bitmap[] bitmap = new Bitmap[ANIM_COUNT];
	Bitmap display = null;
	Paint paint = null;
	long startTime = 0;
	int playID = 0;

	public FramView(Context context) {
	    super(context);
	    for (int i = 0; i < ANIM_COUNT; i++) {
		bitmap[i] = BitmapFactory.decodeResource(this.getResources(),
			R.drawable.hero_a + i);
	    }
	    display = bitmap[0];
	    paint = new Paint();
	    startTime = System.currentTimeMillis();
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    paint.setColor(Color.WHITE);
	    canvas.drawText("²¥·Å¶¯»­ÖÐ...", 100, 30, paint);
	    long nowTime = System.currentTimeMillis();
	    if (nowTime - startTime >= 500) {
		playID++;
		if (playID >= ANIM_COUNT) {
		    playID = 0;
		}
		canvas.drawBitmap(bitmap[playID], 100, 100, paint);
	    }
	    invalidate();
	}
    }

}
