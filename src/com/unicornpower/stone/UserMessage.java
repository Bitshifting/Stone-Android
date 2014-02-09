package com.unicornpower.stone;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;

public class UserMessage extends Dialog implements OnClickListener, OnCancelListener{

	private Button sendMessage;
	private Button cancelMessage;
	private EditText message;
	private AutoCompleteTextView friendsList;
	private ArrayList<String> friends;
	private Location location;
	private LocationClient locC;
	private Context context;



	public UserMessage(Context context, List<Friend> fList, LocationClient loc) {
		super(context);
		this.context = context;
		locC = loc;
		friends = new ArrayList<String>();
		for (Friend f : fList) {
			friends.add(f.getName());
		}
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sendMessage = ((Button) findViewById(R.id.send_message));
		cancelMessage = ((Button) findViewById(R.id.cancel_message));
		message = ((EditText) findViewById(R.id.message_dialog_message));
		friendsList = ((AutoCompleteTextView) findViewById(R.id.friend_search));


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, friends);
		friendsList.setAdapter(adapter);

		sendMessage.setOnClickListener(this);
		cancelMessage.setOnClickListener(this);
		this.setOnCancelListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) 
		{
		case R.id.send_message:
			saveMessage();
			this.hide();
			break;
		case R.id.cancel_message:
			this.cancel();
			break;
		}

	}

	@Override
	public void onCancel(DialogInterface arg0) {
		this.message.setText("");
		this.friendsList.setText("");
	}


	public void saveMessage() {
		
		String uName = PreferencesUtil.getFromPrefs(context, PreferencesUtil.PREFS_LOGIN_USERNAME_KEY, PreferencesUtil.PREFS_LOGIN_USERNAME_KEY);
		location = locC.getLastLocation();
		ServerAPITask messageTask = new ServerAPITask();
		try {
			String dec = URLDecoder.decode(this.message.getText().toString(), "UTF-8");

			if (this.friendsList.getText().toString().equals("")) {
				
				messageTask.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/message/post/" + dec.replaceAll(" ", "%20") + "/" + location.getLatitude() + "/" + location.getLongitude() + "/" + uName + "/public");
			}
			else {
				String f = URLDecoder.decode(this.friendsList.getText().toString(), "UTF-8");
				messageTask.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/message/post/" + dec.replaceAll(" ", "%20") + "/" + location.getLatitude() + "/" + location.getLongitude() + "/" + uName + "/" + f);
			}
			String createResp = messageTask.execute("").get();
			JSONObject cObj = new JSONObject(createResp);
			
			String success = cObj.getString("success");
			if (success.equals("false")) {
				Toast.makeText(this.getContext(), "An error occured saving message", Toast.LENGTH_LONG).show();
			}
			else if (success.equals("true")) {	 
				Toast.makeText(this.getContext(), "success", Toast.LENGTH_LONG).show();
			}
			
		} catch(Exception e) {
			Toast.makeText(this.getContext(), "from saveMessage", Toast.LENGTH_LONG);
			e.printStackTrace();
		}
	}
}
