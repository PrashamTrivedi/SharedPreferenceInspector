package com.ceelites.librariesdemo;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
/**
 * Created by Prasham on 29-01-2015.
 */
public class LocalDataProvider {

	public static void startActivity(Context context) {
		//Do Nothing. Release should not handle this
	}

	public static void inflateMenu(Context context, MenuInflater inflater, Menu menu) {
		//Do Nothing. Release should not handle this
	}

	public static boolean isDebugHandled(Context context, MenuItem menu) {
		//Always return false. So other menus can be handled
		return false;
	}

}
