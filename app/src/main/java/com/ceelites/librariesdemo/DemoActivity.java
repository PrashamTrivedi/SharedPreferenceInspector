package com.ceelites.librariesdemo;

import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.seismic.ShakeDetector;


public class DemoActivity
		extends AppCompatActivity implements ShakeDetector.Listener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		SharedPreferences defualtPref = PreferenceManager.getDefaultSharedPreferences(this);
		defualtPref.edit().putString("Test", "Done").apply();

		SharedPreferences anotherPref = getSharedPreferences("Prasham", MODE_PRIVATE);
		anotherPref.edit().putString("Test", "Love you life").apply();
		anotherPref.edit().putInt("Test Int", 1138).apply();
		anotherPref.edit().putFloat("Test Float", 11.38F).apply();
		anotherPref.edit().putLong("Test Long", 113834).apply();
		anotherPref.edit().putBoolean("Test Boolean", true).apply();


		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		ShakeDetector sd = new ShakeDetector(this);
		sd.start(sensorManager);
	}

	public void debug(View view) {
		LocalDataProvider.startActivity(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_demo, menu);
		LocalDataProvider.inflateMenu(this, getMenuInflater(), menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();


		if (!LocalDataProvider.isDebugHandled(this, item)) {
			if (id == R.id.action_settings) {
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void hearShake() {
		LocalDataProvider.startActivity(this);
	}
}
