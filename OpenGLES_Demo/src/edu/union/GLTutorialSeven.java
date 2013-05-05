package edu.union;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;

/**
 * http://www.zeuscmd.com/tutorials/opengles/15-Lighting.php
 * @author bburns
 */
public class GLTutorialSeven extends GLTutorialBase {
	float lightAmbient[] = new float[] { 0.2f, 0.3f, 0.6f, 1.0f };
	float lightDiffuse[] = new float[] { 0.2f, 0.3f, 0.6f, 1.0f };
	float[] lightPos = new float[] {0,0,3,1};
	
	float matAmbient[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	float matDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	
	float box[] = new float[] {
			// FRONT
			-0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			// BACK
			-0.5f, -0.5f, -0.5f,
			-0.5f,  0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			// LEFT
			-0.5f, -0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
			-0.5f, -0.5f, -0.5f,
			-0.5f,  0.5f, -0.5f,
			// RIGHT
			 0.5f, -0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			// TOP
			-0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			 -0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			// BOTTOM
			-0.5f, -0.5f,  0.5f,
			-0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f, -0.5f,
		};
	
	float norms[] = new float[] {
			// FRONT
			0f,  0f,  1f,
			0f,  0f,  1f,
			0f,  0f,  1f,
			0f,  0f,  1f,
			// BACK
			0f,  0f,  -1f,
			0f,  0f,  -1f,
			0f,  0f,  -1f,
			0f,  0f,  -1f,
			// LEFT
			-1f,  0f,  0f,
			-1f,  0f,  0f,
			-1f,  0f,  0f,
			-1f,  0f,  0f,
			// RIGHT
			1f, 0f, 0f,
			1f, 0f, 0f,
			1f, 0f, 0f,
			1f, 0f, 0f,
			// TOP
			0f,  1f, 0f,
			0f,  1f, 0f,
			0f,  1f, 0f,
			0f,  1f, 0f,
			// BOTTOM
			0f,  -1f, 0f,
			0f,  -1f, 0f,
			0f,  -1f, 0f,
			0f,  -1f, 0f
		};

	FloatBuffer cubeBuff;
	FloatBuffer normBuff;
		
	public GLTutorialSeven(Context c) {
		super(c, 20);
		
		cubeBuff = makeFloatBuffer(box);
		normBuff = makeFloatBuffer(norms);
	}

	protected void init(GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient,	0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse,	0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
	

		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepthf(1.0f);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normBuff);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
	}
	
	float xrot = 0.0f;
	float yrot = 0.0f;
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 3, 0, 0, 0, 0, 1, 0);
	
		gl.glRotatef(xrot, 1, 0, 0);
		gl.glRotatef(yrot, 0, 1, 0);
	
		gl.glColor4f(1.0f, 0, 0, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
	
		gl.glColor4f(0, 1.0f, 0, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
		
		gl.glColor4f(0, 0, 1.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
	
		xrot += 1.0f;
		yrot += 0.5f;
	}
}