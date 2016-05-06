package com.ZZH.travelplanner;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment used to display and convert different weights from metric to imperial
 * and vice versa.
 * 
 * @author Christoffer Baur
 *
 */
public class DVWWeightFragment extends Fragment {
	
	// class variables
	private final String TAG = "DVWWeightFragment";
	private final String MED_UNIT = "medUnitWeight";
	private final String MED_VALUE = "medValueWeight";
	private final double KILO_TO_POUND = 2.20462;
	private final double POUND_TO_KILO = 0.453592;
	private TextView medium;
	private TextView mediumConvert;
	private EditText mediumUnit;
	private TextView mediumUnitConvert;
	private boolean metric;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated()");
		View view = getActivity().findViewById(R.id.dvw_content);
		
		// get references to the weight views for labels and values
		medium = (TextView) view.findViewById(R.id.mediumText);
		mediumConvert = (TextView) view.findViewById(R.id.mediumTextConvert);
		mediumUnit = (EditText) view.findViewById(R.id.mediumUnit);
		mediumUnitConvert = (TextView) view.findViewById(R.id.mediumUnitConvert);
		
		SharedPreferences pref = getActivity().getPreferences(0);
		// set shared preferences values
		if(pref != null){
			mediumUnit.setText(pref.getString(MED_UNIT, ""));
			mediumUnitConvert.setText(pref.getString(MED_VALUE, ""));
		}
		
		Button button = (Button) view.findViewById(R.id.convert);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				convert(view);
			}
		});
		// show the appropriate labels
		showLabels();
	}
	
	// converts the weight
	private void convert(View view) {
		double med = 0;
		DecimalFormat df = new DecimalFormat("#0.00");
		String m = mediumUnit.getText().toString();
		// assign the double zero if there is no value entered from the user
		m = m.isEmpty()?"0":m;
		// convert it to doubles
		try {
			med = Double.parseDouble(m);
		} catch (NumberFormatException nfe) {
			Log.e(TAG, "Error One of the values provided is not a valid double");
			Toast.makeText(getActivity(), "Please use valid numbers", Toast.LENGTH_SHORT).show();;
		}
		// different calculation based on preferred conversion
		if (metric) {
			mediumUnitConvert.setText(df.format(med * POUND_TO_KILO));
		} else {
			mediumUnitConvert.setText(df.format(med * KILO_TO_POUND));
		}
		Log.i(TAG, "Converted Units");
	}
	
	// show labels according to the desired metric to convert to
	private void showLabels() {
		if (metric) {
			medium.setText(R.string.pound);
			mediumConvert.setText(R.string.kilogram);
		} else {
			medium.setText(R.string.kilogram);
			mediumConvert.setText(R.string.pound);
		}
		Log.i(TAG, "showLabels()");
		convert(null);
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "FRAGMENT onAttach()");
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "FRAGMENT onCreate()");
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView() WEIGHT");
		super.onCreateView(inflater, container, savedInstanceState);
		if(container != null){
			container.removeAllViews();
			Log.i(TAG, "Removed views");
		}
		if(this.getArguments() != null){
			metric = this.getArguments().getBoolean("metric");
			Log.i(TAG, "got metric");
		}
        return inflater.inflate(R.layout.fragment_dvwweight, container, false);
    }
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "FRAGMENT onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		Log.d(TAG, "FRAGMENT onDetach()");
		super.onDetach();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "FRAGMENT onPause()");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "FRAGMENT onResume()");
		super.onResume();
	}

	@Override
	public void onStart() {
		Log.d(TAG, "FRAGMENT onStart()");
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "FRAGMENT onStop()");
		super.onStop();
		
		SharedPreferences settings = getActivity().getPreferences(0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(MED_UNIT, mediumUnit.getText().toString());
	    editor.putString(MED_VALUE, mediumUnitConvert.getText().toString());
	    Log.i(TAG, "Saved Preferences");
	    // Commit the edits!
	    editor.commit();
	}
}
