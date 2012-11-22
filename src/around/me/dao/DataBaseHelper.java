package around.me.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "AroundMe";

	// Table User
	public  static final String TABLE_USER 			= "users";
	private static final String TABLE_USER_ID 		= "id";
	private static final String TABLE_USER_NAME 	= "name";
	private static final String TABLE_USER_EMAIL 	= "email";
	private static final String TABLE_USER_PASSWORD = "password";

	// Table Events
	public  static final String TABLE_EVENT				= "events";
	private static final String TABLE_EVENT_ID 			= "id";
	private static final String TABLE_EVENT_NAME 		= "name";
	private static final String TABLE_EVENT_DESCRIPTION	= "description";
	private static final String TABLE_EVENT_DATETIME 	= "date";
	private static final String TABLE_EVENT_LATITUDE 	= "latitude";
	private static final String TABLE_EVENT_LONGITUDE 	= "longitude";

	private String[] allColumnsTableUser = { DataBaseHelper.TABLE_USER_ID,
			DataBaseHelper.TABLE_USER_NAME,
			DataBaseHelper.TABLE_USER_EMAIL,
			DataBaseHelper.TABLE_USER_PASSWORD };

	private String[] allColumnsTableEvent = { DataBaseHelper.TABLE_EVENT_ID,
			DataBaseHelper.TABLE_EVENT_NAME,
			DataBaseHelper.TABLE_EVENT_DESCRIPTION,
			DataBaseHelper.TABLE_EVENT_DATETIME,
			DataBaseHelper.TABLE_EVENT_LATITUDE,
			DataBaseHelper.TABLE_EVENT_LONGITUDE };


	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
				+ TABLE_USER_ID 		+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TABLE_USER_NAME 		+ " TEXT,"
				+ TABLE_USER_EMAIL 		+ " TEXT,"
				+ TABLE_USER_PASSWORD 	+ " TEXT" + ")";

		db.execSQL(CREATE_TABLE_USER);

		String CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_EVENT + "("
				+ TABLE_EVENT_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TABLE_EVENT_NAME 			+ " TEXT,"
				+ TABLE_EVENT_DESCRIPTION 	+ " TEXT,"
				+ TABLE_EVENT_DATETIME 		+ " TEXT," 
				+ TABLE_EVENT_LATITUDE 		+ " INTEGER," 
				+ TABLE_EVENT_LONGITUDE 	+ " INTEGER" + ")";

		db.execSQL(CREATE_TABLE_EVENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DataBaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
		onCreate(db);
	}

	public void insertData(String table, String[] params) {
		SQLiteDatabase db = this.getWritableDatabase();
		String[] allColumns = verifyTableAndReturnColumns(table);

		if (params.length != allColumns.length){
			throw new IllegalArgumentException("Verify params for insert in table "+ table);
		}

		ContentValues values = new ContentValues();
		//set param id = null
		params[0] = null;

		for (int i=0; i>allColumns.length; i++) {
			values.put(allColumns[i], params[i]);
		}		

		Log.w(DataBaseHelper.class.getName(), "Insert data in table " + table);
		if(db.insert(table, "id", values) == -1){
			Log.w(DataBaseHelper.class.getName(), "Error when insert data in table " + table);
		} else {
			Log.w(DataBaseHelper.class.getName(), "Insert data in table " + table);
		}
		
		db.close();
	}

	public int deleteData(String table, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		System.out.println("Delete data in " + table + " with id: " + id);

		int returns = db.delete(table, "id = " + id, null);
		db.close();

		return returns;
	}
	
	// Updating single data
	public int updateData(String table, String[] params) {
		SQLiteDatabase db = this.getWritableDatabase();
		String[] allColumns = verifyTableAndReturnColumns(table);

		if (params.length != allColumns.length){
			throw new IllegalArgumentException("Verify params for insert in table "+ table);
		}

		ContentValues values = new ContentValues();

		for (int i=0; i>allColumns.length; i++) {
			values.put(allColumns[i], params[i]);
		}		

		// updating row
		int returns = db.update(table, values, allColumns[0] + " = ?",
				new String[] { String.valueOf(params[0]) });

		System.out.println("Update data in table " + table + "with id " + params[0]);
		db.close();
		return returns;
	}

	// Getting single data
	public Object getData(String table, long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] allColumns = verifyTableAndReturnColumns(table);

		Cursor cursor = db.query(table, allColumns, allColumns[0] + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		cursor.close();
		return cursor;
	}

	// Getting all data
	public List<Object> getAllData(String table) {
		List<Object> dataList = new ArrayList<Object>();
		SQLiteDatabase db = this.getWritableDatabase();
		
		String selectQuery = "SELECT  * FROM " + table;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			String[] columns = cursor.getColumnNames();
			do {
				//verificar se essa gambiarra deu certo...
				String[] values = new String[columns.length];
				
				for (int i=0; i<columns.length; i++) {
					values[i] = cursor.getString(i);
				}
				
				dataList.add(values);
				
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		return dataList;
	}

	private String[] verifyTableAndReturnColumns(String table){
		String[] allColumns = null;

		if(table.equals(TABLE_EVENT)){
			allColumns = allColumnsTableEvent;
		} else if(table.equals(TABLE_USER)){
			allColumns = allColumnsTableUser;
		}

		return allColumns;
	}
}
