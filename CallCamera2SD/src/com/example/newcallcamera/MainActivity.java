package com.example.newcallcamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private Button cam_bt;
	private ImageView ivstorepic;
	private String filePath;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cam_bt = (Button) findViewById(R.id.cam);
		ivstorepic = (ImageView) findViewById(R.id.iv);

		final File file = new File(Environment.getExternalStorageDirectory()
				+ "/.EXIUGTMP/");
		if (!file.exists()) {
			file.mkdir();
		}
		
		
		final File img_file = new File(
				Environment.getExternalStorageDirectory() + "/exiugTMP/"
						+ System.currentTimeMillis() + ".JPG");
		filePath = img_file.getAbsolutePath();
		System.out.println(img_file.getAbsolutePath());

		cam_bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img_file));
				// startActivity(intent);
				startActivityForResult(intent, 1);
			}
		});
	}

	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		
		Bitmap bm = ResizeBitmap(BitmapFactory.decodeFile(filePath), 640);
		ivstorepic.setImageBitmap(bm);
	}
}
