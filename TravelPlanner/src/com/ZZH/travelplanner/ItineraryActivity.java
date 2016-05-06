package com.ZZH.travelplanner;

import com.ZZH.travelplanner.ItineraryListFragment.Communicatable;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
/**
 * Activity which holds The list and Details Fragments
 * Also acts as a communicator between both fragments
 * 
 * @author Andrew
 *
 */
public class ItineraryActivity extends Activity implements Communicatable {
	
	private long selectedId;
	private long tripId = 1;
	private ItineraryListFragment ilf;
	private ItineraryDetailsFragment idf;
	private TextView tv;
	private String date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary);
		//Get shared preferences for trip id, defaults to the first trip in the database
		SharedPreferences fs = getSharedPreferences("LASTCHECKEDTRIPID", MODE_PRIVATE);
		tripId = fs.getLong("LAST_TRIPID", 1);
		
		//get list Fragment and details fragment
		ilf = (ItineraryListFragment)getFragmentManager().findFragmentById(R.id.fragment2);
		idf = (ItineraryDetailsFragment)getFragmentManager().findFragmentById(R.id.fragment1);
		
		//get the title text view
		tv = (TextView) findViewById(R.id.selectedBudget);
		
		//sets the class variable in the list fragment
		ilf.setTripId(tripId);
		
		//get extras if they exist
		getExtrasFromIntent();
		
		//if there is a date the set the class variable of the list fragment
		//to that date
		if(date != null){
			ilf.setDate(date);
		}
		
		//to retrieve selected budget in case activity is stopped
		if(savedInstanceState != null){
			selectedId = savedInstanceState.getLong("SelectedId");
			idf.setViewValues(selectedId);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    savedInstanceState.putLong("SelectedId", selectedId);    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// create a SharedPreferences object across the application
		// mostly used for weather checking to know which location to get weather from
		SharedPreferences lastcheckedtripId = getSharedPreferences("LASTCHECKEDBUDGETID", MODE_PRIVATE);
		SharedPreferences.Editor editor = lastcheckedtripId.edit();
		editor.putLong("LAST_BUDGETID", selectedId);
		editor.commit();
	}

	@Override
	public void sendId(long id) {
		this.selectedId = id;
		tv.setText("Budgets");
		idf.setViewValues(id);
	}
	
	/**
	 * retrieves the date if this activity
	 * was launched by the today activity
	 */
	private void getExtrasFromIntent(){
		Intent i = getIntent();
		//if not intent null
		if(i != null){
			//then get extras
			Bundle extras = i.getExtras();
			//if extras no null
			if(extras != null){
				//then get date
				date = extras.getString("dateInput");
			}
		}
		
	}
	
}
