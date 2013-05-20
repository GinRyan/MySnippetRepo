package org.cn.gv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cn.bean.MyService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class SelectPicturesFromSD extends Activity {

	static List<File> fileList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdcardindex);
		final GridView gridView = (GridView) findViewById(R.id.sdcard);
		fileList = new ArrayList<File>();
		getFiles(Environment.getExternalStorageDirectory());
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				MyService.bm = (Bitmap)av.getItemAtPosition(position);
				finish();
				Intent itnt = new Intent(SelectPicturesFromSD.this, SelectPicActivity.class);
				startActivity(itnt);
			};
		});
		
		gridView.setAdapter(new ImageAdapter(this, fileList));
	}

	public static void getFiles(File filePath) {
		File[] files = filePath.listFiles();
		
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					getFiles(files[i]);
				} else {
					if (files[i].getName().toLowerCase().endsWith(".jpg")) {
						fileList.add(files[i]);
						
					}
				}
			}
		}
	}
}

class ImageAdapter extends BaseAdapter {
	private List<Bitmap> bm = new ArrayList<Bitmap>();
	private Context mContext;
	private List<File> mImagePath;
	private boolean yesNo = true;
	
	public ImageAdapter(Context c, List<File> imagePath) {
		mContext = c;
		mImagePath = imagePath;
		Log.e("gv",mImagePath+"");
		Log.e("gv",mImagePath.size()+"");
		for (int i = 0; i < mImagePath.size(); i++) {
			Log.e("gv",mImagePath.get(i).getPath() + "/" + mImagePath.get(i).getName() );
		}
		
	}

	public int getCount() {
		// return pics.length;
		return mImagePath.size();
	}

	public Object getItem(int position) {
		return bm == null?null:bm.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageview;
		if (convertView == null) {
			imageview = new ImageView(mContext);
			imageview.setLayoutParams(new GridView.LayoutParams(120, 120));
			imageview.setPadding(6, 6, 6, 6);
		} else {
			imageview = (ImageView) convertView;
		}
		if(yesNo)
		{
			convert(mImagePath);
		}
		yesNo=false;
		imageview.setImageBitmap(bm.get(position));
		// imageview.setImageResource(pics[position]);
		return imageview;
	}

	

	public void convert(List<File> mImagePath) {
		System.out.println("*********转换**********");
		//FileInputStream fis;
		for (int i = 0; i < mImagePath.size(); i++) {
			try {
				System.out.println("********************"+mImagePath.get(i).getPath());
				Options opts= new Options();
				opts.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeFile(mImagePath.get(i).getPath(), opts);
				opts.inJustDecodeBounds = false;
				int be = (int)(opts.outHeight / (float)200);
		          if (be <= 0){
		               be = 1;
		         }
		        opts.inSampleSize = be;
		        bitmap=BitmapFactory.decodeFile(mImagePath.get(i).getPath(),opts);
		          int w = bitmap.getWidth();
		          int h = bitmap.getHeight();
		         System.out.println(w+"    "+h);
				System.out.println("////////////////"+bitmap);
				System.out.println("赋值之前"+bm.size());
				bm.add(bitmap);
				System.out.println("赋值之后:"+bm.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
