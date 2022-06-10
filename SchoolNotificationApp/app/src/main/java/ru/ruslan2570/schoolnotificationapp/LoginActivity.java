package ru.ruslan2570.schoolnotificationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
	public static final String APP_PREFERENCES = "auth_data";
	public static final String APP_PREFERENCES_HOST = "host";
	public static final String APP_PREFERENCES_USERNAME = "username";
	public static final String APP_PREFERENCES_PASSWORD = "password";

	SharedPreferences sp;
	SharedPreferences.Editor editor;

	private static final String TAG = "LoginActivity";

	Handler handler;

	EditText edHost;
	EditText edUsername;
	EditText edPassword;
	Button btnLogin;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		handler = new Handler();
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

			new Thread(new LoginUpdateRunnable()).start();

			Toast.makeText(this, "Вход", Toast.LENGTH_SHORT).show();
		});
	}

	public class LoginUpdateRunnable implements Runnable{

		public void run(){
			try {
				URL url = new URL("http://" + edHost.getText() + "/request?action=login&login="
						+ edUsername.getText() + "&password=" + edPassword.getText());
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				Log.d(TAG, "run: " + connection.getResponseCode());
				InputStream input = connection.getInputStream();
				byte[] buff = readAllBytes(input);

				String result = new String(buff);

				Log.d(TAG, "run: " + result);
				connection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static byte[] readAllBytes(InputStream inputStream) throws IOException {
		final int bufLen = 1024;
		byte[] buf = new byte[bufLen];
		int readLen;
		IOException exception = null;

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
				outputStream.write(buf, 0, readLen);

			return outputStream.toByteArray();
		} catch (IOException e) {
			exception = e;
			throw e;
		} finally {
			if (exception == null) inputStream.close();
			else try {
				inputStream.close();
			} catch (IOException e) {
				exception.addSuppressed(e);
			}
		}
	}
}


