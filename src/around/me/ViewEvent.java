package around.me;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ViewEvent extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        
        TextView name = (TextView) findViewById(R.id.view_event_name);
        name.setText("Festa no Apê");
        
        TextView description = (TextView) findViewById(R.id.view_event_description);
        description.setText("Hoje é festa lá no meu apê, pode aparecer, vai rolar bunda lelê... \\o/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_event, menu);
        return true;
    }
}
