package com.ZZH.travelplanner;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment used to display and convert different distances from metric to imperial
 * and vice versa.
 * 
 * @author Christoffer Baur
 *
 */
public class DVWDistanceFragment extends Fragment {
	
	// class variables
	private final String TAG = "DVWDistanceFragment";
	private final String BIG_UNIT = "bigUnitDistance";
	private final String MED_UNIT = "medUnitDistance";
	private final String SMALL_UNIT = "smallUnitDistance";
	private final String BIG_VALUE = "bigValueDistance";
	private final String MED_VALUE = "medValueDistance";
	private final String SMALL_VALUE = "smallValueDistance";
	private final double MILE_TO_KM = 1.60934;
	private final double FOOT_TO_METRE = 0.3048;
	private final double INCH_TO_CM = 2.54;
	private final double KM_TO_MILE = 0.621371;
	private final double METRE_TO_FOOT = 3.28084;
	private final double CM_TO_INCH = 0.393701;
	private boolean metric;
	private TextView big;
	private TextView medium;
	private TextView small;
	private TextView bigConvert;
	private TextView mediumConvert;
	private TextView smallConvert;
	private EditText bigUnit;
	private EditText mediumUnit;
	private EditText smallUnit;
	private TextView bigUnitConvert;
	private TextView mediumUnitConvert;
	private TextView smallUnitConvert;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated()");
		View view = getActivity().findViewById(R.id.dvw_content);

		// get references to the distance labels
		big = (TextView) view.findViewById(R.id.bigText);
		medium = (TextView) view.findViewById(R.id.mediumText);
		small = (TextView) view.findViewById(R.id.smallText);
		bigConvert = (TextView) view.findViewById(R.id.bigTextConvert);
		mediumConvert = (TextView) view.findViewById(R.id.mediumTextConvert);
		smallConvert = (TextView) view.findViewById(R.id.smallTextConvert);
		// get references to the distance values
		bigUnit = (EditText) view.findViewById(R.id.bigUnit);
		mediumUnit = (EditText) view.findViewById(R.id.mediumUnit);
		smallUnit = (EditText) view.findViewById(R.id.smallUnit);
		bigUnitConvert = (TextView) view.findViewById(R.id.bigUnitConvert);
		mediumUnitConvert = (TextView) view.findViewById(R.id.mediumUnitConvert);
		smallUnitConvert = (TextView) view.findViewById(R.id.smallUnitConvert);
		
		SharedPreferences pref = getActivity().getPreferences(0);
		// set shared preferences values
		if(pref != null){
			bigUnit.setText(pref.getString(BIG_UNIT, ""));
			mediumUnit.setText(pref.getString(MED_UNIT, ""));
			smallUnit.setText(pref.getString(SMALL_UNIT, ""));
			bigUnitConvert.setText(pref.getString(BIG_VALUE, ""));
			mediumUnitConvert.setText(pref.getString(MED_VALUE, ""));
			smallUnitConvert.setText(pref.getString(SMALL_VALUE, ""));
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

	// converts the distances
	private void convert(View view) {
		double big = 0;
		double med = 0;
		double small = 0;
		DecimalFormat df = new DecimalFormat("#0.00");
		String b = bigUnit.getText().toString();
		String m = mediumUnit.getText().toString();
		String s = smallUnit.getText().toString();
		// assign the doubles zero if there is no value entered from the user
		b = b.isEmpty()?"0":b;
		m = m.isEmpty()?"0":m;
		s = s.isEmpty()?"0":s;
		// convert them to doubles
		try {
			big = Double.parseDouble(b);
			med = Double.parseDouble(m);
			small = Double.parseDouble(s);
		} catch (NumberFormatException nfe) {
			Log.e(TAG, "Error One of the values provided is not a valid double");
			Toast.makeText(getActivity(), "Please use valid numbers", Toast.LENGTH_SHORT).show();;
		}
		// different calculation based on preferred conversion
		if (metric) {
			bigUnitConvert.setText(df.format(big * MILE_TO_KM));
			mediumUnitConvert.setText(df.format(med * FOOT_TO_METRE));
			smallUnitConvert.setText(df.format(small * INCH_TO_CM));
		} else {
			bigUnitConvert.setText(df.format(big * KM_TO_MILE));
			mediumUnitConvert.setText(df.format(med * METRE_TO_FOOT));
			smallUnitConvert.setText(df.format(small * CM_TO_INCH));
		}
		Log.i(TAG, "Converted Units");
	}

	// show labels according to the desired metric to convert to
	private void showLabels() {
		if (metric) {
			big.setText(R.string.mile);
			medium.setText(R.string.foot);
			small.setText(R.string.inch);
			bigConvert.setText(R.string.kilometre);
			mediumConvert.setText(R.string.metre);
			smallConvert.setText(R.string.centimetre);
		} else {
			big.setText(R.string.kilometre);
			medium.setText(R.string.metre);
			small.setText(R.string.centimetre);
			bigConvert.setText(R.string.mile);
			mediumConvert.setText(R.string.foot);
			smallConvert.setText(R.string.inch);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		Log.i(TAG, "onCreateView()");
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_dvwdistance, container, false);
		if(container != null){
			container.removeAllViews();
			Log.i(TAG, "Removed views");
		}
		if(this.getArguments() != null){
			metric = this.getArguments().getBoolean("metric");
			Log.i(TAG, "got metric");
		}
		return view;
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
		editor.putString(BIG_UNIT, bigUnit.getText().toString());
		editor.putString(MED_UNIT, mediumUnit.getText().toString());
		editor.putString(SMALL_UNIT, smallUnit.getText().toString());
		editor.putString(BIG_VALUE, bigUnitConvert.getText().toString());
		editor.putString(MED_VALUE, mediumUnitConvert.getText().toString());
		editor.putString(SMALL_VALUE, smallUnitConvert.getText().toString());
		Log.i(TAG, "Saved Preferences");
		// Commit the edits!
		editor.commit();
		
	}
}
