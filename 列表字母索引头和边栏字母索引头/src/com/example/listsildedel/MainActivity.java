package com.example.listsildedel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ListView mListView;
	private SideBar indexBar;
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private View head;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mListView = (ListView) this.findViewById(R.id.list);
		indexBar = (SideBar) findViewById(R.id.sideBar);
		mDialogText = (TextView) LayoutInflater.from(this).inflate(
				R.layout.list_position, null);
		head = LayoutInflater.from(this).inflate(R.layout.head, null);
		mListView.addHeaderView(head);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		// 初始化数据
		List<Content> list = new ArrayList<Content>();
		getDataIntoList(list);
		// 根据a-z进行排序
		Collections.sort(list, new PinyinComparator());
		// 实例化自定义内容适配类
		NameAdapter adapter = new NameAdapter(this, list);
		// 为listView设置适配
		mListView.setAdapter(adapter);
		// 设置SideBar的ListView内容实现点击a-z中任意一个进行定位
		indexBar.setListView(mListView);
	}

	/**
	 * 将内容填充到list中
	 * 
	 * @param list
	 */
	public void getDataIntoList(List<Content> list) {
		String[] array = getResources().getStringArray(
				R.array.default_smiley_texts);
		for (int i = 0; i < array.length; i++) {
			String string = PinyinLetterHelper.getPinyinFirstLetter(array[i]);
			list.add(new Content(string, array[i]));
		}
	}

}
