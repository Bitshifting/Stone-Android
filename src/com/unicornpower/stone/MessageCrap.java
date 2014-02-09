package com.unicornpower.stone;

import com.google.android.gms.maps.model.Marker;

public class MessageCrap {

	public final String id;
	public final String message;
	public final double rating;
	public final double lat;
	public final double lon;
	public final String username;
	public final String recipient;
	public final boolean isPrivate;
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
