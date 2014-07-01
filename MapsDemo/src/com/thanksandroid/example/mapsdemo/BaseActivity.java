package com.thanksandroid.example.mapsdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

public class BaseActivity extends Activity {

	public ProgressDialog progressDialog;

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public void showProgRessDialog(String message) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.setMessage(message);
		} else {
			progressDialog = ProgressDialog.show(this, "", message, false);
		}
	}

	public void hideProgRessDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
