package com.ceelites.sharedpreferenceinspector.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ceelites.sharedpreferenceinspector.SharedPrefsBrowser;
import java.io.File;

/**
 * Created by Prasham on 25-01-2015.
 */
public class SharedPreferencesList
		extends ListFragment {


	/**
	 * Instantiates new fragment.
	 *
	 * @return : Instance of current fragment.
	 */
	public static SharedPreferencesList newInstance() {
		SharedPreferencesList sharedPreferencesList = new SharedPreferencesList();
		return sharedPreferencesList;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView lists = getListView();
		File sharedPrefsDir = new File(getActivity().getApplicationInfo().dataDir, "shared_prefs");
		if (sharedPrefsDir.exists() && sharedPrefsDir.isDirectory()) {
			final String[] list = sharedPrefsDir.list();
			ArrayAdapter<String> adapter =
					new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, android.R.id.text1, list);
			lists.setAdapter(adapter);
			setListAdapter(adapter);

			lists.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String name = list[position];
					Bundle bundle = new Bundle();
					String prefName = name.substring(0, name.length() - 4);
					bundle.putString("name", prefName);
					SharedPreferencesItem sharedPreferencesItem = SharedPreferencesItem.newInstance(bundle);
					((SharedPrefsBrowser) getActivity()).startFragment(sharedPreferencesItem, "PreferencesList", true);
				}
			});
		} else {
			setListAdapter(null);
			setEmptyText("Nothing shared in stored preference");
		}
	}
}
