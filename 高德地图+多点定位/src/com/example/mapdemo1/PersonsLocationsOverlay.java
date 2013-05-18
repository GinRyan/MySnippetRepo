package com.example.mapdemo1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Projection;

public class PersonsLocationsOverlay extends ItemizedOverlay<OverlayItem> {
	public static final List<GeoPoint> geopoint = new LinkedList<GeoPoint>();

	public PersonsLocationsOverlay(Drawable drawable) {
		super(drawable);
	}

	public static List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private Context mContext;
	private double mLat1 = 36.080988; // point1 纬度
	private double mLon1 = 120.332134; // point1 经度
	private double mLat2 = 36.084795;
	private double mLon2 = 120.33237;
	private double mLat3 = 36.08385;
	private double mLon3 = 120.333282;

	public PersonsLocationsOverlay(Drawable marker, Context context) {
		super(boundCenterBottom(marker));
		this.marker = marker;
		this.mContext = context;
		// 用给定的经纬度构造GeoPoint，单位是微度(度* 1E6)
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));
		geopoint.add(p1);
		geopoint.add(p2);
		geopoint.add(p3);
		// 构造OverlayItem 的三个参数依次为：item 的位置，标题文本，文字片段
		GeoList.add(new OverlayItem(p1, "P1", "point1"));
		GeoList.add(new OverlayItem(p2, "P2", "point2"));
		GeoList.add(new OverlayItem(p3, "P3", "point3"));

		populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// Projection 接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
		Projection projection = mapView.getProjection();
		for (int index = size() - 1; index >= 0; index--) { // 遍历GeoList
			OverlayItem overLayItem = getItem(index); // 得到给定索引的item
			String title = overLayItem.getTitle();
			// 把经纬度变换到相对于MapView 左上角的屏幕像素坐标
			Point point = projection.toPixels(overLayItem.getPoint(), null);
			// 可在此处添加您的绘制代码
			Paint paintText = new Paint();
			paintText.setColor(Color.BLACK);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x - 30, point.y - 25, paintText); // 绘制文本
		}
		super.draw(canvas, mapView, shadow);
		// 调整一个drawable 边界，使得（0，0）是这个drawable 底部最后一行中心的一个像素
		boundCenterBottom(marker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return GeoList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return GeoList.size();
	}

	@Override
	// 处理当点击事件
	protected boolean onTap(int i) {
		setFocus(GeoList.get(i));
		Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		// TODO Auto-generated method stub
		return super.onTap(point, mapView);
	}
}
