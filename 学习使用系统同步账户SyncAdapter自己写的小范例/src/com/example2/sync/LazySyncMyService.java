package com.example2.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LazySyncMyService extends Service {
	private static LazySyncAdapter lazySyncAdapter = null;
	private static final Object sSyncAdapterLock = new Object();

	@Override
	public void onCreate() {
		super.onCreate();
		synchronized (sSyncAdapterLock) {
			if (lazySyncAdapter == null) {
				lazySyncAdapter = new LazySyncAdapter(this, true);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return lazySyncAdapter.getSyncAdapterBinder();
	}
}
