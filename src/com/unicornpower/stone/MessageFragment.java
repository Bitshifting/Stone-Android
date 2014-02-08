package com.unicornpower.stone;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MessageFragment extends Fragment{

	private final Context mContext;
	private EditText mEdit;
	
	public MessageFragment(){
		mContext = (Context) this.getActivity();
	}

	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		ViewGroup rootView;
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_message, container, false);
		((ListView) rootView.findViewById(R.id.friend_list)).setAdapter(new FriendListAdapter(this.getActivity()));
		((RadioButton) ((RadioGroup) rootView.findViewById(R.id.privacy_group)).getChildAt(0)).setChecked(true);
		mEdit = (EditText) rootView.findViewById(R.id.message_space);
		((Button) rootView.findViewById(R.id.post_button)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String message = mEdit.getText().toString();
				// TODO Auto-generated method stub
				ServerAPITask task = new ServerAPITask();
				task.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/post_message/" + message.replaceAll(" ", "%20") + "/40.42853/-86.9222/SmartAssSam");
				task.execute("Hello");
			}
			
		});
		return rootView;
	}
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//don't reload the current page when the orientation is changed
		super.onConfigurationChanged(newConfig);
	}
	
}
