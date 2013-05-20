package com.krislq.cache.activity;

import android.app.Activity;
import android.os.Bundle;

import com.krislq.cache.R;
import com.krislq.cache.adapter.TextAdapter;
import com.krislq.cache.widget.ScrollToFootRefreshListView;
import com.krislq.cache.widget.ScrollToFootRefreshListView.OnRefreshListener;

public class TextListActivity extends Activity implements OnRefreshListener{

	private ScrollToFootRefreshListView mListView = null;
	private int totalCount; 	// total count of the user's friend
	private TextAdapter	textAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list);
		mListView = (ScrollToFootRefreshListView) findViewById(R.id.list_view);
		mListView.setOnRefreshListner(this);
		
		textAdapter = new TextAdapter(this);
		totalCount = 20;
		textAdapter.setCount(totalCount);
		mListView.setAdapter(textAdapter);
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
	
	@Override
	public void onRefresh() {
		totalCount +=20;
		textAdapter.setCount(totalCount);
		textAdapter.notifyDataSetChanged();
		mListView.onRefreshCompleted();
	}
	
}
