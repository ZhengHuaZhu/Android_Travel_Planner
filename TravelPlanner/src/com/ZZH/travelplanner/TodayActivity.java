/**
 * 
 */
package com.ZZH.travelplanner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author Derek
 *
 */
public class TodayActivity extends Activity {
	private int day;
	private int month;
	private int year;
	private TextView dayTV;
	private TextView monthTV;
	private TextView yearTV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today);
		
		Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH) + 1;
		year = c.get(Calendar.YEAR);
		
		dayTV = (TextView) findViewById(R.id.dayTV);
		monthTV = (TextView) findViewById(R.id.monthTV);
		yearTV = (TextView) findViewById(R.id.yearTV);
		
		dayTV.setText("" + day);
		monthTV.setText("" + month);
		yearTV.setText("" + year);	
	}	
	
	public void launchTodayFragment(View view) {
		//Launch the itinerary fragment for the specified date
		day = Integer.parseInt((String) dayTV.getText());
		month = Integer.parseInt((String) monthTV.getText());
		year = Integer.parseInt((String) yearTV.getText());
		Calendar c = Calendar.getInstance();
		c.set(year, month-1, day);
		Date date = c.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = df.format(date);
		Intent i = new Intent(this, ItineraryActivity.class);
		i.putExtra("dateInput", dateString);
		Log.i("TODAY", dateString);
		startActivity(i);
	}
	
	public void incrementDay(View view) {	
		if(day < 31 && (month == 1 || month == 3 || month == 5 ||
		    month == 7 || month == 8 || month == 10 || month == 12)) {
			addDay(false);	
			return;
		}
		if(day < 30 && (month == 4 || month == 6 || month == 9 || month == 11)) {
			addDay(false);
			return;
		}
		if(day < 28 && month == 2 && year % 4 != 0) {
			addDay(false);
			return;
		}
		if(day < 29 && month == 2 && year % 4 == 0) {
			addDay(false);
			return;
		}
		
		addDay(true);
	}
	
	private void addDay(boolean resetToOne) {
		if(resetToOne) {
			day = 1;
		}
		else {
			day++;
		}
		dayTV.setText("" + day);
	}
	
	public void decrementDay(View view) {
		if(day == 1) {
			if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
				day = 31;
				dayTV.setText("" + day);
				return;
			}
			
			if(month == 4 || month == 6 || month == 9 || month == 11) {
				day = 30;
				dayTV.setText("" + day);
				return;
			}
			
			if(month == 2 && year % 4 != 0) {
				day = 28;
				dayTV.setText("" + day);
				return;
			}
			else {
				day = 29;
				dayTV.setText("" + day);
				return;
			}
		}
		day--;
		dayTV.setText("" + day);
	}
	
	public void incrementMonth(View view) {
		if(month == 12) {
			month = 1;
		}
		else {
			month++;
		}
		
		monthTV.setText("" + month);
		checkDay();
		
	}
	
	public void decrementMonth(View view) {
		if(month == 1) {
			month = 12;
		}
		else {
			month--;
		}
		
		monthTV.setText("" + month);
		checkDay();
	}
	
	private void checkDay() {
		if(day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
			day = 30;
			dayTV.setText("" + day);
		}
		if(month == 2 && day > 28) {
			if(year % 4 == 0) {
				day = 29;
			}
			else {
				day = 28;
			}
			dayTV.setText("" + day);
		}
	}

	public void incrementYear(View view) {
		if(year < Calendar.getInstance().get(Calendar.YEAR) + 5) {
			checkLeapYear();
			
			year++;
			yearTV.setText("" + year);			
		}
	}

	public void decrementYear(View view) {
		if(year > Calendar.getInstance().get(Calendar.YEAR) - 5) {
			checkLeapYear();
			
			year--;
			yearTV.setText("" + year);			
		}
	}
	
	private void checkLeapYear() {
		if(year % 4 == 0 && month == 2 && day == 29) {
			day = 28;
			dayTV.setText("" + day);
		}
	}
}