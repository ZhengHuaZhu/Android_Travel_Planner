package com.ZZH.travelplanner;

import android.os.Bundle;
import android.util.Log;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Fragment compatible with the today and curren trip itinerary.
 * Shows details of selected budgets
 * 
 * @author Andrew
 *
 */
public class ItineraryDetailsFragment extends Fragment {
	//Location TextViews
	private static TextView locationNameValueText;
	private static TextView locationDescriptionValueText; 
	private static TextView locationCityValueText; 
	private static TextView locationCountryCodeValueText;
	
	//Budget TextViews
	private static TextView budgetDateArrivedValueText;
	private static TextView budgetDateDepartValueText;
	private static TextView budgetAmountValueText;
	private static TextView budgetDescriptionValueText;
	private static TextView budgetCategoryValueText;
	private static TextView budgetSupplierValueText;
	private static TextView budgetAddressValueText;
	
	//Actual Expenses TextViews
	private static TextView actualDateArrivedValueText;
	private static TextView actualDateDepartValueText;
	private static TextView actualAmountValueText;
	private static TextView actualDescriptionValueText;
	private static TextView actualCategoryValueText;
	private static TextView actualSupplierValueText;
	private static TextView actualAddressValueText;
	
	private static DBHelper dbh;
	private View view;
	
	/*
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.itinerary_details, container, false);
		dbh = DBHelper.getDBHelper(getActivity());
		getViewReferences();
		
		return view;
	}*/
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
	    final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.detailsTextViewStyle);

	    // clone the inflater using the ContextThemeWrapper
	    LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
		
		view = localInflater.inflate(R.layout.itinerary_details, container, false);
		dbh = DBHelper.getDBHelper(getActivity());
		//get references to the textviews
		getViewReferences();
			
		return view;
	}
	/*
	public static ItineraryDetailsFragment newInstance(int index, int someId) {
		
		ItineraryDetailsFragment fragment = new ItineraryDetailsFragment();
		Bundle args = new Bundle();
		args.putInt("budgetId", index);
		fragment.setArguments(args);
		
		return fragment;
	}*/
	/**
	 * Sets the text values from the results retrieved in the Database
	 * @param id
	 */
	public void setViewValues(long id){
		Cursor c = dbh.getItineraryDetails(id);
		Log.i("DETAILSFRAGMENT", "after retrieve cursor");
		if(c.moveToLast()){
			Log.i("DETAILSFRAGMENT", Integer.toString(c.getCount()));
			Log.i("DETAILSFRAGMENT", "after retrieve move to last");
			//locations table
			locationNameValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_LOCATIONS_NAME)));
			locationDescriptionValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_LOCATIONS_DESCRIPTION)));
			locationCityValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_LOCATIONS_CITY)));
			locationCountryCodeValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_LOCATIONS_COUNTRY_CODE)));
			
			//Budget TextViews
			budgetDateArrivedValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE)));
			budgetDateDepartValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_DATE_DEPART)));
			budgetAmountValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_AMOUNT)));
			budgetDescriptionValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_DESCRIPTION)));
			budgetCategoryValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_CATEGORY)));
			budgetSupplierValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_SUPPLIER_NAME)));
			budgetAddressValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_ADDRESS)));
			
			//Actual Expenses TextViews
			//Check if column is returned
			if(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_BUDGETED_ID) != -1){
				actualDateArrivedValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_DATE_ARRIVE)));
				actualDateDepartValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_DATE_DEPART)));
				actualAmountValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_AMOUNT)));
				actualDescriptionValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_DESCRIPTION)));
				actualCategoryValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_CATEGORY)));
				actualSupplierValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_SUPPLIER_NAME)));
				actualAddressValueText.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_ADDRESS)));
			}
			//else set to unavailable
			else{
				actualDateArrivedValueText.setText(R.string.notAvailable);
				actualDateDepartValueText.setText(R.string.notAvailable);
				actualAmountValueText.setText(R.string.notAvailable);
				actualDescriptionValueText.setText(R.string.notAvailable);
				actualCategoryValueText.setText(R.string.notAvailable);
				actualSupplierValueText.setText(R.string.notAvailable);
				actualAddressValueText.setText(R.string.notAvailable);
			}
			
		}
		c.close();
	}
	
	private void getViewReferences(){
		//Locations Table
		locationNameValueText = (TextView) view.findViewById(R.id.locationNameValue);
		locationDescriptionValueText = (TextView) view.findViewById(R.id.locationDescriptionValue);
		locationCityValueText = (TextView) view.findViewById(R.id.locationCityValue);
		locationCountryCodeValueText = (TextView) view.findViewById(R.id.locationCountryCodeValue);
		
		//Budget TextViews
		budgetDateArrivedValueText = (TextView) view.findViewById(R.id.budgetDateArrivedValue);
		budgetDateDepartValueText = (TextView) view.findViewById(R.id.budgetDateDepartValue);
		budgetAmountValueText = (TextView) view.findViewById(R.id.budgetAmountValue);
		budgetDescriptionValueText = (TextView) view.findViewById(R.id.budgetDescriptionValue);
		budgetCategoryValueText = (TextView) view.findViewById(R.id.budgetCategoryValue);
		budgetSupplierValueText = (TextView) view.findViewById(R.id.budgetSupplierValue);
		budgetAddressValueText = (TextView) view.findViewById(R.id.budgetAddressValue);
		
		//Actual Expenses TextViews
		actualDateArrivedValueText = (TextView) view.findViewById(R.id.actualDateArrivedValue);
		actualDateDepartValueText = (TextView) view.findViewById(R.id.actualDateDepartValue);
		actualAmountValueText = (TextView) view.findViewById(R.id.actualAmountValue);
		actualDescriptionValueText = (TextView) view.findViewById(R.id.actualDescriptionValue);
		actualCategoryValueText = (TextView) view.findViewById(R.id.actualCategoryValue);
		actualSupplierValueText = (TextView) view.findViewById(R.id.actualSupplierValue);
		actualAddressValueText = (TextView) view.findViewById(R.id.actualAddressValue);
		
	}
	
}
	
