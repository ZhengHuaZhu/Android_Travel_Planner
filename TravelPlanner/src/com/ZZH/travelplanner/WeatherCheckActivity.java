/**
 * 
 */
package com.ZZH.travelplanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author 1235063
 *
 */
public class WeatherCheckActivity extends Activity {
	private static final String TAG = "WeatherCheck";
	private static final int MAXBYTES = 700;
	private boolean metricSelected = true;
	private TextView connectionError;
	private String city;
	private String countryCode;
	private String jsonData;
	public static DBHelper dbh;
	private long budgetId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_check);
		dbh = DBHelper.getDBHelper(this);
		SharedPreferences prefs = getSharedPreferences("LASTCHECKEDBUDGETID", MODE_PRIVATE);
		budgetId = prefs.getLong("LAST_BUDGETID", 1);
		connectionError = (TextView) findViewById(R.id.connection_error);
	}
	
	public void onRadioButtonClicked(View view) {
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    switch(view.getId()) {
	        case R.id.metricRB:
	            if (checked)
	                metricSelected = true;
	            break;
	        case R.id.imperialRB:
	            if (checked)
	                metricSelected = false;
	            break;
	    }
	}
	
	private void getDBInfo() {
		//Initialize the city and country code variables 
		Log.i(TAG, "GETTING DB INFO");
		Log.i(TAG, "GETTING TRIP ID");
		//Log.i(TAG, "TRIPID = " + tripId);
		//Log.i(TAG, "LONG.TOSTRING(TRIPID) = " + Long.toString(tripId));
		Cursor c = dbh.getItineraryDetails(budgetId);
		if(c.getCount() > 0){
			c.moveToLast();
			city = c.getString(c.getColumnIndex(DBHelper.COLUMN_LOCATIONS_CITY));
			Log.i(TAG, "CITY = " + city);
			countryCode = c.getString(c.getColumnIndex(DBHelper.COLUMN_LOCATIONS_COUNTRY_CODE));
			Log.i(TAG, "CC = " + countryCode);
		}
	}
	
	public void checkWeatherForecast(View view) {
		Log.i(TAG, "Button pressed");
		//Query the db to get the location information
		getDBInfo();
		
		//Create the url, adding the proper location information
		String stringUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + countryCode;
		if(metricSelected) {
			stringUrl = stringUrl + "&units=metric";
		}
		else {
			stringUrl = stringUrl + "&units=imperial";
		}
		
		//Add app id
		stringUrl = stringUrl + "&appid=2de143494c0b295cca9337e1e96b00e0";
		
		Log.i(TAG, "FINAL URL: " + stringUrl);
		
		//Check if connection is available and working
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		int permissionCheck = ContextCompat.checkSelfPermission(this,
		        Manifest.permission.INTERNET);
		if(permissionCheck == PackageManager.PERMISSION_GRANTED){
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				// invoke the AsyncTask to do the dirtywork.
				new DownloadWebpageText().execute(stringUrl);
			} else {
				 Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
			}
		}
		else
			Toast.makeText(this, "Internet Permission not granted", Toast.LENGTH_SHORT).show();
			
			
	} // myClickHandler()

	/*
	 * Uses AsyncTask to create a task away from the main UI thread. This task
	 * takes a URL string and uses it to create an HttpUrlConnection. Once the
	 * connection has been established, the AsyncTask downloads the contents of
	 * the webpage via an an InputStream. The InputStream is converted into a
	 * string, which is displayed in the UI by the AsyncTask's onPostExecute
	 * method.
	 */

	private class DownloadWebpageText extends AsyncTask<String, Void, String> {

		// onPostExecute displays the results of the AsyncTask.
		// runs in calling thread (in UI thread)
		protected void onPostExecute(String result) {
			setTextViews(result);
		}

		@Override
		// runs in background (not in UI thread)
		protected String doInBackground(String... urls) {
			// params comes from the execute() call: params[0] is the url.
			try {
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				Log.e(TAG, "IOException Caught: " + Log.getStackTraceString(e));
				return "Unable to retrieve web page.";
			}
		}
	} // AsyncTask DownloadWebpageText()

	/*
	 * Given a URL, establishes an HttpUrlConnection and retrieves the web page
	 * content as a InputStream, which it returns as a string.
	 */

	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;
		// Only read the first 500 characters of the retrieved
		// web page content.
		int len = MAXBYTES;
		HttpURLConnection conn = null;
		URL url = new URL(myurl);
		try {
			// create and open the connection
			conn = (HttpURLConnection) url.openConnection();

			/*
			 * set maximum time to wait for stream read read fails with
			 * SocketTimeoutException if elapses before connection established
			 * 
			 * in milliseconds
			 * 
			 * default: 0 - timeout disabled
			 */
			conn.setReadTimeout(10000);
			/*
			 * set maximum time to wait while connecting connect fails with
			 * SocketTimeoutException if elapses before connection established
			 * 
			 * in milliseconds
			 * 
			 * default: 0 - forces blocking connect timeout still occurs but
			 * VERY LONG wait ~several minutes
			 */
			conn.setConnectTimeout(15000 /* milliseconds */);
			/*
			 * HTTP Request method defined by protocol
			 * GET/POST/HEAD/POST/PUT/DELETE/TRACE/CONNECT
			 * 
			 * default: GET
			 */
			conn.setRequestMethod("GET");
			// specifies whether this connection allows receiving data
			conn.setDoInput(true);
			// Starts the query
			conn.connect();

			int response = conn.getResponseCode();
			Log.d(TAG, "Server returned: " + response + " aborting read.");

			/*
			 *  check the status code HTTP_OK = 200 anything else we didn't get what
			 *  we want in the data.
			 */
			if (response != HttpURLConnection.HTTP_OK)
				return "Server returned: " + response + " aborting read.";
			
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            return sb.toString();
			
			// get the stream for the data from the website
			//is = conn.getInputStream();
			// read the stream (max len bytes)
			/*String contentAsString = readIt(is, len);
			return contentAsString;*/
		} catch (IOException e) {
			Log.e(TAG, "IOException Caught 2: " + Log.getStackTraceString(e));
			throw e;
		} finally {
			/*
			 * Make sure that the InputStream is closed after the app is
			 * finished using it.
			 * Make sure the connection is closed after the app is finished using it.
			 */

			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {
				}
				if (conn != null)
					try {
						conn.disconnect();
					} catch (IllegalStateException ignore ) {
					}
			}
		}
	} // downloadUrl()

	//
	/*
	 * Reads stream from HTTP connection and converts it to a String. See
	 * stackoverflow or a good explanation of why I did it this way.
	 * http://stackoverflow
	 * .com/questions/3459127/should-i-buffer-the-inputstream
	 * -or-the-inputstreamreader
	 */
	public String readIt(InputStream stream, int len) throws IOException,
	UnsupportedEncodingException {
		char[] buffer = new char[len];
		Reader reader = null;
		reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), len);
		int count = reader.read(buffer);
		Log.d(TAG, "Bytes read: " + count
				+ "(-1 means end of reader so max of " + len + " )");
		return new String(buffer);
	} // readIt()
	
	private void setTextViews (String data) {
		Log.i(TAG, "RESULT: " + data);
				
		try {
			JSONObject jsonData = new JSONObject(data);
			JSONObject main = jsonData.getJSONObject("main");
			JSONObject wind = jsonData.getJSONObject("wind");
			JSONObject sys = jsonData.getJSONObject("sys");
			
			String location = jsonData.getString("name");
			String forecast = jsonData.getJSONArray("weather").getJSONObject(0).getString("description");
			String weatherIconName = jsonData.getJSONArray("weather").getJSONObject(0).getString("icon");
			double temp = main.getDouble("temp");
			double highTemp = main.getDouble("temp_max");
			double lowTemp = main.getDouble("temp_min");
			String humidity = main.getString("humidity");
			String pressure = main.getString("pressure");
			String windSpeed = wind.getString("speed");
			long sunrise = sys.getLong("sunrise");
			long sunset = sys.getLong("sunset");
						
			TextView locationTV = (TextView) findViewById(R.id.location);
			TextView forecastTV = (TextView) findViewById(R.id.forecast); 
			TextView temperatureTV = (TextView) findViewById(R.id.temperature); 
			TextView highTempTV = (TextView) findViewById(R.id.highTemp); 
			TextView lowTempTV = (TextView) findViewById(R.id.lowTemp); 
			TextView humidityTV = (TextView) findViewById(R.id.humidity); 
			TextView pressureTV = (TextView) findViewById(R.id.pressure); 
			TextView sunriseTV = (TextView) findViewById(R.id.sunrise); 
			TextView sunsetTV = (TextView) findViewById(R.id.sunset); 
			TextView windSpeedTV = (TextView) findViewById(R.id.windSpeed);
			
			
			//Parse the string data and add it to the TextViews
			
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			format.setTimeZone((TimeZone.getTimeZone("UTC")));
	        String formattedSunrise = format.format(new Date(sunrise * 1000));
	        String formattedSunset = format.format(new Date(sunset * 1000));
	        
	        String tempMetric = "\u00b0";
	        String windMetric = "";
	        
			if(metricSelected) {
				tempMetric = tempMetric + "C";
				windMetric = " m/sec";
			}
			else {
				tempMetric = tempMetric + "F";
				windMetric = " MPH";
			}
				        
			locationTV.setText(location);
			forecastTV.setText(forecast);
			temperatureTV.setText("Temperature: " + Math.round(temp) + tempMetric);
			highTempTV.setText("High: " + Math.round(highTemp) + tempMetric);
			lowTempTV.setText("Low: "  + Math.round(lowTemp) + tempMetric);
			humidityTV.setText("Humidity: " + humidity + "%");
			pressureTV.setText("Pressure: " + pressure + " hPa");
			sunriseTV.setText("Sunrise: " + formattedSunrise);
			sunsetTV.setText("Sunset: " + formattedSunset);
			windSpeedTV.setText("Wind Speed: " + windSpeed + windMetric);
			
			
			Log.i(TAG,  "ICON: " + weatherIconName);
			
			ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
			int imageId = getResources().getIdentifier("icon" + weatherIconName, "drawable", getPackageName());
			Log.i(TAG, "IMAGE ID: " + imageId);
			Drawable image = getResources().getDrawable(imageId);
			weatherIcon.setBackground(image);
						
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
