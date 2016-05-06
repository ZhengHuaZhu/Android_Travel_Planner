package com.ZZH.travelplanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
	        case R.id.dawsoncollege:		
	        	Intent i1 = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://www.dawsoncollege.qc.ca"));
	        	startActivity(i1);
	            break;	
	        case R.id.info:	
	        	Intent i2 = new Intent(Intent.ACTION_VIEW,
						Uri.parse("https://sites.google.com/site/travelacdzwiki/"));
	        	startActivity(i2);
	            break;	
	        case R.id.authors:		
	        	startActivity(new Intent(this, AuthorActivity.class));
	            break;	
	        case R.id.courseid:	
	        	Intent i4 = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://www.dawsoncollege.qc.ca/computer-science-technology/"));
	        	startActivity(i4);
	            break;		
        }		
		return super.onOptionsItemSelected(item);
	}
}
