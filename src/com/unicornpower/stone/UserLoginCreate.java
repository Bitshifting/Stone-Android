package com.unicornpower.stone;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class UserLoginCreate extends Dialog implements OnClickListener, OnCancelListener, OnCheckedChangeListener{
	
	private CheckBox chooseCreateAccount;
	private Button loginCreateButton;
	private EditText usernameField;
	private boolean isLogginButton = true;
	

	public UserLoginCreate(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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
			userAccountForm();
			break;
		}
		
	}
	/**
	 * Used to create or login the user
	 */
	private void userAccountForm() {
		if (isLogginButton) {
			
		}
		else {
			
		}
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
