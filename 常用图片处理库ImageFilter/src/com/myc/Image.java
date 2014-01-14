package com.myc;

import imageUtil.ImageUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Image extends Activity {

	ImageView imageView1, imageView2;
	Drawable mDrawable;
	Bitmap mBitmap;

	Context mContext;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		mContext = this;
		findView();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		int pos = bundle.getInt("position", 0);
		Bitmap tmpBitmap;
		switch (pos) {
		case 0:
			tmpBitmap = ImageUtil.zoomBitmap(mBitmap, mBitmap.getWidth()/2, mBitmap.getHeight()/2);
			imageView2.setImageBitmap(tmpBitmap);
			break;
		case 1:
			tmpBitmap = ImageUtil.getRoundedCornerBitmap(mBitmap, 10f);
			imageView2.setImageBitmap(tmpBitmap);
			break;
		case 2:
			tmpBitmap = ImageUtil.createReflectionImageWithOrigin(mBitmap);
			imageView2.setImageBitmap(tmpBitmap);
			break;
		case 3:
			tmpBitmap = ImageUtil.postRotateBitamp(mBitmap, 90);
			imageView2.setImageBitmap(tmpBitmap);
			break;

		case 4:
			tmpBitmap = ImageUtil.reverseBitmap(mBitmap, 0);
			imageView2.setImageBitmap(tmpBitmap);
			break;
		case 6:
			tmpBitmap = ImageUtil.doodle(mBitmap, ImageUtil.readBitMap(mContext, R.drawable.ic_launcher),
					mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
			imageView2.setImageBitmap(tmpBitmap);
			break;

		case 7:
			tmpBitmap = ImageUtil.drawText(mBitmap, "test", mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
			imageView2.setImageBitmap(tmpBitmap);
			break;

		case 8:
			tmpBitmap = ImageUtil.oldRemeber(mBitmap);
			imageView2.setImageBitmap(tmpBitmap);
			break;

		case 9:
			tmpBitmap = ImageUtil.blurImage(mBitmap);
			imageView2.setImageBitmap(tmpBitmap);
			break;

		case 10:
			tmpBitmap = ImageUtil.blurImageAmeliorate(mBitmap);
			imageView2.setImageBitmap(tmpBitmap);
			break;

		case 11:
			tmpBitmap = ImageUtil.emboss(mBitmap);
			imageView2.setImageBitmap(tmpBitmap);
			break;

		case 12:
			tmpBitmap = ImageUtil.sharpenImageAmeliorate(mBitmap);
			imageView2.setImageBitmap(tmpBitmap);
			break;

		case 13:
			tmpBitmap = ImageUtil.film(mBitmap);
			imageView2.setImageBitmap(tmpBitmap);
			ImageUtil.SaveBitmap(tmpBitmap, "test.jpg");
			ImageUtil.saveToLocal(tmpBitmap);
			break;

		case 14:
			tmpBitmap = ImageUtil.sunshine(mBitmap, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
			imageView2.setImageBitmap(tmpBitmap);
			break;
		default:
			mBitmap = ImageUtil.readBitMap("mnt/sdcard/Photos/5.jpg");
			if (mBitmap == null) {
			}
			imageView2.setImageBitmap(mBitmap);
			break;
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		mDrawable = imageView1.getDrawable();
		mBitmap = imageUtil.ImageUtil.readBitMap(mContext, R.drawable.image);
		imageView2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageView1.setVisibility(View.GONE);
			}
		});
	}
}
