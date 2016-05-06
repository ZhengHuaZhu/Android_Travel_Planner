package com.ZZH.travelplanner;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TipCalActivity extends Activity {

	private EditText bill;
	private TextView tip, sum, eachpay;
	private TextView numofpeople;
	private String billstr;
	private double perperson, tipamt, sumttl, percentage;
	private int numberofpeople=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_cal);
		if (savedInstanceState != null) {
			super.onRestoreInstanceState(savedInstanceState);
			tipamt = savedInstanceState.getDouble("TIPAMT");
			sumttl = savedInstanceState.getDouble("SUMTTL");
			perperson = savedInstanceState.getDouble("PERPERSON");
			numberofpeople = savedInstanceState.getInt("NUMBEROFPEOPLE");
			percentage = savedInstanceState.getDouble("PERCENTAGE");
			
			View view=this.findViewById(android.R.id.content);
			calculateTip(view);
			
			numofpeople=(TextView) findViewById(R.id.numberofpeople);
	    	numofpeople.setText(Integer.toString(numberofpeople));			
		}
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.ten:
	            if (checked){
	            	percentage=0.10;
	            }
	            break;
	        case R.id.fifteen:
	            if (checked){
	            	percentage=0.15;
	            }                
	            break;
	        case R.id.twenty:
	            if (checked){
	            	percentage=0.20;
	            }
	            break;	            
	    }
	}

	public void calculateTip(View view) {
		// take in the bill
		try{		
			bill=(EditText) findViewById(R.id.billamount);
	    	billstr=bill.getText().toString();
	    	double billamt=Double.parseDouble(billstr);
	    	
	    	if(percentage==0){
				Toast.makeText(this,R.string.choosepercentage, Toast.LENGTH_SHORT).show();
			}
    		
			perperson = Math.round((billamt*(1+percentage)/numberofpeople) * 100.0 ) / 100.0;
			tipamt = Math.round(billamt*percentage * 100.0 ) / 100.0;
			sumttl = Math.round(billamt*(1+percentage) * 100.0 ) / 100.0;
			
			tip=(TextView) findViewById(R.id.tipamount);		
			tip.setText(Double.toString(tipamt));
			
			sum=(TextView) findViewById(R.id.sumtotal);		
			sum.setText(Double.toString(sumttl));
			
			eachpay=(TextView) findViewById(R.id.paymentperperson);		
			eachpay.setText(Double.toString(perperson));	
			
		}catch(NullPointerException npe){
			Toast.makeText(this,R.string.toastinvalidbill, Toast.LENGTH_LONG).show();
		}catch(IllegalArgumentException iae){
			Toast.makeText(this,R.string.toastinvalidbill, Toast.LENGTH_LONG).show();
		}
	}
	
	public void plusOne(View view) {
		numofpeople=(TextView) findViewById(R.id.numberofpeople);
    	numberofpeople++;
    	numofpeople.setText(Integer.toString(numberofpeople));
	}
	
	public void minusOne(View view) {
		numofpeople=(TextView) findViewById(R.id.numberofpeople);
    	numberofpeople--;
    	numofpeople.setText(Integer.toString(numberofpeople));
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putDouble("PERCENTAGE", percentage);
		savedInstanceState.putDouble("TIPAMT", tipamt);
		savedInstanceState.putDouble("SUMTTL", sumttl);
		savedInstanceState.putDouble("PERPERSON", perperson);
		savedInstanceState.putInt("NUMBEROFPEOPLE", numberofpeople);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.tip_cal, menu);
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
