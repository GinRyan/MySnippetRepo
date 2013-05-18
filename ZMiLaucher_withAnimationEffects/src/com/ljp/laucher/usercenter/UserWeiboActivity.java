package com.ljp.laucher.usercenter;

import java.util.LinkedList;
import java.util.List;

import weibo4android.Comment;
import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adview.AdViewInterface;
import com.adview.AdViewLayout;
import com.ljp.laucher.R;
import com.ljp.laucher.itemcontent.Comments_Pop_ListAdapter;
import com.ljp.laucher.itemcontent.ViewpageAdater;
import com.ljp.laucher.itemcontent.imageview.ImageViewActivity;
import com.ljp.laucher.itemcontent.usercomment.UserCommentActivity;
import com.ljp.laucher.itemcontent.userforward.UserForwardActivity;
import com.ljp.laucher.myview.PullToRefreshListView;
import com.ljp.laucher.myview.PullToRefreshListView.OnChangeStateListener;
import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.HttpClients;
import com.ljp.laucher.util.ImgAsync_Weibo;
import com.ljp.laucher.util.ImgAsync_Weibo.ImageCallback_DW;
import com.ljp.laucher.util.IntentData;
import com.ljp.laucher.util.MathOperation;
import com.ljp.laucher.util.OAuthConstantBean;
import com.ljp.laucher.util.ToastAndDialog;



public class UserWeiboActivity extends Activity implements OnClickListener, OnPageChangeListener, OnItemClickListener, OnChangeStateListener, AdViewInterface{
	List<Status> statuses,moreStatuses;boolean isBackAddOccur=false;private boolean[] isLoading,isAnimationShowed;
	ViewPager viewpager;ImageButton btn_back;ProgressBar loading_pb;
	LinearLayout[] linear_btns=new LinearLayout[5];int[] linear_ids={R.id.d_refresh,R.id.d_comment,R.id.d_forward,R.id.d_collect,R.id.d_more};
	LinkedList<View> view_List;ViewpageAdater adapter;LinearLayout li_bg;

	ImgAsync_Weibo asyncImageLoader_Weibo;
	private Weibo userWeibo;
	ProgressBar loadPb;ProgressDialog progressDialog;boolean isLoadingMore=false;
	SharedPreferences sp_skin;boolean skin_id;
	
	boolean filterWeibo=true,LoadImgOrNot = false;
	
	private int intentTo=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp_skin = getSharedPreferences("skin", MODE_PRIVATE);Configure.inits(UserWeiboActivity.this);
		skin_id = sp_skin.getBoolean("id", true);
		// =***************是否下载图片**************
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String isLoadImgS = settings.getString("isload_pic", "1");
		if (isLoadImgS.equals("2")|| (isLoadImgS.equals("1") && HttpClients.isWiFiActive(UserWeiboActivity.this)))
			LoadImgOrNot = true;
		// =***************是否过滤转发的微博**************
		filterWeibo = settings.getBoolean("checkbox_filterweibo", false);
		// =*****************************
		if(!LoadImgOrNot){
			Toast.makeText(UserWeiboActivity.this, "默认WIFI下下载图片，请到设置里面设置", 3000).show();
		}
		setContentView(skin_id?R.layout.layout_weibo_detail2:R.layout.layout_weibo_detail);
		initData();
		initWidght();
		
