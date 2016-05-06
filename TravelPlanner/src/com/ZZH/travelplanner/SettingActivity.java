package com.ZZH.travelplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SettingActivity extends Activity {
	private String name, pwd;
	private EditText username;
	private EditText password;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		sp = getPreferences(MODE_PRIVATE);
		// retrieve saved values
		name=sp.getString("USERNAME", "");
		pwd=sp.getString("PASSWORD", "");
		username.setText(name); 
		password.setText(pwd); 
	}
	
	public void saveSetting(View view){
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		name = username.getText().toString();
		pwd = password.getText().toString();
		Intent i = new Intent();
		i.putExtra("USERNAME", name);
		i.putExtra("PASSWORD", pwd);
		setResult(RESULT_OK, i);
		finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		sp = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		editor.putString("USERNAME", name);
		editor.putString("PASSWORD", pwd);

		editor.commit();
	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.setting, menu);
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
