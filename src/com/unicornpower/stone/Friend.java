package com.unicornpower.stone;

public class Friend {
	
	private String mname;
	private boolean isChecked;
	private int id;
	
	public Friend(String name, int id){
		mname = name;
		isChecked = false;
		this.id = id;
	}
	public String getName(){
		return mname;
	}
	
	public boolean getChecked(){
		return isChecked;
	}
	public int getId(){
		return id;
	}
	
	public void setChecked(boolean c){
		isChecked = c;
	}

}
