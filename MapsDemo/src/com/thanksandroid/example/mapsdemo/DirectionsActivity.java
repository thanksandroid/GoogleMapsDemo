package com.thanksandroid.example.mapsdemo;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.AsyncTask;
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

public class DirectionsActivity extends BaseActivity {

	private static final int ACTION_GOOGLE_PLAY_SERVICES = 100;

	private GoogleMap map;
	private ArrayList<Marker> markers;
	private ArrayList<Polyline> polylines;
	private ArrayList<Place> places;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directions);
		
		// check if GooglePlayServivces is available
		int result = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());
		if (result == ConnectionResult.SUCCESS) {
			initialize();
			addDummyLocations();
			drawMarkers(places);
			drawDirections(places.get(0), places.get(1));
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

		// move center to first place
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(places
				.get(0).getLatitude(), places.get(0).getLongitude()));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

		map.moveCamera(center);
		map.animateCamera(zoom);

	}

	private void drawDirections(Place source, Place destination) {

		// Remove previous polylines if any
		for (int i = 0; i < polylines.size(); i++) {
			polylines.get(i).remove();
		}
		// clear the polylines list
		polylines.clear();

		String url = MapHelper.makeURL(source.getLatitude(),
				source.getLongitude(), destination.getLatitude(),
				destination.getLongitude());
		new DirectionsTask().execute(url);
	}

	private class DirectionsTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showProgRessDialog("Fetching directions...");

		}

		@Override
		protected String doInBackground(String... params) {

			String json = MapHelper.getJSONFromUrl(params[0]);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			hideProgRessDialog();
			if (result != null) {
				MapHelper.drawPath(result, map, polylines);
			} else {
				showToast("Error while fetching path.");
			}
		}
	}

}
