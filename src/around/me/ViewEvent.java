package around.me;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ViewEvent extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_event, menu);
        return true;
    }
}
