package around.me;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

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

		 /*Bitmap markerImage = BitmapFactory.decodeResource(this.resources, R.drawable.hand);
		 
		    canvas.drawBitmap(markerImage,
		        screenpoint.x - markerImage.getWidth() / 2,
		        screenpoint.y - markerImage.getHeight() / 2, null);
		*/
		canvas.drawBitmap(BitmapFactory.decodeResource(this.resources, R.drawable.hand),
				screenpoint.x, screenpoint.y, null);
	}

	@Override
	public boolean onTouchEvent(android.view.MotionEvent e, MapView mapView){
		/*
		float startX = location.getLatitudeE6();
		float startY = location.getLongitudeE6();
		
		Bitmap markerImage = BitmapFactory.decodeResource(this.resources, R.drawable.hand);

		@SuppressWarnings("deprecation")
		Drawable d = new BitmapDrawable(this.resources, markerImage);

		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			if (d != null) {
				int[] states = {android.R.attr.state_pressed};
				if (d.setState(states)) {
					d.invalidateSelf();
				}
			}
			startX = e.getX();
			startY = e.getY();
			return true;
		} else if (e.getAction() == MotionEvent.ACTION_UP) {
			if (d != null) {
				int newStates[] = {};
				if (d.setState(newStates)) {
					d.invalidateSelf();
				}
			}
			if (Math.abs(startX - e.getX()) < 32 && Math.abs(startY - e.getY()) < 49 ) {
				Intent intent_view = new Intent(mapView.getContext(), ViewEvent.class);
				mapView.getContext().startActivity(intent_view);
			}
			return true;

		} else {
			return false;
		}
		*/
		return false;
	}
}