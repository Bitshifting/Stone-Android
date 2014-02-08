package com.unicornpower.stone;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter{
	private ArrayList<String> nameList = new ArrayList<String>(); //names of the friends. probably gotten from the api
	private ArrayList<Friend> friendList = new ArrayList<Friend>(); //a list of friend objects made from the friend name list
	private Context mContext;


	public FriendListAdapter(Context c) {
		mContext = c;
		//Normally these will be added via the api, but for now we're faking them
		nameList.add("Sally Poofenbach");
		nameList.add("John Johnson");
		nameList.add("Ferg Ferguson");
		nameList.add("Kenneth Soo");
		nameList.add("Emily Thorne");
		nameList.add("Herp Derpington");
		nameList.add("Alex Kersteeeeeeen");
		
		//Make the names into friend objects
		for (int i = 0; i < nameList.size(); i++){
			friendList.add(new Friend(nameList.get(i), i));
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nameList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View newView;
		if (convertView == null){
			newView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.friend_item, null);
		}else{
			newView = convertView;
		}
		((TextView) newView.findViewById(R.id.friend_text)).setText(friendList.get(position).getName());
		((CheckBox) newView.findViewById(R.id.friend_check)).setChecked(friendList.get(position).getChecked());
		newView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((CheckBox) v.findViewById(R.id.friend_check)).setChecked(!((CheckBox) v.findViewById(R.id.friend_check)).isChecked());
				friendList.get(position).setChecked(!friendList.get(position).getChecked());
			}
			
		});
		return newView;
	}

}
