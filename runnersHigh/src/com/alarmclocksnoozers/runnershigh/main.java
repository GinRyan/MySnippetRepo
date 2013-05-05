package com.alarmclocksnoozers.runnershigh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import com.alarmclocksnoozers.highscore.HighscoreAdapter;

public class main extends Activity {
		PowerManager.WakeLock wakeLock ;
		
		private static long lastCreationTime = 0;
		private static final int MIN_CREATION_TIMEOUT = 10000;
		
		
		//MediaPlayer musicPlayerIntro;
		MediaPlayer musicPlayerLoop;
		boolean MusicLoopStartedForFirstTime = false;

		boolean isRunning = false;
		RunnersHighView mGameView = null;

	    private static final int SLEEP_TIME = 300;
	    
		/** Called when the activity is first created. */
	    @Override
		public void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	
	    	
	    	//setContentView(R.layout.main);	 
	    	
	    	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "tag");
			wakeLock.acquire();	    	
			
			SoundManager.getInstance();
	        SoundManager.initSounds(this);
	        SoundManager.loadSounds();
	        
	        musicPlayerLoop = MediaPlayer.create(getApplicationContext(), R.raw.gamebackground);
	        
	        musicPlayerLoop.setLooping(true);
			musicPlayerLoop.seekTo(0);
			musicPlayerLoop.setVolume(0.5f, 0.5f);
			
			
			requestWindowFeature(Window.FEATURE_NO_TITLE);  
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
			isRunning = true;
			mGameView = new RunnersHighView(getApplicationContext()); 
			setContentView(mGameView);	     
		}	
		
	    @Override
	    protected void onDestroy() {
	    	if(Settings.RHDEBUG)
	    		Log.d("debug", "onDestroy main");
	    	isRunning = false;
	    	
			wakeLock.release();
			musicPlayerLoop.release();
			SoundManager.cleanup();
			if (mGameView != null) mGameView.cleanup();
			System.gc();
			super.onDestroy();
		}
		@Override
		public void onResume() {
			if(Settings.RHDEBUG)
				Log.d("debug", "onResume");
			wakeLock.acquire();
			if(MusicLoopStartedForFirstTime)
				musicPlayerLoop.start();
			super.onResume();

		}
		@Override
		public void onStop() {
			if(Settings.RHDEBUG)
				Log.d("debug", "onStop");
			super.onStop();
		}
		@Override
		public void onRestart() {
			if(Settings.RHDEBUG)
				Log.d("debug", "onRestart");

			super.onRestart();
		}
		@Override
		public void onPause() {
			if(Settings.RHDEBUG)
				Log.d("debug", "onPause");
			wakeLock.release();
			musicPlayerLoop.pause();
			super.onPause();
		}
	    
		public void saveScore(int score) {			
			Intent myIntent = new Intent (this, HighScoreForm.class);
			myIntent.putExtra("score", score);			
			startActivity (myIntent);
		}

		public void sleep() {
			sleep(SLEEP_TIME);
		}

		public void sleep(int time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    
	public class RunnersHighView extends GLSurfaceView implements Runnable {
		private Player player = null;
		private Level level;
		private ParalaxBackground background;
		private int width;
		private int height;

		private Button resetButton = null;
		private Bitmap resetButtonImg = null;
		private Button saveButton = null;
		private Bitmap saveButtonImg = null;
		private RHDrawable blackRHD = null;
		private Bitmap blackImg = null;
		private RHDrawable gameLoadingRHD = null;
		private Bitmap gameLoadingImg = null;
		private float blackImgAlpha;
		private boolean scoreWasSaved = false;
		private boolean deathSoundPlayed = false;
		private OpenGLRenderer mRenderer = null;
		private CounterGroup mCounterGroup;
		private CounterDigit mCounterDigit1;
		private CounterDigit mCounterDigit2;
		private CounterDigit mCounterDigit3;
		private CounterDigit mCounterDigit4;
		private Bitmap CounterFont = null;; 
		private Bitmap CounterYourScoreImg = null;;
		private RHDrawable CounterYourScoreDrawable = null;;;
		public  boolean doUpdateCounter = true;
		private long timeAtLastSecond;
		private int runCycleCounter;
		private HighscoreAdapter highScoreAdapter;

		private int mTotalHighscores = 0;
		private int mHighscore1 = 0;
		private int mHighscore2 = 0;
		private int mHighscore3 = 0;
		private int mHighscore4 = 0;
		private int mHighscore5 = 0;

		private HighscoreMark mHighscoreMark1 = null;
		private HighscoreMark mHighscoreMark2 = null;
		private HighscoreMark mHighscoreMark3 = null;
		private HighscoreMark mHighscoreMark4 = null;
		private HighscoreMark mHighscoreMark5 = null;
		
		private Bitmap mHighscoreMarkBitmap = null;
		private RHDrawable mNewHighscore = null;
		
		private int totalScore = 0;
		private boolean nineKwasplayed = false;
		private boolean gameIsLoading = true;
		

		public RunnersHighView(Context context) {
			super(context);
			
			mRenderer = new OpenGLRenderer();
			this.setRenderer(mRenderer);

			Util.getInstance().setAppContext(context);
			Util.getInstance().setAppRenderer(mRenderer);
			
	        Thread rHThread = new Thread(this);
			rHThread.start();
			
//			
//			initialize();
		}
		
		public void cleanup() {
			if (saveButtonImg != null) saveButtonImg.recycle();
			if (blackImg != null) blackImg.recycle();
			if (resetButtonImg!= null) resetButtonImg.recycle();
			if (background != null) background.cleanup();
			if (mHighscoreMarkBitmap != null) mHighscoreMarkBitmap.recycle();
			if (level != null) level.cleanup(); 
			if (player != null) player.cleanup();
		}

		private void initialize() {
			if(Settings.RHDEBUG)
				Log.d("debug", "initialize begin");
			
			long timeOfInitializationStart = System.currentTimeMillis();
			Util.roundStartTime = System.currentTimeMillis();
			
			Context context = Util.getAppContext();
			
			Rect rectgle= new Rect();
			Window window= getWindow();
			window.getDecorView().getWindowVisibleDisplayFrame(rectgle);

			DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
			
			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			width= display.getWidth();  
			height= Math.abs(rectgle.top - rectgle.bottom);
			
			if(Settings.RHDEBUG)
				Log.d("debug", "displaywidth: " + width + ", displayheight: " + height);
			
			Util.mScreenHeight=height;
			Util.mScreenWidth=width;
			Util.mWidthHeightRatio=width/height;
			
 
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inTempStorage = new byte[16*1024];
			
			gameLoadingImg = Util.loadBitmapFromAssets("game_loading.jpg"); 
			gameLoadingRHD = new RHDrawable(0, 0, 1, width, height);
			gameLoadingRHD.loadBitmap(gameLoadingImg);
			mRenderer.addMesh(gameLoadingRHD);
			
			long currentTime = System.currentTimeMillis();
	    	
	    	if (currentTime < lastCreationTime + MIN_CREATION_TIMEOUT) {
	    		long sleeptime = MIN_CREATION_TIMEOUT - (currentTime - lastCreationTime);
	    		lastCreationTime = currentTime;
    			try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {
					e.printStackTrace();
					finish();
				}
	    	}
	    	lastCreationTime = System.currentTimeMillis();
	        
			background = new ParalaxBackground(width, height);

			mRenderer.addMesh(background);
			sleep();
			

			try {
			
				background.loadLayerFar(Util.loadBitmapFromAssets("game_background_layer_3.png"));
				sleep();
			} catch (OutOfMemoryError oome) {
				System.gc();
				try {
					Thread.sleep(MIN_CREATION_TIMEOUT);
			
					background.loadLayerFar(
							Util.loadBitmapFromAssets("game_background_layer_3.png"));
					sleep();

				}catch (OutOfMemoryError e) { 
					e.printStackTrace();
					setResult(1);
					finish();
				} catch (InterruptedException e) {
					setResult(0);
					finish();
				}
			}
			
			try {
				background.loadLayerMiddle(Util.loadBitmapFromAssets("game_background_layer_2.png"));
				sleep();
			} catch (OutOfMemoryError oome) {
				System.gc();
				try {
					Thread.sleep(MIN_CREATION_TIMEOUT);
					background.loadLayerMiddle(Util.loadBitmapFromAssets("game_background_layer_2.png"));
					sleep();

				}catch (OutOfMemoryError e) { 
					e.printStackTrace();
					setResult(1);
					finish();
				} catch (InterruptedException e) {
					setResult(0);
					finish();
				}
			}

			try {
				background.loadLayerNear(Util.loadBitmapFromAssets("game_background_layer_1.png"));
				sleep();

			} catch (OutOfMemoryError oome) {
				System.gc();
				try {
					Thread.sleep(MIN_CREATION_TIMEOUT);
					background.loadLayerNear(Util.loadBitmapFromAssets("game_background_layer_1.png"));
					sleep();

				}catch (OutOfMemoryError e) { 
					e.printStackTrace();
					setResult(1);
					finish();
				} catch (InterruptedException e) {
					setResult(0);
					finish();
				}
			}
			
			
			if(Settings.RHDEBUG)
				Log.d("debug", "before addMesh");
			
			
			resetButtonImg = Util.loadBitmapFromAssets("game_button_play_again.png");
			resetButton = new Button(
					Util.getPercentOfScreenWidth(72), 
					height-Util.getPercentOfScreenHeight(18),
					-2, 
					Util.getPercentOfScreenWidth(26), 
					Util.getPercentOfScreenHeight(13));
			resetButton.loadBitmap(resetButtonImg);
			mRenderer.addMesh(resetButton);			
			
			saveButtonImg =Util.loadBitmapFromAssets("game_button_save.png");
			saveButton = new Button(
					Util.getPercentOfScreenWidth(42), 
					height-Util.getPercentOfScreenHeight(18),
					-2, 
					Util.getPercentOfScreenWidth(26),
					Util.getPercentOfScreenHeight(13));
			saveButton.loadBitmap(saveButtonImg);
			mRenderer.addMesh(saveButton);
			

			player = new Player(getApplicationContext(), mRenderer, height);
			sleep();
			
			level = new Level(context, mRenderer, width, height);
			sleep();
			
		
			
			if(Settings.RHDEBUG)
				Log.d("debug", "after player creation");
//			loadingDialog = new ProgressDialog( context );
//		    loadingDialog.setProgressStyle(0);
//		    loadingDialog.setMessage("Loading Highscore ...");
		
			

			
		    if(Settings.RHDEBUG)
				Log.d("debug", "after loading messages");
		    
		    highScoreAdapter = new HighscoreAdapter(context);

		    if(Settings.RHDEBUG)
		    	Log.d("debug", "after HighscoreAdapter");
		    
			//new counter
			CounterYourScoreImg = Util.loadBitmapFromAssets("game_background_score.png");
			CounterYourScoreDrawable = new RHDrawable(
					Util.getPercentOfScreenWidth(5),
					height-Util.getPercentOfScreenHeight(15), 
					0.9f, 
					Util.getPercentOfScreenWidth(27), 
					Util.getPercentOfScreenHeight(10));

			CounterYourScoreDrawable.loadBitmap(CounterYourScoreImg); 
			mRenderer.addMesh(CounterYourScoreDrawable);

			if(Settings.RHDEBUG)
				Log.d("debug", "after CounterYourScoreDrawable addMesh");
			
			CounterFont = Util.loadBitmapFromAssets("game_numberfont.png");
			mCounterGroup = new CounterGroup(
					Util.getPercentOfScreenWidth(14), 
					height-Util.getPercentOfScreenHeight(13.5f),
					0.9f, Util.getPercentOfScreenWidth(16), 
					Util.getPercentOfScreenHeight(6), 
					25);

			
			if(Settings.RHDEBUG)
				Log.d("debug", "after mCounterGroup");
			


			mCounterDigit1 = new CounterDigit(
					Util.getPercentOfScreenWidth(19), 
					height-Util.getPercentOfScreenHeight(13.5f), 
					0.9f, 
					Util.getPercentOfScreenWidth(3), 
					Util.getPercentOfScreenHeight(6));
			mCounterDigit1.loadBitmap(CounterFont); 
			mCounterGroup.add(mCounterDigit1);

			mCounterDigit2 = new CounterDigit(
					Util.getPercentOfScreenWidth(22),
					height-Util.getPercentOfScreenHeight(13.5f),
					0.9f,
					Util.getPercentOfScreenWidth(3), 
					Util.getPercentOfScreenHeight(6));
			mCounterDigit2.loadBitmap(CounterFont); 
			mCounterGroup.add(mCounterDigit2);

			mCounterDigit3 = new CounterDigit(
					Util.getPercentOfScreenWidth(25),
					height-Util.getPercentOfScreenHeight(13.5f), 
					0.9f,
					Util.getPercentOfScreenWidth(3), 
					Util.getPercentOfScreenHeight(6));
			mCounterDigit3.loadBitmap(CounterFont); 
			mCounterGroup.add(mCounterDigit3);

			mCounterDigit4 = new CounterDigit(
					Util.getPercentOfScreenWidth(28),
					height-Util.getPercentOfScreenHeight(13.5f),
					0.9f, 
					Util.getPercentOfScreenWidth(3), 
					Util.getPercentOfScreenHeight(6));

			mCounterDigit4.loadBitmap(CounterFont); 
			mCounterGroup.add(mCounterDigit4);
			
			mRenderer.addMesh(mCounterGroup);
			sleep();
			
			if(Settings.RHDEBUG)
				Log.d("debug", "after counter");
			

			blackImg = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888);
			blackRHD = new RHDrawable(0, 0, 1, width, height);
			blackImg.eraseColor(-16777216);
			blackImgAlpha=1;
			blackRHD.setColor(0, 0, 0, blackImgAlpha);
			blackRHD.loadBitmap(blackImg);
			mRenderer.addMesh(blackRHD);
			
			gameLoadingRHD.z = -1.0f;
			
			
			mHighscoreMarkBitmap = Util.loadBitmapFromAssets("game_highscoremark.png");
			 
			mNewHighscore = new RHDrawable(width/2 - 128, height/2 - 64, -2, 256, 128);
			mNewHighscore.loadBitmap(Util.loadBitmapFromAssets("game_new_highscore.png"));
			mRenderer.addMesh(mNewHighscore);

			if(Settings.showHighscoreMarks)
				initHighscoreMarks();
			
			
			//give the player time to read loading screen and controls
			while(System.currentTimeMillis() < timeOfInitializationStart+Settings.TimeForLoadingScreenToBeVisible)
				sleep(10);
			
			timeAtLastSecond = System.currentTimeMillis();
	        runCycleCounter=0;
	        
	        
	        
	        
			if(Settings.RHDEBUG)
				Log.d("debug", "RunnersHighView initiation ended");
		}
		
		public int getAmountOfLocalHighscores() {
			highScoreAdapter.open();
		    Cursor cursor = highScoreAdapter.fetchScores("0");
		    int amount = cursor.getCount();
		    highScoreAdapter.close();
			return amount;
		}
		
		public int getHighscore(long id) {		    
			highScoreAdapter.open();
			if (mTotalHighscores >= id)
			{
			    Cursor cursor = highScoreAdapter.getHighscore(id);
			    String hs = cursor.getString(cursor.getColumnIndexOrThrow(HighscoreAdapter.KEY_SCORE));
			    highScoreAdapter.close();		    
			    return new Integer(hs);
			}
			else
				return 0;
		}
		
		@SuppressWarnings("unused")
		public void run() {

			if(Settings.RHDEBUG)
				Log.d("debug", "run method started");
					
			// wait until the intro is over
			// this gives the app enough time to load
			try{
				//loadingDialog.show();
				if(Settings.RHDEBUG)
					Log.d("debug", "run method in try");
				if(Settings.RHDEBUG)
					Log.d("debug", "mRenderer.firstFrameDone: " + mRenderer.firstFrameDone);
			
				while(!mRenderer.firstFrameDone)
					Thread.sleep(10);
				
				initialize();
				

				long timeAtStart = System.currentTimeMillis();
				while (System.currentTimeMillis() < timeAtStart + 2000 && isRunning)
				{
					blackImgAlpha-=0.005;
					blackRHD.setColor(0, 0, 0, blackImgAlpha);
					Thread.sleep(10);
				}
				
				
				blackImg.recycle();
				gameLoadingImg.recycle();
				
				blackRHD.shouldBeDrawn = false;
				gameLoadingRHD.shouldBeDrawn = false;
				
				mRenderer.removeMesh(blackRHD);
				mRenderer.removeMesh(gameLoadingRHD);
				
				if(Settings.RHDEBUG)
					Log.d("debug", "after fade in");

				try {
					if(isRunning && !musicPlayerLoop.isPlaying())
						musicPlayerLoop.start();
					
				} catch (IllegalStateException e) {
					e.printStackTrace();
					Log.w(Settings.LOG_TAG, "seems like you startet the game more" +
							" than once in a few seconds or canceld the game start");
					Log.w(Settings.LOG_TAG, "PLEASE DO NOT DO THIS UNLESS IT IS A STRESS TEST");
					return;
				}
				
				MusicLoopStartedForFirstTime=true;

			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				return;
			}
			
			if(Settings.RHDEBUG)
				Log.d("debug", "run method after try catch");
			
			blackRHD.z=-1.0f;
			blackRHD.setColor(0, 0, 0, 0);
			//mRenderer.removeMesh(blackRHD); //TODO: find a way to remove mesh without runtime errors

			long timeForOneCycle=0;
			long currentTimeTaken=0;
			long starttime = 0;
			
	        gameIsLoading = false;
			
			if(Settings.RHDEBUG)				
				Log.d("debug", "run method befor while");
			//			long debugTime = System.currentTimeMillis(); // FIXME DEBUG TIME FOR VIDEO CAPTURE
			Util.roundStartTime = System.currentTimeMillis();
			
			while(isRunning){
				
				starttime = System.currentTimeMillis();
				
//				if (debugTime + 15000 < starttime) sleep(100); // FIXME DEBUG TIME FOR VIDEO CAPTURE

//				level.update(); // FIXME remove this line
				
				player.playerSprite.setFrameUpdateTime(
						(level.baseSpeedMax+level.extraSpeedMax)*10 - 
						((level.baseSpeed+level.extraSpeed)*10) +
						60 );
				if (player.update()) {
						if(Settings.RHDEBUG){
							currentTimeTaken = System.currentTimeMillis()- starttime;
							Log.d("runtime", "time after player update: " + Integer.toString((int)currentTimeTaken));
						}
						level.update();
						if(Settings.RHDEBUG){
							currentTimeTaken = System.currentTimeMillis()- starttime;
							Log.d("runtime", "time after level update: " + Integer.toString((int)currentTimeTaken));
						}
						background.update();
						
						if(Settings.RHDEBUG){
							currentTimeTaken = System.currentTimeMillis()- starttime;
							Log.d("runtime", "time after background update: " + Integer.toString((int)currentTimeTaken));
						}
						if(Settings.showHighscoreMarks)
							updateHighscoreMarks();
						
				} else {
					if(player.y < 0){
						doUpdateCounter=false;
						resetButton.setShowButton(true);
						resetButton.z = 1.0f;
						saveButton.setShowButton(true);
						saveButton.z = 1.0f;
						if(!deathSoundPlayed){
							SoundManager.playSound(7, 1, 0.5f, 0.5f, 0);
							deathSoundPlayed=true;
							
							System.gc(); //do garbage collection
						}
						if(Settings.showHighscoreMarks){
							if (totalScore > mHighscore1)
								mNewHighscore.z = 1.0f;
						}
					}
				}
				
				if(player.collidedWithObstacle(level.getLevelPosition()) ){
					level.lowerSpeed();
				}
				
				if(doUpdateCounter)
				{
					totalScore = level.getDistanceScore() + player.getBonusScore();
					if (Settings.SHOW_FPS) mCounterGroup.tryToSetCounterTo(mRenderer.fps);
					else mCounterGroup.tryToSetCounterTo(totalScore);
					
					if(totalScore>=9000 && nineKwasplayed==false)
					{
						nineKwasplayed=true;
						SoundManager.playSound(9, 1, 1000, 1000, 0);
					}
				}
					

				if(Settings.RHDEBUG){				
					timeForOneCycle= System.currentTimeMillis()- starttime;
					Log.d("runtime", "time after counter update: " + Integer.toString((int)timeForOneCycle));
				}
				timeForOneCycle= System.currentTimeMillis()- starttime;

				if(timeForOneCycle < 10) {
					try{ Thread.sleep(10-timeForOneCycle); }
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				runCycleCounter++;
				
				if(Settings.RHDEBUG) {
					currentTimeTaken = System.currentTimeMillis()- starttime;
					Log.d("runtime", "time after thread sleep : " + Integer.toString((int)currentTimeTaken));
				}
				
				timeForOneCycle= System.currentTimeMillis()- starttime;
				if((System.currentTimeMillis() - timeAtLastSecond) > 1000 && Settings.RHDEBUG)
				{
					timeAtLastSecond = System.currentTimeMillis();
					Log.d("runtime", "run cycles per second: " + Integer.toString(runCycleCounter));
					runCycleCounter=0;
				}
				if(Settings.RHDEBUG){
					timeForOneCycle= System.currentTimeMillis()- starttime;
					Log.d("runtime", "overall time for this run: " + Integer.toString((int)timeForOneCycle));
				}
			}
			
			if(Settings.RHDEBUG)
				Log.d("debug", "run method ended");
			
		}
		
		private void initHighscoreMarks()
		{
			mTotalHighscores = getAmountOfLocalHighscores();

			if(Settings.RHDEBUG)
				Log.d("debug", "mTotalHighscores: " + mTotalHighscores);
			
			// awesome switch usage XD // TODO: remove this comment :D
			switch(mTotalHighscores)
			{
			default:
			case 5:
				mHighscore5 = getHighscore(5);
				if (mHighscoreMark5 == null)
					mHighscoreMark5 = new HighscoreMark(mRenderer, mHighscoreMarkBitmap, CounterFont);
				mHighscoreMark5.setMarkTo(5, mHighscore5);
				mHighscoreMark5.z = 0.0f;
			case 4:
				mHighscore4 = getHighscore(4);
				if (mHighscoreMark4 == null)
					mHighscoreMark4 = new HighscoreMark(mRenderer, mHighscoreMarkBitmap, CounterFont);
				mHighscoreMark4.setMarkTo(4, mHighscore4);
				mHighscoreMark4.z = 0.0f;
			case 3:
				mHighscore3 = getHighscore(3);
				if (mHighscoreMark3 == null)
					mHighscoreMark3 = new HighscoreMark(mRenderer, mHighscoreMarkBitmap, CounterFont);
				mHighscoreMark3.setMarkTo(3, mHighscore3);
				mHighscoreMark3.z = 0.0f;
			case 2:
				mHighscore2 = getHighscore(2);
				if (mHighscoreMark2 == null)
					mHighscoreMark2 = new HighscoreMark(mRenderer, mHighscoreMarkBitmap, CounterFont);
				mHighscoreMark2.setMarkTo(2, mHighscore2);
				mHighscoreMark2.z = 0.0f;
			case 1:
				mHighscore1 = getHighscore(1);

				if(Settings.RHDEBUG)
					Log.d("debug", "mHighscore1: " + mHighscore1);
				
				if (mHighscoreMark1 == null)
					mHighscoreMark1 = new HighscoreMark(mRenderer, mHighscoreMarkBitmap, CounterFont);
				mHighscoreMark1.setMarkTo(1, mHighscore1);

				mHighscoreMark1.z = 0.0f;
			case 0:
			}
		}
		
		private void updateHighscoreMarks()
		{	
			switch(mTotalHighscores)
			{
			default:
			case 5:
				if (mHighscoreMark5 != null)
				{
					if (totalScore < mHighscore5)
						mHighscoreMark5.x = (mHighscore5 - totalScore) * 10 + player.x;
					else
						mHighscoreMark5.x = 0;
				}
			case 4:
				if (mHighscoreMark4 != null)
				{
					if (totalScore < mHighscore4)
						mHighscoreMark4.x = (mHighscore4 - totalScore) * 10 + player.x;
					else
					{
						mHighscoreMark4.x = 0;
						if (mHighscoreMark5 != null)
							mHighscoreMark5.z = -2.0f;
					}
				}
			case 3:
				if (mHighscoreMark3 != null)
				{
					if (totalScore < mHighscore3)
						mHighscoreMark3.x = (mHighscore3 - totalScore) * 10 + player.x;
					else
					{
						mHighscoreMark3.x = 0;
						if (mHighscoreMark4 != null)
							mHighscoreMark4.z = -2.0f;
					}
				}
			case 2:
				if (mHighscoreMark2 != null)
				{
					if (totalScore < mHighscore2)
						mHighscoreMark2.x = (mHighscore2 - totalScore) * 10 + player.x;
					else
					{
						mHighscoreMark2.x = 0;
						if (mHighscoreMark3 != null)
							mHighscoreMark3.z = -2.0f;
					}
				}
			case 1:
				if (mHighscoreMark1 != null)
				{
					if (totalScore < mHighscore1)
						mHighscoreMark1.x = (mHighscore1 - totalScore) * 10 + player.x;
					else
					{
						mHighscoreMark1.x = 0;
						if (mHighscoreMark2 != null)
							mHighscoreMark2.z = -2.0f;
					}
				}
			case 0:
			}

		}
		

		public boolean onTouchEvent(MotionEvent event) {
			if(!gameIsLoading){
				if(event.getAction() == MotionEvent.ACTION_UP)
					player.setJump(false);
				
				else if(event.getAction() == MotionEvent.ACTION_DOWN){
					if (resetButton.getShowButton() || saveButton.getShowButton()) {
						if(resetButton.isClicked( event.getX(), Util.getInstance().toScreenY((int)event.getY()) ) ){
							System.gc(); //do garbage collection
							player.reset();
							level.reset();
							resetButton.setShowButton(false);
							resetButton.z = -2.0f;
							saveButton.setShowButton(false);
							saveButton.z = -2.0f;
							saveButton.x = saveButton.lastX; 
							mCounterGroup.resetCounter();
							scoreWasSaved=false;
							deathSoundPlayed=false;
							SoundManager.playSound(1, 1);
							doUpdateCounter=true;
							
							if(Settings.showHighscoreMarks){
								mNewHighscore.z = -2.0f;
								initHighscoreMarks();
							}
								
							nineKwasplayed = false;
							totalScore = 0;
							Util.roundStartTime = System.currentTimeMillis();
						}
						else if(saveButton.isClicked( event.getX(), Util.getInstance().toScreenY((int)event.getY())  ) && !scoreWasSaved){
							//save score
							saveButton.setShowButton(false);
							saveButton.z = -2.0f;
							saveButton.lastX = saveButton.x;
							saveButton.x = -5000;
							
							saveScore(totalScore);
	
							//play save sound
							SoundManager.playSound(4, 1);
							scoreWasSaved=true;
						}
					}
					else {
						player.setJump(true);
					}
				}
			}
			
			return true;
		}
	}
}
