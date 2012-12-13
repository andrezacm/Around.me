package around.me;

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

import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AroundMeLogin extends Activity {

	private JSONObject jObject;
	private String mAuthToken;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_me_login);
        
        Button buttonCreate = (Button) findViewById(R.id.login_enviar);

		buttonCreate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				authenticate();
				
				Intent intent_map = new Intent(v.getContext(), AroundMe.class);
				startActivity(intent_map);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_around_me_login, menu);
        return true;
    }
    
    private void authenticate(){
    	final Context context = this;
    	AsyncTask<Void, Void, Void> auth = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				EditText mEmailField = (EditText) findViewById(R.id.login_email);
		        EditText mPasswordField = (EditText) findViewById(R.id.login_password);
		        
		        String email = mEmailField.getText().toString();
		        String password = mPasswordField.getText().toString();
		        
		        Log.e("Authentication JSON", "EMAIL = "+ email);
		        Log.e("Authentication JSON", "PASSWORD = "+ password);
		        
				DefaultHttpClient client = new DefaultHttpClient();
	    		HttpPost post = new HttpPost("http://sleepy-castle-9664.herokuapp.com/sessions");
	    	    
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

	    	    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
	    	    	SharedPreferences.Editor editor = settings.edit();
	    	    	editor.putString("token", mAuthToken);
	    	    	editor.commit();
	    	    	GCMRegistrar.register(context, GCMIntentService.SENDER_ID);
	    		} catch (Exception e) {
	    			Log.e("token", e.getMessage());
	    		}
				return null;
			}
    	};
    	auth.execute();
	}
    
    public String parseToken(String jsonResponse) throws Exception {
		jObject = new JSONObject(jsonResponse);
		JSONObject sessionObject = jObject.getJSONObject("session");
		String attributeError = sessionObject.getString("error");
//		Toast.makeText(this, attributeError, Toast.LENGTH_LONG).show();
		String attributeToken = sessionObject.getString("auth_token");
		return attributeToken;
	}
}
