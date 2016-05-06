package com.ZZH.travelplanner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AddBudgetActivity extends Activity implements OnItemSelectedListener {

	private long lid = -1L;
	private long rowId = -1L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_budget);
		
		DBHelper dbh = DBHelper.getDBHelper(this);
		Cursor c = dbh.getAllLocations();
		Spinner s = (Spinner) findViewById(R.id.locationspinner);
		
		c.moveToFirst();
	    ArrayList<String> list = new ArrayList<String>();
	    while(!c.isAfterLast()) {
	        list.add(c.getString(c.getColumnIndex("name")));
	        c.moveToNext();
	    }
	    c.close();
	    String[] locationnames = list.toArray(new String[list.size()]);
  		
	    // Creating adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locationnames);
 
        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
  		// Apply the adapter to the spinner
  		s.setAdapter(adapter);	
  		// set select listener
  		s.setOnItemSelectedListener(this);
  		
	}
	
	@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        lid = id;
 
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + lid,
                Toast.LENGTH_LONG).show();	 
    }
	
	public void saveBudget(View view){ 
		Intent i =  getIntent();
	    long tripId = i.getExtras().getLong("tripid", -1L);
//	    Toast.makeText(this, "You selected tripID: " + tripId,
//                Toast.LENGTH_LONG).show();
	    long locationId = lid+1;
	    
        EditText et1 = (EditText)findViewById(R.id.budgetDateArrivedValue);
        EditText et2 = (EditText)findViewById(R.id.budgetDateDepartValue);
        EditText et3 = (EditText)findViewById(R.id.budgetAmountValue);
        EditText et4 = (EditText)findViewById(R.id.budgetDescriptionValue);
        EditText et5 = (EditText)findViewById(R.id.budgetCategoryValue);
        EditText et6 = (EditText)findViewById(R.id.budgetSupplierValue);
        EditText et7 = (EditText)findViewById(R.id.budgetAddressValue);
        
        if( et1.getText().toString().length() == 0 )
            et1.setError( "Arrive date is required!" );
        else if( et2.getText().toString().length() == 0 )
            et2.setError( "Depart date is required!" );
        else if( et3.getText().toString().length() == 0 || !et3.getText().toString().matches("\\d+(?:\\.\\d+)?"))
            et3.setError( "Amount is required and it has to be a number!" );
        else if( et4.getText().toString().length() == 0 )
            et4.setError( "Description is required!" );
        else if( et5.getText().toString().length() == 0 )
            et5.setError( "Category is required!" );
        else if( et6.getText().toString().length() == 0 )
            et6.setError( "Supplier is required!" );
        else if( et7.getText().toString().length() == 0 )
            et7.setError( "Address is required!" );
        else{
		    rowId = (DBHelper.getDBHelper(this)).createBudget(tripId, locationId, et1.getText().toString(),
		    			et2.getText().toString(), Double.parseDouble(et3.getText().toString()), et4.getText().toString(),
		    			et5.getText().toString(), et6.getText().toString(), et7.getText().toString());
		    Toast.makeText(this, "New Budget has been added, its id is: " + rowId, Toast.LENGTH_LONG).show();
		    finish();
        }
	}
	
	public void saveActual(View view){
		
		EditText et8 = (EditText)findViewById(R.id.actualDateArrivedValue);
        EditText et9 = (EditText)findViewById(R.id.actualDateDepartValue);
        EditText et10 = (EditText)findViewById(R.id.actualAmountValue);
        EditText et11 = (EditText)findViewById(R.id.actualDescriptionValue);
        EditText et12 = (EditText)findViewById(R.id.actualCategoryValue);
        EditText et13 = (EditText)findViewById(R.id.actualSupplierValue);
        EditText et14 = (EditText)findViewById(R.id.actualAddressValue);
        
        if( et8.getText().toString().length() == 0 )
            et8.setError( "Arrive date is required!" );
        else if( et9.getText().toString().length() == 0 )
            et9.setError( "Depart date is required!" );
        else if( et10.getText().toString().length() == 0 || !et10.getText().toString().matches("\\d+(?:\\.\\d+)?"))
            et10.setError( "Amount is required and it has to be a number!" );
        else if( et11.getText().toString().length() == 0 )
            et11.setError( "Description is required!" );
        else if( et12.getText().toString().length() == 0 )
            et12.setError( "Category is required!" );
        else if( et13.getText().toString().length() == 0 )
            et13.setError( "Supplier is required!" );
        else if( et14.getText().toString().length() == 0 )
            et14.setError( "Address is required!" );
        else{
        	if(rowId == -1L){
        		Toast.makeText(this, "Actual expense can only be added to an existing Budget.", Toast.LENGTH_LONG).show();
        	}else{
	        	long rowId2 = (DBHelper.getDBHelper(this)).createActual(rowId, et8.getText().toString(),
		    			et9.getText().toString(), Double.parseDouble(et10.getText().toString()), et11.getText().toString(),
		    			et12.getText().toString(), et13.getText().toString(), et14.getText().toString());
	       
			    Toast.makeText(this, "New Actual has been added, its id is: " + rowId2, Toast.LENGTH_LONG).show();
			    finish();
        	}
        }
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.add_budget, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
