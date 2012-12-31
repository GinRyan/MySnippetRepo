package hong.specialEffects.ui;

import hong.specialEffects.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 *  Copyright (C) 2010 ideasandroid
 *  欢迎访问http://www.ideasandroid.com
 *  让程序开发不再那么神秘
 */
public class HeaderBottomListActivity extends Activity {

	private ListView mylist = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.head_bottom);
		mylist = (ListView) findViewById(R.id.myListView);
		ArrayAdapter<String> arrayA=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray);
		mylist.setAdapter(arrayA);
	}
	
	String[] listArray = { "ideasAndrid", "ideasandroid.com", "演示程序", "欢迎访问ideasandroid.com",
			"让程序开发不再神秘",  "android", "ListView", "渐变效果",
			"欢迎光临！","测试1", "测试2", "测试3",
			"测试4"};
}