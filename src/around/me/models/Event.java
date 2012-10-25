package around.me.models;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

public class Event {
	private GeoPoint geoPoint;
	
	private String name;
	private String description;
	
	private static ArrayList<Event> events = new ArrayList<Event>();

	private Event(){};
	
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
}
