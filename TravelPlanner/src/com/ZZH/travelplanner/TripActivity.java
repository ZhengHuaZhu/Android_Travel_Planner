package com.ZZH.travelplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


public class TripActivity extends Activity {

	private DBHelper dbh;
	private Intent intent;
	private long tripId;
	private Context context = this;
	private ListView lv;
	private Cursor cursor;
	private SimpleCursorAdapter sca;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip);

		dbh = DBHelper.getDBHelper(this);
		lv = (ListView) findViewById(R.id.budgetlist);
        Intent i =  getIntent();
        tripId = i.getExtras().getLong("id", -1L);
        Toast.makeText(this, "You selected: " + tripId,
                Toast.LENGTH_LONG).show();
        
        Cursor c = dbh.getTripById(tripId);
        TextView tv1=(TextView) findViewById(R.id.trip_name);
        TextView tv2=(TextView) findViewById(R.id.trip_desc);
             
        if(c.moveToNext()){
        	tv1.setText(c.getString(2));
        	tv2.setText(c.getString(3));
        }
	}
	
	public void addBudget(View view){
		Intent i=new Intent(this, AddBudgetActivity.class);
		i.putExtra("tripid",tripId);
	    startActivity(i);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Intent i =  getIntent();
        tripId = i.getExtras().getLong("id", -1L);
        
      //get cursor of trips from database (SQlite)
  		cursor = dbh.getItinerariesByTripId(tripId);
  		// binding data from this column
  		String[] from = {DBHelper.COLUMN_BUDGETED_EXPENSE_DESCRIPTION};
  		// to this TextView id 
  		int[] to = { R.id.textview1 };
  		sca = new SimpleCursorAdapter(this, R.layout.custom_list, cursor, from, to, 0);
  		// set an Adapter to render results by a list view
  		lv.setAdapter(sca);
  		
  		intent= new Intent(this, EditExpensesActivity.class);			
  		
  		lv.setOnItemClickListener(new OnItemClickListener() { 
  			public void onItemClick(AdapterView<?> parent, View view, 
  					int position, long id) {
  				intent.putExtra("budgetid", id);
  				startActivity(intent);
  			} 
  		}); 
  		
  		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, final long id) {
            	AlertDialog db = new AlertDialog.Builder(context).setTitle("Delete Alert")
        				.setMessage("Do you really want to Delete it?")
        				.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog, int whichButton) {
        						Cursor c = dbh.getActualByBudgetId(id);
        						if(c.moveToLast()){
        							dbh.deleteActual(c.getLong(c.getColumnIndex(DBHelper.COLUMN_ACTUAL_EXPENSE_ID)));
        						}
        						dbh.deleteBudget(id);
        						dialog.dismiss();
        						refreshView();
        					}
        				})
        				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog, int which) {
        						dialog.dismiss();
        					}
        				}).create();
        		db.show();		
                return true;
            }
        }); 
	}
	
	public void refreshView() {
		// renew the cursor
  		cursor = dbh.getItinerariesByTripId(tripId);
		// have the adapter use the new cursor, changeCursor closes old cursor too
		sca.changeCursor(cursor);
		// have the adapter tell the observers
		sca.notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// create a SharedPreferences object across the APP
		SharedPreferences lastcheckedtripId = getSharedPreferences("LASTCHECKEDTRIPID", MODE_PRIVATE);
		SharedPreferences.Editor editor = lastcheckedtripId.edit();
		editor.putLong("LAST_TRIPID", tripId);
		editor.commit();
		
//		to retrieve at any Activity
//		SharedPreferences lastcheckedtripId = getSharedPreferences("LASTCHECKEDTRIPID", MODE_PRIVATE);
//	    long tripId = lastcheckedtripId.getLong("LAST_TRIPID", -1L);
	}
	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.trip, menu);
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
