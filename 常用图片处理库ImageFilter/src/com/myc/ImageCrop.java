package com.myc;

import imageUtil.ImageUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.crop.CropImage;
import com.crop.view.CropImageView;

public class ImageCrop extends Activity {

	private CropImageView mImageView;
	private Bitmap mBitmap;

	private CropImage mCrop;
	String saveFileName = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_view);

		init();
	}

	private void init() {
		String path = getIntent().getStringExtra("path");
		Log.d("may", "path=" + path);
		mBitmap = BitmapFactory.decodeFile(path);

		mImageView = (CropImageView) findViewById(R.id.image);
		mImageView.setImageBitmap(mBitmap);
		mImageView.setImageBitmapResetBase(mBitmap, true);

		mCrop = new CropImage(this, mImageView);
		mCrop.crop(mBitmap);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			mCrop.cropCancel();
			break;
		case R.id.crop:
			mCrop.crop(mBitmap);
			break;
		case R.id.save:
			inputDialog();
//			String path = null;
//			if (saveFileName != null) {
//				path = ImageUtil.SaveBitmap(mCrop.cropAndSave(mBitmap), "qwe.jpg");
//				
//			} else {
//				path = mCrop.saveToLocal(mCrop.cropAndSave(mBitmap));
//				Intent intent = new Intent();
//				intent.putExtra("path", path);
//				setResult(RESULT_OK, intent);
//				finish();
//			}
			break;
		}
	}

	private void inputDialog() {
		saveFileName = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(ImageCrop.this);
		LayoutInflater factory = LayoutInflater.from(ImageCrop.this);
		final View textEntryView = factory.inflate(R.layout.input_dialog, null);
		builder.setTitle("保存的文件名");
		builder.setView(textEntryView);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				EditText filename = (EditText) textEntryView.findViewById(R.id.filename);
				saveFileName = filename.getText().toString();
				String path = ImageUtil.SaveBitmap(mCrop.cropAndSave(mBitmap), saveFileName);
				Intent intent = new Intent();
				intent.putExtra("path", path);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.show();
	}

}
