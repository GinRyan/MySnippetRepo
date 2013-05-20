package com.testmemoryadapter;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;

/**
 * 也是为了解决OOM问题的类，暂时没有Demo
 * 
 * @author Liang
 * 
 */
public class BitmapCacheForLocal {
	static private BitmapCacheForLocal bitmapCacheForLocal;
	/** 用于Cache内容的存储 */
	private HashMap<String, BitmapSoftReference> mImageCaches = new LinkedHashMap<String, BitmapSoftReference>() {
		private static final long serialVersionUID = 1L;

	};

	public HashMap<String, BitmapSoftReference> getmImageCaches() {
		return mImageCaches;
	}

	/** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
	private ReferenceQueue<Bitmap> queue;

	/**
	 * 继承SoftReference，使得每一个实例都具有可识别的标识。
	 */
	private class BitmapSoftReference extends SoftReference<Bitmap> {
		private String _key;

		public BitmapSoftReference(Bitmap bmp, ReferenceQueue<Bitmap> queue,
				String key) {
			super(bmp, queue);
			_key = key;
		}
	}

	private BitmapCacheForLocal() {
		mImageCaches = new LinkedHashMap<String, BitmapSoftReference>(50,
				0.75f, true);

		queue = new ReferenceQueue<Bitmap>();
	}

	/**
	 * 取得缓存器实例
	 */
	public static BitmapCacheForLocal getInstance() {
		if (bitmapCacheForLocal == null) {
			bitmapCacheForLocal = new BitmapCacheForLocal();
		}
		return bitmapCacheForLocal;
	}

	/**
	 * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
	 */
	public void addCacheBitmap(Bitmap bmp, String key) {
		// cleanCache();// 清除垃圾引用
		BitmapSoftReference ref = new BitmapSoftReference(bmp, queue, key);
		mImageCaches.put(key, ref);
	}

	public Bitmap getBitmap(String path) {
		Bitmap bmp = null;
		// 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
		if (mImageCaches.containsKey(path)) {
			BitmapSoftReference ref = (BitmapSoftReference) mImageCaches
					.get(path);
			bmp = (Bitmap) ref.get();
		}

		return bmp;
	}

	public void cleanCache() {
		BitmapSoftReference ref = null;
		while ((ref = (BitmapSoftReference) queue.poll()) != null) {
			mImageCaches.remove(ref._key);
		}
	}

	/**
	 * 清除Cache内的全部内容
	 */
	public void clearCache() {
		cleanCache();
		mImageCaches.clear();
		System.gc();
		System.runFinalization();
	}
}