package com.thanksandroid.example.mapsdemo;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends BaseActivity {

	private static final int ACTION_GOOGLE_PLAY_SERVICES = 100;

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// check if GooglePlayServivces is available
		int result = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());
		if (result == ConnectionResult.SUCCESS) {
			initialize();
		} else {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result, this,
					ACTION_GOOGLE_PLAY_SERVICES);
			dialog.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_my_location) {
			moveToMyLocation();
			return true;
		} else if (id == R.id.action_map_type) {
			changeMapType();
			return true;
		} else if (id == R.id.action_change_taffic) {
			changeTraffic();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initialize() {

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);

		moveToMyLocation();

	}

	private void changeTraffic() {
		if (map.isTrafficEnabled()) {
			map.setTrafficEnabled(false);
			showToast("Traffic has been disabled");
		} else {
			map.setTrafficEnabled(true);
			showToast("Traffic has been enabled");
		}
	}

	private void changeMapType() {
		if (map.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			showToast("Map Type: Terrain");
		} else if (map.getMapType() == GoogleMap.MAP_TYPE_TERRAIN) {
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			showToast("Map Type: Satellite");
		} else if (map.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			showToast("Map Type: Hybrid");
		} else if (map.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			showToast("Map Type: Normal");
		}
	}

	private void moveToMyLocation() {
		Location location = map.getMyLocation();
		if (location == null) {
			showToast("Your current location is not available.");
		} else {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			centerMap(latLng);
		}
	}

	private void centerMap(LatLng latLng) {

		CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
		final CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);

		map.animateCamera(center, new CancelableCallback() {

			@Override
			public void onFinish() {
				map.animateCamera(zoom);

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	}
}
