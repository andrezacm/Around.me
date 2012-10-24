package around.me.models;

public class Event {
	private int x;
	private int y;
	
	private String name;
	private String description;
	
	private Event(){};
	
	public Event(int x, int y, String name, String description){
		this.x = x;
		this.y = y;
		this.name = name;
		this.description = description; 
	}
}
