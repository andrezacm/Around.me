package around.me;

import com.google.android.maps.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import around.me.models.Event;

public class AddEvent extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        
        final Intent intent = getIntent();
        int geo_point_x = (Integer) intent.getExtras().get("geo_point_x");
        int geo_point_y = (Integer) intent.getExtras().get("geo_point_y");
        final GeoPoint geoPoint = new GeoPoint(geo_point_x, geo_point_y);
        
        Button buttonCreate = (Button) findViewById(R.id.add_event_criar);
        
        buttonCreate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText name = (EditText) findViewById(R.id.add_event_name);
				EditText description = (EditText) findViewById(R.id.add_event_description);
				
				Event.create(name.getText().toString(), description.getText().toString(), geoPoint);
				
				Intent intent_map = new Intent(v.getContext(), AroundMe.class);
				intent_map.putExtra("geo_point_x", geoPoint.getLatitudeE6());
				intent_map.putExtra("geo_point_y", geoPoint.getLongitudeE6());
	            startActivity(intent_map);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_event, menu);
        return true;
    }
}
