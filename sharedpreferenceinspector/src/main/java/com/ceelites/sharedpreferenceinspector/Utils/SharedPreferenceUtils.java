package com.ceelites.sharedpreferenceinspector.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ceelites.sharedpreferenceinspector.R;
import com.ceelites.sharedpreferenceinspector.SharedPrefsBrowser;
import java.util.Map;

/**
 * Helper class to create shared preferences files. It will also help you to read from and write to shared preferences. Created by Prasham on
 * 17-01-2015.
 */
public class SharedPreferenceUtils {
	public static final String keyTestMode = "test_mode_";
	private static SharedPreferenceUtils sharedPreferenceUtils;
	private SharedPreferences sharedPreferences;

	/**
	 * Init SharedPreferenceUtils with SharedPreferences file
	 *
	 * @param sharedPreferences
	 * 		: SharedPreferences object to init with.
	 *
	 * @return : SharedPreferenceUtils object. It will store the sharedPreferences value with given SharedPreferences.
	 */
	public static SharedPreferenceUtils initWith(SharedPreferences sharedPreferences) {

		if (sharedPreferenceUtils == null) {
			sharedPreferenceUtils = new SharedPreferenceUtils();
		}
		sharedPreferenceUtils.sharedPreferences = sharedPreferences;
		return sharedPreferenceUtils;
	}

	/**
	 * Init SharedPreferences with context and a SharedPreferences name
	 *
	 * @param context:
	 * 		Context to init SharedPreferences
	 * @param name:
	 * 		Name of SharedPreferences file. If you pass <code>null</code> it will create default SharedPrefernces
	 *
	 * @return: SharedPreferenceUtils object. It will store given the sharedPreferences value with given SharedPreferences.
	 */
	public static SharedPreferenceUtils initWith(Context context, String name) {
		if (sharedPreferenceUtils == null) {
			sharedPreferenceUtils = new SharedPreferenceUtils();
		}
		if (isEmptyString(name)) {
			sharedPreferenceUtils.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		} else {
			sharedPreferenceUtils.sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		}
		return sharedPreferenceUtils;
	}

	/**
	 * This method checks if the string is empty or having null value.
	 *
	 * @param string
	 * 		: Charsequence string to check.
	 *
	 * @return <code>true</code> if string is null, blank or having "null" as value
	 */
	public static boolean isEmptyString(CharSequence string) {
		return (TextUtils.isEmpty(string) || string.toString().equalsIgnoreCase("null"));
	}

	/**
	 * Extract number from string, failsafe. If the string is not a proper number it will always return 0;
	 *
	 * @param string
	 * 		: String that should be converted into a number
	 *
	 * @return : 0 if conversion to number is failed anyhow, otherwise converted number is returned
	 */
	public static int getNumber(CharSequence string) {
		int number = 0;
		if (!isEmptyString(string)) {
			if (TextUtils.isDigitsOnly(string)) {

				number = Integer.parseInt(string.toString());
			}
		}
		return number;
	}

	/**
	 * Extract number from string, failsafe. If the string is not a proper number it will always return 0.0f;
	 *
	 * @param string
	 * 		: String that should be converted into a number
	 *
	 * @return : 0.0f if conversion to number is failed anyhow, otherwise converted number is returned
	 */
	public static float getNumberFloat(CharSequence string) {
		float number = 0.0f;
		if (!isEmptyString(string)) {
			if (TextUtils.isDigitsOnly(string)) {

				number = Float.parseFloat(string.toString());
			}
		}
		return number;
	}

	/**
	 * Extract number from string, failsafe. If the string is not a proper number it will always return 0l;
	 *
	 * @param string
	 * 		: String that should be converted into a number
	 *
	 * @return : 0l if conversion to number is failed anyhow, otherwise converted number is returned
	 */

	public static long getNumberLong(CharSequence string) {
		long number = 0l;
		if (!isEmptyString(string)) {
			if (TextUtils.isDigitsOnly(string)) {

				number = Long.parseLong(string.toString());
			}
		}
		return number;
	}

	/**
	 * Gets all KeyValue pairs from Given Shared Preferences.
	 *
	 * @return : Map of KeyValue pair from Given SharedPreferences
	 */
	public Map<String, ?> getAll() {
		return sharedPreferences.getAll();
	}

