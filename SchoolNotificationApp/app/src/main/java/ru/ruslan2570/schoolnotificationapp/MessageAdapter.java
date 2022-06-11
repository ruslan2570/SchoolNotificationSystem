package ru.ruslan2570.schoolnotificationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<JSONObject> {

	int listLayout;
	ArrayList<JSONObject> list;
	Context context;

	public MessageAdapter(Context context, int listLayout, int field, ArrayList<JSONObject> list) {
		super(context, listLayout, field, list);
		this.listLayout = listLayout;
		this.list = list;
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(listLayout, parent, false);
		TextView name = itemView.findViewById(R.id.name);
		TextView role = itemView.findViewById(R.id.role);
		TextView message = itemView.findViewById(R.id.message);
		try {
			name.setText(list.get(position).getString("author"));
			role.setText(list.get(position).getString("role"));
			message.setText(list.get(position).getString("text"));
		} catch(JSONException e){
			e.printStackTrace();
		}
		return itemView;
	}
}
