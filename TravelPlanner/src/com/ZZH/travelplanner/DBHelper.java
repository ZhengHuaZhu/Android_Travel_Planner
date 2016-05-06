package com.ZZH.travelplanner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Class that creates the database as well as provide database CRUD methods and
 * other application specific methods.
 * 
 * @author Andrew Villavera, Zheng Hua Zhu, Derek Herbert, and Christoffer Baur
 *
 */
public class DBHelper extends SQLiteOpenHelper{
	
	//Trips table definition
	private static final String TABLE_NAME_TRIPS = "trips";
	public static final String COLUMN_TRIPS_ID = "_id";
	public static final String COLUMN_TRIPS_TRIP_ID = "trip_id";
	public static final String COLUMN_TRIPS_NAME = "name";
	public static final String COLUMN_TRIPS_DESCRIPTION = "description";
	public static final String COLUMN_TRIPS_CREATION_DATE = "creation_date";
	public static final String COLUMN_TRIPS_UPDATE_DATE = "update_date";
	public static final String COLUMN_TRIPS_CLOSE_DATE = "close_date";
	
	//Locations table definition
	private static final String TABLE_NAME_LOCATIONS = "locations";
	public static final String COLUMN_LOCATIONS_ID = "_id";
	public static final String COLUMN_LOCATIONS_NAME = "name";
	public static final String COLUMN_LOCATIONS_DESCRIPTION = "description";
	public static final String COLUMN_LOCATIONS_CITY = "city";
	public static final String COLUMN_LOCATIONS_COUNTRY_CODE = "country_code";
	
	//Budgeted Expense Table definitions
	private static final String TABLE_NAME_BUDGETED_EXPENSE = "budgeted_expense";
	public static final String COLUMN_BUDGETED_EXPENSE_ID = "_id";
	public static final String COLUMN_BUDGETED_EXPENSE_TRIP_ID = "trip_id";
	public static final String COLUMN_BUDGETED_EXPENSE_LOCATION_ID = "location_id";
	public static final String COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE = "date_arrive_planned";
	public static final String COLUMN_BUDGETED_EXPENSE_DATE_DEPART = "date_depart_planned";
	public static final String COLUMN_BUDGETED_EXPENSE_AMOUNT = "amount";
	public static final String COLUMN_BUDGETED_EXPENSE_DESCRIPTION = "description";
	public static final String COLUMN_BUDGETED_EXPENSE_CATEGORY = "category";
	public static final String COLUMN_BUDGETED_EXPENSE_SUPPLIER_NAME = "name_of_supplier";
	public static final String COLUMN_BUDGETED_EXPENSE_ADDRESS = "address";
	
	//Actual Expense Table definitions
	private static final String TABLE_NAME_ACTUAL_EXPENSE = "actual_expense";
	public static final String COLUMN_ACTUAL_EXPENSE_ID = "_id";
	public static final String COLUMN_ACTUAL_EXPENSE_BUDGETED_ID = "actual_id";
	public static final String COLUMN_ACTUAL_EXPENSE_DATE_ARRIVE = "actual_date_arrive";
	public static final String COLUMN_ACTUAL_EXPENSE_DATE_DEPART = "actual_date_depart";
	public static final String COLUMN_ACTUAL_EXPENSE_AMOUNT = "actual_amount";
	public static final String COLUMN_ACTUAL_EXPENSE_DESCRIPTION = "actual_description";
	public static final String COLUMN_ACTUAL_EXPENSE_CATEGORY = "actual_category";
	public static final String COLUMN_ACTUAL_EXPENSE_SUPPLIER_NAME = "actual_name_of_supplier";
	public static final String COLUMN_ACTUAL_EXPENSE_ADDRESS = "actual_address";
	
	//Database definition
	private static final String DB_NAME = "travelacdz.db";	
	private static final int DB_VERSION = 1;
	
	//create trips table query
	private static final String DATABASE_CREATE_TRIPS = "CREATE TABLE " + TABLE_NAME_TRIPS + " (" +
			COLUMN_TRIPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_TRIPS_TRIP_ID + " INTEGER NOT NULL, " +
			COLUMN_TRIPS_NAME + " TEXT NOT NULL, " +
			COLUMN_TRIPS_DESCRIPTION + " TEXT NOT NULL, " +
			COLUMN_TRIPS_CREATION_DATE + " TEXT NOT NULL, " +
			COLUMN_TRIPS_UPDATE_DATE + " TEXT NOT NULL, " +
			COLUMN_TRIPS_CLOSE_DATE + " TEXT NOT NULL);";
	
