package com.example2.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LazyAccountService extends Service {
	private LazyAuthenticator authenticator;

	public LazyAccountService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		authenticator = new LazyAuthenticator(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return authenticator.getIBinder();
	}
}