	/**
	 * Inflate menu item for debug.
	 *
	 * @param inflater:
	 * 		MenuInflater to inflate the menu.
	 * @param menu
	 * 		: Menu object to inflate debug menu.
	 */
	public void inflateDebugMenu(MenuInflater inflater, Menu menu) {
		inflater.inflate(R.menu.debug, menu);
	}

	/**
	 * Checks if debug menu item clicked or not.
	 *
	 * @param context
	 * 		: context to start activity if debug item handled.
	 * @param item
	 * 		: Menu item whose click is to be checked.
	 *
	 * @return : <code>true</code> if debug menu item is clicked, it will automatically Start the SharedPrefsBrowser activity. If debug menu item is
	 * not clicked, you can handle rest menu items in onOptionsItemSelcted code.
	 */
	public boolean isDebugHandled(Context context, MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_debug) {
			startActivity(context);
			return true;
		}
		return false;
	}

	/**
	 * Start the SharedPrefsBrowser activity.
	 *
	 * @param context
	 * 		: : context to start activity.
	 */
	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, SharedPrefsBrowser.class));
	}

	/**
	 * Check if value exists for key.
	 *
	 * @param key
	 * 		: Key for which we need to check the value exists or not.
	 *
	 * @return : <code>true</code> if the value exists and it's other than default data.
	 */
	public boolean isValueExistForKey(String key) {
		boolean isValueExists;
		try {
			String string = getString(key, "");
			isValueExists = !string.equalsIgnoreCase("");
		} catch (ClassCastException e) {
			try {
				int anInt = getInt(key, 0);
				isValueExists = anInt != 0;
			} catch (ClassCastException e1) {
				try {
					long aLong = getLong(key, 0);
					isValueExists = aLong != 0;
				} catch (ClassCastException e2) {
					try {
						float aFloat = getFloat(key, 0f);
						isValueExists = aFloat != 0;
					} catch (ClassCastException e3) {
						try {
							boolean aBoolean = getBoolean(key, false);
							isValueExists = !aBoolean;
						} catch (Exception e4) {
							isValueExists = false;
							e.printStackTrace();
						}
					}

				}

			}
		} catch (Exception e) {
			isValueExists = false;
		}
		return isValueExists;
	}

	/**
	 * Retrieve a String value from the preferences.
	 *
	 * @param key
	 * 		The name of the preference to retrieve.
	 * @param defaultValue
	 * 		value to return when a key does not exists.
	 *
	 * @return the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
	 *
	 * @see android.content.SharedPreferences#getString(String, String)
	 */
	public String getString(String key, String defaultValue) {

		return sharedPreferences.getString(key, (defaultValue == null) ? "" : defaultValue);
	}

	/**
	 * Retrieve an int value from the preferences.
	 *
	 * @param key
	 * 		The name of the preference to retrieve.
	 * @param defaultValue
	 * 		value to return when a key does not exists.
	 *
	 * @return the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not an int.
	 *
	 * @see android.content.SharedPreferences#getInt(String, int)
	 */
	public int getInt(String key, int defaultValue) {

		return sharedPreferences.getInt(key, defaultValue);
	}

	/**
	 * Retrieve a long value from the preferences.
	 *
	 * @param key
	 * 		The name of the preference to retrieve.
	 * @param defaultValue
	 * 		value to return when a key does not exists.
	 *
	 * @return the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a long.
	 *
	 * @see android.content.SharedPreferences#getLong(String, long)
	 */
	public long getLong(String key, long defaultValue) {

		return sharedPreferences.getLong(key, defaultValue);
	}

	/**
	 * Retrieve a float value from the preferences.
	 *
	 * @param key
	 * 		The name of the preference to retrieve.
	 * @param defaultValue
	 * 		value to return when a key does not exists.
	 *
	 * @return the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a long.
	 *
	 * @see android.content.SharedPreferences#getFloat(String, float)
	 */

	public float getFloat(String key, float defaultValue) {

		return sharedPreferences.getFloat(key, defaultValue);
	}

	/**
	 * Retrieve a boolean value from the preferences.
	 *
	 * @param key
	 * 		The name of the preference to retrieve.
	 * @param defaultValue
	 * 		value to return when a key does not exists.
	 *
	 * @return the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a
	 * boolean.
	 *
	 * @see android.content.SharedPreferences#getBoolean(String, boolean)
	 */
	public boolean getBoolean(String key, boolean defaultValue) {

		return sharedPreferences.getBoolean(key, defaultValue);
	}

	/**
	 * Restore the original value for the key after it has been changed in test mode.
	 *
	 * @param key
	 * 		: Key whose value is to be restored
	 */
	public void restoreKey(String key) {
		String originalKey = key.substring(keyTestMode.length());
		Object value = get(key);
		put(originalKey, value);
		clear(key);
	}

	/**
	 * Get the value of the key.
	 *
	 * @param key
	 * 		The name of the preference to retrieve.
	 *
	 * @return value of the preferences
	 */
	public Object get(String key) {
		try {
			return getString(key, null);
		} catch (ClassCastException e) {
			try {
				return getInt(key, 0);
			} catch (ClassCastException e1) {
				try {
					return getLong(key, 0);
				} catch (ClassCastException e2) {
					try {
						return getFloat(key, 0f);
					} catch (ClassCastException e3) {
						try {
							return getBoolean(key, false);
						} catch (Exception e4) {
							e.printStackTrace();
						}
					}

				}

			}
		}
		return null;
	}

	/**
	 * Put the value in given shared preference
	 *
	 * @param key
	 * 		the name of the preference to save
	 * @param value
	 * 		the value to save
	 */
	public void put(String key, Object value) {
		if (value.getClass().equals(String.class)) {
			putString(key, value.toString());
		} else if (value.getClass().equals(Integer.class)) {
			putInt(key, (Integer) value);
		} else if (value.getClass().equals(Float.class)) {
			putFloat(key, (Float) value);
		} else if (value.getClass().equals(Long.class)) {
			putLong(key, (Long) value);
		} else if (value.getClass().equals(Boolean.class)) {
			putBoolean(key, (Boolean) value);
		} else {
			putString(key, value.toString());
		}
	}

	/**
	 * remove the preference
	 *
	 * @param key
	 * 		the name of the preference to remove
	 */
	public void clear(String key) {
		sharedPreferences.edit().remove(key).commit();
	}

	/**
	 * put the string value to shared preference
	 *
	 * @param key
	 * 		the name of the preference to save
	 * @param value
	 * 		the name of the preference to modify.
	 *
	 * @see android.content.SharedPreferences#edit()#putString(String, String)
	 */
	public void putString(String key, String value) {
		sharedPreferences.edit().putString(key, value).commit();
	}

	/**
	 * put the int value to shared preference
	 *
	 * @param key
	 * 		the name of the preference to save
	 * @param value
	 * 		the name of the preference to modify.
	 *
	 * @see android.content.SharedPreferences#edit()#putInt(String, int)
	 */
	public void putInt(String key, int value) {
		sharedPreferences.edit().putInt(key, value).commit();
	}


	/**
	 * put the float value to shared preference
	 *
	 * @param key
	 * 		the name of the preference to save
	 * @param value
	 * 		the name of the preference to modify.
	 *
	 * @see android.content.SharedPreferences#edit()#putFloat(String, float)
	 */
	public void putFloat(String key, float value) {
		sharedPreferences.edit().putFloat(key, value).commit();
	}


	/**
	 * put the int value to shared preference
	 *
	 * @param key
	 * 		the name of the preference to save
	 * @param value
	 * 		the name of the preference to modify.
	 *
	 * @see android.content.SharedPreferences#edit()#putLong(String, long)
	 */
	public void putLong(String key, long value) {
		sharedPreferences.edit().putLong(key, value).commit();
	}


	/**
	 * put the int value to shared preference
	 *
	 * @param key
	 * 		the name of the preference to save
	 * @param value
	 * 		the name of the preference to modify.
	 *
	 * @see android.content.SharedPreferences#edit()#putBoolean(String, boolean)
	 */
	public void putBoolean(String key, boolean value) {
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

}
