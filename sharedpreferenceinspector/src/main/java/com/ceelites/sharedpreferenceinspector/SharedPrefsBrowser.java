package com.ceelites.sharedpreferenceinspector;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.ceelites.sharedpreferenceinspector.fragments.SharedPreferencesList;

/**
 * Entry Point of the library.
 */
@TargetApi(VERSION_CODES.HONEYCOMB)
public class SharedPrefsBrowser
		extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared_prefs_browser);
		SharedPreferencesList listFragments = SharedPreferencesList.newInstance();
		startFragment(listFragments, "SharedPrefsList", false);
	}

	/**
	 * Start given Fragment
	 *
	 * @param fragment
	 * 		: Fragment Instance to start.
	 * @param tag
	 * 		: Tag to be notified when adding to backstack
	 * @param canAddToBackstack
	 * 		: pass <code>true</code> if you want to add this item in backstack. pass <code>false</code> otherwise
	 */
	public void startFragment(Fragment fragment, String tag, boolean canAddToBackstack) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment);
		if (canAddToBackstack) {
			transaction.addToBackStack(tag);
		}
		transaction.commit();

	}

}
