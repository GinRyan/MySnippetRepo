package com.alarmclocksnoozers.runnershigh;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Block extends Group {
	
	public float mWidth;
	public float mHeight;
	
	public Rect BlockRect;
	
	private RHDrawable mLeft;
	private RHDrawable mMiddle;
	private RHDrawable mRight;

	private static Bitmap mTextureLeft = null;
	private static Bitmap mTextureMiddle = null;
	private static Bitmap mTextureRight = null;
	
	private static int mTextureWidthLeft = 0;
	private static int mTextureWidthMiddle = 0;
	private static int mTextureWidthRight = 0;

	private static int mTextureHeightLeft = 0;
	private static int mTextureHeightMiddle = 0;
	private static int mTextureHeightRight = 0;
	
	
	final private static float mTextureCoordinates[] = { 0.0f, 1.0f, //
		1.0f, 1.0f, //
		0.0f, 0.0f, //
		1.0f, 0.0f, //
	};
	
	private float mTextureCoordinatesMiddle[] = { 0, 1.0f, //
		1.0f, 1.0f, //
		0, 0.0f, //
		1.0f, 0.0f, //
	};
	
	final private static short[] mIndices = new short[] { 0, 1, 2, 1, 3, 2 };
	
	private float[] mVerticesLeft = new float[] { 0, 0, 0, 1, 0, 0, 0, 1,
		0, 1, 1, 0.0f };
	private float[] mVerticesMiddle = new float[] { 0, 0, 0, 1, 0, 0, 0, 1,
		0, 1, 1, 0.0f };
	private float[] mVerticesRight = new float[] { 0, 0, 0, 1, 0, 0, 0, 1,
		0, 1, 1, 0.0f };

	
	public Block() {

		mLeft = new RHDrawable(0, 0, 0, 10, 10);
		mMiddle = new RHDrawable(0, 0, 0, 10, 10);
		mRight = new RHDrawable(0, 0, 0, 10, 10);
		
		mLeft.loadBitmap(mTextureLeft, GL10.GL_REPEAT, GL10.GL_CLAMP_TO_EDGE);
		mMiddle.loadBitmap(mTextureMiddle, GL10.GL_REPEAT, GL10.GL_CLAMP_TO_EDGE);
		mRight.loadBitmap(mTextureRight, GL10.GL_REPEAT, GL10.GL_CLAMP_TO_EDGE);
		
		mLeft.setIndices(mIndices);
		mMiddle.setIndices(mIndices);
		mRight.setIndices(mIndices);
		
		mLeft.setVertices(mVerticesLeft);
		mMiddle.setVertices(mVerticesMiddle);
		mRight.setVertices(mVerticesRight);
		
		mLeft.setTextureCoordinates(mTextureCoordinates);
		mMiddle.setTextureCoordinates(mTextureCoordinates);
		mRight.setTextureCoordinates(mTextureCoordinates);
		
		add(mLeft);
		add(mMiddle);
		add(mRight);
		
		BlockRect = new Rect((int)x, (int)(y+mHeight), (int)(x+mWidth), (int)y );
	}
	
	public static void cleanup() {
		if (mTextureLeft != null) mTextureLeft.recycle();
		if (mTextureMiddle != null) mTextureMiddle.recycle();
		if (mTextureRight != null) mTextureRight.recycle();
	}
	
	public void updateRect()
	{
		BlockRect.left =(int)x;
		BlockRect.top = (int)(y+mHeight);
		BlockRect.right = (int)(x+mWidth);
		BlockRect.bottom = (int)y;
	}
	
	public void setWidth(float width)
	{
		mWidth = width;
		
		mVerticesLeft[3] = mTextureWidthLeft;
		mVerticesLeft[9] = mTextureWidthLeft;

		mVerticesMiddle[0] = mTextureWidthLeft;
		mVerticesMiddle[3] = mWidth-mTextureWidthRight;
		mVerticesMiddle[6] = mTextureWidthLeft;
		mVerticesMiddle[9] = mWidth-mTextureWidthRight;

		mVerticesRight[0] = mWidth-mTextureWidthRight;
		mVerticesRight[3] = mWidth;
		mVerticesRight[6] = mWidth-mTextureWidthRight;
		mVerticesRight[9] = mWidth;
		
		mTextureCoordinatesMiddle[2] = (width - mTextureWidthLeft - mTextureWidthRight) / mTextureWidthMiddle;
		mTextureCoordinatesMiddle[6] = mTextureCoordinatesMiddle[2];
		
		mLeft.setVertices(mVerticesLeft);
		mMiddle.setVertices(mVerticesMiddle);
		mRight.setVertices(mVerticesRight);
		
		mMiddle.setTextureCoordinates(mTextureCoordinatesMiddle);
	}
	
	public void setHeight(float height)
	{
		mHeight = height;

		mVerticesLeft[1] = mHeight-mTextureHeightLeft;
		mVerticesLeft[4] = mHeight-mTextureHeightLeft;
		mVerticesLeft[7] = mHeight;
		mVerticesLeft[10] = mHeight;

		mVerticesMiddle[1] = mHeight-mTextureHeightMiddle;
		mVerticesMiddle[4] = mHeight-mTextureHeightMiddle;
		mVerticesMiddle[7] = mHeight;
		mVerticesMiddle[10] = mHeight;

		mVerticesRight[1] = mHeight-mTextureHeightRight;
		mVerticesRight[4] = mHeight-mTextureHeightRight;
		mVerticesRight[7] = mHeight;
		mVerticesRight[10] = mHeight;

		mLeft.setVertices(mVerticesLeft);
		mMiddle.setVertices(mVerticesMiddle);
		mRight.setVertices(mVerticesRight);
	}

	public static void setTextureLeft(Bitmap texture)
	{
		mTextureLeft = texture;
		mTextureWidthLeft = mTextureLeft.getWidth();
		mTextureHeightLeft = mTextureLeft.getHeight();
	}
	public static void setTextureMiddle(Bitmap texture)
	{
		mTextureMiddle = texture;
		mTextureWidthMiddle = mTextureMiddle.getWidth();
		mTextureHeightMiddle = mTextureMiddle.getHeight();
	}
	public static void setTextureRight(Bitmap texture)
	{
		mTextureRight = texture;
		mTextureWidthRight = mTextureRight.getWidth();
		mTextureHeightRight = mTextureRight.getHeight();
	}
	
	public static int getTextureLeftWidth()
	{
		assert(mTextureWidthLeft != 0);
		return mTextureWidthLeft;
	}
	public static int getTextureMiddleWidth()
	{
		assert(mTextureWidthMiddle != 0);
		return mTextureWidthMiddle;
	}
	public static int getTextureRightWidth()
	{
		assert(mTextureWidthRight != 0);
		return mTextureWidthRight;
	}
	
	
	
}


