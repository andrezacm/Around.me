package around.me;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay{

	private List<OverlayItem> mOverlays = new ArrayList();
	private Context context;
	
	public MyItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
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

	@Override
	protected boolean onTap(int i) {
		OverlayItem overlayItem = mOverlays.get(i);
		Toast.makeText(context, overlayItem.getSnippet(), Toast.LENGTH_SHORT).show();
		return true;
	}

}
