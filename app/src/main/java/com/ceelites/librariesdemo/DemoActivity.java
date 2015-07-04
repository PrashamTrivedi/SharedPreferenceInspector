package com.ceelites.librariesdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class DemoActivity
		extends ActionBarActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		SharedPreferences defualtPref = PreferenceManager.getDefaultSharedPreferences(this);
		defualtPref.edit().putString("Test", "Done").commit();

		SharedPreferences anotherPref = getSharedPreferences("Prasham", MODE_PRIVATE);
		anotherPref.edit().putString("Test", "Love you life").commit();
		anotherPref.edit().putInt("Test Int", 1138).commit();
		anotherPref.edit().putFloat("Test Float", 11.38F).commit();
		anotherPref.edit().putLong("Test Long", 113834).commit();
		anotherPref.edit().putBoolean("Test Boolean", true).commit();
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
}
