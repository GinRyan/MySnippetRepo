package com.alarmclocksnoozers.runnershigh;

import android.util.Log;

public class BonusScoreEffect extends RHDrawable {
	
	public boolean doBonusScoreEffect;
	public boolean fadeIn;
	public float currentWidth;
	public float currentHeight;
	public float maxWidth;
	public float maxHeight;
	public float stepSizeFactor;
	public float effectX;
	public float effectY;
	
	public BonusScoreEffect(float _x, float _y, float _z, float _maxWidth, float _maxHeight) {
		super(_x, _y, _z, 0, 0);
		
		maxWidth=_maxWidth;
		maxHeight=_maxHeight;
		doBonusScoreEffect = false;
		fadeIn = true;
		currentWidth=currentHeight=0;
		stepSizeFactor=25;
	}
	
	public void updateBonusScoreEffect(float deltaLevelPosition){
		if(doBonusScoreEffect){
			if(fadeIn){
				currentWidth+=maxWidth/stepSizeFactor;
				currentHeight+=maxHeight/stepSizeFactor;
			}
			else{
				currentWidth-=maxWidth/stepSizeFactor/2;
				currentHeight-=maxHeight/stepSizeFactor/2;
			}
			
			if(currentWidth>=maxWidth || currentHeight>=maxHeight)
				fadeIn=false;
			if(currentWidth<=0 || currentHeight<=0){
				fadeIn=true;
				doBonusScoreEffect=false;
				effectX=effectY=-1000;
				if(currentWidth<0)
					currentWidth=0;
				if(currentHeight<0)
					currentHeight=0;
			}
			if (Settings.RHDEBUG) {
				Log.d("debug", "currentWidth: " + currentWidth);
				Log.d("debug", "currentHeight: " + currentHeight);
			}
			x=effectX-currentWidth/2-deltaLevelPosition;
			y=effectY-currentHeight/2;
			setWidth((int)currentWidth);
			setHeight((int)currentHeight);
		}
	}

	

}