	//create locations table query
	private static final String DATABASE_CREATE_LOCATIONS = "CREATE TABLE " + TABLE_NAME_LOCATIONS + " (" +
			COLUMN_LOCATIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_LOCATIONS_NAME + " TEXT NOT NULL, " +
			COLUMN_LOCATIONS_DESCRIPTION + " TEXT NOT NULL, " +
			COLUMN_LOCATIONS_CITY + " TEXT NOT NULL, " +
			COLUMN_LOCATIONS_COUNTRY_CODE + " TEXT NOT NULL);";
	
	//create budgeted expense table query
	private static final String DATABASE_CREATE_BUDGET = "CREATE TABLE " + 
			TABLE_NAME_BUDGETED_EXPENSE + " (" +
			COLUMN_BUDGETED_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_BUDGETED_EXPENSE_TRIP_ID + " INTEGER NOT NULL, " +
			COLUMN_BUDGETED_EXPENSE_LOCATION_ID + " INTEGER NOT NULL, " +
			COLUMN_BUDGETED_EXPENSE_DATE_DEPART + " TEXT NOT NULL, " +
			COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE + " TEXT NOT NULL, " +
			COLUMN_BUDGETED_EXPENSE_AMOUNT + " REAL NOT NULL, " +
			COLUMN_BUDGETED_EXPENSE_DESCRIPTION + " TEXT NOT NULL, " +
			COLUMN_BUDGETED_EXPENSE_CATEGORY + " TEXT NOT NULL," +
			COLUMN_BUDGETED_EXPENSE_SUPPLIER_NAME + " TEXT NOT NULL, " + 
			COLUMN_BUDGETED_EXPENSE_ADDRESS + " TEXT NOT NULL," +
			" FOREIGN KEY (" + COLUMN_BUDGETED_EXPENSE_LOCATION_ID + 
			") REFERENCES " + TABLE_NAME_LOCATIONS + "(" + COLUMN_LOCATIONS_ID + "), " +
			" FOREIGN KEY (" + COLUMN_BUDGETED_EXPENSE_LOCATION_ID + 
			") REFERENCES " + TABLE_NAME_LOCATIONS + "(" + COLUMN_LOCATIONS_ID + "));";
	
	//create actual expense table query
	private static final String DATABASE_CREATE_ACTUAL = "CREATE TABLE " + 
			TABLE_NAME_ACTUAL_EXPENSE + " (" +
			COLUMN_ACTUAL_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_ACTUAL_EXPENSE_BUDGETED_ID + " INTEGER NOT NULL, " +
			COLUMN_ACTUAL_EXPENSE_DATE_DEPART + " TEXT NOT NULL, " +
			COLUMN_ACTUAL_EXPENSE_DATE_ARRIVE + " TEXT NOT NULL, " +
			COLUMN_ACTUAL_EXPENSE_AMOUNT + " REAL NOT NULL, " +
			COLUMN_ACTUAL_EXPENSE_DESCRIPTION + " TEXT NOT NULL, " +
			COLUMN_ACTUAL_EXPENSE_CATEGORY + " TEXT NOT NULL, " + 
			COLUMN_ACTUAL_EXPENSE_SUPPLIER_NAME + " TEXT NOT NULL, " + 
			COLUMN_ACTUAL_EXPENSE_ADDRESS + " TEXT NOT NULL, " +			
			" FOREIGN KEY (" + COLUMN_ACTUAL_EXPENSE_BUDGETED_ID + 
			") REFERENCES " + TABLE_NAME_BUDGETED_EXPENSE + "(" + COLUMN_BUDGETED_EXPENSE_ID + "));";
	
	//DBClass members
	private static DBHelper dbh = null;	
	private final static String TAG = "DBHelper";
	
	/**
	 * Constructor for the class. It's private to prevent instantiation of
	 * another instance
	 * 
	 * @param context
	 * 				the context
	 */
	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	/**
	 * Instantiates the db helper.
	 * 
	 * @param context
	 * 				the context
	 */
	public static DBHelper getDBHelper(Context context){
		if(dbh == null){
			dbh = new DBHelper(context.getApplicationContext());
		}
		return dbh;
	}
	
