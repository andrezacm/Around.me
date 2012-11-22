package around.me.models;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
import android.widget.EditText;
import around.me.R;
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

	/*public void insert(){
		AsyncTask<Void, Void, Void> auth = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				Log.e("SignUp JSON", "NAME = "+ name);
				Log.e("SignUp JSON", "EMAIL = "+ email);
				Log.e("SignUp JSON", "PASSWORD = "+ password);

				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://sleepy-castle-9664.herokuapp.com/users");

				JSONObject holder = new JSONObject();
				JSONObject userObj = new JSONObject();  

				try {
					userObj.put("password", password);
					userObj.put("email", email);   
					holder.put("user", userObj);
					Log.e("Authentication JSON", "Authentication JSON = "+ holder.toString());
					StringEntity se = new StringEntity(holder.toString());
					post.setEntity(se);
					post.setHeader("Accept", "application/json");
					post.setHeader("Content-Type","application/json");
				} catch (UnsupportedEncodingException e) {
					Log.e("Error",""+e);
					e.printStackTrace();
				} catch (JSONException js) {
					js.printStackTrace();
				}

				String response = null;
				try {
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					response = client.execute(post, responseHandler);
					Log.i("AUTHENTICATION", "Received "+ response +"!");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					Log.e("ClientProtocol",""+e);
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("IO",""+e);
				}
				try {
					mAuthToken = parseToken(response);

					SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("email", email);
					editor.putString("token", mAuthToken);
					editor.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		auth.execute();
	}*/
}
