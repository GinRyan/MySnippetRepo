package hong.specialEffects.wight;

import hong.specialEffects.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;


public class PlasmaView extends View implements View.OnTouchListener{	
	private Bitmap mBitmap;

    public PlasmaView(Context context) {
        super(context);      
        this.setOnTouchListener(this);
    }

    
    @Override 
    protected void onDraw(Canvas canvas) {
    	if(mBitmap==null){
    		Bitmap bmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.mm);
            Matrix matrix=new Matrix(); 
            matrix.setScale(this.getWidth()/(float)bmp.getWidth(), this.getHeight()/(float)bmp.getHeight());
            //mBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);\
            mBitmap = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
            AnimRender.setBitmap(mBitmap);
    	}
        AnimRender.render(mBitmap);    	
        canvas.drawBitmap(mBitmap, 0, 0, null);
        postInvalidate();
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//测试了以下height参数，这个参数的名字应该叫做weight，值越大，则水波效果越明显
		AnimRender.drop((int)event.getX(), (int)event.getY(), 3600);
		return false;
	}
}

class AnimRender{
	public static native void setBitmap(Bitmap src);
    public static native void render(Bitmap dst);
    public static native void drop(int x, int y, int height);
   
    static {
        System.loadLibrary("plasma");
    }
}
