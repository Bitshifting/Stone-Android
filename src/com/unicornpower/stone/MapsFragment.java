package com.unicornpower.stone;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MapsFragment extends Fragment implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {

	private Context mContext;
	private GoogleMap map;
	private LocationClient locationClient;
	private Location currentLocation;
	private LocationRequest locationRequest;
	private boolean isInit = false;

	public MapsFragment(){
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
		map = ((MapFragment) this.getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
		return rootView;
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
	
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		locationRequest = LocationRequest.create(); //Create location request
		locationRequest.setFastestInterval(1000);
		locationRequest.setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationClient.requestLocationUpdates(locationRequest, this);
		
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//		Toast.makeText(this, "Location has changed", Toast.LENGTH_SHORT).show();
		if (!isInit) {
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
			isInit = true;
		}	
		
	}
	
	public void setContext(Context c){
		mContext = c;
	}
	
	@Override
	public void onDestroy() {
	    locationClient.disconnect();
	}

}
