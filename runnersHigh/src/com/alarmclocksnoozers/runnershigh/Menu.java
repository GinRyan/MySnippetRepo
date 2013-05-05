package com.alarmclocksnoozers.runnershigh;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Menu extends Activity {
	MediaPlayer menuLoop;
	private Toast loadMessage;
	private Runnable gameLauncher;
	private Intent gameIntent;
	private Handler mHandler;
	private android.widget.Button mPlayButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	requestWindowFeature(Window.FEATURE_NO_TITLE);  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); 
        

		loadMessage = Toast.makeText(getApplicationContext(), "loading game...", Toast.LENGTH_SHORT );
		loadMessage.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
        
		gameIntent = new Intent (this, main.class);
		mPlayButton = (android.widget.Button)findViewById(R.id.startButton);
		mPlayButton.setClickable(true);
		mPlayButton.setEnabled(true);
		gameLauncher = new Runnable() {
			
			public void run() {
				mPlayButton.setClickable(false);
		    	mPlayButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
				startActivityForResult(gameIntent, 0);
			}
		};
		
		mHandler = new Handler();
		
		
		
        /*
        menuLoop = MediaPlayer.create(getApplicationContext(), R.raw.menu);  
        menuLoop.setLooping(true);
        menuLoop.seekTo(0);
        menuLoop.setVolume(0.5f, 0.5f);
        menuLoop.start();
        */
    }
    
    public void playGame(View view) {

		// Loading Toast
		loadMessage.show();
    	Settings.SHOW_FPS = false;
    	mHandler.post(gameLauncher);
    }
    
    public void playGameWithFPS(View view) {

		// Loading Toast
		loadMessage.show();
    	Settings.SHOW_FPS = true;
    	mHandler.post(gameLauncher);
    }
    
    public void showScore(View view) {
    	Intent myIntent = new Intent (this, HighScoreActivity.class);
    	startActivity (myIntent);
    }
    
    public void showInfo(View view) {
    	Intent myIntent = new Intent (this, Info.class);
    	startActivity (myIntent);
    }
    
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    	if (resultCode == 1) {
    		showDialog(1);
    		mHandler.postDelayed(new Runnable() {
				
				public void run() {
					mPlayButton.setClickable(true);
					mPlayButton.getBackground().clearColorFilter();
				}
			}, 10000);
    	} else {
    		mPlayButton.setClickable(true);
    		mPlayButton.getBackground().clearColorFilter();
    	}
    	
    }
    
    public void donate(View view) {
    	Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_DONATE));
    	startActivity(browserIntent);
    }
    
    protected Dialog onCreateDialog(int id) {
    	return new AlertDialog.Builder(this)
		  .setTitle("Error while changing view")
		  .setMessage("System needs some time to free memory. Please try again in 10 seconds.")
		  .setCancelable(true)
		  .create();
    }
}
