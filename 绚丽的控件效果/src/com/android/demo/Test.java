package com.android.demo;

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Test extends ListActivity {

    private ArrayAdapter<Item> adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Item[] items = {
        		new Item(TestPanels.class, "Test Panels"),
        		new Item(TestSwitcher.class, "Test Switcher"),
        		new Item(TestSmoothButton.class, "Test SmoothButton"),
        		new Item(TestInterpolators.class, "Test Interpolators"),
        		new Item(TestTheme.class, "Test Theme"),
        		new Item(TestVirtualKeyboard.class, "Test Virtual Keyboard"),
        		new Item(TestCustomTitle.class, "Test Custom Title"),
        };
		adapter = new ArrayAdapter<Item>(this, R.layout.simple_list_item_1, items);
		setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		startActivity(adapter.getItem(position));
    }
    
    class Item extends Intent {
    	String s;
    	public Item(Class<?> c, String s) {
    		super(Test.this, c);
    		this.s = s;
		}
    	
    	@Override
    	public String toString() {
    		return s;
    	}
    }
}
