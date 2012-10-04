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
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AroundMe extends MapActivity {

	private MapView mapview;
	private MapController mapcontroller;
	private LocationManager location_manager;
	private MyLocationListener mylocationlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_around_me);

		mapview = (MapView) findViewById(R.id.mapview);
		mapcontroller = mapview.getController();
		mapview.setBuiltInZoomControls(true);

		location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mylocationlist = new MyLocationListener();

		location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, mylocationlist);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
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

		@Override
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

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS Disable", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS Enable", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}
}
