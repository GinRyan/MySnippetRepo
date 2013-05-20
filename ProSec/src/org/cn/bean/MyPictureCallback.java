package org.cn.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;

public class MyPictureCallback implements PictureCallback {

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		try {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		File file = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+".jpg");
		FileOutputStream output = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, output);
		output.close();
		camera.stopPreview();
		camera.startPreview();
		MyService.bm = bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
