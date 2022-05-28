package ru.ruslan2570.schoolnotificationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
	public static final String APP_PREFERENCES = "auth_data";
	public static final String APP_PREFERENCES_HOST = "auth_data";
	public static final String APP_PREFERENCES_USERNAME = "auth_data";
	public static final String APP_PREFERENCES_PASSWORD = "auth_data";


	SharedPreferences sp;
	SharedPreferences.Editor editor;

	EditText edHost;
	EditText edUsername;
	EditText edPassword;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		edHost = findViewById(R.id.editTextHost);
		edUsername = findViewById(R.id.editTextUsername);
		edPassword = findViewById(R.id.editTextPassword);
		sp = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
		editor = sp.edit();


	}
}
