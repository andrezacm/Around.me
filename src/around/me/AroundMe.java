package around.me;

import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AroundMe extends MapActivity {

	private MapView mapview;
	private MapController mapcontroller;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_me);
        
        mapview = (MapView) findViewById(R.id.mapview);
        mapcontroller = mapview.getController();
        
        String coordinates[] = {"30", "71"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);

        GeoPoint point = new GeoPoint(
        	(int) (lat * 1E6),
        	(int) (lng * 1E6)
        );

        mapcontroller.animateTo(point);
        mapcontroller.setZoom(7);
        mapview.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_around_me, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
