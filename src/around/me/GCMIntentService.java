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

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMIntentService extends com.google.android.gcm.GCMBaseIntentService {
	static public final String SENDER_ID = "110615823763";
	static public final Integer SENDER_ID_INT = 1106158237;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Intent notificationIntent = new Intent(context, ViewEvent.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context,
		        SENDER_ID_INT, notificationIntent,
		        PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationManager nm = (NotificationManager) context
		        .getSystemService(Context.NOTIFICATION_SERVICE);

		Resources res = context.getResources();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		builder.setContentIntent(contentIntent)
					.setDefaults(Notification.DEFAULT_ALL)
		            .setSmallIcon(R.drawable.hand)
//		            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.some_big_img))
		            .setTicker("Ticker")
		            .setWhen(System.currentTimeMillis())
		            .setAutoCancel(true)
		            .setContentTitle("Titulo")
		            .setContentText("Texto");
		Notification n = builder.build();

		nm.notify(SENDER_ID_INT, n);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String token = settings.getString("token", "");
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://sleepy-castle-9664.herokuapp.com/gcm_registrations");
	    
		JSONObject holder = new JSONObject();
	    JSONObject registerObj = new JSONObject();  
	    
	    try {   
	    	registerObj.put("token", token)
	    			   .put("reg_id", regId);
		    holder.put("registration", registerObj);
		    
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
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