		loadPb.setVisibility(0);viewpager.setVisibility(8);
		new Thread(){
			public void run(){
				try {
					statuses =userWeibo.getHomeTimeline(0,filterWeibo?1:0,new Paging());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = UIHandler.obtainMessage();
				UIHandler.sendMessage(msg);
			}
		}.start();
	//  应用Id  应用密码 广告请求间隔(s)设置测试模式[false为发布模式]  
	//	AdManager.init(this,"409a91b1004fb647", "79adf6968f4dd7aa",  30,  false);  
	    LinearLayout ad_linear = (LinearLayout) findViewById(R.id.detail_adview);
		AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20122324460254wedw9ona4thve2i");
		adViewLayout.setAdViewInterface(this);
		LayoutParams params = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
				FrameLayout.LayoutParams.WRAP_CONTENT);		
		params.gravity=Gravity.BOTTOM; 
		ad_linear.addView(adViewLayout, params);
		ad_linear.invalidate();
	    
	}
	public void initData(){
		userWeibo = Configure.getUserWeibo(UserWeiboActivity.this);
		asyncImageLoader_Weibo = new ImgAsync_Weibo();
	}
	public void initWidght(){
		li_bg = (LinearLayout) findViewById(R.id.weibo_detail);
		li_bg.setBackgroundResource(Configure.images[getSharedPreferences("mysetup", 0).getInt("bg_id", 0)]);
		btn_back =(ImageButton) findViewById(R.id.detail_back);btn_back.setOnClickListener(this);
		viewpager = (ViewPager) findViewById(R.id.detail_viewpager);
		loadPb =(ProgressBar) findViewById(R.id.detail_progress);
		view_List = new LinkedList<View>();
		for(int i=0;i<5;i++){
			linear_btns[i]=(LinearLayout) findViewById(linear_ids[i]);
			linear_btns[i].setOnClickListener(this);
		}
		TextView title = (TextView) findViewById(R.id.today_title);
		title.setText(Configure.N_USER_NAME);
	}
	Handler UIHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(statuses==null || statuses.size()==0){
				ToastAndDialog.Toast(UserWeiboActivity.this, "网络有点不稳定哦", 3000);
				loadPb.setVisibility(8);return;
			}
			for (int i = 0; i <statuses.size(); i++)
						view_List.add(addView(statuses.get(i), UserWeiboActivity.this));
			Configure.DetailWeiboImages = new Bitmap[view_List.size()+1];
			isLoading = new boolean[view_List.size()];isAnimationShowed= new boolean[view_List.size()];
			loadPb.setVisibility(8);viewpager.setVisibility(0);
			adapter = new ViewpageAdater(view_List);
			viewpager.setAdapter(adapter);
			viewpager.setOnPageChangeListener(UserWeiboActivity.this);
			viewpager.setDuration(555);
			loadContentImg(0,1);
		}
	};
	public View addView(Status td, Context context) {
		View view= LayoutInflater.from(context).inflate(
				skin_id?R.layout.layout_weibo_detail_content2:R.layout.layout_weibo_detail_content, null);
		TextView content = (TextView) view.findViewById(R.id.detail_content);
		TextView user_name = (TextView) view.findViewById(R.id.user_name);
		TextView time = (TextView) view.findViewById(R.id.detail_ptime);
		if(getSharedPreferences("mysetup", 0).getInt("bg_id", 0)==1)content.setTextColor(0xff0bc814);
/*		if(getSharedPreferences("mysetup", 0).getInt("bg_id", 0)==0)content.setTextColor(0xff000000);
		if(getSharedPreferences("mysetup", 0).getInt("bg_id", 0)==2)content.setTextColor(0xffc9c9c9);
		if(getSharedPreferences("mysetup", 0).getInt("bg_id", 0)==3)content.setTextColor(0xffc9c9c9);
		if(getSharedPreferences("mysetup", 0).getInt("bg_id", 0)==4)content.setTextColor(0xff000000);*/
		content.setText("    "+td.getText());
		content.setAutoLinkMask(0);
		
		user_name.setText(td.getUser().getName());
		time.setText("  ："+MathOperation.getDateDifferFromNow(td.getCreatedAt()));
		return view;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	@Override
	public void onPageSelected(final int position) {
		// TODO Auto-generated method stub
		Configure._position = position;
		
		if(LoadImgOrNot){
			if(position ==0){loadContentImg(0,1);}
			else if(position ==view_List.size()-1){loadContentImg(position,position-1);}
			else{loadContentImg(position-1,position,position+1);}
			recyleBitmap(position-3,position+3);
		}
		else {
			String content_url = null;Status status = statuses.get(position<statuses.size()?position:statuses.size()-1);
			if (status.getRetweeted_status() == null) {
				content_url = status.getBmiddle_pic();
			} else {
				content_url = status.getRetweeted_status()
						.getBmiddle_pic();
			}
			if(content_url!=null && !content_url.equals("")&&Configure.DetailWeiboImages[position<Configure.DetailWeiboImages.length?position:Configure.DetailWeiboImages.length-1] ==null){
				view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.detail_progress).setVisibility(0);
				view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.detail_progress).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						loadContentImg(position);
					}
				});
			}
		}

		if(!isLoadingMore&& position>statuses.size()-3&& HttpClients.isConnect(UserWeiboActivity.this)){
			moreLoad();
		}
	}
	public void moreLoad(){
		isLoadingMore = true;
		progressDialog = ProgressDialog.show(UserWeiboActivity.this, "请稍等片刻...",
				"小夜正在努力的为您载入更多", true, true);
		new Thread() {
			public void run() {
				Message msg = moreJokeHandler.obtainMessage();
				moreStatuses = null;
				try {
					moreStatuses =userWeibo.getHomeTimeline(0,filterWeibo?1:0,new Paging(1,20,1,statuses.get(statuses.size()-1).getId()-1));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(moreStatuses!=null&&moreStatuses.size()>0){
					for(int i=0;i<moreStatuses.size();i++){
						statuses.add(moreStatuses.get(i));
					}
				}
				moreJokeHandler.sendMessage(msg);
			}
		}.start();
	}
	private Handler moreJokeHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if(moreStatuses==null||moreStatuses.size()==0){
				ToastAndDialog.Toast(UserWeiboActivity.this, "请再等等。", 3000);
				return;
			}
				for(int i=0;i<moreStatuses.size();i++){
					view_List.add(addView(moreStatuses.get(i), UserWeiboActivity.this));
				}
				isLoading = MathOperation.addToArray(isLoading, moreStatuses.size());
				isAnimationShowed = MathOperation.addToArray(isAnimationShowed, view_List.size());
				Configure.DetailWeiboImages = MathOperation.addToArray(Configure.DetailWeiboImages,moreStatuses.size());
				
				if(moreStatuses.size()==0)ToastAndDialog.Toast(UserWeiboActivity.this, "抱歉，已经没有更多资源了", 3000);
				else ToastAndDialog.Toast(UserWeiboActivity.this, "已经为您载入"+moreStatuses.size()+"条新数据", 3000);
				adapter.notifyDataSetChanged();
				if(moreStatuses.size()>15)
					isLoadingMore = false;
			}

	};
	public void recyleBitmap(int... positions) {
		for(int position: positions){
			if(view_List!=null&&Configure.DetailWeiboImages!=null
					&&position>=0&&position<view_List.size()&&Configure.DetailWeiboImages[position<Configure.DetailWeiboImages.length?position:Configure.DetailWeiboImages.length-1]!=null){
				ImageView imageview = (ImageView) view_List.get(
						position<view_List.size()?position:view_List.size()-1).findViewById(
						R.id.detail_image);
				imageview.setImageBitmap(null);
				Configure.DetailWeiboImages[position<Configure.DetailWeiboImages.length?position:Configure.DetailWeiboImages.length-1].recycle();
				Configure.DetailWeiboImages[position<Configure.DetailWeiboImages.length?position:Configure.DetailWeiboImages.length-1]=null;
			}
		}
		System.gc();
	}
	public void loadContentImg(int... positions) {
		for (final int position : positions) {
			if (isLoading[position<isLoading.length?position:isLoading.length-1])
				continue;
			isLoading[position<isLoading.length?position:isLoading.length-1] = true;
			Status status = statuses.get(position<statuses.size()?position:statuses.size()-1);
			String content_url = null;
			if (status.getRetweeted_status() == null) {
				content_url = status.getBmiddle_pic();
			} else {
				content_url = status.getRetweeted_status()
						.getBmiddle_pic();
			}
			if(content_url==null || content_url.equals(""))
				continue;
			final ImageView content_img = (ImageView) view_List.get(position<view_List.size()?position:view_List.size()-1)
					.findViewById(R.id.detail_image);
			content_img.setOnClickListener(UserWeiboActivity.this);
			if (Configure.DetailWeiboImages[position<Configure.DetailWeiboImages.length?position:Configure.DetailWeiboImages.length-1]!=null) {
				content_img.setImageBitmap(Configure.DetailWeiboImages[position]);
				if(content_url.endsWith("gif")){
					content_img.setBackgroundResource(R.drawable.isgif);//11111111111111数组中有图片资源，直接用
				}else{
					content_img.setBackgroundResource(R.drawable.image_bg);//11111111111111数组中有图片资源，直接用
				}
				isLoading[position<isLoading.length?position:isLoading.length-1] = false;
				continue;
			}
			if(!isAnimationShowed[position<isAnimationShowed.length?position:isAnimationShowed.length-1]){
				Button btn = (Button) view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.detail_progress);
				btn.setVisibility(0);btn.setBackgroundDrawable(null);btn.setText("正在载入图片");
			}
			loading_pb= (ProgressBar) view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.image_loading);
			loading_pb.setVisibility(0);
			asyncImageLoader_Weibo.loadDrawable(UserWeiboActivity.this,content_url, new ImageCallback_DW() {
						public void imageLoaded(Bitmap imageDrawable,
								String imageUrls) {//22222222222222网络或者本地文件下载
							if(position>Configure._position-2 && position<Configure._position+2){
								((ImageView) view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.detail_image)).setImageBitmap(imageDrawable);
								((ImageView) view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.detail_image)).setBackgroundResource(R.drawable.image_bg);
								if(imageUrls.endsWith("gif")){
									((ImageView) view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.detail_image)).setBackgroundResource(R.drawable.isgif);//11111111111111数组中有图片资源，直接用
								}else{
									((ImageView) view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.detail_image)).setBackgroundResource(R.drawable.image_bg);//11111111111111数组中有图片资源，直接用
								}
								if(!isAnimationShowed[position<isAnimationShowed.length?position:isAnimationShowed.length-1]){
								isAnimationShowed[position<isAnimationShowed.length?position:isAnimationShowed.length-1] = true;
								}
							}
							view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.image_loading).setVisibility(8);
							view_List.get(position<view_List.size()?position:view_List.size()-1).findViewById(R.id.detail_progress).setVisibility(8);
							isLoading[position<isLoading.length?position:isLoading.length-1] = false;
						}
					},position,loading_pb);
		}
	}

	public void Comment(String c_id){
		if (Configure.N_USER_KEY == null) {
			intentTo=2;
			intentToLogin();
		} else {
			Intent intent  = new Intent();
			intent.setClass(UserWeiboActivity.this, UserCommentActivity.class);
			intent.putExtra("w_id", statuses.get(Configure._position).getId()+"");
			intent.putExtra("c_id", c_id);
			startActivity(intent);
		}
	}
	public void Forward() {
		if (Configure.N_USER_KEY == null) {
			intentTo=0;intentToLogin();
		} else {
			intentToForward();
		}

	}
	public void Collect(){
		if (Configure.N_USER_KEY == null) {
			intentTo=1;intentToLogin();
		}else{
			progressDialog = ProgressDialog.show(UserWeiboActivity.this, "请稍等片刻...",
					"小夜正在努力的为您与服务端通信", true, true);
			new Thread(){
				public void run(){
					Status status=null;
					try {
						status = Configure.getUserWeibo(UserWeiboActivity.this).createFavorite(statuses.get(Configure._position).getId());
					} catch (WeiboException e) {
						status=null;
					}
					Message msg = colHandler.obtainMessage();
					msg.obj = status;
					colHandler.sendMessage(msg);
				}
			}.start();
		}
	}
	private Handler colHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if(msg.obj !=null && ((Status) msg.obj).getId()>0)
				ToastAndDialog.Toast(UserWeiboActivity.this, "已经帮您收藏好了。", 3000);
			else
				ToastAndDialog.Toast(UserWeiboActivity.this, "网络通讯出现了一点小问题噢。", 3000);
		}
	};

	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(view_List!=null && view_List.size()>0)
				recyleBitmap(Configure._position-2,Configure._position-1,Configure._position,Configure._position+1,Configure._position+2);
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			return false;
		}
		return false;
	}

	private PopupWindow m_popupWindow;List<Comment> comments ;
	private ListView p_listview;Comments_Pop_ListAdapter popadapter;
	private PullToRefreshListView p_contain;
	private RelativeLayout p_list_footer,p_loading;private TextView p_more;
	private TextView p_mRefreshViewLastUpdated;
	private long p_sinceId,p_maxId;private List<Comment> p_refreshComments,p_moreComments = null;
	
	private Handler commentHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();

			if(comments !=null&&comments.size()>0){
				LayoutInflater lay = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout v = (LinearLayout) lay.inflate(
						R.layout.poplist_category, null);
				p_contain = (PullToRefreshListView) v.findViewById(R.id.container);
				p_listview = p_contain.getList();
				p_list_footer = (RelativeLayout) LayoutInflater.from(UserWeiboActivity.this).inflate(
						R.layout.listitem_weibolist_footer, null);
				p_more = (TextView) p_list_footer.findViewById(R.id.more);
				p_loading = (RelativeLayout) p_list_footer.findViewById(R.id.loading);
				p_listview.addFooterView(p_list_footer);
				p_mRefreshViewLastUpdated = (TextView) v.findViewById(R.id.pull_to_refresh_time);
				p_mRefreshViewLastUpdated.setText("更新于：12-13 11：00");
				
				p_listview.setDivider(null);p_listview.setOnItemClickListener(UserWeiboActivity.this);
				p_loading.setOnClickListener(UserWeiboActivity.this);p_more.setOnClickListener(UserWeiboActivity.this);
				p_contain.setOnChangeStateListener(UserWeiboActivity.this);
				p_listview.setDivider(null);
				p_sinceId = comments.get(0).getId();p_maxId = comments.get(comments.size()-1).getId();
				popadapter = new Comments_Pop_ListAdapter(
						UserWeiboActivity.this, comments);
				p_listview.setAdapter(popadapter);
	
				m_popupWindow = new PopupWindow(v, LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT,true);
				m_popupWindow.setBackgroundDrawable(new BitmapDrawable()); 
				m_popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style); 
				m_popupWindow.showAtLocation(btn_back,
						Gravity.CENTER, 0, 0); 
			}
			else{
				Comment("");
			}
		}
	};

	Handler moreCommentsHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			if(p_moreComments==null||p_moreComments.size()==0){
				Toast.makeText(UserWeiboActivity.this, "请再等等。", 1000).show();
				p_more.setVisibility(View.GONE);
				p_loading.setVisibility(View.GONE);
			}
			else{
				Toast.makeText(UserWeiboActivity.this, "已经为您载入"+p_moreComments.size()+"条新数据", 1000).show();
				for(int i=0;i<p_moreComments.size();i++)
					comments.add(p_moreComments.get(i));
				popadapter.notifyDataSetChanged();
				p_maxId = comments.get(comments.size()-1).getId();
				p_more.setVisibility(View.VISIBLE);
				p_loading.setVisibility(View.GONE);
			}
			isLoadingMore = false;
		}
	};
	Handler RefreshHandler = new Handler(){
		public void handleMessage(Message msg) {
			p_contain.onRefreshComplete();
			if(p_refreshComments==null||p_refreshComments.size()==0){
				Toast.makeText(UserWeiboActivity.this, "请再等等。", 1000).show();
			}
			else{
				Toast.makeText(UserWeiboActivity.this, "已经为您载入"+p_refreshComments.size()+"条新数据", 1000).show();
				for(int i=p_refreshComments.size()-1;i>=0;i--)
					comments.add(0, p_refreshComments.get(i));
				popadapter.notifyDataSetChanged();
				p_sinceId = comments.get(0).getId();
			}
		}
	};
	public void RefreshComments(){
		new Thread(){
			public void run(){
				p_refreshComments = null;
				try {
					p_refreshComments = userWeibo.getComments(statuses.get(Configure._position).getId()+"",new Paging(p_sinceId));
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = RefreshHandler.obtainMessage();
				RefreshHandler.sendMessageDelayed(msg,1000);
			}
		}.start();
	}
	public void LoadingMoreComments(){
		isLoadingMore = true;
		p_more.setVisibility(View.GONE);
		p_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run(){
				p_moreComments =null;
				try {
					p_moreComments = userWeibo.getComments(statuses.get(Configure._position).getId()+"",new Paging(1,20,1,p_maxId-1));
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg =moreCommentsHandler.obtainMessage();
				moreCommentsHandler.sendMessageDelayed(msg,1000);
			}
		}.start();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.detail_back:
			if(view_List!=null && view_List.size()>0)
				recyleBitmap(Configure._position-2,Configure._position-1,Configure._position,Configure._position+1,Configure._position+2);
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			break;
		
		case R.id.d_refresh:
			if(statuses!=null){
				((ImageView) view_List.get(Configure._position).findViewById(R.id.detail_image)).setImageBitmap(null);
				((ImageView) view_List.get(Configure._position).findViewById(R.id.detail_image)).setBackgroundDrawable(null);
				loadContentImg(Configure._position);
			}
			break;
		case R.id.d_comment:
			if(statuses!=null){
				progressDialog = ProgressDialog.show(UserWeiboActivity.this, "请稍等片刻...",
						"小夜正在努力的为您载入评论", true, true);
				new Thread(){
					public void run(){
						comments =null;int loadingPosition = Configure._position;
						try {
							comments = userWeibo.getComments(statuses.get(Configure._position).getId()+"");
						} catch (WeiboException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(loadingPosition == Configure._position){
							Message msg = commentHandler.obtainMessage();
							commentHandler.sendMessage(msg);
						}
					}
				}.start();
			}
			break;
		case R.id.d_forward:
			if(statuses!=null){
				Forward();
			}
			break;
		case R.id.d_collect:
			if(statuses!=null){
				Collect();
			}
			break;
		case R.id.d_more:
			viewpager.setCurrentItem(Configure._position+1);
			break;
		case R.id.detail_image:
			if(Configure.DetailWeiboImages[Configure._position] !=null){
				Intent intent = new Intent(UserWeiboActivity.this,
						ImageViewActivity.class);
				String content_url = null;
				if (statuses.get(Configure._position).getRetweeted_status() == null) {
					content_url = statuses.get(Configure._position).getBmiddle_pic();
				} else {
					content_url = statuses.get(Configure._position).getRetweeted_status()
							.getBmiddle_pic();
				}
				intent.putExtra("imgUrl",content_url);IntentData.PicFrom=0;
				startActivity(intent);
				//overridePendingTransition(R.anim.popshow_anim, R.anim.pophidden_anim);
			}
			break;
		case R.id.more:
			if(!isLoadingMore && HttpClients.isConnect(UserWeiboActivity.this))
				LoadingMoreComments();
			break;
		case R.id.loading:
			break;
		}
	}
	public void intentToLogin(){
		progressDialog = ProgressDialog.show(UserWeiboActivity.this,
				"请稍等片刻...", "小夜正在努力的为您处理数据中", true, true);
		new Thread() {
			public void run() {

				System.setProperty("weibo4j.oauth.consumerKey",
						Weibo.CONSUMER_KEY);
				System.setProperty("weibo4j.oauth.consumerSecret",
						Weibo.CONSUMER_SECRET);
				String authUrl =null;
				Weibo weibo = new Weibo();
				RequestToken requestToken;
				try {
					requestToken = weibo
							.getOAuthRequestToken("life://UserWeiboActivity");
					OAuthConstantBean.getInstance()
							.setRequestToken(requestToken);
					authUrl = requestToken
							.getAuthenticationURL()
							+ "&display=mobile";
				
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				Message msg = loginHandler.obtainMessage();
				msg.obj=authUrl;
				loginHandler.sendMessage(msg);
			}
		}.start();
	}
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("url",(String) msg.obj);
			intent.putExtras(bundle);
			intent.setClass(UserWeiboActivity.this,
					UserLoginActivity.class);
			startActivity(intent);
		}
	};
	public void intentToForward(){
		String content_url = null;
		if (statuses.get(Configure._position).getRetweeted_status() == null) {
			content_url = statuses.get(Configure._position).getBmiddle_pic();
		} else {
			content_url = statuses.get(Configure._position).getRetweeted_status()
					.getBmiddle_pic();
		}
		Intent intent = new Intent();
		intent.putExtra("text", statuses.get(Configure._position).getText());
		intent.putExtra("image",content_url);
		intent.putExtra("position", Configure._position);
		intent.setClass(UserWeiboActivity.this, UserForwardActivity.class);
		startActivity(intent);
	}
	@Override
	protected void onNewIntent(final Intent intent) {
		progressDialog = ProgressDialog.show(UserWeiboActivity.this,
				"请稍等片刻...", "小夜正在努力的为您处理数据中", true, true);
		new Thread() {
			public void run() {
				try {
					Uri uri = intent.getData();
					if (uri == null) {
						return;
					}
					Weibo weibo = OAuthConstantBean.getInstance().getWeibo();
					RequestToken requestToken = OAuthConstantBean.getInstance()
							.getRequestToken();
					String nulls = uri.getQueryParameter("oauth_verifier");
					if (nulls != null) {
						AccessToken accessToken = requestToken
								.getAccessToken(nulls);
						weibo.setToken(accessToken.getToken(),
								accessToken.getTokenSecret());
						List<weibo4android.Status> statuses = null;
						statuses = weibo.getUserTimeline();
						if (statuses.size() != 0) {
							SharedPreferences refreshtime = getSharedPreferences(
									"sp_users", 0);
							refreshtime.edit().putLong("UserId",accessToken.getUserId()).commit();
							refreshtime.edit().putString("UserName",statuses.get(0).getUser().getName()).commit();
							refreshtime.edit().putString("Token", accessToken.getToken()).commit();
							refreshtime.edit().putString("TokenSecret",accessToken.getTokenSecret()).commit();
							Configure.setUserWeibo(accessToken.getUserId(),statuses.get(0).getUser().getName(),accessToken.getToken(),accessToken.getTokenSecret());
						} else {
							Toast.makeText(getApplicationContext(), "授权失败,请重试",
									Toast.LENGTH_LONG).show();
						}
					}
				} catch (WeiboException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				Message msg = ForwardHandler.obtainMessage();
				ForwardHandler.sendMessage(msg);
			}
		}.start();

	}
	Handler ForwardHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
		if(intentTo==0)	Forward();
		else if(intentTo==1)	Collect();
		else if(intentTo==2)	Comment("");
		}
	};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onChangeState(PullToRefreshListView container, int state) {
		// TODO Auto-generated method stub
		switch (state) {
		case PullToRefreshListView.STATE_LOADING:
			RefreshComments();
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(UserWeiboActivity.this)             
		.setMessage("请选择您的操作")             
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("评论该微博",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Comment("");
					}
				})
		.setNegativeButton("回复该评论", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Comment(comments.get(Configure._position).getId()+"");
					}
				})
		.create().show();
	}
	@Override
	public void onClickAd() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDisplayAd() {
		// TODO Auto-generated method stub
		
	}

}












