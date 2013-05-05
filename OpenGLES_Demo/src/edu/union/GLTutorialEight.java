package edu.union;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLU;

/**
 * http://www.zeuscmd.com/tutorials/opengles/17-TextureMapping.php
 * @author bburns
 *
 */
public class GLTutorialEight extends GLTutorialBase {
	float lightAmbient[] = new float[] { 0.2f, 0.2f, 0.2f, 1.0f };
	float lightDiffuse[] = new float[] { 1f, 1f, 1f, 1.0f };
	float[] lightPos = new float[] {0,0,3,1};
	
	float matAmbient[] = new float[] { 1f, 1f, 1f, 1.0f };
	float matDiffuse[] = new float[] { 1f, 1f, 1f, 1.0f };
	
	int tex;
	Bitmap bmp;
	
	public GLTutorialEight(Context c) {
		super(c, 20);
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
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepthf(1.0f);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		tex = loadTexture(gl, R.drawable.icon);
	}
	
	float xrot = 0.0f;
	float yrot = 0.0f;
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			
		setupCube(gl);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 3, 0, 0, 0, 0, 1, 0);
	
		gl.glRotatef(xrot, 1, 0, 0);
		gl.glRotatef(yrot, 0, 1, 0);
	
		drawCube(gl);
	
		xrot += 1.0f;
		yrot += 0.5f;
	}
}