	/**
	 * Overrides the onCreate() and Creates the db
	 * 
	 * @param db the sqlite db
	 * 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_TRIPS);
		db.execSQL(DATABASE_CREATE_LOCATIONS);
		db.execSQL(DATABASE_CREATE_ACTUAL);
		db.execSQL(DATABASE_CREATE_BUDGET);
				
	}
	
	/**
	 * Overrides the onUpgrade and drops table if exists and calls the onCreate()
	 * 
	 * @param db the sqlite db
	 * @param oldVersion the old version of the db
	 * @param newVersion the new version of the db
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BUDGETED_EXPENSE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRIPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCATIONS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_ACTUAL);

		onCreate(db);
	}
	
	/**
	 * Overrides the onOpen() method and enables foreign keys
	 * 
	 * @param db the sqlite db
	 * 
	 */
	@Override
	public void onOpen(SQLiteDatabase db){
		super.onOpen(db);
		db.execSQL("PRAGMA foreign_keys = ON");
	}
	
	/**
	 * returns a trip with the web id
	 * 
	 * @param id
	 * @return
	 */
	public Cursor getTripFromWebId(long id){
		Cursor c = getReadableDatabase().query(TABLE_NAME_TRIPS, null, COLUMN_TRIPS_TRIP_ID + " = ?", new String[]{Long.toString(id)}, null, null, null);
		
		return c;
	}
	
	/**
	 * returns the location by name
	 * 
	 * @param name
	 * @return
	 */
	public Cursor getLocationByName(String name){
		Cursor c = getReadableDatabase().query(TABLE_NAME_LOCATIONS, null, COLUMN_LOCATIONS_NAME + " = ?", new String[]{name}, null, null, null);		
		return c;
	}
	
