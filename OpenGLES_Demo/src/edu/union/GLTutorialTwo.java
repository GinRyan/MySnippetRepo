package edu.union;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;

/**
 * http://www.zeuscmd.com/tutorials/opengles/07-OrthographicProjection.php
 * @author bburns
 */
public class GLTutorialTwo extends GLTutorialBase { 
	// Coordinates for a 2D square
	float[] square = new float[] { 	-0.25f, -0.25f, 0.0f,
									0.25f, -0.25f, 0.0f,
									-0.25f, 0.25f, 0.0f,
									0.25f, 0.25f, 0.0f };
	// NIO Buffer for the square
	FloatBuffer squareBuff;
	
	public GLTutorialTwo(Context c) {
		super(c);
		squareBuff = makeFloatBuffer(square);
	}

	protected void init(GL10 gl) {		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0,0,-1);
		
		gl.glColor4f(1, 0, 0, 0.5f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, squareBuff);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
}