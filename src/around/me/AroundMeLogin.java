package around.me;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AroundMeLogin extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_me_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_around_me_login, menu);
        return true;
    }
}