	/**
	 * Creates a trip with the given parameter list
	 * 
	 * @param tripID the trip id in the remote database
	 * @param name the name of the trip
	 * @param desc the description of the rip
	 * @param creation the creation date of the trip
	 * @param update the update date of the trip
	 * @param close the close date of the trip
	 * @return the id of the row created
	 */
	public long createTrip(long tripID, String name, String desc, String creation, String update, String close) {		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TRIPS_TRIP_ID, tripID);
		cv.put(COLUMN_TRIPS_DESCRIPTION, desc);
		cv.put(COLUMN_TRIPS_CREATION_DATE, creation);
		cv.put(COLUMN_TRIPS_UPDATE_DATE, update);
		cv.put(COLUMN_TRIPS_CLOSE_DATE, close);
		cv.put(COLUMN_TRIPS_NAME, name);
		long code = getWritableDatabase().insert(TABLE_NAME_TRIPS, null, cv);
		return code;
	}
	
	
	/**
	 * deletes a trip by id
	 * 
	 * @param id the id of the row to be deleted
	 * @return the number of rows deleted
	 */
	public int deleteTrip(long id) {
		int rows = getWritableDatabase().delete(TABLE_NAME_TRIPS,  COLUMN_TRIPS_ID + "=?", new String[] {String.valueOf(id)});
		return rows;
	}

	/**
	 * Updates a row of trips
	 * 
	 * @param id the id of the row to be updated
	 * @param tripID the web id of the trip
	 * @param name the name of the trip
	 * @param desc the description of the trip
	 * @param creation the creation date of the trip
	 * @param update the update date of the trip
	 * @param close the close date of the trip
	 * @return the number of trips updated
	 */
	public int updateTrip(long id, long tripID, String name, String desc, String creation, String update, String close) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TRIPS_TRIP_ID, tripID);
		cv.put(COLUMN_TRIPS_DESCRIPTION, desc);
		cv.put(COLUMN_TRIPS_CREATION_DATE, creation);
		cv.put(COLUMN_TRIPS_UPDATE_DATE, update);
		cv.put(COLUMN_TRIPS_CLOSE_DATE, close);
		cv.put(COLUMN_TRIPS_NAME, name);
		int rows = getWritableDatabase().update(TABLE_NAME_TRIPS, cv, COLUMN_TRIPS_ID + "=?", new String[] {String.valueOf(id)});
		return rows;
	}
	
	/**
	 * Create a location with the given parameters list
	 * 
	 * @param name the location name
	 * @param desc the location description
	 * @param city the location's city
	 * @param countrycode the location's country code
	 * @return the id of the location created
	 */
	public long createLocation(String name, String desc, String city, String countrycode) {		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_LOCATIONS_NAME, name);
		cv.put(COLUMN_LOCATIONS_DESCRIPTION, desc);
		cv.put(COLUMN_LOCATIONS_CITY, city);
		cv.put(COLUMN_LOCATIONS_COUNTRY_CODE, countrycode);
		long code = getWritableDatabase().insert(TABLE_NAME_LOCATIONS, null, cv);
		return code;
	}
	
	/**
	 * deletes a location by it's id
	 * 
	 * @param id the id of the row
	 * @return the number of rows affected
	 */
	public int deleteLocation(long id) {
		int rows = getWritableDatabase().delete(TABLE_NAME_LOCATIONS,  COLUMN_LOCATIONS_ID + "=?", new String[] {String.valueOf(id)});
		return rows;
	}

	/**
	 * updates a row of locations with the given id
	 * 
	 * @param id the id of the row to be updated
	 * @param name the name of the location
	 * @param desc the description of the location
	 * @param city the city of the location
	 * @param countrycode the country code of the location
	 * @return the number of rows updated
	 */
	public int update(long id, String name, String desc, String city, String countrycode) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_LOCATIONS_NAME, name);
		cv.put(COLUMN_LOCATIONS_DESCRIPTION, desc);
		cv.put(COLUMN_LOCATIONS_CITY, city);
		cv.put(COLUMN_LOCATIONS_COUNTRY_CODE, countrycode);
		int rows = getWritableDatabase().update(TABLE_NAME_LOCATIONS, cv, COLUMN_LOCATIONS_ID + "=?", new String[] {String.valueOf(id)});
		return rows;
	}

	/**
	 * creates a budget with the given parameter list
	 * 
	 * @param tripid the trip id in which the budget will be used
	 * @param locationid the location id of where the budget will be used
	 * @param arrive the arrive date
	 * @param depart the depart date
	 * @param amt the budgeted amount
	 * @param desc the description of the budget
	 * @param catg the category of the budget
	 * @param supplier the name of the budget's supplier
	 * @param addr the address of the budget
	 * @return the id of the budget created
	 */
	public long createBudget(long tripid, long locationid, String arrive, 
			String depart, Double amt, String desc, String catg, String supplier, String addr) {		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_BUDGETED_EXPENSE_TRIP_ID, tripid);
		cv.put(COLUMN_BUDGETED_EXPENSE_LOCATION_ID , locationid);
		cv.put(COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE, arrive);
		cv.put(COLUMN_BUDGETED_EXPENSE_DATE_DEPART, depart);
		cv.put(COLUMN_BUDGETED_EXPENSE_AMOUNT, amt);
		cv.put(COLUMN_BUDGETED_EXPENSE_DESCRIPTION, desc);
		cv.put(COLUMN_BUDGETED_EXPENSE_CATEGORY, catg);
		cv.put(COLUMN_BUDGETED_EXPENSE_SUPPLIER_NAME, supplier);
		cv.put(COLUMN_BUDGETED_EXPENSE_ADDRESS, addr);	
		long code = getWritableDatabase().insert(TABLE_NAME_BUDGETED_EXPENSE, null, cv);
		return code;
	}

	/**
	 * deletes a budget with the given id
	 * 
	 * @param id the id of the budget to be deleted
	 * @return the number of budgets deleted
	 */
	public int deleteBudget(long id) {
		int rows = getWritableDatabase().delete(TABLE_NAME_BUDGETED_EXPENSE,  COLUMN_BUDGETED_EXPENSE_ID + "=?", new String[] {String.valueOf(id)});
		return rows;
	}
	
	/**
	 * deletes budgets of the given trip using the trip id
	 * 
	 * @param id the id of the budget to be deleted
	 * @return the number of budgets deleted
	 */
	public int deleteBudgetsByTripId(long tripId) {
		int rows = getWritableDatabase().delete(TABLE_NAME_BUDGETED_EXPENSE,  COLUMN_BUDGETED_EXPENSE_TRIP_ID + "=?", new String[] {String.valueOf(tripId)});
		return rows;
	}

	/**
	 * updates a row of budgets using the id in the parameter list
	 * 
	 * @param id the id of the budget
	 * @param arrive the arrive date of the budget
	 * @param depart the depart date of the budget
	 * @param amt the budgeted amount
	 * @param desc the description of the budget
	 * @param catg the category of the budget
	 * @param supplier the name of the budget's supplier
	 * @param addr the address of the budget
	 * @return the number of rows updated
	 */
	public int updateBudget(long id, String arrive, 
			String depart, Double amt, String desc, String catg, String supplier, String addr) {
		ContentValues cv = new ContentValues();
//		cv.put(COLUMN_BUDGETED_EXPENSE_TRIP_ID, tripid);
//		cv.put(COLUMN_BUDGETED_EXPENSE_LOCATION_ID , locationid);
		cv.put(COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE, arrive);
		cv.put(COLUMN_BUDGETED_EXPENSE_DATE_DEPART, depart);
		cv.put(COLUMN_BUDGETED_EXPENSE_AMOUNT, amt);
		cv.put(COLUMN_BUDGETED_EXPENSE_DESCRIPTION, desc);
		cv.put(COLUMN_BUDGETED_EXPENSE_CATEGORY, catg);
		cv.put(COLUMN_BUDGETED_EXPENSE_SUPPLIER_NAME, supplier);
		cv.put(COLUMN_BUDGETED_EXPENSE_ADDRESS, addr);	
		int rows = getWritableDatabase().update(TABLE_NAME_BUDGETED_EXPENSE, cv, COLUMN_BUDGETED_EXPENSE_ID + "=?", new String[] {String.valueOf(id)});
		return rows;
	}
	
	/**
	 * Create an Actual Expense of a given budget using the budget's id
	 * 
	 * @param budgetid the id of the budget
	 * @param arrive the actual arrive date
	 * @param depart the actual depart date
	 * @param amt the actual expenses
	 * @param desc the actual description
	 * @param catg the actual category
	 * @param supplier the actual supplier's name
	 * @param addr the actual address
	 * @return the id of the actual expense created
	 */
	public long createActual(long budgetid, String arrive, 
			String depart, Double amt, String desc, String catg, String supplier, String addr) {		
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ACTUAL_EXPENSE_BUDGETED_ID, budgetid);
		cv.put(COLUMN_ACTUAL_EXPENSE_DATE_ARRIVE, arrive);
		cv.put(COLUMN_ACTUAL_EXPENSE_DATE_DEPART, depart);
		cv.put(COLUMN_ACTUAL_EXPENSE_AMOUNT, amt);
		cv.put(COLUMN_ACTUAL_EXPENSE_DESCRIPTION, desc);
		cv.put(COLUMN_ACTUAL_EXPENSE_CATEGORY, catg);
		cv.put(COLUMN_ACTUAL_EXPENSE_SUPPLIER_NAME, supplier);
		cv.put(COLUMN_ACTUAL_EXPENSE_ADDRESS, addr);
		long code = getWritableDatabase().insert(TABLE_NAME_ACTUAL_EXPENSE, null, cv);
		return code;
	}

	/**
	 * Deletes an actual expense
	 * 
	 * @param id the id of the actual expense to be deleted
	 * @return the number of actual expenses deleted
	 */
	public int deleteActual(long id) {
		int rows = getWritableDatabase().delete(TABLE_NAME_ACTUAL_EXPENSE, COLUMN_ACTUAL_EXPENSE_ID + "=?", new String[] {String.valueOf(id)});
		return rows;
	}

	/**
	 * updates an actual expense with the parameter list
	 * 
	 * @param id actual expense id
	 * @param arrive the actual arrived date
	 * @param depart the actual depart date
	 * @param amt the actual expenses amount
	 * @param desc the actual description
	 * @param catg the actual category
	 * @param supplier the actual supplier's name
	 * @param addr the actual address
	 * @return the number of rows affected
	 */
	public int updateActual(long id, String arrive, 
			String depart, Double amt, String desc, String catg, String supplier, String addr) {
		ContentValues cv = new ContentValues();
		//cv.put(COLUMN_ACTUAL_EXPENSE_BE_ID, budgetid);
		cv.put(COLUMN_ACTUAL_EXPENSE_DATE_ARRIVE, arrive);
		cv.put(COLUMN_ACTUAL_EXPENSE_DATE_DEPART, depart);
		cv.put(COLUMN_ACTUAL_EXPENSE_AMOUNT, amt);
		cv.put(COLUMN_ACTUAL_EXPENSE_DESCRIPTION, desc);
		cv.put(COLUMN_ACTUAL_EXPENSE_CATEGORY, catg);
		cv.put(COLUMN_ACTUAL_EXPENSE_SUPPLIER_NAME, supplier);
		cv.put(COLUMN_ACTUAL_EXPENSE_ADDRESS, addr);
		int rows = getWritableDatabase().update(TABLE_NAME_ACTUAL_EXPENSE, cv, COLUMN_ACTUAL_EXPENSE_ID + "=?", new String[] {String.valueOf(id)});
		return rows;
	}
	
	/**
	 * Retrieves all locations in the database
	 * 
	 * @return the cursor containing the results of the query
	 */
	public Cursor getAllLocations(){		
		Cursor c = getReadableDatabase().query(TABLE_NAME_LOCATIONS, null, null, null, null, null, null);	
		return c;		
	}
	
	
	/**
	 * Retrieves all of the trips in the database
	 * 
	 * @return the cursor containing the results of the query
	 */
	public Cursor getAllTrips(){
		
		Cursor c = getReadableDatabase().query(TABLE_NAME_TRIPS, null, null, null, null, null, null);		
		return c;
		
	}
	
	/**
	 * Retrieves a location by its id
	 * 
	 * @param id the id of the location
	 * @return the cursor containing the results of the query
	 */
	public Cursor getLocationById(long id){
		String query = "Select * " +
			   "From " + TABLE_NAME_LOCATIONS + " " +
			   "Where " + COLUMN_LOCATIONS_ID + " = ?";	
		Cursor c = getReadableDatabase().rawQuery(query
				   ,new String[]{Long.toString(id)});	
		return c;		
	}
	
	/**
	 * Retrieves a budget by its id
	 * @param id the id of the budget to be retrieved
	 * @return the cursor containing the results of the query
	 */
	public Cursor getBudgetById(long id){
		String query = "Select * " +
			   "From " + TABLE_NAME_BUDGETED_EXPENSE + " " +
			   "Where " + COLUMN_BUDGETED_EXPENSE_ID + " = ?";	
		Cursor c = getReadableDatabase().rawQuery(query
				   ,new String[]{Long.toString(id)});	
		return c;		
	}
	
	/**
	 * Retrieves an actual expense by its budget's id
	 * 
	 * @param budgetId the budgeted id of the actual expense
	 * @return the cursor containing the results of the query
	 */
	public Cursor getActualByBudgetId(long budgetId){
		String query = "Select * " +
			   "From " + TABLE_NAME_ACTUAL_EXPENSE + " " +
			   "Where " + COLUMN_ACTUAL_EXPENSE_BUDGETED_ID + " = ?";	
		Cursor c = getReadableDatabase().rawQuery(query
				   ,new String[]{Long.toString(budgetId)});	
		return c;		
	}
	
	/**
	 * Retrieves a trip by its id
	 * 
	 * @param id the id of the trip
	 * @return the cursor containing the results of the query
	 */
	public Cursor getTripById(long id){
		
		Cursor c = getReadableDatabase().query(TABLE_NAME_TRIPS, null, COLUMN_TRIPS_ID + " = ?", new String[]{Long.toString(id)}, null, null, null);		
		return c;
		
	}
	
	/**
	 * Retrieves all trips of today and after 
	 * by comparing the close date
	 * @return the cursor containing the results of the query
	 */
	@SuppressLint("SimpleDateFormat")
	public Cursor getTripsOfTOdayAndAfter(){
		
		Calendar calendar = Calendar.getInstance();
		
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String today = df.format(date);
		
		String query= "select * " +
					  "from " + TABLE_NAME_TRIPS + " " + 
					  "where " + COLUMN_TRIPS_CLOSE_DATE + " >= Datetime(?)";
		
		Cursor c = getReadableDatabase().rawQuery(query, new String[]{today});
		return c;
		
	}
	
	/**
	 * Retrieves all trips
	 * 
	 * @return the cursor containing the results of the query
	 */
	public Cursor getAllItineraries(){
		
		Cursor c = getReadableDatabase().query(TABLE_NAME_BUDGETED_EXPENSE, null, null, null, null, null, null, null);		
		return c;
		
	}
	
	/**
	 * Retrieves a budget by it's trip id
	 * 
	 * @param id the id budget's trip
	 * @return the cursor containing the results of the query
	 */
	public Cursor getItinerariesByTripId(long id){
		Cursor c = getReadableDatabase().query(TABLE_NAME_BUDGETED_EXPENSE, null, COLUMN_BUDGETED_EXPENSE_TRIP_ID + " = ?", new String[]{Long.toString(id)}, null, null, null, null);		
		return c;
	}
	
	/**
	 * Retrieves the budgets by the arrive date
	 * 
	 * @param date the arrive date of the budget
	 * @return the cursor containing the results of the query
	 */
	public Cursor getItinerariesByDate(String date){
		Log.i("DBHELPER DATE", date);
		Cursor c = getReadableDatabase().query(TABLE_NAME_BUDGETED_EXPENSE, null, COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE + " = ?", new String[]{date}, null, null, null, null);		
		return c;
	}
	
	/**
	 * Retrieves all the columns of all tables using a join query.
	 * The details are retrieved according to the budget's id.
	 * It is necessary to re-query, because it's possible that the budget
	 * that is being searched for does not contain an actual expense
	 * 
	 * @param id of the itinerary
	 * @return the cursor containing the results of the query
	 */
	public Cursor getItineraryDetails(long selectedId){
		
		String query ;
		
		 query = "Select * " +
				   "From " + TABLE_NAME_BUDGETED_EXPENSE + ", " + TABLE_NAME_LOCATIONS + ", " + TABLE_NAME_ACTUAL_EXPENSE + " " +
				   "Where " + TABLE_NAME_BUDGETED_EXPENSE + "." + COLUMN_BUDGETED_EXPENSE_LOCATION_ID + " = " +
				   TABLE_NAME_LOCATIONS + "." + COLUMN_LOCATIONS_ID + " " +
				   "AND " + TABLE_NAME_BUDGETED_EXPENSE + "." + COLUMN_BUDGETED_EXPENSE_ID + " = ?" + " " +
				   "AND " + TABLE_NAME_ACTUAL_EXPENSE + "." + COLUMN_ACTUAL_EXPENSE_BUDGETED_ID + " = " +
				   TABLE_NAME_BUDGETED_EXPENSE + "." + COLUMN_BUDGETED_EXPENSE_ID;
				   
				   
		Cursor c = getReadableDatabase().rawQuery( query
												   ,new String[]{Long.toString(selectedId)});
		Log.i("DBHELPER", "AFTER QUERY WITH LOCATIONS");
		if(c.getCount() == 0){
			c.close();
			 query = "Select * " +
					   "From " + TABLE_NAME_BUDGETED_EXPENSE + ", " + TABLE_NAME_LOCATIONS + " " +
					   "Where " + TABLE_NAME_BUDGETED_EXPENSE + "." + COLUMN_BUDGETED_EXPENSE_LOCATION_ID + " = " +
					   TABLE_NAME_LOCATIONS + "." + COLUMN_LOCATIONS_ID + " " +
					   "AND " + TABLE_NAME_BUDGETED_EXPENSE + "." + COLUMN_BUDGETED_EXPENSE_ID + " = ?";
			 c = getReadableDatabase().rawQuery( query
					   ,new String[]{Long.toString(selectedId)});
			 Log.i("DBHELPER", "AFTER QUERY WITHOUT LOCATIONS");
		}
		return c;
	}
	
	/**
	 * Fills the database with dummy data
	 * 
	 */
	public void fillDatabase(){
		
		SQLiteDatabase writtable = getReadableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_TRIPS_TRIP_ID, 1);
		values.put(COLUMN_TRIPS_NAME, "Name:test");
		values.put(COLUMN_TRIPS_DESCRIPTION, "Description:test");
		values.put(COLUMN_TRIPS_CREATION_DATE, "2015-11-21");
		values.put(COLUMN_TRIPS_CLOSE_DATE, "2015-11-21");
		values.put(COLUMN_TRIPS_UPDATE_DATE, "2015-11-21");
		
		writtable.insert(TABLE_NAME_TRIPS, null, values);
		
		values = new ContentValues();
		
		values.put(COLUMN_TRIPS_TRIP_ID, 2);
		values.put(COLUMN_TRIPS_NAME, "Name:test2");
		values.put(COLUMN_TRIPS_DESCRIPTION, "Description:test2");
		values.put(COLUMN_TRIPS_CREATION_DATE, "2015-12-21");
		values.put(COLUMN_TRIPS_CLOSE_DATE, "2015-12-21");
		values.put(COLUMN_TRIPS_UPDATE_DATE, "2015-12-21");
		
		writtable.insert(TABLE_NAME_TRIPS, null, values);
		
		values = new ContentValues();
		
		values.put(COLUMN_LOCATIONS_NAME,"Louvres");
		values.put(COLUMN_LOCATIONS_DESCRIPTION, "Visit Museum");
		values.put(COLUMN_LOCATIONS_CITY, "Paris");
		values.put(COLUMN_LOCATIONS_COUNTRY_CODE, "FRA");
		
		writtable.insert(TABLE_NAME_LOCATIONS, null, values);
		
		values = new ContentValues();
		
		values.put(COLUMN_LOCATIONS_NAME,"Dawson College");
		values.put(COLUMN_LOCATIONS_DESCRIPTION, "Visit School");
		values.put(COLUMN_LOCATIONS_CITY, "Montreal");
		values.put(COLUMN_LOCATIONS_COUNTRY_CODE, "CAN");
		
		writtable.insert(TABLE_NAME_LOCATIONS, null, values);
		
		values = new ContentValues();
		
		values.put(COLUMN_BUDGETED_EXPENSE_ADDRESS, "Test Address 1234");
		values.put(COLUMN_BUDGETED_EXPENSE_AMOUNT, 12345.00);
		values.put(COLUMN_BUDGETED_EXPENSE_CATEGORY, "visit");
		values.put(COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE, "2015-11-01");
		values.put(COLUMN_BUDGETED_EXPENSE_DATE_DEPART, "2015-11-01");
		values.put(COLUMN_BUDGETED_EXPENSE_DESCRIPTION, "a Visit");
		values.put(COLUMN_BUDGETED_EXPENSE_LOCATION_ID, 1);
		values.put(COLUMN_BUDGETED_EXPENSE_TRIP_ID, 1);
		values.put(COLUMN_BUDGETED_EXPENSE_SUPPLIER_NAME, "Supply co.");
		
		long lastInserted = writtable.insert(TABLE_NAME_BUDGETED_EXPENSE, null, values);
		
		values = new ContentValues();
		
		values.put(COLUMN_BUDGETED_EXPENSE_ADDRESS, "Test Address 4321");
		values.put(COLUMN_BUDGETED_EXPENSE_AMOUNT, 25423.00);
		values.put(COLUMN_BUDGETED_EXPENSE_CATEGORY, "Food");
		values.put(COLUMN_BUDGETED_EXPENSE_DATE_ARRIVE, "2015-01-01");
		values.put(COLUMN_BUDGETED_EXPENSE_DATE_DEPART, "2015-10-02");
		values.put(COLUMN_BUDGETED_EXPENSE_DESCRIPTION, "eat");
		values.put(COLUMN_BUDGETED_EXPENSE_LOCATION_ID, 2);
		values.put(COLUMN_BUDGETED_EXPENSE_TRIP_ID, 1);
		values.put(COLUMN_BUDGETED_EXPENSE_SUPPLIER_NAME, "SupplyForMe co.");
		
		writtable.insert(TABLE_NAME_BUDGETED_EXPENSE, null, values);
		
		values = new ContentValues();
		
		values.put(COLUMN_ACTUAL_EXPENSE_BUDGETED_ID, lastInserted);
		values.put(COLUMN_ACTUAL_EXPENSE_ADDRESS, "15 5th Avenue");
		values.put(COLUMN_ACTUAL_EXPENSE_AMOUNT, 12345.00);
		values.put(COLUMN_ACTUAL_EXPENSE_CATEGORY, "Food");
		values.put(COLUMN_ACTUAL_EXPENSE_DATE_ARRIVE, "2015-12-22");
		values.put(COLUMN_ACTUAL_EXPENSE_DATE_DEPART, "2016-01-22");
		values.put(COLUMN_ACTUAL_EXPENSE_DESCRIPTION, "eating something at restaurant");
		values.put(COLUMN_ACTUAL_EXPENSE_SUPPLIER_NAME, "TravelAcdz");
		
		writtable.insert(TABLE_NAME_ACTUAL_EXPENSE, null, values);
		
		Log.d(TAG, "inserted all");
	}

}
