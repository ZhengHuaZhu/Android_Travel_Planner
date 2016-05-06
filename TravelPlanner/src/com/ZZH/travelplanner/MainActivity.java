package com.ZZH.travelplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	private static DBHelper dbh;
	private String name, password;
	private TextView tvusername;
	private boolean hasusernamepassword=false;
	private static final int REQUEST_ONE=1;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//dbh = DBHelper.getDBHelper(this);
		//dbh.fillDatabase();
		tvusername=(TextView) findViewById(R.id.username);
		
		prefs = getPreferences(MODE_PRIVATE);
		// retrieve saved boolean value
		hasusernamepassword = prefs.getBoolean("HASUSERNAMEPASSWORD", false); 
		
		if(!hasusernamepassword){
			Intent i = new Intent(this, SettingActivity.class);		
			startActivityForResult(i,REQUEST_ONE);
		}else{
			name=prefs.getString("USERNAME", "");
			password=prefs.getString("PASSWORD", "");
			Toast.makeText(this, "username is: "+name, Toast.LENGTH_LONG).show();
			tvusername.setText(name);
		}    
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
	        case R.id.dawson:		
	        	Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://www.dawsoncollege.qc.ca/computer-science-technology/"));
	        	startActivity(intent);
	            break;	
	        case R.id.about:	
	        	startActivity(new Intent(this, AboutActivity.class));	
	            break;		
			case R.id.settings:	
				Intent i = new Intent(this, SettingActivity.class);		
				startActivityForResult(i,REQUEST_ONE);
		        break;		
		}	
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onActivityResult(int request, int result, Intent i) {
		Log.i("MAIN", "onActivityResult");
		if(request==REQUEST_ONE){
			switch (result) {
				case RESULT_OK:
					if (i != null && i.hasExtra("USERNAME")) {
						Log.i("MAIN", "has username");
						name = i.getExtras().getString("USERNAME");
						password = i.getExtras().getString("PASSWORD");
						Log.i("MAIN", password);
						if (name == null || name.trim().length()==0 ||
							password == null || password.trim().length()==0) {
							tvusername.setText(R.string.nodata);
						}else{
							Log.i("MAIN", "setting username");
							tvusername.setText(name);
							hasusernamepassword=true;
						}
					} else {
						tvusername.setText(R.string.nodata);
					}
					break;
				case RESULT_CANCELED:
				default:
					tvusername.setText(R.string.nodata);
			}
		}
	} 
	
	@Override
	public void onPause() {
		super.onPause();
		
		prefs = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString("USERNAME", name);
		editor.putString("PASSWORD", password);
		editor.putBoolean("HASUSERNAMEPASSWORD", hasusernamepassword);
		editor.commit();
	}

	
	public void currentItinerary(View view){
		Intent i =  new Intent(this, ItineraryActivity.class);
    	startActivity(i);		
	}
	
	public void calTip(View view){
		Intent i =  new Intent(this, TipCalActivity.class);
    	startActivity(i);
		
	}
	
	public void callDVWConversion(View view){
		Intent i = new Intent(this, DVWConversionActivity.class);
		startActivity(i);
	}
	
	public void openAboutActivity(View view){
		Intent i =  new Intent(this, AboutActivity.class);
    	startActivity(i);		
	}
	
	public void comingSoon(View view){
		Intent i =  new Intent(this, ComingsoonActivity.class);
    	startActivity(i);		
	}
	
	public void callCurrencyActivity(View view){
		Intent i = new Intent(this, CurrencyConversionActivity.class);
		Log.i(TAG, "Starting CurrencyConversionActivity");
		startActivity(i);
	}
	
	public void callManageTripActivity(View view){
		Intent i = new Intent(this, ManageTripActivity.class);
		Log.i(TAG, "Starting ManageTripActivity");
		i.putExtra("username", name);
		i.putExtra("password", password);
		startActivity(i);
	}
	public void launchToday(View view){
		Intent i = new Intent(this, TodayActivity.class);
		Log.i(TAG, "Starting Today");
		startActivity(i);
	}
	public void launchWeatherActivity(View view) {
		Intent i = new Intent(this, WeatherCheckActivity.class);
		startActivity(i);
	}

}
