package around.me;

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
        
        Button buttonCreate = (Button) findViewById(R.id.add_event_criar);
        
        buttonCreate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText name = (EditText) findViewById(R.id.add_event_name);
				EditText description = (EditText) findViewById(R.id.add_event_description);
				
				Event.create(name.getText().toString(), description.getText().toString());
				
				Intent intent_map = new Intent(v.getContext(), AroundMe.class);
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
