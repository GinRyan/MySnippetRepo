package imageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageUtil {
	// 放大缩小图片
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return newbmp;
	}

	// 将Drawable转化为Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	public static String SaveBitmap(Bitmap bmp, String name) {
		File file = new File("mnt/sdcard/picture/");
		String path = null;
		if (!file.exists())
			file.mkdirs();
		try {
			path = file.getPath() + "/" + name;
			FileOutputStream fileOutputStream = new FileOutputStream(path);

			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			System.out.println("saveBmp is here");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return path;
	}

	public static String saveToLocal(Bitmap bm) {
		String path = "/sdcard/test.jpg";
		try {
			FileOutputStream fos = new FileOutputStream(path);
			bm.compress(CompressFormat.JPEG, 75, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return path;
	}

	// 获得圆角图片的方法
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// 获得带倒影的图片方法
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 读取资源图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 从sd卡读取图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap readBitMap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		Bitmap bm = BitmapFactory.decodeFile(path, options);
		return bm;
	}

	/**
	 * 图片旋转
	 * 
	 * @param bmp
	 * @param degree
	 * @return
	 */
	public static Bitmap postRotateBitamp(Bitmap bmp, float degree) {
		// 获得Bitmap的高和宽
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		// 产生resize后的Bitmap对象
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
		return resizeBmp;
	}

	// 图片翻转
	public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
		float[] floats = null;
		switch (flag) {
		case 0: // 水平反转
			floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
			break;
		case 1: // 垂直反转
			floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
			break;
		}

		if (floats != null) {
			Matrix matrix = new Matrix();
			matrix.setValues(floats);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		}

		return bmp;
	}

	/**
	 * 组合涂鸦图片和源图片
	 * 
	 * @param src
	 *            源图片
	 * @param watermark
	 *            涂鸦图片
	 * @return
	 */
	public static Bitmap doodle(Bitmap src, Bitmap watermark, int x, int y) {
		// 另外创建一张图片
		Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas canvas = new Canvas(newb);
		canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src
		canvas.drawBitmap(watermark, (src.getWidth() - watermark.getWidth()) / 2,
				(src.getHeight() - watermark.getHeight()) / 2, null); // 涂鸦图片画到原图片中间位置
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		watermark.recycle();
		watermark = null;
		return newb;
	}

	/**
	 * 图片上写文字
	 * 
	 * @param src源图片
	 * @param msg文字
	 * @param x
	 * @param y
	 * @return
	 */
	public static Bitmap drawText(Bitmap src, String msg, int x, int y) {
		// 另外创建一张图片
		Canvas canvas = new Canvas(src);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawText(msg, x, y, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return src;
	}

	public static Bitmap oldRemeber(Bitmap bmp) {
		if (ImageCache.get("oldRemeber") != null) {
			return ImageCache.get("oldRemeber");
		}
		// 速度测试
		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int pixColor = 0;
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				pixColor = pixels[width * i + k];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255
						: newB);
				pixels[width * i + k] = newColor;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.e("may", "used time=" + (end - start));
		ImageCache.put("oldRemeber", bitmap);
		return bitmap;
	}

	/**
	 * 模糊效果
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImage(Bitmap bmp) {
		if (ImageCache.get("blurImage") != null) {
			return ImageCache.get("blurImage");
		}
		// 速度测试
		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int newColor = 0;

		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++) {
			for (int k = 1, len = height - 1; k < len; k++) {
				for (int m = 0; m < 9; m++) {
					int s = 0;
					int p = 0;
					switch (m) {
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					case 8:
						s = i;
						p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}

				for (int m = 0; m < 9; m++) {
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}

				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		long end = System.currentTimeMillis();
		Log.e("blurImage()", "used time=" + (end - start));
		ImageCache.put("blurImage", bitmap);
		return bitmap;
	}

	/**
	 * 柔化效果(高斯模糊)(优化后比上面快三倍)
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImageAmeliorate(Bitmap bmp) {
		if (ImageCache.get("blurImageAmeliorate") != null) {
			return ImageCache.get("blurImageAmeliorate");
		}
		long start = System.currentTimeMillis();
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // 值越小图片会越亮，越大则越暗

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("blurImageAmeliorate", "used time=" + (end - start));
		ImageCache.put("blurImageAmeliorate", bitmap);
		return bitmap;
	}

	public static Bitmap sketch(Bitmap bmp) {
		if (ImageCache.get("sketch") != null) {
			return ImageCache.get("sketch");
		}
		long start = System.currentTimeMillis();
		int pos, row, col, clr;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] pixSrc = new int[width * height];
		int[] pixNvt = new int[width * height];
		// 先对图象的像素处理成灰度颜色后再取反
		bmp.getPixels(pixSrc, 0, width, 0, 0, width, height);
		
		for (row = 0; row < height; row++) {
			for (col = 0; col < width; col++) {
				pos = row * width + col;
				pixSrc[pos] = (Color.red(pixSrc[pos]) + Color.green(pixSrc[pos]) + Color.blue(pixSrc[pos])) / 3;
				pixNvt[pos] = 255 - pixSrc[pos];
			}
		}

		// 对取反的像素进行高斯模糊, 强度可以设置，暂定为5.0
		gaussGray(pixNvt, 5.0, 5.0, width, height);

		// 灰度颜色和模糊后像素进行差值运算
		for (row = 0; row < height; row++) {
			for (col = 0; col < width; col++) {
				pos = row * width + col;

				clr = pixSrc[pos] << 8;
				clr /= 256 - pixNvt[pos];
				clr = Math.min(clr, 255);

				pixSrc[pos] = Color.rgb(clr, clr, clr);
			}
		}
		bmp.setPixels(pixSrc, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("blurImageAmeliorate", "used time=" + (end - start));
		ImageCache.put("sketch", bmp);
		return bmp;
	}

	private static int gaussGray(int[] psrc, double horz, double vert, int width, int height) {
		int[] dst, src;
		double[] n_p, n_m, d_p, d_m, bd_p, bd_m;
		double[] val_p, val_m;
		int i, j, t, k, row, col, terms;
		int[] initial_p, initial_m;
		double std_dev;
		int row_stride = width;
		int max_len = Math.max(width, height);
		int sp_p_idx, sp_m_idx, vp_idx, vm_idx;

		val_p = new double[max_len];
		val_m = new double[max_len];

		n_p = new double[5];
		n_m = new double[5];
		d_p = new double[5];
		d_m = new double[5];
		bd_p = new double[5];
		bd_m = new double[5];

		src = new int[max_len];
		dst = new int[max_len];

		initial_p = new int[4];
		initial_m = new int[4];

		// 垂直方向
		if (vert > 0.0) {
			vert = Math.abs(vert) + 1.0;
			std_dev = Math.sqrt(-(vert * vert) / (2 * Math.log(1.0 / 255.0)));

			// 初试化常量
			findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);

			for (col = 0; col < width; col++) {
				for (k = 0; k < max_len; k++) {
					val_m[k] = val_p[k] = 0;
				}

				for (t = 0; t < height; t++) {
					src[t] = psrc[t * row_stride + col];
				}

				sp_p_idx = 0;
				sp_m_idx = height - 1;
				vp_idx = 0;
				vm_idx = height - 1;

				initial_p[0] = src[0];
				initial_m[0] = src[height - 1];

				for (row = 0; row < height; row++) {
					terms = (row < 4) ? row : 4;

					for (i = 0; i <= terms; i++) {
						val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i] * val_p[vp_idx - i];
						val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i] * val_m[vm_idx + i];
					}
					for (j = i; j <= 4; j++) {
						val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
						val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
					}

					sp_p_idx++;
					sp_m_idx--;
					vp_idx++;
					vm_idx--;
				}

				transferGaussPixels(val_p, val_m, dst, 1, height);

				for (t = 0; t < height; t++) {
					psrc[t * row_stride + col] = dst[t];
				}
			}
		}

		// 水平方向
		if (horz > 0.0) {
			horz = Math.abs(horz) + 1.0;

			if (horz != vert) {
				std_dev = Math.sqrt(-(horz * horz) / (2 * Math.log(1.0 / 255.0)));

				// 初试化常量
				findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
			}

			for (row = 0; row < height; row++) {
				for (k = 0; k < max_len; k++) {
					val_m[k] = val_p[k] = 0;
				}

				for (t = 0; t < width; t++) {
					src[t] = psrc[row * row_stride + t];
				}

				sp_p_idx = 0;
				sp_m_idx = width - 1;
				vp_idx = 0;
				vm_idx = width - 1;

				initial_p[0] = src[0];
				initial_m[0] = src[width - 1];

				for (col = 0; col < width; col++) {
					terms = (col < 4) ? col : 4;

					for (i = 0; i <= terms; i++) {
						val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i] * val_p[vp_idx - i];
						val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i] * val_m[vm_idx + i];
					}
					for (j = i; j <= 4; j++) {
						val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
						val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
					}

					sp_p_idx++;
					sp_m_idx--;
					vp_idx++;
					vm_idx--;
				}

				transferGaussPixels(val_p, val_m, dst, 1, width);

				for (t = 0; t < width; t++) {
					psrc[row * row_stride + t] = dst[t];
				}
			}
		}

		return 0;
	}

	private static void findConstants(double[] n_p, double[] n_m, double[] d_p, double[] d_m, double[] bd_p,
			double[] bd_m, double std_dev) {
		double div = Math.sqrt(2 * 3.141593) * std_dev;
		double x0 = -1.783 / std_dev;
		double x1 = -1.723 / std_dev;
		double x2 = 0.6318 / std_dev;
		double x3 = 1.997 / std_dev;
		double x4 = 1.6803 / div;
		double x5 = 3.735 / div;
		double x6 = -0.6803 / div;
		double x7 = -0.2598 / div;
		int i;

		n_p[0] = x4 + x6;
		n_p[1] = (Math.exp(x1) * (x7 * Math.sin(x3) - (x6 + 2 * x4) * Math.cos(x3)) + Math.exp(x0)
				* (x5 * Math.sin(x2) - (2 * x6 + x4) * Math.cos(x2)));
		n_p[2] = (2
				* Math.exp(x0 + x1)
				* ((x4 + x6) * Math.cos(x3) * Math.cos(x2) - x5 * Math.cos(x3) * Math.sin(x2) - x7 * Math.cos(x2)
						* Math.sin(x3)) + x6 * Math.exp(2 * x0) + x4 * Math.exp(2 * x1));
		n_p[3] = (Math.exp(x1 + 2 * x0) * (x7 * Math.sin(x3) - x6 * Math.cos(x3)) + Math.exp(x0 + 2 * x1)
				* (x5 * Math.sin(x2) - x4 * Math.cos(x2)));
		n_p[4] = 0.0;

		d_p[0] = 0.0;
		d_p[1] = -2 * Math.exp(x1) * Math.cos(x3) - 2 * Math.exp(x0) * Math.cos(x2);
		d_p[2] = 4 * Math.cos(x3) * Math.cos(x2) * Math.exp(x0 + x1) + Math.exp(2 * x1) + Math.exp(2 * x0);
		d_p[3] = -2 * Math.cos(x2) * Math.exp(x0 + 2 * x1) - 2 * Math.cos(x3) * Math.exp(x1 + 2 * x0);
		d_p[4] = Math.exp(2 * x0 + 2 * x1);

		for (i = 0; i <= 4; i++) {
			d_m[i] = d_p[i];
		}

		n_m[0] = 0.0;
		for (i = 1; i <= 4; i++) {
			n_m[i] = n_p[i] - d_p[i] * n_p[0];
		}

		double sum_n_p, sum_n_m, sum_d;
		double a, b;

		sum_n_p = 0.0;
		sum_n_m = 0.0;
		sum_d = 0.0;

		for (i = 0; i <= 4; i++) {
			sum_n_p += n_p[i];
			sum_n_m += n_m[i];
			sum_d += d_p[i];
		}

		a = sum_n_p / (1.0 + sum_d);
		b = sum_n_m / (1.0 + sum_d);

		for (i = 0; i <= 4; i++) {
			bd_p[i] = d_p[i] * a;
			bd_m[i] = d_m[i] * b;
		}
	}

	private static void transferGaussPixels(double[] src1, double[] src2, int[] dest, int bytes, int width) {
		int i, j, k, b;
		int bend = bytes * width;
		double sum;

		i = j = k = 0;
		for (b = 0; b < bend; b++) {
			sum = src1[i++] + src2[j++];

			if (sum > 255)
				sum = 255;
			else if (sum < 0)
				sum = 0;

			dest[k++] = (int) sum;
		}
	}

	/**
	 * 锐化效果
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap emboss(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				pixColor = pixels[pos + 1];
				newR = Color.red(pixColor) - pixR + 127;
				newG = Color.green(pixColor) - pixG + 127;
				newB = Color.blue(pixColor) - pixB + 127;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 图片锐化（拉普拉斯变换）
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap sharpenImageAmeliorate(Bitmap bmp) {
		if (ImageCache.get("sharpenImageAmeliorate") != null) {
			return ImageCache.get("sharpenImageAmeliorate");
		}
		long start = System.currentTimeMillis();
		// 拉普拉斯矩阵
		int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int idx = 0;
		float alpha = 0.3F;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + n) * width + k + m];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.e("sharpenImageAmeliorate", "used time=" + (end - start));
		ImageCache.put("sharpenImageAmeliorate", bitmap);
		return bitmap;
	}

	/**
	 * 底片效果
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap film(Bitmap bmp) {
		// RGBA的最大值
		final int MAX_VALUE = 255;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				newR = MAX_VALUE - pixR;
				newG = MAX_VALUE - pixG;
				newB = MAX_VALUE - pixB;

				newR = Math.min(MAX_VALUE, Math.max(0, newR));
				newG = Math.min(MAX_VALUE, Math.max(0, newG));
				newB = Math.min(MAX_VALUE, Math.max(0, newB));

				pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 光照效果
	 * 
	 * @param bmp
	 *            光照中心x坐标
	 * @param centerX
	 *            光照中心要坐标
	 * @param centerY
	 * @return
	 */
	public static Bitmap sunshine(Bitmap bmp, int centerX, int centerY) {
		final int width = bmp.getWidth();
		final int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;
		int radius = Math.min(centerX, centerY);

		final float strength = 150F; // 光照强度 100~150
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				pos = i * width + k;
				pixColor = pixels[pos];

				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);

				newR = pixR;
				newG = pixG;
				newB = pixB;

				// 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
				int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
				if (distance < radius * radius) {
					// 按照距离大小计算增加的光照值
					int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
					newR = pixR + result;
					newG = pixG + result;
					newB = pixB + result;
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

}