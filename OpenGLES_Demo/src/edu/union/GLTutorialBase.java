package edu.union;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;


public abstract class GLTutorialBase extends GLSurfaceView implements GLSurfaceView.Renderer {
	protected Context context;
	int width;
	int height;
	int fps;
	
	float box[] = new float[] {
			// FRONT
			 0.5f, -0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			-0.5f, -0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
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
	
	float texCoords[] = new float[] {
			// FRONT
			 0.0f, 1.0f,
			 0.0f, 0.0f,
			 1.0f, 1.0f,
			 1.0f, 0.0f,
			// BACK
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
			// LEFT
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
			// RIGHT
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
			// TOP
			 0.0f, 0.0f,
			 1.0f, 0.0f,
			 0.0f, 1.0f,
			 1.0f, 1.0f,
			// BOTTOM
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f
		};

	FloatBuffer cubeBuff;
	FloatBuffer texBuff;
	
	/**
	 * Make a direct NIO FloatBuffer from an array of floats
	 * @param arr The array
	 * @return The newly created FloatBuffer
	 */
	protected static FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		makeFloatBuffer(fb, arr);
		return fb;
	}

	protected static void makeFloatBuffer(FloatBuffer fb, float[] arr) {
		fb.put(arr);
		fb.position(0);
	}

	/**
	 * Make a direct NIO IntBuffer from an array of ints
	 * @param arr The array
	 * @return The newly created IntBuffer
	 */
	protected static IntBuffer makeFloatBuffer(int[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		IntBuffer ib = bb.asIntBuffer();
		ib.put(arr);
		ib.position(0);
		return ib;
	}
	
	protected static ByteBuffer makeByteBuffer(Bitmap bmp) {
		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight()*bmp.getWidth()*4);
		bb.order(ByteOrder.BIG_ENDIAN);
		IntBuffer ib = bb.asIntBuffer();

		for (int y = 0; y < bmp.getHeight(); y++)
			for (int x=0;x<bmp.getWidth();x++) {
				int pix = bmp.getPixel(x, bmp.getHeight()-y-1);
				// Convert ARGB -> RGBA
				byte alpha = (byte)((pix >> 24)&0xFF);
				byte red = (byte)((pix >> 16)&0xFF);
				byte green = (byte)((pix >> 8)&0xFF);
				byte blue = (byte)((pix)&0xFF);
								
				ib.put(((red&0xFF) << 24) | 
					   ((green&0xFF) << 16) |
					   ((blue&0xFF) << 8) |
					   ((alpha&0xFF)));
			}
		ib.position(0);
		bb.position(0);
		return bb;
	}
	
	protected int loadTexture(GL10 gl, int resource) {
		return loadTexture(gl, BitmapFactory.decodeResource(context.getResources(), resource));
	}
	
	/**
	 * Create a texture and send it to the graphics system
	 * @param gl The GL object
	 * @param bmp The bitmap of the texture
	 * @param reverseRGB Should the RGB values be reversed?  (necessary workaround for loading .pngs...)
	 * @return The newly created identifier for the texture.
	 */
	protected static int loadTexture(GL10 gl, Bitmap bmp) {
		int[] tmp_tex = new int[1];

		gl.glGenTextures(1, tmp_tex, 0);
		int tex = tmp_tex[0];
		loadTexture(tex, GL10.GL_TEXTURE_2D, bmp, gl);
		return tex;
	}
	
	public void loadTexture(int texture, int type,  int resource, GL10 gl) {
		loadTexture(texture, type, BitmapFactory.decodeResource(context.getResources(), resource), gl);
	}
	
	static public void loadTexture(int texture, int type, Bitmap bmp, GL10 gl) {
		loadTexture(texture, type, bmp.getWidth(), bmp.getHeight(), makeByteBuffer(bmp), gl);
	}
	
	static public void loadTexture(int texture, int type, int width, int height, ByteBuffer bb, GL10 gl) {
		gl.glBindTexture(type, texture);
		gl.glTexImage2D(type, 0, GL10.GL_RGBA, width, height, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, null);
		gl.glTexSubImage2D(type, 0, 0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
		gl.glTexParameterf(type, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(type, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	}

	/**
	 * Constructor
	 */
	public GLTutorialBase(Context context) {
		this(context, -1);
	}

	/**
	 * Constructor for animated views
	 * @param c The View's context
	 * @param fps The frames per second for the animation.
	 */
	public GLTutorialBase(Context context, int fps) {
		super(context);
		this.context = context;
		this.fps = fps;
		
		cubeBuff = makeFloatBuffer(box);
		texBuff = makeFloatBuffer(texCoords);
	}

	public void setupCube(GL10 gl) {
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void drawCube(GL10 gl) {
		gl.glColor4f(1.0f, 1, 1, 1.0f);
		gl.glNormal3f(0,0,1);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glNormal3f(0,0,-1);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
	
		gl.glColor4f(1, 1.0f, 1, 1.0f);
		gl.glNormal3f(-1,0,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
		gl.glNormal3f(1,0,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
		
		gl.glColor4f(1, 1, 1.0f, 1.0f);
		gl.glNormal3f(0,1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
		gl.glNormal3f(0,-1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
	}
	
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {    
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
    	this.width = w;
    	this.height = h;
    	gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0,0,w,h);
		GLU.gluPerspective(gl, 45.0f, ((float)w)/h, 1f, 100f);
		init(gl);
	}
	
	protected void init(GL10 gl) {}	
}