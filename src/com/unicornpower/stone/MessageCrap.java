package com.unicornpower.stone;

import com.google.android.gms.maps.model.Marker;

public class MessageCrap {

	public String id;
	public String message;
	public double rating;
	public double lat;
	public double lon;
	public String username;
	public String recipient;
	public boolean isPrivate;
	public Marker marker;
	
	public MessageCrap(String id, String message, double rating, double lat, double lon, String username, String recipient, boolean isPrivate){
		this.id = id;
		this.message = message;
		this.rating = rating;
		this.lat = lat;
		this.lon = lon;
		this.username = username;
		this.recipient = recipient;
		this.isPrivate = isPrivate;
		
	}
	
}
