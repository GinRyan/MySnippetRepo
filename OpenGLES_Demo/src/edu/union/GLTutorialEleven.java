package edu.union;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.view.KeyEvent;

/**
 * Approximately corresponds to: http://www.zeuscmd.com/tutorials/opengles/25-Reflections.php
 * @author bburns
 *
 */
public class GLTutorialEleven extends GLTutorialBase {
	float y = 1.5f;
	
	float lightAmbient[] = new float[] { 0.2f, 0.3f, 0.6f, 1.0f };
	float lightDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	float[] lightPos = new float[] {0,0,3,1};
	
	float matAmbient[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	float matDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	
	float white[] = new float[] { 1f, 1f, 1f, 1.0f};
	float trans[] = new float[] { 1f, 1f, 1f, 0.3f};
	
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

	float[] floorVertices = new float[] {
				-3.0f, 0.0f, 3.0f,
				 3.0f, 0.0f, 3.0f,
				-3.0f, 0.0f,-3.0f,
				 3.0f, 0.0f,-3.0f
			};

	
	FloatBuffer cubeBuff;
	FloatBuffer floorBuff;
	
	public GLTutorialEleven(Context c) {
		super(c, 20);

		cubeBuff = makeFloatBuffer(box);
		floorBuff = makeFloatBuffer(floorVertices);
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
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepthf(1.0f);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		gl.glClearStencil(0);
	}
	
	float xrot = 0.0f;
	float yrot = 0.0f;
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);
			
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 3, 7, 0, 0, 0, 0, 1, 0);
		
		// Draw the floor into the stencil buffer.
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glColorMask(false, false, false, false);
		gl.glDepthMask(false);
		
		gl.glEnable(GL10.GL_STENCIL_TEST);
		gl.glStencilOp(GL10.GL_REPLACE, GL10.GL_REPLACE, GL10.GL_REPLACE);
		gl.glStencilFunc(GL10.GL_ALWAYS, 1, 0xffffffff);
	
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floorBuff);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, white, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, white, 0);
		gl.glNormal3f(0,1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		// Draw the reflection
		gl.glColorMask(true, true, true, true);
		gl.glDepthMask(true);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glStencilFunc(GL10.GL_EQUAL, 1, 0xffffffff);
		gl.glStencilOp(GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_KEEP);
		
		gl.glPushMatrix();
		gl.glScalef(1.0f, -1f, 1f);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
		gl.glCullFace(GL10.GL_FRONT);
		
		setupCube(gl);
		
		gl.glPushMatrix();
		gl.glTranslatef(0.0f,y,-1.0f);
		gl.glRotatef(xrot, 1, 0, 0);
		gl.glRotatef(yrot, 0, 1, 0);
		drawCube(gl);
		gl.glPopMatrix();
		
		gl.glCullFace(GL10.GL_BACK);
		gl.glPopMatrix();
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
		
		gl.glDisable(GL10.GL_STENCIL_TEST);
		
		// Draw the floor for real.
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floorBuff);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, trans, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, trans, 0);
		gl.glNormal3f(0,1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glDisable(GL10.GL_BLEND);
		
		setupCube(gl);
		gl.glPushMatrix();
		gl.glTranslatef(0.0f,y,-1.0f);
		gl.glRotatef(xrot, 1, 0, 0);
		gl.glRotatef(yrot, 0, 1, 0);
		// Draw the non-reflected cube
		drawCube(gl);
		gl.glPopMatrix();
		xrot+=1.0f;
		yrot+=0.5f;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
			y+=0.1;
			invalidate();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (y > 0.5)
				y-=0.1;
			invalidate();
		}	
		return super.onKeyDown(keyCode, event);
	}
}