package com.ZZH.travelplanner;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Manage trip activity
 * @author Andrew
 *
 */
public class ManageTripActivity extends Activity {
	private final String TAG = "ManageTripActivity";
	private DBHelper dbh;
	public Cursor cursor;
	public ListView lv;
	public TextView tv;
	public SimpleCursorAdapter sca;
	private Intent i;
	public String  url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "handling onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_trip);
		String name = getIntent().getExtras().getString("username");
		String password = getIntent().getExtras().getString("password");

		url = "https://travalplanner-chrisbaur.rhcloud.com/api/sync?email=" + name + "&password=" + password;
		//url = "https://travalplanner-chrisbaur.rhcloud.com/api/sync?email=test@gmail.com&password=123456";
		
		//get DBHelper
		dbh = DBHelper.getDBHelper(this);
		
		//get views
		lv=(ListView) findViewById(R.id.triplist);
		tv=(TextView) findViewById(R.id.textview1);
		
		//get cursor of trips from database (SQlite)
		cursor = dbh.getAllTrips();
		
		// binding data from this column
		String[] from = {DBHelper.COLUMN_TRIPS_NAME};
		
		// to this TextView id 
		int[] to = { R.id.textview1 };
		sca = new SimpleCursorAdapter(this, R.layout.custom_list, cursor, from, to, 0);
		// set an Adapter to render results by a list view
		lv.setAdapter(sca);
		
		i= new Intent(this, TripActivity.class);			
		
