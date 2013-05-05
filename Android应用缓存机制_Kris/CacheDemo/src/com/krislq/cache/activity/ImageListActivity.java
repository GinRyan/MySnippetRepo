package com.krislq.cache.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.krislq.cache.R;
import com.krislq.cache.adapter.ImageAdapter;
import com.krislq.cache.manager.DownloadManager;
import com.krislq.cache.widget.ScrollToFootRefreshListView;
import com.krislq.cache.widget.ScrollToFootRefreshListView.OnRefreshListener;

public class ImageListActivity extends Activity implements OnRefreshListener{

	private ScrollToFootRefreshListView mListView = null;
	private int totalCount; 	// total count of the user's friend
	private DownloadManager downloadManager;
	private ImageAdapter	imageAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		downloadManager = new DownloadManager(this, mHandler, DownloadManager.TYPE_FRIEND_ICON);
		setContentView(R.layout.list);
		mListView = (ScrollToFootRefreshListView) findViewById(R.id.list_view);
		mListView.setOnRefreshListner(this);
		
		imageAdapter = new ImageAdapter(this, downloadManager);
		totalCount = 20;
		imageAdapter.setCount(totalCount);
		mListView.setAdapter(imageAdapter);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		}
	};
	@Override
	public void onRefresh() {
		totalCount +=20;
		imageAdapter.setCount(totalCount);
		imageAdapter.notifyDataSetChanged();
		mListView.onRefreshCompleted();
	}
	
}
