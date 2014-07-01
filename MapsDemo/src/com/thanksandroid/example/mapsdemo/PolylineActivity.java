package com.thanksandroid.example.mapsdemo;

import java.util.ArrayList;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class PolylineActivity extends BaseActivity {
	private static final int ACTION_GOOGLE_PLAY_SERVICES = 100;

	private GoogleMap map;
	private ArrayList<Marker> markers;
	private ArrayList<Polyline> polylines;
	private ArrayList<Place> places;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_polyline);

		// check if GooglePlayServivces is available

		int result = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());
		if (result == ConnectionResult.SUCCESS) {
			initialize();
			addDummyLocations();
			drawMarkers(places);
			drawPolylines(places);
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

		markers = new ArrayList<Marker>();
		polylines = new ArrayList<Polyline>();
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
		// draw markers
		for (int i = 0; i < places.size(); i++) {
			place = places.get(i);
			marker = map.addMarker(new MarkerOptions()
					.position(
							new LatLng(place.getLatitude(), place
									.getLongitude()))
					.title(place.getName())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.flagmark)));
			markers.add(marker);
		}

		// move center to second place
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(places
				.get(1).getLatitude(), places.get(1).getLongitude()));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(8);

		map.moveCamera(center);
		map.animateCamera(zoom);

	}

	private void drawPolylines(ArrayList<Place> places) {
		Place from, to;
		Polyline polyline;

		// Remove all previous polylines if any
		for (int i = 0; i < polylines.size(); i++) {
			polylines.get(i).remove();
		}

		// Clear polyline list
		polylines.clear();

		for (int i = 0; i < (places.size() - 1); i++) {
			from = places.get(i);
			to = places.get(i + 1);
			polyline = map.addPolyline(new PolylineOptions()
					.add(new LatLng(from.getLatitude(), from.getLongitude()),
							new LatLng(to.getLatitude(), to.getLongitude()))
					.width(4).color(Color.RED));
			polylines.add(polyline);
		}

	}
}
