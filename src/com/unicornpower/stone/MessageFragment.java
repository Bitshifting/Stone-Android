package com.unicornpower.stone;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MessageFragment extends Fragment{

	private Context mContext;
	
	public MessageFragment(){
		mContext = this.getActivity();
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
		return rootView;
	}
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//don't reload the current page when the orientation is changed
		super.onConfigurationChanged(newConfig);
	}
	
	public void setContext(Context c){
		mContext = c;
	}
}
