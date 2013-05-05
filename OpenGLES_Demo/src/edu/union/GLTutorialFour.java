package edu.union;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;

/**
 * http://www.zeuscmd.com/tutorials/opengles/10-Transformations.php
 * @author bburns
 */
public class GLTutorialFour extends GLTutorialBase {
	float[] triangle = new float[] { -0.25f, -0.25f, 0.0f,
									  0.25f, -0.25f, 0.0f,
									 -0.25f,  0.25f, 0.0f };
	
	float[] square = new float[] { 	-0.25f, -0.25f, 0.0f,
			0.25f, -0.25f, 0.0f,
			-0.25f, 0.25f, 0.0f,
			0.25f, 0.25f, 0.0f };
	
	float[] colors = new float[] { 	1, 0, 0, 1,
									0, 1, 0, 1,
									0, 0, 1, 1 };

	FloatBuffer squareBuff;
	FloatBuffer triangleBuff;
	FloatBuffer colorBuff;

	float xrot = 0.0f;
	float yrot = 0.0f;
	
	public GLTutorialFour(Context c) {
		super(c, 20);
	
		squareBuff = makeFloatBuffer(square);
		triangleBuff = makeFloatBuffer(triangle);
		colorBuff = makeFloatBuffer(colors);
	}
	
	protected void init(GL10 gl) {
		// Setup background color
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		// Smooth shading
		gl.glShadeModel(GL10.GL_SMOOTH);
	}
	
	public void onDrawFrame(GL10 gl) {
		xrot += 1f;
		yrot += 1f;
			
		// Clear screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// Scene view matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glTranslatef(-0.25f,0.0f,-2f);
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);

		// Setup triangle data and draw it.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleBuff);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuff);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		
		// Reset to identity
		gl.glPopMatrix();
		
		// Clear the color array
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		
		// Translation to square location.
		gl.glTranslatef(0.25f, 0.0f, -2f);
		gl.glRotatef(yrot, 0, 1, 0);

		// Setup square data & draw it
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, squareBuff);
		gl.glColor4f(0.25f, 0.25f, 0.75f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
}