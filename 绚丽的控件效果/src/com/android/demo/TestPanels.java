package com.android.demo;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.android.demo.interpolator.BackInterpolator;
import com.android.demo.interpolator.BounceInterpolator;
import com.android.demo.interpolator.EasingType;
import com.android.demo.interpolator.ElasticInterpolator;
import com.android.demo.interpolator.ExpoInterpolator;
import com.android.demo.widget.Panel;
import com.android.demo.widget.Panel.OnPanelListener;

public class TestPanels extends Activity implements OnPanelListener {

	private Panel bottomPanel;
	private Panel topPanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_main);

        Panel panel;
        
        topPanel = panel = (Panel) findViewById(R.id.topPanel);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BounceInterpolator(EasingType.INOUT));
        
        panel = (Panel) findViewById(R.id.leftPanel1);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BackInterpolator(EasingType.OUT, 2));

        panel = (Panel) findViewById(R.id.leftPanel2);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BackInterpolator(EasingType.OUT, 2));

        panel = (Panel) findViewById(R.id.rightPanel);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new ExpoInterpolator(EasingType.OUT));

        bottomPanel = panel = (Panel) findViewById(R.id.bottomPanel);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new ElasticInterpolator(EasingType.OUT, 1.0f, 0.3f));
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_T) {
    		topPanel.setOpen(!topPanel.isOpen(), false);
    		return false;
    	}
    	if (keyCode == KeyEvent.KEYCODE_B) {
    		bottomPanel.setOpen(!bottomPanel.isOpen(), true);
    		return false;
    	}
    	return super.onKeyDown(keyCode, event);
    }

	public void onPanelClosed(Panel panel) {
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("TestPanels", "Panel [" + panelName + "] closed");
	}
	public void onPanelOpened(Panel panel) {
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("TestPanels", "Panel [" + panelName + "] opened");
	}
}
