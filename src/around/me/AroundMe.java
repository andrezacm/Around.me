package around.me;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import around.me.models.Event;

public class AroundMe extends MapActivity {

	private MyMapView mapview;
	private MapController mapcontroller;
	private LocationManager location_manager;
	private MyLocationListener mylocationlist;

	//por enquanto...
	private static final int MY_LATITUDE = (int) (50.883333 * 1E6);
	private static final int MY_LONGITUDE = (int) (4.7 * 1E6);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_around_me);
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		mapview = (MyMapView) findViewById(R.id.mapview);

		mapcontroller = mapview.getController();
		mapview.setBuiltInZoomControls(true);
		mapcontroller.setZoom(14);

		location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mylocationlist = new MyLocationListener();

		location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, mylocationlist);
		mapview.setLongClickable(true);
		mapview.setOnLongpressListener(new MyMapView.OnLongpressListener() {
			public void onLongpress(final MapView view, final GeoPoint longpressLocation) {
				runOnUiThread(new Runnable() {
					public void run() {
						registerForContextMenu(view);
						openContextMenu(view);
					}
				});
			};
		});

		showMyFirstLocation();
		showEvents();
		//this.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	private void showMyFirstLocation(){
		Drawable markerMyLocation = this.getResources().getDrawable(R.drawable.hand);

		MyItemizedOverlay myLocationOverlay = new MyItemizedOverlay(markerMyLocation, getApplicationContext());
		OverlayItem myLocationItem = new OverlayItem(new GeoPoint(MY_LATITUDE, MY_LONGITUDE), "", "My location");

		myLocationOverlay.addOverlayItem(myLocationItem);
		mapview.getOverlays().add(myLocationOverlay);

		mapcontroller.setCenter(myLocationOverlay.getCenter());
	}

	private void showEvents(){
		Drawable makerEvent = this.getResources().getDrawable(R.drawable.mylocation);

		MyItemizedOverlay eventItemOverlay = new MyItemizedOverlay(makerEvent, getApplicationContext());
		ArrayList<Event> events = getNewEvents();

		for (Event event: events) {
			GeoPoint pointEvent = new GeoPoint(event.getGeoPoint().getLatitudeE6(), event.getGeoPoint().getLongitudeE6());
			OverlayItem item = new OverlayItem(pointEvent, event.getName(), event.getDescription());

			eventItemOverlay.addOverlayItem(item);
			mapview.getOverlays().add(eventItemOverlay);
		}
	}

	private ArrayList<Event> getNewEvents(){
		
		AsyncTask<Void, Void, Void> newEvent = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet("http://sleepy-castle-9664.herokuapp.com/events");

				get.setHeader("Accept", "application/json");
				get.setHeader("Content-Type","application/json");

				String response = null;
				try {
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					response = client.execute(get, responseHandler);
					Log.i("List Event", "Received "+ response +"!");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					Log.e("ClientProtocol",""+e);
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("IO",""+e);
				}
				return null;
			}
		};

		newEvent.execute();
		
