package com.hitfm.improve;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class Cube extends Activity {
	private SurfaceView_Cube surfaceView_Cube;
	private final static int KEY_FIRE = KeyEvent.KEYCODE_HEADSETHOOK;
	private final static int KEY_LEFT = KeyEvent.KEYCODE_MEDIA_PREVIOUS;
	private final static int KEY_RIGHT = KeyEvent.KEYCODE_MEDIA_NEXT;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		surfaceView_Cube=new SurfaceView_Cube(this);
		setContentView(surfaceView_Cube);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KEY_RIGHT != keyCode && KEY_LEFT != keyCode && KEY_FIRE != keyCode) {
			return super.onKeyDown(keyCode, event);
		}
		return surfaceView_Cube.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (KEY_RIGHT != keyCode && KEY_LEFT != keyCode && KEY_FIRE != keyCode) {
			return super.onKeyDown(keyCode, event);
		}
		return surfaceView_Cube.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (KEY_RIGHT != keyCode && KEY_LEFT != keyCode && KEY_FIRE != keyCode) {
			return super.onKeyDown(keyCode, event);
		}
		return surfaceView_Cube.onKeyLongPress(keyCode, event);
	}
	
	@Override
	protected void onStop() {
		System.exit(0);
	}
}