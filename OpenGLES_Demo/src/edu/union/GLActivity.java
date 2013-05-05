package edu.union;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

public class GLActivity extends Activity {
	public static final int FIRST = 0;
	public static final int SECOND = 1;
	public static final int THIRD = 2;
	public static final int FOURTH = 3;
	public static final int FIFTH = 4;
	public static final int SIXTH = 5;
	public static final int SEVENTH = 6;
	public static final int EIGHTH = 7;
	public static final int NINTH = 8;
	public static final int TENTH = 9;
	public static final int ELEVENTH = 10;
	public static final int TWELFTH = 11;
	public static final int THIRTEENTH = 12;
	public static final int FOURTEENTH = 13;
	
	GLTutorialBase v = null;
	
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		int type = getIntent().getExtras().getInt(AndroidGL.GL_DRAW);
		
		switch (type) {
		case FIRST:
			v = new GLTutorialOne(this);
			break;
		case SECOND:
			v = new GLTutorialTwo(this);
			break;
		case THIRD:
			v = new GLTutorialThree(this);
			break;
		case FOURTH:
			v = new GLTutorialFour(this);
			break;
		case FIFTH:
			v = new GLTutorialFive(this);
			break;
		case SIXTH:
			v = new GLTutorialSix(this);
			break;
		case SEVENTH:
			v = new GLTutorialSeven(this);
			break;
		case EIGHTH:
			v = new GLTutorialEight(this);
			break;
		case NINTH:
			v = new GLTutorialNine(this);
			break;
		case TENTH:
			v = new GLTutorialTen(this);
			break;
		case ELEVENTH:
			v = new GLTutorialEleven(this);
			break;
		case TWELFTH:
			v = new GLTutorialTwelve(this);
			break;
		case THIRTEENTH:
			v = new GLTutorialThirteen(this);
			break;
		case FOURTEENTH:
			v = new GLTutorialFourteen(this);
			break;
		}
		v.setRenderer(v);
        v.setFocusable(true);
        setContentView(v);
    }

    @Override
    protected void onPause() {
        super.onPause();
        v.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        v.onResume();
    }
}