//		Event.create("Name Party 1", "Description Party1 Party1 Party1 Party1 Party1", new GeoPoint((int) (50.878 * 1E6), (int) (4.7 * 1E6)));
//		Event.create("Name Party 2", "Description Party2 Party2 Party2 Party2 Party2", new GeoPoint((int) (50.875 * 1E6), (int) (4.705 * 1E6)));
//		Event.create("Name Party 3", "Description Party3 Party3 Party3 Party3 Party3", new GeoPoint((int) (50.870 * 1E6), (int) (4.710 * 1E6)));
		return Event.getEvents();
	}

	@Override
	public void onStart() {
		super.onStart();

		showEvents();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.layout.menu, menu);
		//menu.add(0, 98, 0, "Registre-se");
		//menu.add(0, 99, 0, "Login");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu_longpress, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		/*case R.id.mylocation:
			Toast.makeText(AroundMe.this, "Current location", Toast.LENGTH_SHORT).show();
			//mylocationlist.gpsCurrentLocation();	
			return true;
		*/
		case R.id.normalview:
			Toast.makeText(AroundMe.this, "Normal Street View", Toast.LENGTH_SHORT).show();
			if(mapview.isSatellite()==true){
				mapview.setSatellite(false);
			}
			return true;

		case R.id.sateliteview:
			Toast.makeText(AroundMe.this, "Map Satellite View", Toast.LENGTH_SHORT).show();
			if(mapview.isSatellite()==false){
				mapview.setSatellite(true);
			}
			return true;

		case 98:
			Intent intent_register = new Intent(this, Register.class);
			startActivity(intent_register);
			return true;

		case 99:
			Intent intent_login = new Intent(this, AroundMeLogin.class);
			startActivity(intent_login);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.longpress_add_event:
			GeoPoint geoPoint = MyMapView.getLastLocationPressed();

			Intent intent_add = new Intent(this, AddEvent.class);
			intent_add.putExtra("geo_point_x", geoPoint.getLatitudeE6());
			intent_add.putExtra("geo_point_y", geoPoint.getLongitudeE6());
			startActivity(intent_add);

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private Boolean isAuthenticated() throws InterruptedException, ExecutionException{
		AsyncTask<Void, Void, Boolean> auth = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// Restore preferences
				SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);
				String token = settings.getString("token", "");

				Log.e("Authentication JSON", "TOKEN = "+ token);

				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://sleepy-castle-9664.herokuapp.com/sessions?auth_token=" + token);

				post.setHeader("Accept", "application/json");
				post.setHeader("Content-Type","application/json");

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
					JSONObject jObject = new JSONObject(response);
					JSONObject sessionObject = jObject.getJSONObject("session");
					String attributeError = sessionObject.getString("error");

					if (attributeError.equals("Success"))
						return new Boolean(true);
					else
						return new Boolean(false);

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};				
		return auth.execute().get();
	}

	private void sendToServer(){
		final Context context = this;
		AsyncTask<Event, Void, Void> newEvent = new AsyncTask<Event, Void, Void>() {
			@Override
			protected Void doInBackground(Event... params) {
				Event[] param = (Event[]) params;
				Log.e("New Event JSON", "NAME = "+ param[0].getName());
				Log.e("New Event JSON", "DESCRIPTION = "+ param[0].getDescription());
				Log.e("New Event JSON", "DATE = "+ param[0].getDate());

				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
				String token = settings.getString("token", "");

				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://sleepy-castle-9664.herokuapp.com/events?auth_token=" + token);

				JSONObject holder = new JSONObject();
				JSONObject userObj = new JSONObject();  

				try {
					userObj.put("name", param[0].getName());
					userObj.put("description", param[0].getDescription());
					userObj.put("date", param[0].getDate());
					userObj.put("x", param[0].getGeoPoint().getLatitudeE6());
					userObj.put("y", param[0].getGeoPoint().getLongitudeE6());

					holder.put("event", userObj);
					Log.e("Event JSON", "Event JSON = "+ holder.toString());
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
					Log.i("New Event", "Received "+ response +"!");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					Log.e("ClientProtocol",""+e);
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("IO",""+e);
				}
				return null;
			}
		};

		for (Event event : Event.getEvents()) {
			newEvent.execute(event);
		}
	}

	private boolean isNotUser(){
		// Restore preferences
		SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);
		String email = settings.getString("email", "");

		if(email == ""){
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	} 
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(BroadcastReceiver.class.getSimpleName(), "action: "
					+ intent.getAction());
			
			if(isNetworkAvailable()){
				sendToServer();
			}

		}
	};

	public class MyLocationListener implements LocationListener{

		public void onLocationChanged(Location location) {
			String coordinates[] = {""+location.getLatitude(), ""+location.getLongitude()};

			String text = "My location" +
					"Latitude: " + coordinates[0] +
					"Longitude: " + coordinates[1];

			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

			double lat = Double.parseDouble(coordinates[0]);
			double lng = Double.parseDouble(coordinates[1]);

			GeoPoint point = new GeoPoint(
					(int) (lat * 1E6),
					(int) (lng * 1E6)
					);

			mapcontroller.animateTo(point);
			mapcontroller.setZoom(14);

			MyMapOverlays marker = new MyMapOverlays(point, getResources());
			List listoverlays = mapview.getOverlays();
			listoverlays.clear();
			listoverlays.add(marker);

			mapview.invalidate();
		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS Disable", Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS Enable", Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}
}