		//launch new activity if trip is clicked
		lv.setOnItemClickListener(new OnItemClickListener() { 
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				i.putExtra("id", id);
				startActivity(i);
			} 
		}); 				
	}

	/**
	 * Method to run on click of the sync button
	 * checks for wifi connection then syncs the local database from 
	 * the remote
	 * 
	 * @param view
	 */
	public void syncToLocal(View view){
		//String data = "[{'t_id':2,'t_name':'Tests first trip','t_description':'seoigjoszdrgjrsg','t_created_at':'2015-12-09 19:14:52','t_updated_at':'2015-12-09 19:14:52','t_closed_at':'0000-00-00 00:00:00','b_id':1,'b_location_id':1,'b_trip_id':2,'b_amount':100,'b_description':'Budget one for location 1','b_category':'fun','b_supplier':'myself','b_address':'CA','b_created_at':'2015-12-09 19:14:52','b_updated_at':'2015-12-09 19:14:52','b_planned_arrive':'0000-00-00 00:00:00','b_planned_depart':'0000-00-00 00:00:00','a_id':null,'a_budgeted_id':null,'a_amount':null,'a_description':null,'a_category':null,'a_supplier':null,'a_address':null,'a_created_at':null,'a_updated_at':null,'a_actual_arrive':null,'a_actual_depart':null,'l_id':1,'l_name':'Montreal','l_description':'Going back to montreal','l_city':'Montreal','l_province':'Quebec','l_country_code':'CA'},{'t_id':3,'t_name':'Tests second trip','t_description':'qwetyywerweyw','t_created_at':'2015-12-09 19:14:52','t_updated_at':'2015-12-09 19:14:52','t_closed_at':'0000-00-00 00:00:00','b_id':2,'b_location_id':1,'b_trip_id':3,'b_amount':200,'b_description':'Budget two for location 1','b_category':'too fun','b_supplier':'herself','b_address':'FR','b_created_at':'2015-12-09 19:14:52','b_updated_at':'2015-12-09 19:14:52','b_planned_arrive':'0000-00-00 00:00:00','b_planned_depart':'0000-00-00 00:00:00','a_id':null,'a_budgeted_id':null,'a_amount':null,'a_description':null,'a_category':null,'a_supplier':null,'a_address':null,'a_created_at':null,'a_updated_at':null,'a_actual_arrive':null,'a_actual_depart':null,'l_id':1,'l_name':'Montreal','l_description':'Going back to montreal','l_city':'Montreal','l_province':'Quebec','l_country_code':'CA'},{'t_id':3,'t_name':'Tests second trip','t_description':'qwetyywerweyw','t_created_at':'2015-12-09 19:14:52','t_updated_at':'2015-12-09 19:14:52','t_closed_at':'0000-00-00 00:00:00','b_id':3,'b_location_id':2,'b_trip_id':3,'b_amount':300,'b_description':'Budget one for location 2','b_category':'three fun','b_supplier':'theyself','b_address':'US','b_created_at':'2015-12-09 19:14:52','b_updated_at':'2015-12-09 19:14:52','b_planned_arrive':'0000-00-00 00:00:00','b_planned_depart':'0000-00-00 00:00:00','a_id':null,'a_budgeted_id':null,'a_amount':null,'a_description':null,'a_category':null,'a_supplier':null,'a_address':null,'a_created_at':null,'a_updated_at':null,'a_actual_arrive':null,'a_actual_depart':null,'l_id':2,'l_name':'Paris','l_description':'Going to paris for the first time','l_city':'Paris','l_province':'France','l_country_code':'FR'}]";		
		
		//Check for network connection
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		
		//if connection is available
		if (networkInfo != null && networkInfo.isConnected()) {
			Toast.makeText(this, "Network connection detected. Synching to from server...", Toast.LENGTH_LONG).show();
			//invoke the AsyncTask to do the dirty work.
			new SyncFromServer().execute(url);
			
		} else {
			//jsonResult.setText("No network connection available.");
			 Toast.makeText(this, "Error: No network connection available.", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * An async task the updates the database from the remote database
	 * 
	 * @author Andrew
	 *
	 */
	private class SyncFromServer extends AsyncTask<String, Void, String>{
		//private static final int MAX_READ = 100;

		
		// onPostExecute displays the results of the AsyncTask.
		// runs in calling thread (in UI thread)
		
		@Override
		protected void onPostExecute(String result) {
			//calls private method
			updateDatabase(result);
			//Requery to update trip list
			cursor = dbh.getAllTrips();
			sca.changeCursor(cursor);
			sca.notifyDataSetChanged();
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			InputStream is = null;
			int response;
			URL url;
			
			
			HttpURLConnection conn = null;
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

				//conn.setRequestProperty("Content-Type", 
						//"application/json; charset=UTF-8");
				// set length of POST data to send
				//conn.addRequestProperty("Content-Length", bytesLeng.toString());
				// specifies whether this connection allows receiving data
				conn.setDoInput(true);
				//conn.setDoOutput(true);
				// Starts the query
				conn.connect();
				
				//get response code
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
				
				//reads input stream and returns the string of data
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
                
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
					} 
					catch (IOException ignore) {
					}
					if (conn != null){
							try {
								conn.disconnect();
							} 
							catch (IllegalStateException ignore ) {
						}
					}
				}
			}
			return null;
		}// end do in background
		
		/**
		 * updates the database by reading json data
		 * sent from the remote server
		 * 
		 * @param data the data as a string
		 */
		private void updateDatabase(String data){
			//get ddbhelper
			DBHelper dbh = DBHelper.getDBHelper(getApplicationContext());
			Log.i(TAG, data);
			
			try {
				JSONObject json;
				//get json array from data
				JSONArray arr = new JSONArray(data);
				//check array length
				int length = arr.length();
				
			    //loop to process each data in array
			    for(int i = 0; i < length; i++){
			    	//get json object from array in specified index
			    	json = arr.getJSONObject(i);
			    	
			    	//trip values returned
					long tripIdArr = json.getLong("t_id");
					Log.i("TRIPID FROM WEB", Long.toString(tripIdArr));
					String tripNameArr =  json.getString("t_name");
					String tripDescriptionArr =  json.getString("t_description");
					String tripCreateArr = json.getString("t_created_at");
					tripCreateArr = tripCreateArr.substring(0, 10);	
					String tripUpdateArr = json.getString("t_updated_at");
					tripUpdateArr = tripUpdateArr.substring(0, 10);
					String tripDeleteArr = json.getString("t_closed_at");
					tripDeleteArr = tripDeleteArr.substring(0, 10);
					
					//location values returned
					String locationNameArr = json.getString("l_name");
					String locationDescriptionArr = json.getString("l_description");
					String locationCountryCodeArr = json.getString("l_country_code");
					String locationCityArr = json.getString("l_city");
					
					//budget values returned
					String budgetArriveArr = json.getString("b_planned_arrive");
					budgetArriveArr = budgetArriveArr.substring(0, 10);
					String budgetDepartArr = json.getString("b_planned_depart");
					budgetDepartArr = budgetDepartArr.substring(0, 10);
					Double budgetAmountArr = json.getDouble("b_amount");
					String budgetDescriptionArr = json.getString("b_description");
					String budgetCategoryArr = json.getString("b_category");
					String budgetSupplierArr = json.getString("b_supplier");
					String budgetAddressArr = json.getString("b_address");
					
					//actual values returned
					String actualArriveArr = json.getString("a_actual_arrive");
					String actualDepartArr = json.getString("a_actual_depart");
					Double actualAmountArr = 0.0;
					
					// check if there is an actual returned
					if(actualArriveArr != "null"){
							actualArriveArr = json.getString("a_actual_arrive");
							actualArriveArr = actualArriveArr.substring(0, 10);
							actualDepartArr = actualDepartArr.substring(0, 10);
							actualAmountArr = json.getDouble("a_amount");
					}
					String actualDescriptionArr = json.getString("a_description");
					String actualCategoryArr = json.getString("a_category");
					String actualSupplierArr = json.getString("a_supplier");
					String actualAddressArr = json.getString("a_address");
					
					//gets trip from web id
					Cursor c = dbh.getTripFromWebId(tripIdArr);
					long lastTrip;
					long lastLocation;
					long lastBudget;
					
					//if trip exists
					if(c.moveToLast()){
						//update trip
						lastTrip = c.getLong(c.getColumnIndex(DBHelper.COLUMN_TRIPS_ID));
						dbh.updateTrip(lastTrip, tripIdArr, tripNameArr, tripDescriptionArr, tripCreateArr, tripUpdateArr, tripDeleteArr);
					}
					//else create trip
					else{
						lastTrip = dbh.createTrip(tripIdArr, tripNameArr, tripDescriptionArr, tripCreateArr, tripUpdateArr, tripDeleteArr);
						Log.i("CREATE TRIPID", Long.toString(lastTrip));
					}
					c.close();
					
					//get location by name
					c = dbh.getLocationByName(locationNameArr);
					
					//if location exists
					if(c.moveToLast()){
						//update location
						lastLocation = c.getLong(c.getColumnIndex(DBHelper.COLUMN_LOCATIONS_ID));
						dbh.update(lastLocation, locationNameArr, locationDescriptionArr, locationCityArr, locationCountryCodeArr);
					}
					//else create location
					else{
						lastLocation = dbh.createLocation(locationNameArr, locationDescriptionArr, locationCityArr, locationCountryCodeArr);
					}
					c.close();
					
					//create a budget from last trip created/updated and last location created/updated
					lastBudget = dbh.createBudget(lastTrip, lastLocation, budgetArriveArr, budgetDepartArr, budgetAmountArr, budgetDescriptionArr, budgetCategoryArr, budgetSupplierArr, budgetAddressArr);
					
					//create an actual from last budget created if there is an actual returned
					if(actualArriveArr != "null"){
								dbh.createActual(lastBudget, actualArriveArr, actualDepartArr, actualAmountArr, actualDescriptionArr, actualCategoryArr, actualSupplierArr, actualAddressArr);

					}
					
			    }								
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
