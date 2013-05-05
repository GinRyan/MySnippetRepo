package com.alarmclocksnoozers.runnershigh;

import android.graphics.Bitmap;
import android.util.Log;

public class HighscoreMark extends Group
{
	private OpenGLRenderer mRenderer;

	private CounterDigit mHighscoreDigit1;
	private CounterDigit mHighscoreDigit2;
	private CounterDigit mHighscoreDigit3;
	private CounterDigit mHighscoreDigit4;

	private CounterDigit mHighscoreId;

	private CounterGroup mHighscoreGroup;
	
	private RHDrawable mHighscoreMark;
	
	public HighscoreMark(OpenGLRenderer glrenderer, Bitmap highscoreMarkBitmap, Bitmap counterFont)
	{
		mRenderer = glrenderer;
		

		mHighscoreMark = new RHDrawable(0, 0, 1, 64, 512);
		mHighscoreMark.loadBitmap(highscoreMarkBitmap);
		this.add(mHighscoreMark);
		
		mHighscoreGroup = new CounterGroup(0, 0, 1, 128*4, 20, 25);
		
		mHighscoreDigit1 = new CounterDigit(0, 0, 1, 16, 20);
		mHighscoreDigit1.loadBitmap(counterFont);
		mHighscoreGroup.add(mHighscoreDigit1);
		
		mHighscoreDigit2 = new CounterDigit(15, 0, 1, 16, 20);
		mHighscoreDigit2.loadBitmap(counterFont);
		mHighscoreGroup.add(mHighscoreDigit2);
		
		mHighscoreDigit3 = new CounterDigit(30, 0, 1, 16, 20);
		mHighscoreDigit3.loadBitmap(counterFont);
		mHighscoreGroup.add(mHighscoreDigit3);
		
		mHighscoreDigit4 = new CounterDigit(45, 0, 1, 16, 20);
		mHighscoreDigit4.loadBitmap(counterFont);
		mHighscoreGroup.add(mHighscoreDigit4);
		
		this.add(mHighscoreGroup);
		

		mHighscoreId = new CounterDigit(5, 16, 1, 16, 20);
		mHighscoreId.loadBitmap(counterFont);
		this.add(mHighscoreId);
		
		mRenderer.addMesh(this);
	}
	
	public void setMarkTo(int id, int score)
	{
		mHighscoreId.setDigitTo(id);
		mHighscoreGroup.tryToSetCounterTo(score);

		if(Settings.RHDEBUG)
			Log.d("debug", "setting mark " + id + " to score " + score);
		
	}
}
