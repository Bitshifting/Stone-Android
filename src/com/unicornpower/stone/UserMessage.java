package com.unicornpower.stone;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class UserMessage extends Dialog implements OnClickListener, OnCancelListener{

	private Button sendMessage;
	private Button cancelMessage;
	private EditText message;
	private AutoCompleteTextView friendsList;
	private static final String[] userFriendsList = new String [] {
		"test", "name", "tester", "buddy"
	};
	
	
	
	public UserMessage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sendMessage = ((Button) findViewById(R.id.send_message));
		cancelMessage = ((Button) findViewById(R.id.cancel_message));
		message = ((EditText) findViewById(R.id.message_dialog_message));
		friendsList = ((AutoCompleteTextView) findViewById(R.id.friend_search));
		
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, userFriendsList);
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
			break;
		case R.id.cancel_message:
			this.cancel();
			break;
		}
		
	}

	@Override
	public void onCancel(DialogInterface arg0) {
		this.message.setText("");
	}

	

}
