package edu.union;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

/**
 * http://www.zeuscmd.com/tutorials/opengles/06-Rendering.php
 * @author bburns
 */
public class GLTutorialOne extends GLTutorialBase {
	public GLTutorialOne(Context c) {
		super(c);
	}
	
	protected void init(GL10 gl) {
		gl.glClearColor(0, 0, 0, 1);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	}
}
