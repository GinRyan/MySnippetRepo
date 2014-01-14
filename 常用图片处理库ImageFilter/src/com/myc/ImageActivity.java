package com.myc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ImageActivity extends Activity {
	/** Called when the activity is first created. */
	ListView mListView;
	private String[] mListStr = { "图片缩放", "图片圆角", "图片倒影", "旋转图片", "图片反转", "图片色调饱和度、色相、亮度处理", "涂鸦，水印", "图片上写文字", "怀旧效果",
			"模糊效果" ,"柔化效果(高斯模糊)", "浮雕效果", "锐化效果", "底片效果", "光照效果", "图片裁剪", "素描"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findView();

		// ImageIO.write(r, "jpg", new File("d:\\u.jpg"));// 生成图片文件
	}

	private void findView() {
		mListView = (ListView) findViewById(R.id.listview);
		// SimpleAdapter = new SimpleAdapter(context, data, resource, from, to)
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListStr));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				if (position == 5) {
					Intent intent = new Intent(ImageActivity.this, ImageToneActivity.class);
					startActivity(intent);
					return;
				}
				
				if (position == 15) {
					Intent intent = new Intent(ImageActivity.this, ChooseImage.class);
					startActivity(intent);
					return;
				}
				
				if (position == 16) {
					Intent intent = new Intent(ImageActivity.this, SketchActivity.class);
					startActivity(intent);
					return;
				}


				Intent intent = new Intent(ImageActivity.this, Image.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
	}
}