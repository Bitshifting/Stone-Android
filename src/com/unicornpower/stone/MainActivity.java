
package com.unicornpower.stone;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class MainActivity extends Activity implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {

	private CharSequence mTitle = "Stone";
	private CharSequence mDrawerTitle = "Friends";
	private GoogleMap map;
	private LocationClient locationClient;
	private Location currentLocation;
	private LocationRequest locationRequest;
	private boolean isInit = false;
	private JSONArray jsonResponse;
	private ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> jsonF = new ArrayList<JSONObject>();
	private ArrayList<MessageCrap> messageOs = new ArrayList<MessageCrap>();
	private ArrayList<Friend> friendList = new ArrayList<Friend>();
	private ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
	private static final String PROVIDER = "flp";
	private static final double LAT = 40.42853;
	private static final double LNG = -86.9222;
	private Location l;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String uid = "52f6fa1247feac5464614016";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		 mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
	                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

	            /** Called when a drawer has settled in a completely closed state. */
	            public void onDrawerClosed(View view) {
	                super.onDrawerClosed(view);
	                getActionBar().setTitle(mTitle);
	                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	            }

	            /** Called when a drawer has settled in a completely open state. */
	            public void onDrawerOpened(View drawerView) {
	                super.onDrawerOpened(drawerView);
	                getActionBar().setTitle(mDrawerTitle);
	                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	            }
	        };

	        // Set the drawer toggle as the DrawerListener
	        mDrawerLayout.setDrawerListener(mDrawerToggle);

		
		
		map = ((MapFragment) fragment).getMap();
		map.setMyLocationEnabled(true);
		locationClient = new LocationClient(this, this, this);
		locationClient.connect();


		final Dialog myDialog = new Dialog(this);
		myDialog.setContentView(R.layout.dialog_message);
		myDialog.setTitle("Send a Message");

		((Button) findViewById(R.id.message_button)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myDialog.show();
			}

		});
		getFriendsList();


	}

	public void getFriendsList(){
		ServerAPITask friendRequest = new ServerAPITask();
		//change this to get the real user ID at some point
		String userId = "52f6fa1247feac5464614016";
		friendRequest.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/account/getfollowees/" + userId);
		try{
			String friends = friendRequest.execute("Hello").get();

			JSONArray jsonFriends = new JSONArray(friends);
			Log.e("friendjson", "" + friends);
			for (int i = 0; i < jsonFriends.length(); i++){
				jsonF.add(jsonFriends.getJSONObject(i));
				friendList.add(new Friend(jsonF.get(i).getString("followeeName"), jsonF.get(i).getString("followee")));
				Log.e("friend","added " + friendList.get(i) + "!");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		String[] mFriendNames = new String[friendList.size()];
		for (int i = 0; i < mFriendNames.length; i++){
			mFriendNames[i] = friendList.get(i).getName();
		}
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mFriendNames));
		//Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {

		// Highlight the selected item, update the title, and close the drawer
		//setTitle(mPlanetTitles[position]);
		removeFriend(position);
	}

	
	private void removeFriend(int position){
		final Friend toDelete = friendList.get(position);
		new AlertDialog.Builder(this)
		.setTitle("Really delete " + toDelete.getName() +"?")
		.setPositiveButton("Cancel", null)
		.setNegativeButton("Delete", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ServerAPITask deleteFriend = new ServerAPITask();
				deleteFriend.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/account/delfriend/uid/" + toDelete.getName());
				// TODO Auto-generated method stub
				deleteFriend.execute("Hello");
				getFriendsList();
				mDrawerLayout.closeDrawers();
			}

		})
		.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		locationRequest = LocationRequest.create(); //Create location request
		locationRequest.setFastestInterval(1000);
		locationRequest.setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationClient.requestLocationUpdates(locationRequest, this);
		//currentLocation = locationClient.getLastLocation();

		Toast.makeText(this, "Connected to location services", Toast.LENGTH_SHORT).show();
		l = new Location(PROVIDER);
		l.setLatitude(LAT);
		l.setLongitude(LNG);
		//locationClient.setMockLocation(l);
		currentLocation = new Location(l);
		populateMap();

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		locationClient.disconnect();
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
		//		Toast.makeText(this, "Location has changed", Toast.LENGTH_SHORT).show();
		// if the app just started up then pan to the current location, otherwise let user pan elsewhere
		if (!isInit) {
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
			isInit = true;

		}	

	}

	/*
	 * This method makes the server calls to get the data from the server.
	 */
	public void populateMap(){
		ServerAPITask getMapTask = new ServerAPITask();
		//getMapTask.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/message/post/hello/40.42853/-86.9222/SmartAssSam/public");
		if (currentLocation !=null){
			getMapTask.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/message/get/" + currentLocation.getLatitude() + "/" + currentLocation.getLongitude() + "86/5280");
		}else{
			getMapTask.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/message/get/" + l.getLatitude() + "/" + l.getLongitude() + "86/5280");
		}
		try {
			String s = getMapTask.execute("Hello").get();

			jsonResponse = new JSONArray(s);
			for (int i = 0; i < jsonResponse.length(); i++){
				jsonObjects.add(jsonResponse.getJSONObject(i));
			}
			for (int i = 0; i < jsonObjects.size(); i++){
				JSONObject temp = jsonObjects.get(i);
				messageOs.add(new MessageCrap(temp.getString("_id"), temp.getString("message"), temp.getDouble("rating"), temp.getDouble("lat"), temp.getDouble("lon"), temp.getString("username"), temp.getString("recipient"), temp.getBoolean("private")));
			}
			for (int i = 0; i < messageOs.size(); i++){
				messageOs.get(i).marker = map.addMarker(new MarkerOptions().anchor(0.0f,  1.0f) .position(new LatLng(messageOs.get(i).lat, messageOs.get(i).lon)));
				//Log.e("POINT", "Added a point at latitude " + messageOs.get(i).lat + " and longitude " + messageOs.get(i).lon);

			}
			map.setOnMarkerClickListener(new OnMarkerClickListener(){

				@Override
				public boolean onMarkerClick(Marker marker) {
					MessageCrap mmessage = null;
					for (int i = 0; i < messageOs.size(); i++){
						if (messageOs.get(i).marker.equals(marker))
							mmessage = messageOs.get(i);	
					}

					createMarkerDialog(mmessage);
					return false;
				}

			});

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException jso) {
			jso.printStackTrace();
		}


	}

	public void createMarkerDialog(MessageCrap m){
		final View addView = LayoutInflater.from(this).inflate(R.layout.marker_dialog, null);
		TextView author = (TextView)addView.findViewById(R.id.message_author);
		final TextView rating = (TextView)addView.findViewById(R.id.message_rating);
		rating.setText("Rating: " + m.rating);
		author.setText(m.username);		
		final String id = m.id;
		final double mmrating = m.rating;
		new AlertDialog.Builder(this)
		.setTitle(m.message)
		.setView(addView)
		.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				// TODO Auto-generated method stub

			}
		})
		.setPositiveButton("Vote Down", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				rating.setText("Rating: " + (mmrating));
				ServerAPITask request = new ServerAPITask();
				request.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/message/vote/" + id + "/1/-1");
				request.execute("Hello");
				dialog.dismiss();
				// TODO Auto-generated method stub

			}
		})
		.setNegativeButton("Vote Up", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				rating.setText("Rating: " + (mmrating));
				ServerAPITask request = new ServerAPITask();
				request.setAPIRequest("http://riptide.alexkersten.com:3333/stoneapi/message/vote/" + id + "/1/1");
				request.execute("Hello");
				dialog.dismiss();
				// TODO Auto-generated method stub

			}

		})
		.show();
	}
}