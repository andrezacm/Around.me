package around.me;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyMapOverlays extends Overlay{

	GeoPoint location;
	Resources resources;
	
	public MyMapOverlays(GeoPoint location, Resources resources) {
		this.location = location;
		this.resources = resources;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapview, boolean shadow) {
		Point screenpoint = new Point();
		mapview.getProjection().toPixels(location, screenpoint);
		
		canvas.drawBitmap(BitmapFactory.decodeResource(this.resources, R.drawable.mylocation),
				screenpoint.x, screenpoint.y, null);
		
	}
	
	@Override
	public boolean onTouchEvent(android.view.MotionEvent e, MapView mapView){
		Intent intent_view = new Intent(mapView.getContext(), ViewEvent.class);
        mapView.getContext().startActivity(intent_view);
		return true;
	}
}
