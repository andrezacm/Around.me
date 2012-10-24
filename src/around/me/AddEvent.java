package around.me;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddEvent extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_event, menu);
        return true;
    }
}
