package com.alarmclocksnoozers.runnershigh;


public class CounterDigit extends Mesh {
	protected float width;
	protected float height;
	protected float widthOfDigit;
	protected int digitValue;
	protected float textureCoordinates[] = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
	
	public CounterDigit(float _x, float _y, float _z, float _width, float _height) {
		x = _x;
		y = _y;
		z = _z;
		
		width= _width;
		height= _height;
		widthOfDigit = 1.0f/10.0f;
		
		digitValue=0;
		
		float textureCoordinates[] = { 0.0f, 1.0f, //
				widthOfDigit, 1.0f, //
				0.0f, 0.0f, //
				widthOfDigit, 0.0f, //
		};

		short[] indices = new short[] { 0, 1, 2, 1, 3, 2 };

		float[] vertices = new float[] { 0, 0, 0, width, 0, 0.0f, 0, height,
				0.0f, width, height, 0.0f };

		setIndices(indices);
		setVertices(vertices);
		setTextureCoordinates(textureCoordinates);
	}
	
	public void setWidth(int width)
	{
		this.width = width;
		
		float[] vertices = new float[] { 0, 0, 0, width, 0, 0.0f, 0, height,
				0.0f, width, height, 0.0f };

		setVertices(vertices);
	}
	
	public void setHeight(int height)
	{
		this.height = height;
		float[] vertices = new float[] { 0, 0, 0, width, 0, 0.0f, 0, height,
				0.0f, width, height, 0.0f };

		setVertices(vertices);
	}
	public void incrementDigit() {
		digitValue++;
		if(digitValue==10)
			digitValue=0;
		
		float textureCoordinates[] = {widthOfDigit*digitValue, 1.0f, //
				widthOfDigit*digitValue+widthOfDigit, 1.0f, //
				widthOfDigit*digitValue, 0.0f, //
				widthOfDigit*digitValue+widthOfDigit, 0.0f, //
		};
		setTextureCoordinates(textureCoordinates);
	}
	public void setDigitToZero() {
		digitValue=0;
		float textureCoordinates[] = { 0.0f, 1.0f, //
				widthOfDigit, 1.0f, //
				0.0f, 0.0f, //
				widthOfDigit, 0.0f, //
		};
		setTextureCoordinates(textureCoordinates);
	}
	public void setDigitTo(int value) {
		digitValue=value;
		
		textureCoordinates[0] = widthOfDigit*digitValue;
		textureCoordinates[1] = 1.0f;
		textureCoordinates[2] =widthOfDigit*digitValue+widthOfDigit;
		textureCoordinates[3] = 1.0f;
		textureCoordinates[4] = widthOfDigit*digitValue;
		textureCoordinates[5] = 0.0f;
		textureCoordinates[6] = widthOfDigit*digitValue+widthOfDigit;
		textureCoordinates[7] = 0.0f;
		
		setTextureCoordinates(textureCoordinates);
		
	}
}
