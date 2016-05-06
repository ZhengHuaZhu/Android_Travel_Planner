package com.ZZH.travelplanner;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * This takes care of providing a menu for converting different metrics such as volume, distance
 * and weight, through the use of fragmnts for each.
 * 
 * @author Christoffer Baur
 */
public class DVWConversionActivity extends Activity implements FragmentManager.OnBackStackChangedListener{
	
	// class variables
	private final String TAG = "DVWConversionActivity";
	private final String DISTANCE = "distance";
	private final String VOLUME = "volume";
	private final String WEIGHT = "weight";
	private final String METRIC = "metric";
	private final String FRAGMENT = "fragment";
	private boolean oldMetric;
	private boolean newMetric;
	private String fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dvwconversion);
		Log.i(TAG, "onCreate()");
		
		// get shared preferences
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		
		// get references to different views
		Spinner spinner = (Spinner) findViewById(R.id.metrics_spinner);
		ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(this, R.array.DVW_metrics, android.R.layout.simple_spinner_item);
		// get values stored in shared preferences
		oldMetric  = preferences.getBoolean(METRIC, false);
		fragment = preferences.getString(FRAGMENT, "");
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(aa);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "spinner item selected");
				if(position == 0)
					//editor.putBoolean(METRIC, true);
					newMetric = true;
				else
					//editor.putBoolean(METRIC, false);
					newMetric = false;
				//editor.commit();
				showFragment();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.i(TAG, "No spinner item selected");
				
			}
		});
		if(oldMetric == true)
			spinner.setSelection(0);
		else
			spinner.setSelection(1);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Log.i(TAG, "onPause handled");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.i(TAG, "onResume handled");
	}
	
	// show the distance fragment
	public void showDistance(View view){
		Log.i(TAG, "handling showDistance");
		// check if user already has it displayed and that the metric is the same
		if(!(fragment.equals(DISTANCE)) || (oldMetric != newMetric) || view == null){
			Bundle bundle = startTransaction(DISTANCE);
			// get the frag. manager and assign the new frag. passing it the bundle
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			DVWDistanceFragment fragment = new DVWDistanceFragment();
			fragment.setArguments(bundle);
			Log.i(TAG, "Intent has extras for fragment class");
			ft.replace(R.id.dvw_content, fragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			Log.i(TAG, "Distance fragment being commited");
			ft.commit();
		}
		else
			Log.i(TAG, "Distance fragment is already displayed");
		
	}
	
	// show the volume fragment
	public void showVolume(View view){
		Log.i(TAG, "handling showVolume");
		// check if user already has it displayed and that the metric is the same
		if(!(fragment.equals(VOLUME)) || (oldMetric != newMetric) || view == null){
			Bundle bundle = startTransaction(VOLUME);
			// get the frag. manager and assign the new frag. passing it the bundle
			DVWVolumeFragment fragment = new DVWVolumeFragment();
			fragment.setArguments(bundle);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.dvw_content, fragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			Log.i(TAG, "Volume fragment being commited");
			ft.commit();
		}
		else
			Log.i(TAG, "Volume fragment is already displayed");
	}
	
	// show the weight fragment
	public void showWeight(View view){
		Log.i(TAG, "handling showWeight");
		// check if user already has it displayed and that the metric is the same
		if(!(fragment.equals(WEIGHT)) || (oldMetric != newMetric) || view == null){
			Bundle bundle = startTransaction(WEIGHT);
			// pass the bundle to the frag.
			DVWWeightFragment fragment = new DVWWeightFragment();
			fragment.setArguments(bundle);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.dvw_content, fragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			Log.i(TAG, "Weight fragment being commited");
			ft.commit();
		}
		else
			Log.i(TAG, "Weight fragment is already displayed");
	}

	// called by the on item selected for the spinner
	private void showFragment(){
		Log.i(TAG, "handling showFragment()");
		if(fragment.equals(DISTANCE))
			showDistance(null);
		else if(fragment.equals(VOLUME))
			showVolume(null);
		else if(fragment.equals(WEIGHT))
			showWeight(null);
		else
			Log.i(TAG, "Empty fragment preference");
	}
	
	@Override
	public void onBackStackChanged() {
		Log.i(TAG, "handling onBackStackChanged");
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		Log.i(TAG, "onStop()");
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(METRIC, oldMetric);
		editor.putString(FRAGMENT, fragment);
		Log.i(TAG, "Saved Preferences");
		// Commit the edits!
		editor.commit();
    }
	
	// sets up the bundle to be sent to the fragments, and assigns values 
	private Bundle startTransaction(String frag){
		// set if values to the current frag. and desired metric converions
		fragment = frag;
		oldMetric = newMetric;
		// get bundle and pass the metric to the frag.
		Bundle bundle = new Bundle();
		bundle.putBoolean(METRIC, newMetric);
		return bundle; 
	}
}
