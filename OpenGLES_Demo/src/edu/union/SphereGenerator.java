package edu.union;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class SphereGenerator {
	FloatBuffer strip, fan_top, fan_bottom;
	FloatBuffer tex_strip, tex_fan_top, tex_fan_bottom;
	float radius;
	int stacks,  slices;
	int tex;
	
	public SphereGenerator(int tex, int stacks, int slices, float radius) {
		this.tex = tex;
		this.stacks = stacks;
		this.slices = slices;
		this.radius = radius;
		unitSphere(stacks, slices);
	}
	
	public void draw(GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fan_top);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
		gl.glNormalPointer(GL10.GL_FLOAT, 0, fan_top);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tex_fan_top);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, slices + 2);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, strip);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
		gl.glNormalPointer(GL10.GL_FLOAT, 0, strip);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, (slices + 1) * 2 * stacks);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fan_bottom);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
		gl.glNormalPointer(GL10.GL_FLOAT, 0, fan_bottom);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tex_fan_bottom);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, slices + 2);
	}
	
	protected FloatBuffer[] makeEndCap(int stacks, int slices, boolean top) {
		// Calculate the Triangle Fan for the endcaps
		int triangleFanVertexCount = slices + 2;
		float dtheta = (float)(2.0 * Math.PI / slices);
		float drho =  (float)(Math.PI / stacks);
		float[] fanVertices = new float[triangleFanVertexCount * 3];
		float[] fanTextures = new float[triangleFanVertexCount * 2];
		float theta = 0;
		float sin_drho = (float)Math.sin(drho);
		//float cos_drho = (float)Math.cos(Math.PI / stacks);
		int tex_index = 0;
		fanTextures[tex_index++] = (top ? 0 : 1.0f);
		fanTextures[tex_index++] = 0.5f;
		
		int index = 0;
		fanVertices[index++] = 0.0f;
		fanVertices[index++] = 0.0f; 
		fanVertices[index++] = (top ? 1 : -1);
		
		
		for (int j = 0; j <= slices; j++) 
		{
			theta = (j == slices) ? 0.0f : j * (top ? 1 : -1) * dtheta;
			float x = (float)-Math.sin(theta) * sin_drho;
			float y = (float)Math.cos(theta) * sin_drho;
			float z = (top ? 1 : -1) * (float)Math.cos(drho);
			
			fanTextures[tex_index++] = x;
			fanTextures[tex_index++] = y;
			
			fanVertices[index++] = x;
			fanVertices[index++] = y;
			fanVertices[index++] = z;
			
		}

		FloatBuffer[] result = new FloatBuffer[2];
		result[0] = GLTutorialBase.makeFloatBuffer(fanVertices);
		result[1] = GLTutorialBase.makeFloatBuffer(fanTextures);
		return result;
	}
	
	protected void unitSphere(int stacks, int slices) {
		float drho =  (float)(Math.PI / stacks);
		float dtheta = (float)(2.0 * Math.PI / slices);

		FloatBuffer[] buffs = makeEndCap(stacks, slices, true);
		fan_top = buffs[0];
		tex_fan_top = buffs[1];
		buffs = makeEndCap(stacks, slices, false);
		fan_bottom = buffs[0];
		tex_fan_bottom = buffs[1];
		
		// Calculate the triangle strip for the sphere body
		int triangleStripVertexCount = (slices + 1) * 2 * stacks;
		float[] stripVertices = new float[triangleStripVertexCount * 3];
		
		int index = 0;
		for (int i = 0; i < stacks; i++) {
			float rho = i * drho;
			
			for (int j = 0; j <= slices; j++) 
			{
				float theta = (j == slices) ? 0.0f : j * dtheta;
				float x = (float)(-Math.sin(theta) * Math.sin(rho));
				float y = (float)(Math.cos(theta) * Math.sin(rho));
				float z = (float)Math.cos(rho);
				// TODO: Implement texture mapping if texture used
				//                TXTR_COORD(s, t);
				stripVertices[index++] = x;
				stripVertices[index++] = y;
				stripVertices[index++] = z;
				
				x = (float)(-Math.sin(theta) * Math.sin(rho + drho));
				y = (float)(Math.cos(theta) * Math.sin(rho + drho));
				z = (float)Math.cos(rho + drho);
				// TODO: Implement texture mapping if texture used
				//                TXTR_COORD(s, t);
				stripVertices[index++] = x;
				stripVertices[index++] = y;
				stripVertices[index++] = z;
			}
		}
		strip = GLTutorialBase.makeFloatBuffer(stripVertices);
	}
}
