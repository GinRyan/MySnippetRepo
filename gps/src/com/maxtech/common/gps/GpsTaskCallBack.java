package com.maxtech.common.gps;

import com.maxtech.common.gps.GpsTask.GpsData;

public interface GpsTaskCallBack {

	public void gpsConnected(GpsData gpsdata);
	
	public void gpsConnectedTimeOut();
	
}
