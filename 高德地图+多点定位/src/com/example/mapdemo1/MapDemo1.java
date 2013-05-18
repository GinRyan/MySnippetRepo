package com.example.mapdemo1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;

public class MapDemo1 extends MapActivity {
	private MapView mMapView;
	private MapController mapController;
	MyLocationOverlay myLoc;
	public static final List<GeoPoint> geopoint = new LinkedList<GeoPoint>();
	public static List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
	private double mLat1 = 36.080988; // point1 纬度
	private double mLon1 = 120.332134; // point1 经度
	private double mLat2 = 36.084795;
	private double mLon2 = 120.33237;
	private double mLat3 = 36.08385;
	private double mLon3 = 120.333282;
	private void initPoints() {
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
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setMapMode(MAP_MODE_VECTOR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_demo1);
		initPoints();
		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.setBuiltInZoomControls(true);
		mapController = mMapView.getController();
		mapController.setZoom(18);
		
		myLoc = new MyLocationOverlay(this, mMapView) {
			@Override
			public void onLocationChanged(Location arg0) {
				super.onLocationChanged(arg0);
				mMapView.getController().animateTo(getMyLocation());
				mapController.setFitView(geopoint);
			}
		};
		
		myLoc.enableMyLocation();
		myLoc.enableCompass();
		mMapView.getOverlays().add(myLoc);
		Drawable marker = getResources().getDrawable(R.drawable.loc);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		mMapView.getOverlays().add(new PersonsLocationsOverlay(marker, this));

	}

	@Override
	protected void onStop() {
		super.onStop();
		myLoc.disableCompass();
		myLoc.disableMyLocation();

	}
}
