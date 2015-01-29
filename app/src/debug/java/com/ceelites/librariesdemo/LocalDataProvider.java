package com.ceelites.librariesdemo;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ceelites.sharedpreferenceinspector.Utils.SharedPreferenceUtils;

/**
 * Created by Prasham on 29-01-2015.
 */
public class LocalDataProvider {

	public static void startActivity(Context context) {
		SharedPreferenceUtils.startActivity(context);
	}

	public static void inflateMenu(Context context, MenuInflater inflater, Menu menu) {
		SharedPreferenceUtils utils = SharedPreferenceUtils.initWith(context, null);
		utils.inflateDebugMenu(inflater, menu);
	}

	public static boolean isDebugHandled(Context context, MenuItem item) {
		SharedPreferenceUtils utils = SharedPreferenceUtils.initWith(context, null);
		return utils.isDebugHandled(context, item);
	}
}
