/*
 * HighscoreActivity
 * runnersHigh 1.0
 * 
 * _DESCRIPTION:
 * 	Highscore Activity itself - shows highscores of user 
 */

package com.alarmclocksnoozers.runnershigh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import com.alarmclocksnoozers.highscore.HighscoreAdapter;

public class HighScoreActivity extends Activity {
	
	private HighscoreAdapter highScoreAdapter = null;
	
	private static final String POST_HIGHSCORE_URL = Settings.HIGHSCORE_POST_URL; // "http://rh.fidrelity.at/post/post_highscore.php";
	private static final String GET_HIGHSCORE_URL = Settings.HIGHSCORE_GET_URL; // "http://rh.fidrelity.at/best.php";
	
	private TableLayout highscoreTable;
	
	// ---------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
//    	requestWindowFeature(Window.FEATURE_NO_TITLE);  
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);

        highScoreAdapter = new HighscoreAdapter(this);
        highScoreAdapter.open();
        
        highscoreTable = (TableLayout) findViewById(R.id.highscoreTable);
        
        final Context context = this;
        
        final Handler handler = new Handler();

        findViewById(R.id.buttonLocalHighscore).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				Toast.makeText(context, R.string.hs_loading_local, Toast.LENGTH_SHORT).show();
				
				handler.postDelayed(new Runnable() {
					
					public void run() {
						showLocalScore();
					}
				}, 500);
			}
		});
        

        findViewById(R.id.buttonOnlineHighscore).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				Toast.makeText(context, R.string.hs_loading_online, Toast.LENGTH_SHORT).show();
				
				handler.postDelayed(new Runnable() {
					
					public void run() {
						showOnlineScore();
					}
				}, 500);
			}
		});
        
        
        Toast.makeText(context, R.string.hs_loading_local, Toast.LENGTH_SHORT).show();
		
		handler.postDelayed(new Runnable() {
			
			public void run() {
				showLocalScore();
			}
		}, 500);
    }
    
    private void showLocalScore() {
    	
    	highscoreTable.removeAllViews();
    	
    	Cursor c = highScoreAdapter.fetchScores("0");
    	
    	if (c.isAfterLast()) {
            Toast.makeText(this, R.string.hs_no_data, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	int currentPlace = 1;
    	
    	do {

    		final String placeString = ""+(currentPlace++)+".";
    		final String scoreString = c.getString(2);
    		final String nameString = c.getString(1);
    		
    		View additional;
    		
    		if (c.getString(3).equalsIgnoreCase("0")) {
    			additional = new Button(this);
    			
    			final Context context = this;
    			final int id = c.getInt(0);
		       
    			additional.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						AlertDialog.Builder alert = new AlertDialog.Builder(context);
				
				        alert.setTitle("Push this score online ?");
				        alert.setMessage("Name: " + nameString + "\nScore: " + scoreString);
				
				        // OK
				        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int whichButton) {          
				        	// Push score online
				        	if(!isOnline()) {
				        		highScoreAdapter.toastMessage(R.string.hs_error_no_internet);
				        	} else {
				        	        		
				        		// Create a new HttpClient and Post Header
				        	    HttpClient httpclient = new DefaultHttpClient();
				        	    HttpPost httppost = new HttpPost(POST_HIGHSCORE_URL);
				
				        	    try {
				        	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				        	        nameValuePairs.add(new BasicNameValuePair("name", nameString));
				        	        nameValuePairs.add(new BasicNameValuePair("score", scoreString));
				        	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				        	        httpclient.execute(httppost);        	       
				        	        highScoreAdapter.updateScore(id, 1);
				        	        highScoreAdapter.toastMessage(R.string.hs_pushed_online);
				        	        
				        	        runOnUiThread(new Runnable() {
										
										public void run() {
											showLocalScore();
											
										}
									});
				        	    } catch (ClientProtocolException e) {
				        	        // TODO Auto-generated catch block
				        	    } catch (IOException e) {
				        	        // TODO Auto-generated catch block
				        	    }        		
				        	}        	
				          }
				        });
				        
				        // CANCEL
				        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
				          public void onClick(DialogInterface dialog, int whichButton) {
				            // Canceled.
				          }
				        });
				        alert.show();  
					}
				});
    			additional.setBackgroundResource(R.drawable.highscore_submit);
    			
    			LayoutParams paramsOfSubmitButton = new LayoutParams(0, LayoutParams.MATCH_PARENT, 3.0f);
            	additional.setLayoutParams(paramsOfSubmitButton);
    		} else {
    			additional = new TextView(this, null, android.R.attr.textAppearanceSmallInverse);
    			((TextView)additional).setText("is online");

        		LayoutParams paramsOfAdditional = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3.0f);
        		paramsOfAdditional.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        		additional.setLayoutParams(paramsOfAdditional);
    		}
    		
    		generateLine(placeString, scoreString, nameString, additional);
    		
    	} while(c.moveToNext());
    	
    	
    	
    }
    
    private void showOnlineScore() {
    	
    	
    	
    	if(!isOnline()) {
    		Toast.makeText(this, R.string.hs_error_no_internet, Toast.LENGTH_SHORT).show();
    	} else {

        	highscoreTable.removeAllViews();
        	
	    	try {
	    		HttpClient client = new DefaultHttpClient();  
	    		String getURL = GET_HIGHSCORE_URL + "?size=" + Integer.toString(Settings.onlineHighscoreLimit);
	    		HttpGet get = new HttpGet(getURL);
	    		// query data from server
	    		HttpResponse responseGet = client.execute(get); 
	    		HttpEntity resEntityGet = responseGet.getEntity();  
	    		if (resEntityGet != null) {
	    			JSONArray jArray = new JSONArray(EntityUtils.toString(resEntityGet));
	    			
					String nameString;
					String scoreString;
					String timeStamp;
					
	    			for(int i = 0; i < jArray.length(); i++) {
	    				nameString = jArray.getJSONObject(i).getString("name");
	    				scoreString = jArray.getJSONObject(i).getString("score");
	    				timeStamp = jArray.getJSONObject(i).getString("created_at");
	    				
	    				View additional = new TextView(this, null, android.R.attr.textAppearanceSmallInverse);
	        			((TextView)additional).setText(timeStamp);

	            		LayoutParams paramsOfAdditional = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3.0f);
	            		paramsOfAdditional.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
	            		additional.setLayoutParams(paramsOfAdditional);
	            		
	            		generateLine(""+(i+1), scoreString, nameString, additional);
	    			}             
	    		}

	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
    	}

    }
    
    
    private void generateLine(String placeString, String scoreString, String nameString, View additional) {
    	
    	TextView place = new TextView(this, null, android.R.attr.textAppearanceLargeInverse);
		place.setText(placeString);
		LayoutParams paramsOfPlace = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2.0f);
		paramsOfPlace.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		place.setLayoutParams(paramsOfPlace);

		TextView score = new TextView(this, null, android.R.attr.textAppearanceMediumInverse);
		score.setText(scoreString);
		LayoutParams paramsOfScore = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3.0f);
		paramsOfScore.gravity = Gravity.CENTER;
		score.setLayoutParams(paramsOfScore);
		
		TextView name = new TextView(this, null, android.R.attr.textAppearanceMediumInverse);
		name.setText(nameString);
		LayoutParams paramsOfName = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 10.0f);
		paramsOfName.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
		name.setLayoutParams(paramsOfName);
		

		addLine(place, score, name, additional);
    }
    
    private void addLine(View place, View score, View name, View additional) {
    	TableRow tr = new TableRow(this);

    	tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    	
    	
    	tr.addView(place);
    	tr.addView(score);
    	tr.addView(name);
    	tr.addView(additional);
    	
    	highscoreTable.addView(tr);
    	ImageView line = new ImageView(this);
    	line.setBackgroundResource(R.drawable.highscore_line);
    	highscoreTable.addView(line);
    }
    
    
    // ---------------------------------------------------------
	// Check if user is connected to the internet
	public boolean isOnline() {		
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    if (ni != null && ni.isAvailable() && ni.isConnected()) {
	        return true;
	    } else {
	        return false; 
	    }
	}
    
    // ---------------------------------------------------------
    @Override    
    protected void onDestroy() {        
        super.onDestroy();
         
        if (highScoreAdapter != null) {
        	highScoreAdapter.close();
        }
    }
}
