/**
 * Copyright 2010 Per-Erik Bergman (per-erik.bergman@jayway.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alarmclocksnoozers.runnershigh;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class OpenGLRenderer implements Renderer {
	private final Group root;
	private long timeAtLastSecond = 0;
	private long currentTimeTaken=0;
	private long starttime = 0;
	private int fpsCounter;
	public int fps = 0;
	
	public boolean firstFrameDone = false;

	public OpenGLRenderer() {
		// Initialize our root.
		Group group = new Group();
		root = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition
	 * .khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

		//besser für performance?
		gl.glDisable(GL10.GL_DITHER);

		
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		//fuer transperancy?
		//gl.glEnable(GL10.GL_ALPHA);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA); 
        
        timeAtLastSecond = System.currentTimeMillis();
        fpsCounter=0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.
	 * khronos.opengles.GL10)
	 */

	public void onDrawFrame(GL10 gl) {
		if(Settings.RHDEBUG){
			starttime = System.currentTimeMillis();
			currentTimeTaken= System.currentTimeMillis()- starttime;
			Log.d("frametime", "time at beginning: " + Integer.toString((int)currentTimeTaken));
		}
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		
		if(Settings.RHDEBUG){
			currentTimeTaken= System.currentTimeMillis()- starttime;
			Log.d("frametime", "time after clear and loadident: " + Integer.toString((int)currentTimeTaken));
		}
		
		// Draw our scene.
		synchronized (root) {
			root.draw(gl);
		}
		fpsCounter++;
		
		
		if(Settings.RHDEBUG){
			currentTimeTaken= System.currentTimeMillis()- starttime;
			Log.d("frametime", "time after draw: " + Integer.toString((int)currentTimeTaken));
		}
		
		if((System.currentTimeMillis() - timeAtLastSecond) > 1000){
			timeAtLastSecond = System.currentTimeMillis();
			fps = fpsCounter;
			fpsCounter=0;
			if(Settings.RHDEBUG) {
				Log.d("framerate", "draws per second: " + Integer.toString(fpsCounter));
			}
		}
		
		firstFrameDone = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition
	 * .khronos.opengles.GL10, int, int)
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.

		// gl.glViewport(0, 0, width, height);
		// GLU.gluOrtho2D(gl, 0, width, 0, height);
		if(Settings.RHDEBUG)
			Log.d("frametime", "onSurfaceChanged called");
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		//GLU.gluOrtho2D(gl, 0, 1, 0, 2);
		GLU.gluOrtho2D(gl, 0, width, 0, height);
		//gl.glOrthox(0, width, 0, height, -5, 5);
		// gl.glOrthox(0, width, 0, height, -100, 100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// Select the projection matrix
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		// gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		// GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
		// 1000.0f);
		// Select the modelview matrix
		// gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		// gl.glLoadIdentity();

	}

	/**
	 * Adds a mesh to the root.
	 * 
	 * @param mesh
	 *            the mesh to add.
	 */
	public void addMesh(Mesh mesh) {
		synchronized (root) {
			root.add(mesh);	
		}
	}
	
	public void removeMesh(Mesh mesh) {
		synchronized (root) {
			root.remove(mesh);
		}
	}
}
