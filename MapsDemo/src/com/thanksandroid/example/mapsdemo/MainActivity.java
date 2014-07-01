package com.thanksandroid.example.mapsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	// Maps Example
	public void mapsExample(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}

	// Directions Example
	public void directionsExample(View view) {
		Intent intent = new Intent(this, DirectionsActivity.class);
		startActivity(intent);
	}

	// Polyline Example
	public void polylineExample(View view) {
		Intent intent = new Intent(this, PolylineActivity.class);
		startActivity(intent);
	}

}
