package com.unicornpower.stone;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;

public class UserLoginCreate extends Dialog implements OnClickListener, OnCancelListener, OnCheckedChangeListener{
	
	private JSONArray jsonResponse;
	private ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
	private ArrayList<MessageCrap> messageOs = new ArrayList<MessageCrap>();
	private ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
	private CheckBox chooseCreateAccount;
	private Button loginCreateButton;
	private EditText usernameField;
	private boolean isLogginButton = true;
	private Context userContext;
	private boolean isInit = false;
	

	public UserLoginCreate(Context context) {
		super(context);
		userContext = context;
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		chooseCreateAccount = ((CheckBox) findViewById(R.id.create_user_checkbox));
		loginCreateButton = ((Button) findViewById(R.id.login_create_button));
		usernameField = ((EditText) findViewById(R.id.username_field));
		
		loginCreateButton.setOnClickListener(this);
		chooseCreateAccount.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.login_create_button: 
			if (isLogginButton) {
				isInit = requestUserLogin();
			}
			else if (!isLogginButton) {
				isInit = requestNewAccount();
			}
			Toast.makeText(userContext, 
					PreferencesUtil.getFromPrefs(userContext, PreferencesUtil.PREFS_LOGIN_USERNAME_KEY, PreferencesUtil.PREFS_LOGIN_USERNAME_KEY), 
					Toast.LENGTH_LONG)
					.show();
			break;
		}
		
		if (isInit) {
			this.hide();
		}
	}
	
	/**
	 * attempt to log the user in
	 */
	private boolean requestUserLogin() {
		ServerAPITask userTasks = new ServerAPITask();
		String uName = usernameField.getText().toString();
		userTasks.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/account/lookup/" + uName);
		try {
			String response = userTasks.execute("").get();
			Log.e("Response String", response);
			
			jsonResponse = new JSONArray(response);
			if (jsonResponse.length() < 1) {
				Toast.makeText(this.getContext(), "The username does not exist", Toast.LENGTH_LONG).show();
			}
			else {
				JSONObject jsObj = jsonResponse.getJSONObject(0);
				if (jsObj == null) {
					Toast.makeText(this.getContext(), "An error occurred while retrieving user", Toast.LENGTH_SHORT).show();
					return false;
				}
				else {
					String _user_id = jsObj.getString("_id");
					PreferencesUtil.saveToPrefs(userContext, PreferencesUtil.PREFS_LOGIN_USER_ID_KEY, _user_id);
					
					PreferencesUtil.saveToPrefs(userContext, PreferencesUtil.PREFS_LOGIN_USERNAME_KEY, uName);
					((Activity) userContext).getActionBar().setTitle("Stone - " + uName);
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return true;
	}
	
	/**
	 * attempt to create a new user
	 */
	private boolean requestNewAccount() {
		ServerAPITask userTasks = new ServerAPITask();
		String uName = usernameField.getText().toString();
		userTasks.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/account/lookup/" + uName);
		try {
			String response = userTasks.execute("").get();
			Log.e("Response String", response);
			
			jsonResponse = new JSONArray(response);
			if (jsonResponse.length() > 0) {
				Toast.makeText(this.getContext(), "The username already exists", Toast.LENGTH_LONG).show();
			}
			else {
				userTasks = new ServerAPITask();
				userTasks.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/account/create/" + uName);
				
				String createResp = userTasks.execute("").get();
				Log.e("Create Response", createResp);
				JSONObject cObj = new JSONObject(createResp);
				
				String success = cObj.getString("success");
				if (success.equals("false")) {
					Toast.makeText(this.getContext(), "The username already exists", Toast.LENGTH_LONG).show();
				}
				else if (success.equals("true")) {					
					userTasks = new ServerAPITask();
					userTasks.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/account/lookup/" + uName);
					
					createResp = userTasks.execute("").get();
					Log.e("Get User ID", createResp);
					
					jsonResponse = new JSONArray(createResp);
					JSONObject jsObj = jsonResponse.getJSONObject(0);
					
					if (jsObj == null) {
						Toast.makeText(this.getContext(), "An error occurred while retrieving user", Toast.LENGTH_SHORT).show();
						return false;
					}
					else {
						String _user_id = jsObj.getString("_id");
						PreferencesUtil.saveToPrefs(userContext, PreferencesUtil.PREFS_LOGIN_USER_ID_KEY, _user_id);
						PreferencesUtil.saveToPrefs(userContext, PreferencesUtil.PREFS_LOGIN_USERNAME_KEY, uName);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			isLogginButton = false;
			loginCreateButton.setText("Create Account");
		}
		else  {
			isLogginButton = true;
			loginCreateButton.setText("Login");
		}
		
	}

}