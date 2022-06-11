package ru.ruslan2570.schoolnotificationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
	static final String APP_PREFERENCES = "auth_data";
	static final String APP_PREFERENCES_HOST = "host";
	static final String APP_PREFERENCES_USERNAME = "username";
	static final String APP_PREFERENCES_PASSWORD = "password";

	static final String TAG = "LoginActivity";

	SharedPreferences sp;
	SharedPreferences.Editor editor;

	Handler handler;

	FragmentManager manager;
	ErrorDialog errorDialog;

	EditText edHost;
	EditText edUsername;
	EditText edPassword;
	Button btnLogin;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		handler = new Handler();
		manager = getSupportFragmentManager();
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
			new Thread(new LoginRunnable()).start();
		});
	}

	public class LoginRunnable implements Runnable {

		public void run() {
			HttpURLConnection connection = null;
			String sessionId = null;
			String error = null;
			try {
				URL url = new URL("http://" + edHost.getText() + "/request?action=login&login="
						+ edUsername.getText() + "&password=" + edPassword.getText());
				connection = (HttpURLConnection) url.openConnection();
				Log.d(TAG, "run: " + connection.getResponseCode());
				InputStream input = connection.getInputStream();
				byte[] buff = Utils.readAllBytes(input);

				String result = new String(buff);

				JSONObject json = new JSONObject(result);
				try {
					sessionId = json.getString("id");
				} catch (JSONException e) {
					error = json.getString("error");
				}

				if (sessionId != null) {
					Intent intent = new Intent(LoginActivity.this, ListActivity.class);
					intent.putExtra("session_id", sessionId);
					startActivity(intent);
				} else {
					errorDialog = new ErrorDialog(error);
					errorDialog.show(manager, "errorDialog");
				}

				Log.d(TAG, "run: " + result);
			} catch (IOException | JSONException e) {
				e.printStackTrace();
				errorDialog = new ErrorDialog(e.toString());
				errorDialog.show(manager, "errorDialog");
			} finally {
				if (connection != null)
					connection.disconnect();
			}
		}
	}


}


