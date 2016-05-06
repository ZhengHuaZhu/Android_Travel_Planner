package com.ZZH.travelplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
/**
 * List fragment the holds a list of all budgets according
 * to the date entered in the today activity or the last tip
 * managed in the manage trips activity.
 * 
 * @author Andrew
 *
 */
public class ItineraryListFragment extends ListFragment {
	
	private static DBHelper dbh;
	private SimpleCursorAdapter sca; 
	private Cursor cursor;
	private View  view;
	private long tripId;
	private String date;
	Communicatable communicator;
	
	/**
	 * Interface used to send data to the details fragment
	 * 
	 * @author Andrew
	 *
	 */
	public interface Communicatable{
		public void sendId(long id);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			//get a handle to the activity this fragment is attached to
			communicator = (Communicatable) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString());
		}	
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.fragment_itinerary_list, container, false);
		 //get database reference
		 dbh = DBHelper.getDBHelper(getActivity());
			
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		//if there is a date
		if(date != null){
			//then fill cursor with budgets by the date
			cursor = dbh.getItinerariesByDate(date);
		}
		else{
			//else fill cursor with budgets of the last trip managed
			cursor = dbh.getItinerariesByTripId(tripId);
		}
		
		//The column to display
		String[] from = new String[]{DBHelper.COLUMN_BUDGETED_EXPENSE_DESCRIPTION};
		
		//the text view to hold the data
		int[] to = new int[]{R.id.itineraryText};
		
		//simple cursor adapter
		sca = new SimpleCursorAdapter(getActivity(), R.layout.item_row, cursor, from, to, 0);
		//set list adapter to the simplecursoradapter
		setListAdapter(sca);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		communicator.sendId(id);	 
	}
	
	/**
	 * sets the trip id
	 * 
	 * @param tripId the trip id to set the class variable to
	 */
	public void setTripId(long tripId){
		this.tripId = tripId;
	}
	
	/**
	 * the date to set the class variable to
	 * 
	 * @param date
	 */
	public void setDate(String date){
		this.date = date;
	}
	
}
