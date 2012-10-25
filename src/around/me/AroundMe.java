package around.me;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public class AroundMe extends MapActivity {

	private MyMapView mapview;
	private MapController mapcontroller;
	private LocationManager location_manager;
	private MyLocationListener mylocationlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_around_me);

		mapview = (MyMapView) findViewById(R.id.mapview);

		mapcontroller = mapview.getController();
		mapview.setBuiltInZoomControls(true);

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
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Intent intent = getIntent();
		if (intent.hasExtra("geo_point_x")) {
			int geo_point_x = (Integer) intent.getExtras().get("geo_point_x");
			int geo_point_y = (Integer) intent.getExtras().get("geo_point_y");
			GeoPoint geoPoint = new GeoPoint(geo_point_x, geo_point_y);

			mapcontroller.animateTo(geoPoint);
			mapcontroller.setZoom(12);

			MyMapOverlays marker = new MyMapOverlays(geoPoint, getResources());
			List listoverlays = mapview.getOverlays();
			listoverlays.clear();
			listoverlays.add(marker);

			Toast.makeText(getApplicationContext(), "" + geo_point_x + " : " + geo_point_y, Toast.LENGTH_LONG).show();
			mapview.invalidate();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
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
			case R.id.mylocation:
				Toast.makeText(AroundMe.this, "Current location", Toast.LENGTH_SHORT).show();
				//mylocationlist.gpsCurrentLocation();	
				return true;

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
			
			case R.id.viewevent:
				Intent intent_view = new Intent(this, ViewEvent.class);
	            startActivity(intent_view);
	        return true;
	        
			case R.id.addevent:
				Intent intent_add = new Intent(this, AddEvent.class);
	            startActivity(intent_add);
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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


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
			mapcontroller.setZoom(12);

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
