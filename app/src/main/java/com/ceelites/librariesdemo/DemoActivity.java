package com.ceelites.librariesdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.ceelites.sharedpreferenceinspector.Utils.SharedPreferenceUtils;


public class DemoActivity
		extends ActionBarActivity {

	private SharedPreferenceUtils prefsUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);

		prefsUtils = SharedPreferenceUtils.initWith(this, null);
		prefsUtils.putString("Test", "Done");

		SharedPreferences anotherPref = getSharedPreferences("Prasham", MODE_PRIVATE);
		anotherPref.edit().putString("Test", "Love you life").commit();
		anotherPref.edit().putInt("Test Int", 1138).commit();
	}

	public void debug(View view) {
		SharedPreferenceUtils.startAcvitivy(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_demo, menu);
		prefsUtils.inflateDebugMenu(getMenuInflater(), menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();


		if (!prefsUtils.isDebugHandled(this, item)) {
			if (id == R.id.action_settings) {
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}
}
