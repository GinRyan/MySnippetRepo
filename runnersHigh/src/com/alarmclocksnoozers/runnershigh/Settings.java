/*
 * RunnersHigh Settings 
 * _DESCRIPTION:
 * 	for global values
 */

package com.alarmclocksnoozers.runnershigh;

public class Settings {	
	
	static final String LOG_TAG = "RunnersHigh";
	
	static final String URL_RUNNERSHIGH = "http://rh.fidrelity.at";
	static final String HIGHSCORE_GET_URL = "http://rh.fidrelity.at/best.php";
	static final String HIGHSCORE_POST_URL = "http://rh.fidrelity.at/post/post_highscore.php";
	
	static final String URL_ANDRE = "http://fidrelity.at/";
	static final String URL_ANDREAS = "http://www.andreasnagl.at";
	static final String URL_CHRIS = "http://cwinkler.multimediatechnology.at/";
	static final String URL_FRANCOIS = "http://www.weberdevelopment.de/";
	static final String URL_HANS = "http://kookaburradesign.at";
	static final String URL_MANUEL = "http://www.manuel-lehermayr.at";
//	static final String URL_MARIO = "http://www.manuel-lehermayr.at";
	static final String URL_STEVE = "http://steve.multimediatechnology.at/";
	static final String URL_WEBI = "http://mwebi.multimediatechnology.at/";
	
	static final String URL_FH = "http://www.fh-salzburg.ac.at";
	static final String URL_MMA = "http://multimediaart.at/";
	static final String URL_MMT = "http://multimediatechnology.at/";
	
	static final String URL_SONY = "http://www.sonydadc.com/";
	
	static final boolean RHDEBUG = false;
	static boolean SHOW_FPS = false;
	
	static final boolean showHighscoreMarks = false;
	
	//gameplay settings
	static public float FirstBlockHeight = 50;
	
	static final int TimeForLoadingScreenToBeVisible = 3500;
	
	static final int TimeOfFirstSpeedIncrease = 30000;
	static final int timeToFurtherSpeedIncreaseMillis = 10000;
	static final int timeUntilLongBlocksStopMillis = 40000;

	static final int onlineHighscoreLimit = 100;
	
	
	static final String URL_DONATE = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=PSPCC2Z4PLL8W";
}