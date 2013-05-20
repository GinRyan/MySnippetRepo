package hong.specialEffects.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class Processor {
	/**
     * 将图片的四角圆化
     * @param bitmap 原图
     * @param roundPixels 圆滑率
     * @param half 是否截取半截
     * @return
     */
    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels,int half)
    {
    	int width=bitmap.getWidth();
    	int height=bitmap.getHeight();
    	
        //创建一个和原始图片一样大小位图
        Bitmap roundConcerImage = Bitmap.createBitmap(width,height, Config.ARGB_8888);
        //创建带有位图roundConcerImage的画布
        Canvas canvas = new Canvas(roundConcerImage);
        //创建画笔
        Paint paint = new Paint();
        //创建一个和原始图片一样大小的矩形
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        // 去锯齿
        paint.setAntiAlias(true);
        
        
        //画一个和原始图片一样大小的圆角矩形
        canvas.drawRoundRect(rectF, roundPixels, roundPixels , paint);
        //设置相交模式
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        //把图片画到矩形去
        canvas.drawBitmap(bitmap, null, rect, paint);
        switch(half){
        	case HalfType.LEFT:
        		return Bitmap.createBitmap(roundConcerImage, 0, 0, width/2, height);
        	case HalfType.RIGHT:
        		return Bitmap.createBitmap(roundConcerImage, width/2, 0, width/2, height);
        	case HalfType.TOP:
        		return Bitmap.createBitmap(roundConcerImage, 0, 0, width, height/2);
        	case HalfType.BOTTOM:
        		return Bitmap.createBitmap(roundConcerImage, 0, height/2, width, height/2);
        	case HalfType.NONE:
        		return roundConcerImage;
        	default:
        		return roundConcerImage;
        }
    }
    
    /**
     * 指定宽度平铺图片
     * @param src 原图
     * @param width 平铺后的宽度
     * @return
     */
    public static Bitmap createRepeater(Bitmap src,int width){
		int count = (width + src.getWidth() - 1) / src.getWidth();
	
		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
	
		for(int idx = 0; idx < count; ++ idx){
			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
	
		return bitmap;
	}
    

    public interface HalfType{
    	public static final int LEFT=1;
    	public static final int RIGHT=2;
    	public static final int TOP=3;
    	public static final int BOTTOM=4;
    	public static final int NONE=0;
    }
}
