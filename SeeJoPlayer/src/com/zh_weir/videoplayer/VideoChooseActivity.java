package com.zh_weir.videoplayer;

import java.util.LinkedList;
import java.util.zip.Inflater;
import com.zh_weir.videoplayer.VideoPlayerActivity.MovieInfo;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class VideoChooseActivity extends Activity{

	private static int height , width;
	private LinkedList<MovieInfo> mLinkedList;
	private LayoutInflater mInflater;
	View root;
	private EditText urlInput;   
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dialog);
		
		mLinkedList = VideoPlayerActivity.playList;
		
		mInflater = getLayoutInflater();
		ImageButton iButton = (ImageButton) findViewById(R.id.cancel);
		iButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				VideoChooseActivity.this.finish();
			}
			
		});
		
		ListView myListView = (ListView) findViewById(R.id.list);
		myListView.setAdapter(new BaseAdapter(){

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mLinkedList.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(int arg0, View convertView, ViewGroup arg2) {
				// TODO Auto-generated method stub
				if(convertView==null){
					convertView = mInflater.inflate(R.layout.list, null);
				}
				TextView text = (TextView) convertView.findViewById(R.id.text);
				text.setText(mLinkedList.get(arg0).displayName);
				
				return convertView;   
			}
			
		});
		
		myListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("CHOOSE", arg2);
				VideoChooseActivity.this.setResult(Activity.RESULT_OK, intent);
				VideoChooseActivity.this.finish();
			}
		});

		urlInput = (EditText) findViewById(R.id.url_input);
		urlInput.setText("http://");
		urlInput.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				
				Log.d("actionId", ""+actionId);
				Editable url = ((EditText)v).getEditableText();
				Intent intent = new Intent();
				intent.putExtra("CHOOSE_URL", url.toString());
				VideoChooseActivity.this.setResult(Activity.RESULT_OK, intent);
				VideoChooseActivity.this.finish();
				
				
				return false;
			}
			
		});

		myListView.requestFocus();
	}
}
