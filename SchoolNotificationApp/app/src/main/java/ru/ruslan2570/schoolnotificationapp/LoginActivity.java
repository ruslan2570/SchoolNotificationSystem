package ru.ruslan2570.schoolnotificationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
	public static final String APP_PREFERENCES = "auth_data";
	public static final String APP_PREFERENCES_HOST = "host";
	public static final String APP_PREFERENCES_USERNAME = "username";
	public static final String APP_PREFERENCES_PASSWORD = "password";

	SharedPreferences sp;
	SharedPreferences.Editor editor;

	EditText edHost;
	EditText edUsername;
	EditText edPassword;
	Button btnLogin;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		edHost = findViewById(R.id.editTextHost);
		edUsername = findViewById(R.id.editTextUsername);
		edPassword = findViewById(R.id.editTextPassword);
		btnLogin = findViewById(R.id.btnLogin);
		sp = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
		editor = sp.edit();

		edHost.setText(sp.getString(APP_PREFERENCES_HOST, ""));
		edUsername.setText(sp.getString(APP_PREFERENCES_USERNAME, ""));
		edPassword.setText(sp.getString(APP_PREFERENCES_PASSWORD, ""));

		btnLogin.setOnClickListener((View) -> {
			editor.putString(APP_PREFERENCES_HOST, edHost.getText().toString());
			editor.putString(APP_PREFERENCES_USERNAME, edUsername.getText().toString());
			editor.putString(APP_PREFERENCES_PASSWORD, edPassword.getText().toString());
			editor.commit();

			Toast.makeText(this, "Вход", Toast.LENGTH_SHORT).show();

		});
	}
}
