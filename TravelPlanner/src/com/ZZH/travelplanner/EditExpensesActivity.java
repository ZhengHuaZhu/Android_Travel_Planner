package com.ZZH.travelplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditExpensesActivity extends Activity {
    private DBHelper dbh;
	private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private TextView tv;
    private EditText et9, et10, et11, et12, et13, et14, et15;
    private long budgetId = -1L;
    private Button btn, btn2;
    private long actualId = -1L;
    private Context context = this;
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expenses);
	
		Intent i =  getIntent();
		budgetId = i.getExtras().getLong("budgetid", -1L);
		dbh = DBHelper.getDBHelper(this);
		Cursor c = dbh.getBudgetById(budgetId);
		boolean notEmpty = c.moveToFirst();
		if(notEmpty){
			Log.i("EDIT", "found budget by id");
			et1 = (EditText)findViewById(R.id.budgetDateArrivedValue);
		    et2 = (EditText)findViewById(R.id.budgetDateDepartValue);
		    et3 = (EditText)findViewById(R.id.budgetAmountValue);
		    et4 = (EditText)findViewById(R.id.budgetDescriptionValue);
		    et5 = (EditText)findViewById(R.id.budgetCategoryValue);
		    et6 = (EditText)findViewById(R.id.budgetSupplierValue);
		    et7 = (EditText)findViewById(R.id.budgetAddressValue);
		    tv = (TextView)findViewById(R.id.chosenlocation);
		    et1.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE)));
		    et2.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_DATE_DEPART)));
		    et3.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_AMOUNT)));
		    et4.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_DESCRIPTION)));
		    et5.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_CATEGORY)));
		    et6.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_SUPPLIER_NAME)));
		    et7.setText(c.getString(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_ADDRESS)));
		    long locationid=c.getLong(c.getColumnIndex(DBHelper.COLUMN_BUDGETED_EXPENSE_LOCATION_ID));
		    c.close();
		
		    Cursor cur=dbh.getLocationById(locationid);
		    cur.moveToFirst();
		    tv.setText(cur.getString(cur.getColumnIndex("name")));	
		    cur.close();
		    
			Cursor cs=dbh.getActualByBudgetId(budgetId);
			
			if(cs.moveToFirst()){
				Log.i("EDIT", "found actual by id");
				btn=(Button) findViewById(R.id.saveactualbtn);
				btn2=(Button) findViewById(R.id.deleteactualbtn);
				btn2.setVisibility(View.VISIBLE);
				btn.setText("Update Actual Expenses");
				
				Log.i("EDIT", "Found actual by budget id");
				actualId=cs.getLong(cs.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_ID));
				et15 = (EditText)findViewById(R.id.actualDateArrivedValue);
			    et9 = (EditText)findViewById(R.id.actualDateDepartValue);
			    et10 = (EditText)findViewById(R.id.actualAmountValue);
			    et11 = (EditText)findViewById(R.id.actualDescriptionValue);
			    et12 = (EditText)findViewById(R.id.actualCategoryValue);
			    et13 = (EditText)findViewById(R.id.actualSupplierValue);
			    et14 = (EditText)findViewById(R.id.actualAddressValue);
			 
			    et15.setText(cs.getString(cs.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_DATE_ARRIVE)));
			    et9.setText(cs.getString(cs.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_DATE_DEPART)));
			    et10.setText(cs.getString(cs.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_AMOUNT)));
			    et11.setText(cs.getString(cs.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_DESCRIPTION)));
			    et12.setText(cs.getString(cs.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_CATEGORY)));
			    et13.setText(cs.getString(cs.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_SUPPLIER_NAME)));
			    et14.setText(cs.getString(cs.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_ADDRESS)));
			    
			    Log.i("EDIT", "set all fields");
				
				btn.setOnClickListener(new View.OnClickListener() {
		              public void onClick(View v) 
		              {
		            	  EditText et15 = (EditText)findViewById(R.id.actualDateArrivedValue);
		                  EditText et9 = (EditText)findViewById(R.id.actualDateDepartValue);
		                  EditText et10 = (EditText)findViewById(R.id.actualAmountValue);
		                  EditText et11 = (EditText)findViewById(R.id.actualDescriptionValue);
		                  EditText et12 = (EditText)findViewById(R.id.actualCategoryValue);
		                  EditText et13 = (EditText)findViewById(R.id.actualSupplierValue);
		                  EditText et14 = (EditText)findViewById(R.id.actualAddressValue);
		                  
		                  if( et15.getText().toString().length() == 0 )
		                      et15.setError( "Arrive date is required!" );
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
		          		    long rowId3 = dbh.updateActual(actualId, et15.getText().toString(),
		          		    			et9.getText().toString(), Double.parseDouble(et10.getText().toString()), et11.getText().toString(),
		          		    			et12.getText().toString(), et13.getText().toString(), et14.getText().toString());
		          		    Toast.makeText(context, "Actual has been updated, its id is: " + rowId3, Toast.LENGTH_LONG).show();
		          		    finish();
		                  } 
		                  
		              }
		        	}
				);
			}
			cur.close();
		}
	}
	
	public void updateBudget(View view){ 
		
	    Toast.makeText(this, "You selected budgetID: " + budgetId,
                Toast.LENGTH_LONG).show();
	    
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
		    long rowId = dbh.updateBudget(budgetId, et1.getText().toString(),
		    			et2.getText().toString(), Double.parseDouble(et3.getText().toString()), et4.getText().toString(),
		    			et5.getText().toString(), et6.getText().toString(), et7.getText().toString());
		    Toast.makeText(this, "Budget has been updated, its id is: " + rowId, Toast.LENGTH_LONG).show();
		    finish();
        } 	      
	}
	
	public void addActual(View view){		
		et15 = (EditText)findViewById(R.id.actualDateArrivedValue);
        et9 = (EditText)findViewById(R.id.actualDateDepartValue);
        et10 = (EditText)findViewById(R.id.actualAmountValue);
        et11 = (EditText)findViewById(R.id.actualDescriptionValue);
        et12 = (EditText)findViewById(R.id.actualCategoryValue);
        et13 = (EditText)findViewById(R.id.actualSupplierValue);
        et14 = (EditText)findViewById(R.id.actualAddressValue);
        
        if( et15.getText().toString().length() == 0 )
            et15.setError( "Arrive date is required!" );
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
        	long rowId2 = dbh.createActual(budgetId, et15.getText().toString(),
	    			et9.getText().toString(), Double.parseDouble(et10.getText().toString()), et11.getText().toString(),
	    			et12.getText().toString(), et13.getText().toString(), et14.getText().toString());
		    Toast.makeText(this, "New Actual has been added, its id is: " + rowId2, Toast.LENGTH_LONG).show();
		    finish();
        	}       
	}   
	

	public void deleteActual(View view) {
		AlertDialog db = new AlertDialog.Builder(this).setTitle("Delete Alert")
				.setMessage("Do you really want to Delete it?")
				.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dbh.deleteActual(actualId);
						dialog.dismiss();
						finish();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		db.show();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.edit_expenses, menu);
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
