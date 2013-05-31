package test.pinnedheaderlistview.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PinnedHeaderListViewActivity extends Activity {

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        PinnedHeaderListView listView = (PinnedHeaderListView) findViewById(R.id.listview);
        
        List<ItemEntity> data = createTestData();
        
        // * 创建新的HeaderView，即置顶的HeaderView
        View HeaderView = getLayoutInflater().inflate(R.layout.listview_item_header, listView, false);
        listView.setPinnedHeader(HeaderView);
        
        CustomAdapter customAdapter = new CustomAdapter(getApplication(), data);
        listView.setAdapter(customAdapter);
        
        listView.setOnScrollListener(customAdapter);
    }
    
    // ===========================================================
    // Methods
    // ===========================================================

    private List<ItemEntity> createTestData() {
        
        List<ItemEntity> data = new ArrayList<ItemEntity>();
        
        ItemEntity itemEntity1 = new ItemEntity("路人甲", "马三立");
        ItemEntity itemEntity2 = new ItemEntity("路人甲", "赵本山");
        ItemEntity itemEntity3 = new ItemEntity("路人甲", "郭德纲");
        ItemEntity itemEntity4 = new ItemEntity("路人甲", "周立波");
        ItemEntity itemEntity5 = new ItemEntity("事件乙", "**贪污");
        ItemEntity itemEntity6 = new ItemEntity("事件乙", "**照门");
        ItemEntity itemEntity7 = new ItemEntity("书籍丙", "10天学会***");
        ItemEntity itemEntity8 = new ItemEntity("书籍丙", "**大全");
        ItemEntity itemEntity9 = new ItemEntity("书籍丙", "7天精通**");
        ItemEntity itemEntity10 = new ItemEntity("书籍丙", "**秘籍");
        ItemEntity itemEntity11 = new ItemEntity("书籍丙", "**宝典");
        ItemEntity itemEntity12 = new ItemEntity("地方丁", "河南");
        ItemEntity itemEntity13 = new ItemEntity("地方丁", "天津");
        ItemEntity itemEntity14 = new ItemEntity("地方丁", "北京");
        ItemEntity itemEntity15 = new ItemEntity("地方丁", "上海");
        ItemEntity itemEntity16 = new ItemEntity("地方丁", "广州");
        ItemEntity itemEntity17 = new ItemEntity("地方丁", "湖北");
        ItemEntity itemEntity18 = new ItemEntity("地方丁", "重庆");
        ItemEntity itemEntity19 = new ItemEntity("地方丁", "山东");
        ItemEntity itemEntity20 = new ItemEntity("地方丁", "陕西");
        
        data.add(itemEntity1);
        data.add(itemEntity2);
        data.add(itemEntity3);
        data.add(itemEntity4);
        data.add(itemEntity5);
        data.add(itemEntity6);
        data.add(itemEntity7);
        data.add(itemEntity8);
        data.add(itemEntity9);
        data.add(itemEntity10);
        data.add(itemEntity11);
        data.add(itemEntity12);
        data.add(itemEntity13);
        data.add(itemEntity14);
        data.add(itemEntity15);
        data.add(itemEntity16);
        data.add(itemEntity17);
        data.add(itemEntity18);
        data.add(itemEntity19);
        data.add(itemEntity20);
        
        return data;
        
    }
    
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
	
	class ItemEntity {
		private String mTitle;
		private String mContent;
		
		public ItemEntity(String pTitle, String pContent) {
			mTitle = pTitle;
			mContent = pContent;
		}
		
		public String getTitle() {
			return mTitle;
		}
		public String getContent() {
			return mContent;
		}
	}
	
}