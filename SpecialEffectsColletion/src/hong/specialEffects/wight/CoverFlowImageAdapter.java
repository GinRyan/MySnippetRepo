package hong.specialEffects.wight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CoverFlowImageAdapter extends BaseAdapter {
	private Context mContext;
	private Integer[] mImageIds = null;
	private Class<?>[] target=null;
	

	public Integer[] getmImageIds() {
		return mImageIds;
	}

	public Class<?>[] getTarget() {
		return target;
	}

	public CoverFlowImageAdapter(Context c,Integer[] mImageIds,Class<?>[] target) {
		mContext = c;
		this.mImageIds=mImageIds;
		this.target=target;
	}

	@Override
	public int getCount() {
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = createReflectedImages(mContext,mImageIds[position]);
		i.setLayoutParams(new CoverFlow.LayoutParams(120, 100));
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		// 设置的抗锯齿,防止图像在旋转的时候出现锯齿
		BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
		drawable.setAntiAlias(true);
		return i;
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
	
	/**
	 * 为图片添加倒影
	 * @param mContext
	 * @param imageId
	 * @return
	 */
	public ImageView createReflectedImages(Context mContext,int imageId) {
		Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), imageId);
		final int reflectionGap = 4;
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(originalImage, 0, 0, null);

		Paint deafaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, originalImage
				.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.MIRROR);

		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(bitmapWithReflection);

		return imageView;
	}
	
}
