package ru.ruslan2570.schoolnotificationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListActivity extends AppCompatActivity {

	static final String APP_PREFERENCES = "auth_data";
	static final String APP_PREFERENCES_HOST = "host";
	static final String APP_PREFERENCES_LAST_MESSAGE_ID = "msg_id";

	private static final String TAG = "ListActivity";

	SharedPreferences sp;
	SharedPreferences.Editor editor;

	String host;
	String sessionId;

	JSONArray messages;

	Handler handler;

	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		handler = new Handler();
		sp = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
		editor = sp.edit();
		Intent intent = getIntent();
		host = sp.getString(APP_PREFERENCES_HOST, "127.0.0.1");
		sessionId = intent.getStringExtra("session_id");
		listView = findViewById(R.id.listview);

		new Thread(new GetMessage(sessionId)).start();
	}

	public class GetMessage implements Runnable{

		String token;

		GetMessage(String token){
			this.token = token;
		}

		HttpURLConnection connection = null;
		String id = null;
		String error = null;
		ErrorDialog errorDialog;
		FragmentManager manager = getSupportFragmentManager();
		public void run(){
			try {
				URL url = new URL("http://" + host + "/request?action=getMessages&session_id=" + token);
				Log.d(TAG, url.toString());
				connection = (HttpURLConnection) url.openConnection();
				InputStream input = connection.getInputStream();
				byte[] buff = Utils.readAllBytes(input);
				String result = new String(buff);

				try {
					Log.d(TAG, "creating messages");
					messages = new JSONObject(result).getJSONArray("messages");
					Log.d(TAG, "messages was created");
				} catch (JSONException e) {
					Log.d(TAG, "creating error");
					error = new JSONObject(result).getString("error");
					Log.d(TAG, "error was created");
				}

				if(error != null){
					errorDialog = new ErrorDialog(error);
					errorDialog.show(manager, "errorDialog");
				}




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