package com.ljp.laucher.picsshow;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.adview.AdViewInterface;
import com.adview.AdViewLayout;
import com.ljp.laucher.R;
import com.ljp.laucher.itemcontent.ViewpageAdater;
import com.ljp.laucher.itemcontent.imageview.ImageViewActivity;
import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.ImageOperation;
import com.ljp.laucher.util.IntentData;
import com.ljp.laucher.util.ToastAndDialog;

public class PicsShowActivity extends Activity implements OnPageChangeListener,
		OnClickListener, AdViewInterface {
	ViewPager viewpager;
	ImageView btn_pre, btn_next, btn_save;
	ImageButton btn_back;
	LinkedList<View> view_List;
	ViewpageAdater adapter;
	ProgressDialog progressDialog;
	String[] files = null;
	AssetManager am;

	String intentString=null;AdViewLayout adViewLayout;SharedPreferences sp ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_picsshow);
		
		intentString= getIntent().getStringExtra("text");
		initWidght();
		bindEvent();
		initData();// new Random().nextInt(7)
		for (int i = 0; i < files.length; i++) {
			view_List.add(addView(i));
		}
		adapter = new ViewpageAdater(view_List);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(this);
		loadContentImg(0, 1);Configure._position=0;
		
		sp = getSharedPreferences("my_adview", 0);
		boolean isClick =sp.getBoolean("isAddview_Click", false);System.out.println(isClick+"===");
		if(!isClick){System.out.println("===");
	    		ToastAndDialog.Toast(PicsShowActivity.this, "广告点击一次后永久不再显示", 6000);
		    	adViewLayout = new AdViewLayout(this, "SDK20122324460254wedw9ona4thve2i");
		    	adViewLayout.setAdViewInterface(PicsShowActivity.this);
		    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
		    			FrameLayout.LayoutParams.WRAP_CONTENT);		
		    	params.gravity=Gravity.TOP; 
		    	this.addContentView(adViewLayout, params);
		}

		ToastAndDialog.Toast(PicsShowActivity.this, "点击图片随便看", 3000);
	}

	public void initData() {
		try {
			files =  getResources().getAssets().list("level-3");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Configure.DetailWeiboImages = new Bitmap[files.length+1];
		view_List = new LinkedList<View>();
		am = getResources().getAssets();
		randomFiles();
	}

	public void initWidght() {
		viewpager = (ViewPager) findViewById(R.id.pic_viewpager);
		btn_pre = (ImageView) findViewById(R.id.iv_pre);
		btn_next = (ImageView) findViewById(R.id.iv_next);
		btn_save = (ImageView) findViewById(R.id.iv_save);
		btn_back = (ImageButton) findViewById(R.id.pic_back);
	}

	public void bindEvent() {
		btn_pre.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}
	public void randomFiles(){
		int size = files.length;
		for(int i=0;i<size;i++){
			int random=new Random().nextInt(size-i);
			String a = files[random];
			files[random] = files[size-1-i];
			files[size-1-i]=a;
		}
	}
	public View addView(final int position) {
		ImageView iv = new ImageView(PicsShowActivity.this);
		iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		iv.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Configure.DetailWeiboImages[Configure._position] !=null){
					Intent intent = new Intent(PicsShowActivity.this,
							ImageViewActivity.class);
					intent.putExtra("imgUrl","level-3/"+ files[position]);
					IntentData.PicFrom=1;
					startActivity(intent);
					//overridePendingTransition(R.anim.popshow_anim, R.anim.pophidden_anim);
				}
			}
		});
		return iv;
	}

	@Override
	public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
	}

	@Override
	public void onPageScrollStateChanged(int paramInt) {
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		Configure._position = position;
		if (position == 0) {
			loadContentImg(0, 1);
		} else if (position == view_List.size() - 1) {
			loadContentImg(position, position - 1);
		} else {
			loadContentImg(position - 1, position, position + 1);
		}
		recyleBitmap(position);
	}

	public void loadContentImg(int... positions) {
		for (final int position : positions) {
			ImageView content_img = (ImageView) view_List
					.get(position < view_List.size() ? position : view_List
							.size() - 1);
			if (Configure.DetailWeiboImages[position] == null) {
				InputStream is = null;
				try {
					is = am.open("level-3/"+ files[position]);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				;
				Configure.DetailWeiboImages[position] = BitmapFactory.decodeStream(is);
			}
			content_img.setImageBitmap(Configure.DetailWeiboImages[position]);
		}
	}

	public void recyleBitmap(int position) {
		if (position - 2 > 0 && position + 2 < Configure.DetailWeiboImages.length) {
			if (Configure.DetailWeiboImages[position - 2] != null) {

				ImageView imageview = (ImageView) view_List.get(position - 2);
				imageview.setImageBitmap(null);
				Configure.DetailWeiboImages[position - 2].recycle();Configure.DetailWeiboImages[position - 2]=null;
			}
			if (Configure.DetailWeiboImages[position + 2] != null) {

				ImageView imageview = (ImageView) view_List.get(position + 2);
				imageview.setImageBitmap(null);
				Configure.DetailWeiboImages[position + 2].recycle();Configure.DetailWeiboImages[position + 2]=null;
			}

		}
		System.gc();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_next:
			viewpager.setCurrentItem(Configure._position + 1);
			break;
		case R.id.iv_pre:
			viewpager.setCurrentItem(Configure._position - 1);
			break;
		case R.id.iv_save:
			if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
				 ToastAndDialog.Toast(PicsShowActivity.this,
							"请插入SDcard", 3000);
			}else if (ImageOperation.isSave("/love_girls/", "level-3/"+ files[Configure._position])) {
				ToastAndDialog.Toast(PicsShowActivity.this,
						"已经保存在love_girls文件夹哦", 3000);
			} else {
				progressDialog = ProgressDialog.show(PicsShowActivity.this, "请稍等片刻...",
						"小夜正在努力的为您保存图片", true, true);
				new Thread(){
					public void run(){
						ImageOperation.saveFileFromAssets(PicsShowActivity.this,"/love_girls/", "level-3/"+ files[Configure._position]);
						Message msg = BroadcastHandler.obtainMessage();
						BroadcastHandler.sendMessage(msg);
					}
				}.start();
			}
			break;
		case R.id.pic_back:
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			break;
		}
	}
	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(view_List!=null && view_List.size()>0){
				ImageView imageview = (ImageView) view_List.get(Configure._position);
				imageview.setImageBitmap(null);
				Configure.DetailWeiboImages =null;
			}
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			return false;
		}
		return false;
	}
	private Handler BroadcastHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			ToastAndDialog.Toast(PicsShowActivity.this,
					"已经为您保存于love_girls文件夹之下", 3000);
		}
	};
	@Override
	public void onClickAd() {
		// TODO Auto-generated method stub
		sp.edit().putBoolean("isAddview_Click", true).commit();
		adViewLayout.setVisibility(8);
	}

	@Override
	public void onDisplayAd() {
		// TODO Auto-generated method stub
	}
}
