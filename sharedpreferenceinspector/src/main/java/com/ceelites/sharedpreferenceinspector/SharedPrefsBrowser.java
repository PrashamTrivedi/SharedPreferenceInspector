package com.ceelites.sharedpreferenceinspector;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import com.ceelites.sharedpreferenceinspector.fragments.SharedPreferencesList;

@TargetApi(VERSION_CODES.HONEYCOMB)
public class SharedPrefsBrowser
		extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared_prefs_browser);
		SharedPreferencesList listFragments = SharedPreferencesList.newInstance();
		startFragment(listFragments, "SharedPrefsList", false);
	}

	public void startFragment(Fragment fragment, String tag, boolean canAddToBackstack) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment);
		if (canAddToBackstack) {
			transaction.addToBackStack(tag);
		}
		transaction.commit();

	}

}
