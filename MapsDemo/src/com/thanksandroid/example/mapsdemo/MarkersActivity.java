package com.thanksandroid.example.mapsdemo;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkersActivity extends BaseActivity implements
		OnMarkerClickListener, OnMarkerDragListener {
	private static final int ACTION_GOOGLE_PLAY_SERVICES = 100;

	private GoogleMap map;
	private ArrayList<Marker> markers;
	private ArrayList<Place> places;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_markers);

		// check if GooglePlayServivces is available
		int result = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());
		if (result == ConnectionResult.SUCCESS) {
			initialize();
			addDummyLocations();
			drawMarkers(places);
		} else {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result, this,
					ACTION_GOOGLE_PLAY_SERVICES);
			dialog.show();
		}
	}

	private void initialize() {

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);

		// set listeners
		map.setOnMarkerClickListener(this);
		map.setOnMarkerDragListener(this);

		markers = new ArrayList<Marker>();
		places = new ArrayList<Place>();

	}

	private void addDummyLocations() {
		places.add(new Place("New Delhi", 28.626079, 77.240753));
		places.add(new Place("Chandigarh", 30.719333, 76.797180));
		places.add(new Place("Amritsar", 31.631442, 74.869766));
	}

	private void drawMarkers(ArrayList<Place> places) {

		// Remove previous markers if any
		for (int i = 0; i < markers.size(); i++) {
			markers.get(i).remove();
		}
		// clear the markers list
		markers.clear();

		Marker marker;
		Place place;
		LatLng latLng;

		// add simple markers
		place = places.get(0);
		latLng = new LatLng(place.getLatitude(), place.getLongitude());
		marker = map
				.addMarker(new MarkerOptions()
						.position(latLng)
						.title(place.getName())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.flagmark)));
		markers.add(marker);

		// add draggable marker
		place = places.get(1);
		latLng = new LatLng(place.getLatitude(), place.getLongitude());
		marker = map
				.addMarker(new MarkerOptions()
						.position(latLng)
						.draggable(true)
						.snippet(
								"This is a draggable and flat marker with snippet")
						.title(place.getName())
						.flat(true)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.flagmark)));
		markers.add(marker);

		// other options-> change marker color(icon()) and opacity(alpha())
		place = places.get(2);
		latLng = new LatLng(place.getLatitude(), place.getLongitude());
		marker = map.addMarker(new MarkerOptions()
				.position(latLng)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.snippet("Customized color and opacity").alpha(0.5f)
				.title(place.getName()));
		markers.add(marker);

		// move center to second place
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(places
				.get(1).getLatitude(), places.get(1).getLongitude()));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(6);

		map.moveCamera(center);
		map.animateCamera(zoom);

	}

	@Override
	public void onMarkerDrag(Marker marker) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		LatLng latLng = marker.getPosition();
		showToast("Drag ended at: " + latLng.latitude + ", " + latLng.longitude);
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		LatLng latLng = marker.getPosition();
		showToast("Drag started at: " + latLng.latitude + ", "
				+ latLng.longitude);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		showToast("Marker with title \"" + marker.getTitle() + "\" clicked");
		// If it returns false, then the default behavior will occur in addition
		// to custom behavior
		return false;
	}

}
