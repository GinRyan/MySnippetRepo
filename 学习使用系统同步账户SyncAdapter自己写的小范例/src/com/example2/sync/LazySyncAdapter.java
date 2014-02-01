package com.example2.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class LazySyncAdapter extends AbstractThreadedSyncAdapter {
	ContentResolver mContentResolver;

	public LazySyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContentResolver = context.getContentResolver();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public LazySyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		mContentResolver = context.getContentResolver();
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
		Log.e("tag", "开始同步:" + authority + "  账户：" + account.name + "  类型:" + account.type);
	}

}
