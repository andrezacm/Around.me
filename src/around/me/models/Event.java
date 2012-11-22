package around.me.models;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import around.me.dao.DataBaseHelper;

import com.google.android.maps.GeoPoint;

public class Event {
	private GeoPoint geoPoint;
	
	private String name;
	private String description;
	private String date;
	private long id;

	private DataBaseHelper database;
	
	private static ArrayList<Event> events = new ArrayList<Event>();

	private Event(){};
		
	private Event(long id, String name, String description, String date, GeoPoint geoPoint){
		this.geoPoint = geoPoint;
		this.name = name;
		this.description = description;
		this.date = date;
		this.id = id;
	}
	
	private Event(String name, String description, String date, GeoPoint geoPoint){
		this.geoPoint = geoPoint;
		this.name = name;
		this.description = description; 
		this.date = date;
	}
	
	private Event(String name, String description, GeoPoint geoPoint){
		this.geoPoint = geoPoint;
		this.name = name;
		this.description = description; 
	}
	
	private Event(String name, String description){
		this.name = name;
		this.description = description; 
	}
	
	public static void create(String name, String description, GeoPoint geoPoint) {
		events.add(new Event(name, description, geoPoint));
	}
	
	public static ArrayList<Event> getEvents() {
		return (ArrayList<Event>) events.clone();
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
	
	public void setGeoPoint(int latitude, int longitude) {
		GeoPoint geoPoint = new GeoPoint(latitude, longitude);
		this.geoPoint = geoPoint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String[] getParams(){
		String[] params = new String[] {
				String.valueOf(this.id), this.name, this.description, this.date,
				String.valueOf(this.geoPoint.getLatitudeE6()), String.valueOf(this.geoPoint.getLongitudeE6())};
		
		return params;
	}
	
	public void setDatabase(Context context){
		database = new DataBaseHelper(context);
	}
	
	public void insert(){
		database.insertData(DataBaseHelper.TABLE_EVENT, getParams());
	}
}
