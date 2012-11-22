package around.me.models;

import android.content.Context;
import around.me.dao.DataBaseHelper;

public class User {

	private long id;
	private String name;
	private String email;
	private String password;

	private DataBaseHelper database;
	
	public User(){}
	
	public User(String name, String email, String password){
		this.name 		= name;
		this.email 		= email;
		this.password 	= password;
	}
	
	public User(long id, String name, String email, String password){
		this.id 		= id;
		this.name 		= name;
		this.email 		= email;
		this.password 	= password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getParams(){
		String[] params = new String[] {
				String.valueOf(this.id), this.name, this.email, this.password};
		
		return params;
	}
	
	public void setDatabase(Context context){
		database = new DataBaseHelper(context);
	}
	
	public void insert(){
		database.insertData(DataBaseHelper.TABLE_USER, getParams());
	}
}
