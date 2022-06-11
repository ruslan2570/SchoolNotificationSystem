package ru.ruslan2570.schoolnotificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import java.net.URL;

public class ListActivity extends AppCompatActivity {

	static final String APP_PREFERENCES = "auth_data";
	static final String APP_PREFERENCES_HOST = "host";
	static final String APP_PREFERENCES_LAST_MESSAGE_ID = "msg_id";

	SharedPreferences sp;
	SharedPreferences.Editor editor;

	String host;
	String id;

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
		id = intent.getStringExtra("id");
		listView = findViewById(R.id.listview);

		new Thread();
	}

	public class GetMessage implements Runnable{
		public void run(){
			try {
				URL url = new URL("http://" + host + "/request?action=getMessages&session_id=" + id);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

}