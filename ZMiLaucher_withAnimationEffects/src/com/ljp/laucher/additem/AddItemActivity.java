package com.ljp.laucher.additem;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.ljp.laucher.R;
import com.ljp.laucher.database.LaucherDataBase;
import com.ljp.laucher.databean.ContentItem;

public class AddItemActivity extends Activity implements OnClickListener, OnItemClickListener{

	boolean isLevel_2 = false;
	HashMap<String, Boolean> isChoiceChanged = new HashMap<String, Boolean>();

	private ListView listview,listview_content;private AddItemAdapter adapter;ItemContentAdapter adapter_content;
	List<ContentItem> listData;List<ContentItem> itemData;
	LaucherDataBase database = new LaucherDataBase(AddItemActivity.this);
	private Button btnOk,btnBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_additem_items);
		
		database.open();
		listData = database.getItems();
		database.close();
		
		listview = (ListView) findViewById(R.id.additem_list);listview_content = (ListView) findViewById(R.id.additem_list_content);
		adapter = new AddItemAdapter(AddItemActivity.this,listview,listData);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);listview_content.setOnItemClickListener(this);
		btnOk = (Button) findViewById(R.id.btn_ok);btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setText("");
		btnOk.setOnClickListener(this);btnBack.setOnClickListener(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.btn_ok:
			if(isLevel_2){
				resetChoice();
			}
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			break;
		case R.id.btn_back:
			if(isLevel_2){
				isLevel_2=false;
				listview.setVisibility(0);
				startOutAnimaion();
				btnBack.setText("");
				resetChoice();
			}
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> view, View arg, int position, long arg3) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.additem_list:
			database.open();
			itemData  = database.getItems(listData.get(position).getFrom());
			database.close();
			isLevel_2 =true;btnBack.setText("＜");
			adapter_content = new ItemContentAdapter(AddItemActivity.this,listview_content,itemData);
			listview_content.setAdapter(adapter_content);
			startInAnimation();
			break;
		case R.id.additem_list_content:
			itemData.get(position).setChoice(!itemData.get(position).isChoice());
			if(isChoiceChanged.containsKey(itemData.get(position).getText())){
				isChoiceChanged.put(itemData.get(position).getText(),! isChoiceChanged.get(itemData.get(position).getText()));
			}else{
				isChoiceChanged.put(itemData.get(position).getText(),true);
			}
			adapter_content.notifyDataSetChanged();
			break;
		}
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub\
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				if(isLevel_2){
					isLevel_2=false;
					listview.setVisibility(0);
					startOutAnimaion();
					btnBack.setText("");
					resetChoice();
				}else{ 
					finish();
					overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
				}
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	public void resetChoice(){
		database.open();
		for(int i=0;i<itemData.size();i++){
			if(isChoiceChanged.containsKey(itemData.get(i).getText())){
				if(isChoiceChanged.get(itemData.get(i).getText())){
					database.updateChoice(itemData.get(i).getText(),itemData.get(i).isChoice());
					Intent intent = new Intent("intentToAddLauncher");
					intent.putExtra("text", itemData.get(i).getText());
					sendBroadcast(intent);
				}
			}
		}
		isChoiceChanged.clear();
		database.close();
	}
	public void startInAnimation(){
		Animation a = AnimationUtils.loadAnimation(AddItemActivity.this, R.anim.default_toleft);
		final Animation b = AnimationUtils.loadAnimation(AddItemActivity.this, R.anim.default_fromright);
		listview_content.setVisibility(0);
		a.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				listview_content.startAnimation(b);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				listview.setVisibility(8);
			}
		});
		listview.startAnimation(a);
	}
	public void startOutAnimaion(){
		Animation a = AnimationUtils.loadAnimation(AddItemActivity.this, R.anim.default_toright);
		final Animation b = AnimationUtils.loadAnimation(AddItemActivity.this, R.anim.default_fromleft);
		listview_content.setVisibility(0);
		a.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				listview.startAnimation(b);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				listview_content.setVisibility(8);
			}
		});
		listview_content.startAnimation(a);
	}
}













