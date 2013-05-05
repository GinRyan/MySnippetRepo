package edu.union;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;

/**
 * http://www.zeuscmd.com/tutorials/opengles/11-Depth.php
 * @author bburns
 */
public class GLTutorialFive extends GLTutorialBase {
	
	float[] triangle = new float[] { -0.25f, -0.25f, 0.0f,
									 0.25f, -0.25f, 0.0f,
									 -0.25f, 0.25f, 0.0f };
	
	float[] colors = new float[] { 	1, 0, 0, 1,
									0, 1, 0, 1,
									0, 0, 1, 1 };

	FloatBuffer triangleBuff;
	FloatBuffer colorBuff;
	
	public GLTutorialFive(Context c) {
		super(c);
		triangleBuff = makeFloatBuffer(triangle);
		colorBuff = makeFloatBuffer(colors);
	}
	

	protected void init(GL10 gl) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glClearDepthf(1.0f);
	
		gl.glShadeModel(GL10.GL_SMOOTH);
	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0,0,-1);
		gl.glPushMatrix();
	
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleBuff);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuff);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(-0.2f,-.1f,-1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0.1f,0.1f,-0.5f);
		gl.glRotatef(45.0f, 0, 0, 1);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		gl.glPopMatrix();
		
		gl.glTranslatef(0.15f,0.05f,-0.4f);
		gl.glRotatef(45,0,1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		gl.glPopMatrix();
	}
}