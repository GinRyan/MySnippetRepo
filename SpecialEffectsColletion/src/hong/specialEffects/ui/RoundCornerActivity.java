package hong.specialEffects.ui;

import hong.specialEffects.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

/**
 * Android实现圆角ListView示例
 * @Description: Android实现圆角ListView示例

 * @FileName: RoundCornerActivity.java 

 * @Package com.corner.test 

 * @Author Hanyonglu

 * @Date 2012-3-5 上午11:48:06 

 * @Version V1.0
 */
public class RoundCornerActivity extends Activity {
	private CornerListView mListView = null;
	ArrayList<HashMap<String, String>> map_list1 = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle_main);

		getDataSource1();
		// getDataSource2();

		SimpleAdapter adapter1 = new SimpleAdapter(this, map_list1,
				R.layout.simple_list_item_1, new String[] { "item" },
				new int[] { R.id.item_title });
		mListView = (CornerListView) findViewById(R.id.list1);
		mListView.setAdapter(adapter1);
		mListView.setOnItemClickListener(new OnItemListSelectedListener());
	}

	public ArrayList<HashMap<String, String>> getDataSource1() {

		map_list1 = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();

		map1.put("item", "设置1");
		map2.put("item", "设置2");

		map_list1.add(map1);
		map_list1.add(map2);

		return map_list1;
	}

	class OnItemListSelectedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 == 0) {
				System.out.println("0");
			}else{
				System.out.println("1");
			}
		}
	}
}