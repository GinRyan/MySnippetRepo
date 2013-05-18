package com.testmemoryadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ArrayList testList = new ArrayList();
        
        for (int i = 0; i < 30; i++) {
			testList.add(0);
		}
        
        TestAdapter ta = new TestAdapter(this,testList);
        
        ListView lv = (ListView) findViewById(R.id.testListView);
        
        lv.setAdapter(ta);
        
    }
}