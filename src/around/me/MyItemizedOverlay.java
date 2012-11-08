package around.me;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay{

	private List<OverlayItem> mOverlays = new ArrayList();

	public MyItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlayItem(OverlayItem overlayItem) {
		mOverlays.add(overlayItem);
		populate();
	}


	public void addOverlayItem(int latitude, int longitude, String title) {
		GeoPoint point = new GeoPoint(latitude, longitude);
		OverlayItem overlayItem = new OverlayItem(point, title, null);
		addOverlayItem(overlayItem);
	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView){
		if (event.getAction() == 1) {
			GeoPoint p = mapView.getProjection().fromPixels(
					(int) event.getX(),
					(int) event.getY());
			
			Toast.makeText(getBaseContext(),
					p.getLatitudeE6() / 1E6 + "," +
							p.getLongitudeE6() /1E6 ,
							Toast.LENGTH_SHORT).show();
		}
		return false;
	}*/

}
