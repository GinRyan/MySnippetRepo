package hong.specialEffects.ui;

import hong.specialEffects.R;
import hong.specialEffects.wight.HorizontialListView;
import android.app.Activity;
import android.os.Bundle;

public class HorizontalListViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.listviewdemo);
		
		HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		
		HorizontialListAdapter mAdapter=new HorizontialListAdapter(dataObjects,R.layout.radio_item);
		listview.setAdapter(mAdapter);
	}
	
	private static String[] dataObjects = new String[]{ "Text #1","Text #2","Text #3" }; 
}
