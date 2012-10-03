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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_me);
        
        mapview = (MapView) findViewById(R.id.mapview);
        mapview.setBuiltInZoomControls(true);
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
