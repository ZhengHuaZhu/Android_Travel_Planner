package com.ZZH.travelplanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

/**
 * This takes care of converting different currencies using a web API.
 * www.fixer.io
 * It fetches this data using an AsyncTask inner class. 
 * 
 * @author Christoffer Baur
 */
public class CurrencyConversionActivity extends Activity {
	
	// class variables
	private final String TAG = "CurrencyConversionActivity";
	private final String RATES = "rates";
	private final String ERROR = "error";
	private final String RESULT = "result";
	private static final int MAX_READ = 80;
	String[] currencies;
	Spinner currencyFrom;
	Spinner currencyTo;
	EditText resultFrom;
	TextView resultTo;
	String from; 
	String to;
	Double fromValue;
	Double toValue = 0.0;
	final String currencyUrl = "http://api.fixer.io/latest";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currency_conversion);
		
		// get references to views and resources
		currencies = getResources().getStringArray(R.array.currencies);
		currencyFrom = (Spinner) findViewById(R.id.currency_from);
		currencyTo = (Spinner) findViewById(R.id.currency_to);
		resultFrom = (EditText) findViewById(R.id.currency_result_from);
		resultTo = (TextView) findViewById(R.id.currency_result_to);
		
		//set up the array adapter to populate the spinners in the layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.currencies, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencyFrom.setAdapter(adapter);
		currencyTo.setAdapter(adapter);
		
		// get reference to the button and assign a listener for converting
		Button button = (Button) findViewById(R.id.convert);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				convert(view);
			}
		});
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	    toValue = savedInstanceState.getDouble(RESULT);
	    DecimalFormat df = new DecimalFormat("#0.00");
	    resultTo.setText((df.format(toValue)));
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    savedInstanceState.putDouble(RESULT, toValue);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	/**
	 * Converts the currencies from the specified currency to the desired currency.
	 * 
	 * @param view
	 */
	private void convert(View view){
		// get the currencies to convert from and to.
		from = currencyFrom.getSelectedItem().toString().toUpperCase();
		to = currencyTo.getSelectedItem().toString().toUpperCase();
		// try parsing the amount entered
		try{
			fromValue = Double.parseDouble(resultFrom.getText().toString());
		}catch(NumberFormatException nfe){
			Toast.makeText(this, "Invalid Conversion number", Toast.LENGTH_SHORT).show();
		}
		List<String> list = Arrays.asList(currencies);
		
		// make sure values are from the currencies provided (paranoid security checking)
		if(list.contains(from) && list.contains(to)){
			// construct the url to use with the web API
			String url = currencyUrl + "?base=" + from + "&symbols=" + to;
			// first check to see if we can get on the network
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			int permissionCheck = ContextCompat.checkSelfPermission(this,
			        Manifest.permission.INTERNET);
			if(permissionCheck == PackageManager.PERMISSION_GRANTED){
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				// if we're connected ot the internet
				if (networkInfo != null && networkInfo.isConnected()) {
					// invoke the AsyncTask to do the I/O and retrieve the currency rate
					new DownloadPageText().execute(url);
				} else {
					 Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
				}
			}
			else
				Toast.makeText(this, "Internet Permission not granted", Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this, "bad data", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Inner class uses to perform the AsyncTask necessary for the I/O of fetching 
	 * a conversion rate from the web API
	 * 
	 * @author Christoffer Baur
	 */
	private class DownloadPageText extends AsyncTask<String, Void, String>{

		
		// onPostExecute displays the results of the AsyncTask.
		// runs in calling thread (in UI thread)
		protected void onPostExecute(String result) {
			if(!result.equals(ERROR)){
				DecimalFormat df = new DecimalFormat("#0.00");
				Double convertedValue = 0.0;
				// convert the string result fetched from the API
				try{
					convertedValue = Double.parseDouble(result);
				}catch(NumberFormatException nfe){
					Log.i(TAG, "Invalid Conversion number");
				}
				Log.i(TAG, "Setting conversion TO value");
				toValue = fromValue * convertedValue;
				// set the result of the converion to a view in the layout
				resultTo.setText(df.format(toValue));
			}
			else
				resultTo.setText("0.0");
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			InputStream is = null;
			int response;
			URL url;
			
			int len = MAX_READ;
			HttpURLConnection conn = null;
			// try getting the url from the first element in the array
			try{
				url = new URL(params[0]);
			}catch (IOException e) {
				Log.e(TAG, "exception" + Log.getStackTraceString(e));
				return "Unable to retrieve web page. URL may be invalid.";
			}
			
			try{
				// create and open the connection
				conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000); /* milliseconds */
				conn.setRequestMethod("GET");
				// specifies whether this connection allows receiving data
				conn.setDoInput(true);
				// Starts the query
				conn.connect();

				response = conn.getResponseCode();
				Log.d(TAG, "Server returned: " + response);

				/*
				 *  check the status code HTTP_OK = 200 anything else we didn't get what
				 *  we want in the data.
				 */
				if (response != HttpURLConnection.HTTP_OK){
					Log.d(TAG, "Server returned: " + response + " aborting read.");
					return "Server returned: " + response + " aborting read.";
				}

				// get the stream for the data from the website
				is = conn.getInputStream();
				// read the stream (max len bytes)
				String conversionRate = readIt(is, len);
				return conversionRate;
			} catch (IOException e) {
				Log.e(TAG, "exception" + Log.getStackTraceString(e));
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
			return null;
		}// end do in background
	}// end private class
	
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
		// read the fetched data
		reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), len);
		// get characters read
		int count = reader.read(buffer);
		// assign the buffer to a string
		String jsonString = new String(buffer);
		if(!jsonString.contains("error")){
			int end = jsonString.lastIndexOf("}") + 1;
			String newJsonString = jsonString.substring(0, end);
			String conversionRate = "";
			Log.d(TAG, "Bytes read: " + count
					+ "(-1 means end of reader so max of " + len + " ) JSON: " + jsonString + "  - NEW JSON: " + newJsonString);
			try {
				// create a jsonm object based on the json string
				JSONObject json = new JSONObject(newJsonString);
				Log.i(TAG, "made JSON object");
				// get the rate attribute
				String rate = json.getString(RATES);
				Log.i(TAG, "got Rates attribute: " + rate);
				// get actual conversion rate
				conversionRate = json.getJSONObject(RATES).getString(to);
						//new JSONObject(newJsonString).getString(to);
				Log.i(TAG, "got conversion rate: " + conversionRate);
			} catch (JSONException e) {
				Log.i(TAG, "Could not create JSONArray from the buffer or JSONObject.");
				e.printStackTrace();
			}
			if (!(conversionRate.isEmpty())){
				Log.i(TAG, "Conversion rate has value");
				return conversionRate;
			}
		}
		Log.i(TAG, "conversion rate is empty");
		return ERROR;
	}
}
