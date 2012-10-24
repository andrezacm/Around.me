package around.me.models;

import java.util.ArrayList;

public class Event {
	private int x;
	private int y;
	
	private String name;
	private String description;
	
	private static ArrayList<Event> events = new ArrayList<Event>();

	private Event(){};
	
	private Event(int x, int y, String name, String description){
		this.x = x;
		this.y = y;
		this.name = name;
		this.description = description; 
	}
	
	private Event(String name, String description){
		this.name = name;
		this.description = description; 
	}
	
	public static void create(String name, String description) {
		events.add(new Event(name, description));
	}
	
	public static ArrayList<Event> getEvents() {
		return (ArrayList<Event>) events.clone();
	}
}
