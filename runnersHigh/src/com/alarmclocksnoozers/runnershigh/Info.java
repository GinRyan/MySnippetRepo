package com.alarmclocksnoozers.runnershigh;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Info extends Activity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        Toast.makeText(getApplicationContext(), "names and logos are clickable", Toast.LENGTH_LONG).show();
    }
    
    public void visitWebsite(View view) {
    	Intent browserIntent = null;
    	
    	switch (view.getId()) {

    	case R.id.buttonRunnersHigh:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_RUNNERSHIGH));
			break;
    	
    	case R.id.buttonAndre:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_ANDRE));
			break;
    	case R.id.buttonAndreas:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_ANDREAS));
			break;
    	case R.id.buttonChris:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_CHRIS));
			break;
    	case R.id.buttonFrancois:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_FRANCOIS));
			break;
    	case R.id.buttonHans:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_HANS));
			break;
    	case R.id.buttonManuel:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_MANUEL));
			break;
    	case R.id.buttonWebi:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_WEBI));
			break;


    	case R.id.buttonFHS:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_FH));
			break;
    	case R.id.buttonMMT:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_MMT));
			break;
    	case R.id.buttonMMA:
			browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Settings.URL_MMA));
			break;

			
		default:
			Log.e("RunnersHigh", "unexpected buttonclick");
			break;
		}
    	
    	if (browserIntent != null) startActivity(browserIntent);
    }
}
