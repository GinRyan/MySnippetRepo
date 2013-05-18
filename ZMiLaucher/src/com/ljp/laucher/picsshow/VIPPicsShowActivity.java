package com.ljp.laucher.picsshow;

import java.io.File;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ljp.laucher.R;
import com.ljp.laucher.itemcontent.ViewpageAdater;
import com.ljp.laucher.itemcontent.imageview.ImageViewActivity;
import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.FileOperation;
import com.ljp.laucher.util.ImageOperation;
import com.ljp.laucher.util.IntentData;
import com.ljp.laucher.util.MathOperation;
import com.ljp.laucher.util.ToastAndDialog;

public class VIPPicsShowActivity extends Activity implements OnPageChangeListener,
		OnClickListener {
	ViewPager viewpager;
	ImageView btn_pre, btn_next, btn_save;
	ImageButton btn_back;ProgressBar pb;
	LinkedList<View> view_List;
	ViewpageAdater adapter;
	ProgressDialog progressDialog;
	String[] files = null;
	AssetManager am;

	String intentString="VIP精品";
	
	int removeViewPosition=100,recordPosition=100;boolean isRecord=false;File fa[]=null;
	int curretZipPage=0;SharedPreferences sp;boolean isNoCoins=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_picsshow_vip);
		
		sp = getSharedPreferences("ljp_sp", 0);
		curretZipPage = sp.getInt("curretZipPage", 0);
		//intentString= getIntent().getStringExtra("text");
		initWidght();
		bindEvent();
		initData();// new Random().nextInt(7)
		for (int i = 0; i < files.length; i++) {
			view_List.add(addView(i));
		}
		View lastView= LayoutInflater.from(VIPPicsShowActivity.this).inflate(
				R.layout.layout_picsshow_picmore, null);
		view_List.add(lastView);
		
		adapter = new ViewpageAdater(view_List);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(this);
		loadContentImg(0, 1);Configure._position=0;
	}

	public void initData() {
		try{
			files =  getResources().getAssets().list("vip");
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
		ImageView iv = new ImageView(VIPPicsShowActivity.this);
		iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		iv.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		return iv;
	}

	@Override
	public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
	}

	@Override
	public void onPageScrollStateChanged(int paramInt) {
	}

	@Override
	public void onPageSelected(final int position) {
		// TODO Auto-generated method stub
		view_List.get(position < view_List.size() ? position : view_List
				.size() - 1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Configure.DetailWeiboImages[Configure._position] !=null){
					Intent intent = new Intent(VIPPicsShowActivity.this,
							ImageViewActivity.class);
					if(position<recordPosition){
						intent.putExtra("imgUrl","vip/"+ files[position]);
						IntentData.PicFrom=1;
					}else {
						intent.putExtra("imgUrl",fa[position-recordPosition].getPath());
						IntentData.PicFrom=2;
					}
					startActivity(intent);
					//overridePendingTransition(R.anim.popshow_anim, R.anim.pophidden_anim);
				}
			}
		});
		Configure._position = position;
		if (position == 0) {
			loadContentImg(0, 1);
		}else if (position == view_List.size() - 1) {
			Button more_btn = (Button) view_List.get(position < view_List.size() ? position : view_List
							.size() - 1).findViewById(R.id.pic_more);
			pb=(ProgressBar) view_List.get(position < view_List.size() ? position : view_List.size() - 1).findViewById(R.id.pic_more_loading);
			more_btn.setOnClickListener(this);
		}else if (position == view_List.size() - 2) {
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
				if(position<recordPosition){
					InputStream is = null;
					try {
						is = am.open("vip/"+ files[position]);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					;
					Configure.DetailWeiboImages[position] = BitmapFactory.decodeStream(is);
				}else{
					Configure.DetailWeiboImages[position] = BitmapFactory.decodeFile(fa[position-recordPosition].getPath());
				}
				
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
			if(Configure._position == view_List.size()-1)
				return;
			String img ="dsd";
			if(Configure._position<recordPosition){
				img ="vip/"+ files[Configure._position]; 
			}else img = fa[Configure._position-recordPosition].getPath();
			if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
				 ToastAndDialog.Toast(VIPPicsShowActivity.this,
							"请插入SDcard", 3000);
			}else if (ImageOperation.isSave("/love_girls/",img)) {
				ToastAndDialog.Toast(VIPPicsShowActivity.this,
						"已经保存在love_girls文件夹哦", 3000);
			} else {
				progressDialog = ProgressDialog.show(VIPPicsShowActivity.this, "请稍等片刻...",
						"小夜正在努力的为您保存图片", true, true);
				new Thread(){
					public void run(){
						if(Configure._position<recordPosition){
							ImageOperation.saveFileFromAssets(VIPPicsShowActivity.this,"/love_girls/", "vip/"+ files[Configure._position]);
						}else{
							ImageOperation.saveFileFromVIP("/love_girls/", fa[Configure._position-recordPosition].getPath());
						}
						Message msg = BroadcastHandler.obtainMessage();
						BroadcastHandler.sendMessage(msg);
					}
				}.start();
			}
			break;
		case R.id.pic_more:
			if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
				 ToastAndDialog.Toast(VIPPicsShowActivity.this,
							"请插入SDcard", 3000);
				 return;
			}
			if(isNoCoins){
			//	YoumiOffersManager.showOffers(VIPPicsShowActivity.this,YoumiOffersManager.TYPE_REWARD_OFFERS);
				isNoCoins=false;
			}
			/*if(YoumiPointsManager.queryPoints(VIPPicsShowActivity.this)<20){
				isNoCoins=true;
				ToastAndDialog.Toast(VIPPicsShowActivity.this,
						"抱歉，积分已经用完了。再次点击进入积分专区", 3000);
				return;
			}*/
			view_List.get(Configure._position).findViewById(R.id.pic_more).setVisibility(8);
			removeViewPosition=Configure._position;
			if(!isRecord){
				recordPosition=removeViewPosition;
				isRecord=true;
			}
			new Thread(){
				public void run(){
					curretZipPage++;
					if(!FileOperation.loadFile("http://1.nightman.sinaapp.com/girls/"+curretZipPage+".zip",pb)){
						Message msg = morePicHandler.obtainMessage();
						msg.what=0;
						morePicHandler.sendMessage(msg);
						return;
					}
					FileOperation.Unzip(Environment.getExternalStorageDirectory()
							+ "/night_girls/vip/"+curretZipPage+".zip", Environment.getExternalStorageDirectory()
							+ "/night_girls/vip/",curretZipPage+"");
					
					File files = new File(Environment.getExternalStorageDirectory()+ "/night_girls/vip/"+curretZipPage+"/");
					fa = MathOperation.addToArray(fa,files.listFiles());
					if(files.listFiles()==null ||files.listFiles().length==0){
						Message msg = morePicHandler.obtainMessage();
						msg.what=2;
						morePicHandler.sendMessage(msg);
						return;
					}
					for(int i=0;i<files.listFiles().length;i++){
						view_List.add(addView(i));
					}
					Configure.DetailWeiboImages=MathOperation.addToArray(Configure.DetailWeiboImages, fa.length);
					View lastView= LayoutInflater.from(VIPPicsShowActivity.this).inflate(
							R.layout.layout_picsshow_picmore, null);
					view_List.add(lastView);
					Message msg = morePicHandler.obtainMessage();
					msg.what=1;
					morePicHandler.sendMessage(msg);
				}
			}.start();
			break;
		case R.id.pic_back:
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			break;
		}
	}
	private Handler morePicHandler = new Handler() {
		public void handleMessage(Message msg) {
			pb.setVisibility(8);
			if(msg.what==0){
				ToastAndDialog.Toast(VIPPicsShowActivity.this,
						"网络异常", 3000);
				view_List.get(Configure._position).findViewById(R.id.pic_more).setVisibility(0);
			}else if(msg.what==2){
				ToastAndDialog.Toast(VIPPicsShowActivity.this,
						"抱歉，没有更多图片资源了", 3000);
			}else{
				adapter.notifyDataSetChanged();
				viewpager.setCurrentItem(Configure._position-2);
				view_List.remove(removeViewPosition);
				adapter.notifyDataSetChanged();
				ToastAndDialog.Toast(VIPPicsShowActivity.this,
						"已经为您加载更多精品，谢谢您的等待", 3000);
				sp.edit().putInt("curretZipPage", curretZipPage).commit();
			//	YoumiPointsManager.spendPoints(VIPPicsShowActivity.this,
			//			20);
			}
		}
	};
	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(view_List!=null && view_List.size()>0){
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
			ToastAndDialog.Toast(VIPPicsShowActivity.this,
					"已经为您保存于love_girls文件夹之下", 3000);
		}
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		File f = new File(Environment.getExternalStorageDirectory()+ "/night_girls/vip");
		FileOperation.deleteFile(f);
	}
	
}
