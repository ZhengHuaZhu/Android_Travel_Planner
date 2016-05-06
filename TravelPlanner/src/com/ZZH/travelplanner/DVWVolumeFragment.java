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
 * Fragment used to display and convert different volumes from metric to imperial
 * and vice versa.
 * 
 * @author Christoffer Baur
 *
 */
public class DVWVolumeFragment extends Fragment {
	
	// class variables
	private final String TAG = "DVWVolumeFragment";
	private final String BIG_UNIT = "bigUnitVolume";
	private final String SMALL_UNIT = "smallUnitVolume";
	private final String BIG_VALUE = "bigValueVolume";
	private final String SMALL_VALUE = "smallValueVolume";
	private final double LITRE_TO_GAL = 0.264172;
	private final double ML_TO_OUNCE = 0.033814;
	private final double GAL_TO_LITRE = 3.78541;
	private final double OUNCE_TO_ML = 29.5735;
	private boolean metric;
	private TextView big;
	private TextView small;
	private TextView bigConvert;
	private TextView smallConvert;
	private EditText bigUnit;
	private EditText smallUnit;
	private TextView bigUnitConvert;
	private TextView smallUnitConvert;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated()");
		View view = getActivity().findViewById(R.id.dvw_content);
		
		// get references to the volume labels
		big = (TextView) view.findViewById(R.id.bigText);
		small = (TextView) view.findViewById(R.id.smallText);
		bigConvert = (TextView) view.findViewById(R.id.bigTextConvert);
		smallConvert = (TextView) view.findViewById(R.id.smallTextConvert);
		// get references to the volume values
		bigUnit = (EditText) view.findViewById(R.id.bigVolume);
		smallUnit = (EditText) view.findViewById(R.id.smallVolume);
		bigUnitConvert = (TextView) view.findViewById(R.id.bigVolumeConvert);
		smallUnitConvert = (TextView) view.findViewById(R.id.smallVolumeConvert);
		
		SharedPreferences pref = getActivity().getPreferences(0);
		// set shared preferences values
		if(pref != null){
			bigUnit.setText(pref.getString(BIG_UNIT, ""));
			smallUnit.setText(pref.getString(SMALL_UNIT, ""));
			bigUnitConvert.setText(pref.getString(BIG_VALUE, ""));
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
	
	// converts the volumes
	private void convert(View view) {
		double big = 0;
		double small = 0;
		DecimalFormat df = new DecimalFormat("#0.00");
		String b = bigUnit.getText().toString();
		String s = smallUnit.getText().toString();
		// assign the doubles zero if there is no value entered from the user
		b = b.isEmpty()?"0":b;
		s = s.isEmpty()?"0":s;
		// convert them to doubles
		try {
			big = Double.parseDouble(b);
			small = Double.parseDouble(s);
		} catch (NumberFormatException nfe) {
			Log.e(TAG, "Error One of the values provided is not a valid double");
			Toast.makeText(getActivity(), "Please use valid numbers", Toast.LENGTH_SHORT).show();;
		}
		// different calculation based on preferred conversion
		if (metric) {
			bigUnitConvert.setText(df.format(big * GAL_TO_LITRE));
			smallUnitConvert.setText(df.format(small * OUNCE_TO_ML));
		} else {
			bigUnitConvert.setText(df.format(big * LITRE_TO_GAL));
			smallUnitConvert.setText(df.format(small * ML_TO_OUNCE));
		}
		Log.i(TAG, "Converted Units");
	}
	
	// show labels according to the desired metric to convert to
	private void showLabels() {
		if (metric) {
			big.setText(R.string.gallon);
			small.setText(R.string.ounce);
			bigConvert.setText(R.string.litre);
			smallConvert.setText(R.string.millilitre);
		} else {
			big.setText(R.string.litre);
			small.setText(R.string.millilitre);
			bigConvert.setText(R.string.gallon);
			smallConvert.setText(R.string.ounce);
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
		Log.d(TAG, "onCreate() Done");
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView() VOLUME");
		super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
		if(container != null){
			Log.i(TAG, "Going to remove views");
			container.removeAllViews();
			Log.i(TAG, "Removed views");
		}
		Log.i(TAG, "getting metric");
		if(this.getArguments() != null){
			metric = this.getArguments().getBoolean("metric");
			Log.i(TAG, "got metric");
		}
        return inflater.inflate(R.layout.fragment_dvwvolume, container, false);
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
	      editor.putString(SMALL_UNIT, smallUnit.getText().toString());
	      editor.putString(BIG_VALUE, bigUnitConvert.getText().toString());
	      editor.putString(SMALL_VALUE, smallUnitConvert.getText().toString());
	      Log.i(TAG, "Saved Preferences");
	      // Commit the edits!
	      editor.commit();
	}
}