package edu.union;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;

/**
 * http://www.zeuscmd.com/tutorials/opengles/08-ColorAndShading.php
 * @author bburns
 */
public class GLTutorialThree extends GLTutorialBase {	
	// Vertices (x,y,z) for a 2D triangle
	float[] triangle = new float[] { -0.25f, -0.25f, 0.0f,
									 0.25f, -0.25f, 0.0f,
									 -0.25f, 0.25f, 0.0f };
	
	// Colors (r,g,b,a) for each vertex
	float[] colors = new float[] { 	1, 0, 0, 1,
									0, 1, 0, 1,
									0, 0, 1, 1 };
	
	// NIO Buffers for each.
	FloatBuffer triangleBuff;
	FloatBuffer colorBuff;
	
	public GLTutorialThree(Context c) {
		super(c);
	
		triangleBuff = makeFloatBuffer(triangle);
		colorBuff = makeFloatBuffer(colors);
		
	}
	
	protected void init(GL10 gl) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL10.GL_SMOOTH);
	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0,0,-1);
		
		// Send the vertices to the renderer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleBuff);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
		// Send the colors to the renderer
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuff);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
	}
}