package com.unicornpower.stone;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener{
	
	private GoogleMap map;
	private LocationClient locationClient;
	private Location currentLocation;
	private LocationRequest locationRequest;
	private boolean isInit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			initMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.setMyLocationEnabled(true);
		locationClient = new LocationClient(this, this, this);
		locationClient.connect();		
	}

	private void initMap() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			
			if (map == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isInit = false;
		try {
			initMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
	    locationClient.disconnect();
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		
		LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//		Toast.makeText(this, "Location has changed", Toast.LENGTH_SHORT).show();
		if (!isInit) {
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
			isInit = true;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Toast.makeText(this, "Connection Successful", Toast.LENGTH_SHORT).show();
		locationRequest = LocationRequest.create(); //Create location request
		locationRequest.setFastestInterval(1000);
		locationRequest.setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationClient.requestLocationUpdates(locationRequest, this);
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Connection Disconnected", Toast.LENGTH_SHORT).show();
	}

}
