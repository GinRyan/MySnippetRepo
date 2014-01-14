package imageUtil;

import java.util.HashMap;

import android.graphics.Bitmap;

public class ImageCache {

	private static HashMap<String, Bitmap> imageCache = new HashMap<String, Bitmap>();
	
	public static void put(String key, Bitmap bmp) {
		imageCache.put(key, bmp);
	}
	
	public static Bitmap get(String key) {
		return imageCache.get(key);
	}
}
