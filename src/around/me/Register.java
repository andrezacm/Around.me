package around.me;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import around.me.models.User;

public class Register extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		Button buttonCreate = (Button) findViewById(R.id.register_criar);

		buttonCreate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText name = (EditText) findViewById(R.id.register_name);
				EditText email = (EditText) findViewById(R.id.register_email);
				EditText password = (EditText) findViewById(R.id.register_password);
				EditText confirm_password = (EditText) findViewById(R.id.register_confirm_password);

				User user = new User(name.getText().toString(), email.getText().toString(), password.getText().toString());
				user.setDatabase(getBaseContext());
				user.insert();
				
				SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("email", email.getText().toString());
				editor.commit();
				
				Intent intent_map = new Intent(v.getContext(), AroundMe.class);
				startActivity(intent_map);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}
}
