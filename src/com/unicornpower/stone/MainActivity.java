
package com.unicornpower.stone;

import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mOptionTitles = {"Map", "Leave a Message", "Account", "Friends", "Settings"};
	private GoogleMap map;
	private LocationClient locationClient;
	private Location currentLocation;
	private LocationRequest locationRequest;
	private boolean isInit = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptionTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }	
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
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Bundle args = new Bundle();
        SupportMapFragment fragment = new SupportMapFragment();
        MessageFragment fragment2 = new MessageFragment();
        if (position == 0){
        	map = ((SupportMapFragment) fragment).getMap();
        	this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }else{
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2).commit();
        }



        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mOptionTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
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
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
	@Override
	public void onConnected(Bundle connectionHint) {
		locationRequest = LocationRequest.create(); //Create location request
		locationRequest.setFastestInterval(1000);
		locationRequest.setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationClient.requestLocationUpdates(locationRequest, this);
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
	
	}
	
	
	@Override
	public void onDestroy() {
	    locationClient.disconnect();
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
	
}