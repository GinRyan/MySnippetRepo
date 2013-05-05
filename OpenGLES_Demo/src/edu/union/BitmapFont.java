package edu.union;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

public class BitmapFont {
	int font_tex;

	FloatBuffer fontTex;
	FloatBuffer face;
	
	public BitmapFont(int texture) {
		this.font_tex = texture;

		float width = 1f/16f;
		float height = width;

		float[] fontTexCoords = new float[16*16*8];
		int ix = 0;
		for (int row = 1; row <= 16; ++row) {
			for(int col = 1; col <= 16; ++col) {				
				fontTexCoords[ix++] = col*width;
				fontTexCoords[ix++] = 1-row*height + 0.01f;

				fontTexCoords[ix++] = col*width;
				fontTexCoords[ix++] = 1-(row-1)*height - 0.01f;

				fontTexCoords[ix++] = (col-1)*width;
				fontTexCoords[ix++] = 1-row*height + 0.01f;

				fontTexCoords[ix++] = (col-1)*width;
				fontTexCoords[ix++] = 1-(row-1)*height - 0.01f;
			}
		}
		fontTex = GLTutorialBase.makeFloatBuffer(fontTexCoords);
		
		float faceVerts[] = new float[] {
				0.5f, -0.5f,  0.5f,
				0.5f,  0.5f,  0.5f,
				-0.5f, -0.5f,  0.5f,
				-0.5f,  0.5f,  0.5f,
		};
		face = GLTutorialBase.makeFloatBuffer(faceVerts);
	}

	public void draw(GL10 gl, String str) {
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, face);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glEnable(GL10.GL_TEXTURE_2D); 
		gl.glBindTexture(GL10.GL_TEXTURE_2D, font_tex); 
		
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		char[] chars = str.toCharArray();

		gl.glTranslatef(-chars.length*0.5f, 0, 0);

		for (int i = 0; i < chars.length; ++i) {
			fontTex.position(chars[i]*8);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fontTex);

			gl.glColor4f(1.0f, 1, 1, 1.0f);
			gl.glNormal3f(0,0,1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glTranslatef(1f, 0, 0);
		}
	}
}
