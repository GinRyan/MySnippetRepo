package com.myc;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ChooseImage extends Activity
{

	private static final int FLAG_CHOOSE = 1;
	private static final int FLAG_HANDLEBACK = 2;
	
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose);
		
		mImageView = (ImageView) findViewById(R.id.image);
	}
	
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.choose_img:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, FLAG_CHOOSE);
			
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivityForResult(intent, FLAG_HANDLEBACK);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && null != data)
		{
			switch (requestCode)
			{
			case FLAG_CHOOSE:
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority()))
				{
					Cursor cursor = getContentResolver().query(uri, new String[]{ MediaStore.Images.Media.DATA }, null, null, null);
					if (null == cursor)
					{
						Toast.makeText(this, "no found", Toast.LENGTH_SHORT).show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.d("may", "path="+path);
					Intent intent = new Intent(this, ImageCrop.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_HANDLEBACK);;
				}
				else
				{
					Log.d("may", "path="+uri.getPath());
					Intent intent = new Intent(this, ImageCrop.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_HANDLEBACK);;
				}
				break;
			case FLAG_HANDLEBACK:
				String imagePath = data.getStringExtra("path");
				mImageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
				Log.d("may", "back");
				break;
			}
		}
		
	}
	
}
