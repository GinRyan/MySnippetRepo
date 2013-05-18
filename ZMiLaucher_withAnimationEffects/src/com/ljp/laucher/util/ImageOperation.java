package com.ljp.laucher.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ProgressBar;

public class ImageOperation {

	public static boolean isSave(String path, String imageUrl) {
		File dir = new File(Environment.getExternalStorageDirectory() + path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(Environment.getExternalStorageDirectory()
				+ path
				+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
						imageUrl.length()));
		return file.isFile();
	}

	public static void saveFile(String path, String imageUrl) {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/night_girls/weibos/"+ imageUrl.hashCode());
		try {
			InputStream is = new FileInputStream(file);

			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory()
							+ path
							+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
									imageUrl.length()));
			int data = is.read();
			while (data != -1) {
				fos.write(data);
				data = is.read();
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("保存图片异常");
		}

	}
	public static void saveFileFromVIP(String path, String imageUrl) {
		File file = new File(imageUrl);
		try {
			InputStream is = new FileInputStream(file);

			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory()
							+ path
							+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
									imageUrl.length()));
			int data = is.read();
			while (data != -1) {
				fos.write(data);
				data = is.read();
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("保存图片异常");
		}

	}

	public static void saveFileFromAssets(Context context, String path,
			String imageUrl) {

		try {
			AssetManager am = context.getResources().getAssets();
			InputStream is = am.open(imageUrl);
			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory()
							+ path
							+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
									imageUrl.length()));
			int data = is.read();
			while (data != -1) {
				fos.write(data);
				data = is.read();
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("保存图片异常");
		}

	}

	public static boolean loadImageFromUrl(Context context, String path,
			String saveUrl) {
		InputStream is = null;
		
		File cacheDir;
		 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
	        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),saveUrl);
	    }else{
	        cacheDir=context.getCacheDir();
	    }
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		URL url = null;
		try {
			url = new URL(path);
			is = (InputStream) url.getContent();
			FileOutputStream fos = new FileOutputStream(cacheDir + "/"+path.hashCode());
			int data = is.read();
			while (data != -1) {
				fos.write(data);
				data = is.read();
			}
			fos.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("异常--》下载图片");
			return true;
		}
		return false;
	}


	public static boolean loadImageFromURL(Context context, String path,
			String saveUrl,ProgressBar pb) {
		InputStream is = null;
		
		File cacheDir;
		 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
	        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),saveUrl);
	    }else{
	        cacheDir=context.getCacheDir();
	    }
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(path);
		HttpResponse response;
		try {
			response = client.execute(get);

			HttpEntity entity = response.getEntity();
			float length = entity.getContentLength();

			is = entity.getContent();
			FileOutputStream fos = null;
			if (is != null) {
				fos = new FileOutputStream(cacheDir + "/"+path.hashCode());
				byte[] buf = new byte[1024];
				int ch = -1;
				float count = 0;
				while ((ch = is.read(buf)) != -1) {
					fos.write(buf, 0, ch);
					count += ch;
					pb.setProgress((int) (count * 100 / length));
				}
			}
			fos.flush();
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			return true;
		}
		return false;
	}
	
	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		Drawable d = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();

			d = Drawable.createFromStream(i, "src");
		} catch (IOException e) {
			System.out.println("下载表情失败");
			return null;
		}
		return d;
	}

	public static byte[] readGifFromUrl(String urls) {
		try {
			URL url = new URL(urls);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			int length = (int) conn.getContentLength();
			if (length != -1) {
				byte[] imgData = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos += readLen;
				}
				return imgData;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	public static Bitmap readNoImageBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	// ��Drawableת��ΪBitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	// ����ͼƬ��С
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

}