package com.gaode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.amap.mapapi.map.Overlay;
import com.amap.mapapi.map.Projection;

public class GaodeMapActivity extends MapActivity {
	/** Called when the activity is first created. */
	private final static String TAG = "bb";
	private final static int REQUESTCODE = 1;
	public MapView mapView;
	Button button;
	LinearLayout layout;
	LayoutParams layoutParams;
	private Geocoder geocoder;
	int heightAn;
	private CompanyLocationOverlay companyLocationOverlay;
	SearchRecentSuggestions suggestions;
	MapController mapController;
	public View popView;
	GeoPoint geoPoint;
	List<Overlay> list;
	private int defaultLng = 116288700;
	private int defaultLat = 40050600;

	@SuppressWarnings("rawtypes")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.mapview);
		setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
		handleIntent(getIntent());
		suggestions = new SearchRecentSuggestions(this,
				SearchSuggestionProvider.AUTHORITY,
				SearchSuggestionProvider.MODE);
		/*
		 * Intent intent = getIntent(); //判断是否是搜索请求 if
		 * (Intent.ACTION_SEARCH.equals(intent.getAction())) { //获取搜索的查询内容（关键字）
		 * String query = intent.getStringExtra(SearchManager.QUERY);
		 * //执行相应的查询动作 Toast.makeText(this, query, Toast.LENGTH_LONG).show(); }
		 */

		// Class track = TrackballGestureDetector.class;
		// Constructor[] constr = track.getDeclaredConstructors();
		// Log.i(TAG,"Constructor====================================");
		// for (Constructor constructor : constr) {
		// Log.i(TAG,"number of Constructor------------" +
		// constructor.getParameterTypes().length);
		// for (Class c : constructor.getParameterTypes()) {
		// Log.i(TAG,c.getName() + "\t");
		// }
		// }
		Class mapactivity = MapActivity.class;
		Method[] methods = mapactivity.getDeclaredMethods();
		for (Method method : methods) {
			Log.i(TAG,
					"number of Constructor------------"
							+ method.getParameterTypes().length);
			Log.i(TAG, "method name------------" + method.getName());
		}
		init();
		initLocation();
		initOverlay();
		initOverlayItem();
		initPopview();
		initPoi();
	}

	@Override
	public boolean onSearchRequested() {
		Log.i(TAG, "onSearchRequested------------========");
		Bundle appData = new Bundle();
		appData.putString("key", "your words");
		startSearch(null, true, appData, false);

		return true;
	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	public void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			suggestions.saveRecentQuery(query, null);

			Toast.makeText(this, query, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "我的位置");
		menu.add(0, 2, 1, "路线");
		menu.add(0, 3, 1, "搜索");
		menu.add(0, 4, 2, "更多。。");
		return true;
	}

	private AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) {
			layout.setVisibility(View.VISIBLE);

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			companyLocationOverlay.setGeoPoint(geoPoint);
			companyLocationOverlay.setPositionName("company软件");
			mapController.animateTo(geoPoint);
			mapView.invalidate();
			break;
		case 3:
			final EditText editText = new EditText(this);
			new AlertDialog.Builder(this).setView(editText).setTitle("搜索")
					.setNegativeButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											editText.getWindowToken(), 0);
							searchName(editText.getText().toString());
						}

					}).show();
			break;
		case 2:
			TranslateAnimation mAnimUp = new TranslateAnimation(0, 0, -200, 0);
			// mAnimUp.setStartOffset(500);
			mAnimUp.setInterpolator(new LinearInterpolator());
			mAnimUp.setDuration(500);
			mAnimUp.setAnimationListener(animationListener);
			layout.setAnimation(mAnimUp);
			layout.startAnimation(mAnimUp);
			button = (Button) findViewById(R.id.search);
			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					layout.setVisibility(View.INVISIBLE);

				}

			});

			break;

		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUESTCODE) {
			Toast.makeText(this, "zheng chang fanhui ", 0).show();
		}
	}

	private void init() {
		mapView.setClickable(true);
		mapView.setEnabled(true);
		mapView.setTraffic(true);
		mapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		mapController = mapView.getController();
		geocoder = new Geocoder(this);
		list = mapView.getOverlays();
		companyLocationOverlay = new CompanyLocationOverlay();

		layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		View view = getLayoutInflater().inflate(R.layout.search_input, null);
		heightAn = view.getHeight();
		layoutParams.setMargins(100, 100, 0, 0);
		layout = new LinearLayout(this);
		layout.addView(view);
		layout.setGravity(Gravity.TOP);
		layout.setVisibility(View.INVISIBLE);
		addContentView(layout, layoutParams);

	}

	private void initLocation() {
		mapView.preLoad();
		geoPoint = new GeoPoint(defaultLat, defaultLng);
		// mapController.animateTo(geoPoint);
		mapController.setZoom(15);
		mapController.setCenter(geoPoint);
	}

	private void initOverlay() {
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
				mapView);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		list.add(myLocationOverlay);
		list.add(companyLocationOverlay);
	}

	private void initPopview() {

		popView = super.getLayoutInflater().inflate(R.layout.overlay_pop, null);
		mapView.addView(popView, new MapView.LayoutParams(
				MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM_CENTER));
		// 由于气泡的尾巴是在下边居中的,因此要设置成MapView.LayoutParams.BOTTOM_CENTER.
		// 这里没有给GeoPoint,在onFocusChangeListener中设置
		// views.add(popView);
		popView.setVisibility(View.GONE);

	}

	private void initOverlayItem() {
		Drawable marker = getResources().getDrawable(R.drawable.poi_1); // 得到需要标在地图上的资源

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight()); // 为maker定义位置和边界

		mapView.getOverlays().add(new MyOverItem(marker, this)); // 添加ItemizedOverlay实例到mMapView
	}

	private void initPoi() {
		Geocoder geocoder = new Geocoder(this);
		int x = geoPoint.getLatitudeE6(); // 得到geo纬度，单位微度 (度 * 1E6)
		double x1 = ((double) x) / 1000000;
		int y = geoPoint.getLongitudeE6(); // 得到geo经度，单位微度 (度 * 1E6)
		double y1 = ((double) y) / 1000000;
		// 得到逆理编码，参数分别为：纬度，经度，最大结果集
		try {
			List<Address> lstAddress = geocoder.getFromLocation(x1, y1, 5);
			if (lstAddress.size() != 0) {
				// Toast输出geo编码得到的地名
				for (int i = 0; i < lstAddress.size(); ++i) {
					Address adsLocation = lstAddress.get(i);
					Toast.makeText(getApplicationContext(),
							adsLocation.getFeatureName().toString(),
							Toast.LENGTH_LONG).show();
					Log.i(TAG, "Address found = " + adsLocation.toString());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Toast.makeText(getApplicationContext(), "hehheeeeeee",
				Toast.LENGTH_LONG).show();
		// TrackballGestureDetector trackballGestureDetector = new
		// TrackballGestureDetector
		return true;
	}

	/**
	 * 绘制单个点的overlay
	 * 
	 * @author wanglong
	 * 
	 */
	class CompanyLocationOverlay extends Overlay {
		private GeoPoint geoPointTemp = null;

		private String positionName = null;

		public void setGeoPoint(GeoPoint geoPoint) {
			this.geoPointTemp = geoPoint;
		}

		public void setPositionName(String positionName) {
			this.positionName = positionName;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			Paint companypaint = new Paint();
			Point companyScreenCoords = new Point();
			// 将经纬度转换成实际屏幕坐标
			if (geoPointTemp == null) {
				mapView.getProjection().toPixels(geoPoint, companyScreenCoords);
			} else {
				mapView.getProjection().toPixels(geoPointTemp,
						companyScreenCoords);
			}
			companypaint.setStrokeWidth(1);
			companypaint.setARGB(255, 255, 0, 0);
			companypaint.setStyle(Paint.Style.STROKE);
			Bitmap bmpCompany = BitmapFactory.decodeResource(getResources(),
					R.drawable.company);
			// 绘制图片
			canvas.drawBitmap(bmpCompany, companyScreenCoords.x,
					companyScreenCoords.y, companypaint);
			// 绘制文字
			if (positionName == null) {
				canvas.drawText("company软件", companyScreenCoords.x,
						companyScreenCoords.y, companypaint);
			} else {
				canvas.drawText(positionName, companyScreenCoords.x,
						companyScreenCoords.y, companypaint);
			}

		}
	}

	public void searchName(String positionName) {
		try {
			List<Address> list = geocoder.getFromLocationName(positionName, 1);
			if (list.size() != 0) {
				Address address = list.get(0);
				GeoPoint geoPointt = new GeoPoint(
						(int) (address.getLatitude() * 1E6),
						(int) (address.getLongitude() * 1E6));
				companyLocationOverlay.setGeoPoint(geoPointt);
				companyLocationOverlay.setPositionName(positionName);
				mapController.animateTo(geoPointt);
				mapView.invalidate();
			} else {
				Toast.makeText(this, positionName + "没有找到！", Toast.LENGTH_SHORT)
						.show();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * 多个overlay显示时通过ItemizedOverlay
 * 
 * @author wanglong
 * 
 */
class MyOverItem extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private GaodeMapActivity mContext;

	private TextView textView1;
	private TextView textView2;

	// 这里写死poi点。。可以从服务器查询获得

	private double mLat1 = 40.0506; // point1纬度
	private double mLon1 = 116.288700; // point1经度

	private double mLat2 = 40.051723;
	private double mLon2 = 116.287741;

	private double mLat3 = 40.052723;
	private double mLon3 = 116.286741;

	public MyOverItem(Drawable marker, Context context) {
		super(boundCenterBottom(marker));

		this.marker = marker;
		this.mContext = (GaodeMapActivity) context;

		// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));

		// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
		GeoList.add(new OverlayItem(p1, "P1", "point1"));
		GeoList.add(new OverlayItem(p2, "P2", "point2"));
		GeoList.add(new OverlayItem(p3, "P3", "point3"));
		// 该方法应该是将当前的GeoList的overlay 同步到 父类ItemizedOverlay中的list中以进行重绘
		// 所以当GeoList数据发生变化时需要调用该方法
		populate();

	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		// Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
		Projection projection = mapView.getProjection();
		for (int index = size() - 1; index >= 0; index--) { // 遍历GeoList
			OverlayItem overLayItem = getItem(index); // 得到给定索引的item

			String title = overLayItem.getTitle();
			// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
			Point point = projection.toPixels(overLayItem.getPoint(), null);

			Paint paintCircle = new Paint();
			paintCircle.setColor(Color.RED);
			canvas.drawCircle(point.x, point.y, 5, paintCircle); // 画圆

			Paint paintText = new Paint();
			paintText.setColor(Color.BLACK);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x, point.y - 25, paintText); // 绘制文本

		}

		super.draw(canvas, mapView, shadow);
		// 调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
		boundCenterBottom(marker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return GeoList.get(i);
	}

	@Override
	public int size() {
		return GeoList.size();
	}

	@Override
	// 处理当点击事件
	// mapview的onTouch事件会传播到overlay的 onTouch方法 通过点击范围可以确定触发哪个overlay的onTap
	protected boolean onTap(int i) {
		setFocus(GeoList.get(i));
		MapView.LayoutParams geoLP = (MapView.LayoutParams) mContext.popView
				.getLayoutParams();
		geoLP.point = GeoList.get(i).getPoint();
		mContext.mapView.updateViewLayout(mContext.popView, geoLP);
		mContext.popView.setVisibility(View.VISIBLE);
		textView1 = (TextView) mContext.findViewById(R.id.map_bubbleTitle);
		textView2 = (TextView) mContext.findViewById(R.id.map_bubbleText);
		textView1.setText("提示信息");
		textView2.setText("aaaaaaaaaaaaaaaaaaaaaaaaa===="
				+ GeoList.get(i).getSnippet());
		ImageView imageView = (ImageView) mContext
				.findViewById(R.id.map_bubbleImage);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				mContext.popView.setVisibility(View.GONE);
			}

		});
		// Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
		// Toast.LENGTH_SHORT).show();
		return true;
	}
}

class MyGuest implements OnGestureListener {

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
