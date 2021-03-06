package ru.ruslan2570.schoolnotificationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.itm_update:
					new Thread(new GetMessage(sessionId)).start();
				return true;
			case R.id.itm_logout:
					finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class GetMessage implements Runnable {

		String token;

		GetMessage(String token) {
			this.token = token;
		}

		HttpURLConnection connection = null;
		String id = null;
		String error = null;
		ErrorDialog errorDialog;
		FragmentManager manager = getSupportFragmentManager();
		ArrayList<JSONObject> messageArray = new ArrayList<>();

		public void run() {
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
					return;
				}

				if (error != null) {
					errorDialog = new ErrorDialog(error);
					errorDialog.show(manager, "errorDialog");
				}

				for (int i = 0; i < messages.length(); i++) {
					messageArray.add(messages.getJSONObject(i));
				}

				String lastMessageID = messages.getJSONObject(messages.length() - 1).getString("messageId");
				editor.putString(APP_PREFERENCES_LAST_MESSAGE_ID, lastMessageID);
				editor.commit();

				MessageAdapter adapter = new MessageAdapter(getApplicationContext(),
						R.layout.row, R.id.listview, messageArray);

				handler.post(() -> {
					listView.setAdapter(adapter);
				});

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