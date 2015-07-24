package com.ceelites.sharedpreferenceinspector.fragments;


import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.ceelites.devutils.ConstantMethods;
import com.ceelites.sharedpreferenceinspector.PrefsAdapter;
import com.ceelites.sharedpreferenceinspector.R;
import com.ceelites.sharedpreferenceinspector.Utils.SharedPreferenceUtils;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Lists all the shared preferences stored in a given shared preference file. Created by Prasham on 25-01-2015.
 */
public class SharedPreferencesItem
		extends ListFragment
		implements OnItemClickListener {

	private final String testModeOpened = "test_mode_opened";
	private SharedPreferenceUtils preferenceUtils;
	private PrefsAdapter prefsAdapter;
	private int typePosition = 0;
	private MenuItem testMode;

	Toast toast;

	/**
	 * Instantiates new fragment.
	 *
	 * @param bundle
	 * 		: Any extra data to be passed to fragment, pass null if you don't want to pass anything.
	 *
	 * @return : Instance of current fragment.
	 */
	public static SharedPreferencesItem newInstance(@NonNull Bundle bundle) {
		SharedPreferencesItem sharedPreferencesItem = new SharedPreferencesItem();
		sharedPreferencesItem.setArguments(bundle);
		return sharedPreferencesItem;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_shared_preference_browser, menu);
		testMode = menu.findItem(R.id.action_testmode);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (!preferenceUtils.getBoolean(testModeOpened, false)) {
			testMode.setTitle(R.string.action_test_mode_enter);
		} else {
			testMode.setTitle(R.string.action_test_mode_exit);

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_testmode) {
			getActivity().supportInvalidateOptionsMenu();
			changeTestMode();

		}
		if (id == R.id.action_search) {
			SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
			if (searchView != null) {
				searchView.setQueryHint(getString(R.string.searchHint));
				searchView.setOnQueryTextListener(new OnQueryTextListener() {
					@Override
					public boolean onQueryTextSubmit(String s) {
						search(s);
						return true;
					}

					@Override
					public boolean onQueryTextChange(String s) {
						search(s);
						return true;
					}
				});
			}
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Toggle test modes. If test mode is already open, it will restore the original data before toggling.
	 */
	private void changeTestMode() {
		boolean testMode = preferenceUtils.getBoolean(testModeOpened, false);
		if (testMode) {
			restoreData();
		}
		preferenceUtils.putBoolean(testModeOpened, !testMode);
	}

	private void search(String s) {
		prefsAdapter.getFilter().filter(s);
	}

	/**
	 * Restore values of original keys stored.
	 */
	private void restoreData() {
		Map<String, ?> map = preferenceUtils.getAll();
		Set<String> strings = map.keySet();

		for (String string : strings) {
			if (string.startsWith(SharedPreferenceUtils.keyTestMode)) {
				preferenceUtils.restoreKey(string);
			}
		}

		refreshKeyValues();

	}

	/**
	 * Refresh key values.
	 */
	private void refreshKeyValues() {
		prefsAdapter.setKeyValues(getKeyValues());
	}

	/**
	 * Get Key value pair for given shared preference files
	 *
	 * @return : Pair of key value files from given shared preferences.
	 */
	private ArrayList<Pair<String, ?>> getKeyValues() {
		ArrayList<Pair<String, ?>> keyValPair = new ArrayList<>();
		Map<String, ?> map = preferenceUtils.getAll();
		Set<String> strings = map.keySet();
		Object value;
		for (String key : strings) {
			if (!key.contains(SharedPreferenceUtils.keyTestMode)) {
				value = map.get(key);
				keyValPair.add(new Pair<String, Object>(key, value));
			}
		}
		return keyValPair;
	}

	/**
	 * Callback method to be invoked when an item in this AdapterView has been clicked.
	 * <p/>
	 * Implementers can call getItemAtPosition(position) if they need to access the data associated with the selected item.
	 *
	 * @param parent
	 * 		The AdapterView where the click happened.
	 * @param view
	 * 		The view within the AdapterView that was clicked (this will be a view provided by the adapter)
	 * @param position
	 * 		The position of the view in the adapter.
	 * @param id
	 * 		The row id of the item that was clicked.
	 */
	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		String cancel = "Cancel";
		/**
		 * Checks if entered in test mode. If not, clicking on preferences will prompt user to enter test mode first. If already entered in
		 * test mode,
		 * it will present the UI to change the value. Once it's changed, it will store original value
		 */
		if (preferenceUtils.getBoolean(testModeOpened, false)) {

			final Pair<String, Object> keyValue = (Pair<String, Object>) parent.getItemAtPosition(position);
			Object second = keyValue.second;
			final String valueType = second.getClass().getSimpleName();

			AlertDialog.Builder builder = new Builder(getActivity());
			View editView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_mode, null);
			final EditText et_value = (EditText) editView.findViewById(R.id.value);
			final SwitchCompat booleanSwitch = (SwitchCompat) editView.findViewById(R.id.switchBoolean);
			Spinner type = (Spinner) editView.findViewById(R.id.type);

			final String value = second.toString();
			et_value.setText(value);


			OnItemSelectedListener listener = new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if (typePosition != position) {
						et_value.setText("");
						typePosition = position;
					}
					booleanSwitch.setVisibility(View.GONE);
					et_value.setVisibility(View.VISIBLE);

					if (typePosition == SharedPreferenceUtils.SPINNER_STRING) {
						et_value.setInputType(InputType.TYPE_CLASS_TEXT);

					} else if (typePosition == SharedPreferenceUtils.SPINNER_INT || typePosition == SharedPreferenceUtils.SPINNER_LONG) {
						et_value.setInputType(InputType.TYPE_CLASS_NUMBER);
					} else if (typePosition == SharedPreferenceUtils.SPINNER_FLOAT) {
						et_value.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

					} else if (typePosition == SharedPreferenceUtils.SPINNER_BOOLEAN) {
						et_value.setVisibility(View.GONE);
						booleanSwitch.setVisibility(View.VISIBLE);
						boolean isPreferenceTrue = !ConstantMethods.isEmptyString(value) && value.equalsIgnoreCase("true");
						booleanSwitch.setText(isPreferenceTrue? "true" : "false");
						booleanSwitch.setChecked(isPreferenceTrue);
						booleanSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								buttonView.setText(isChecked ? "true" : "false");
							}
						});
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					typePosition = 0;
				}
			};
			type.setOnItemSelectedListener(listener);

			OnClickListener listener2 = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//TODO: Hell nothing right now.
				}
			};

			OnClickListener clearListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					storeOriginal(keyValue);
					preferenceUtils.clear(keyValue.first);
					refreshKeyValues();
				}
			};

			if (valueType.equalsIgnoreCase(SharedPreferenceUtils.INT)) {
				typePosition = SharedPreferenceUtils.SPINNER_INT;
				type.setSelection(SharedPreferenceUtils.SPINNER_INT);
			} else if (valueType.equalsIgnoreCase(SharedPreferenceUtils.LONG)) {
				typePosition = SharedPreferenceUtils.SPINNER_LONG;
				type.setSelection(SharedPreferenceUtils.SPINNER_LONG);
			} else if (valueType.equalsIgnoreCase(SharedPreferenceUtils.FLOAT)) {
				typePosition = SharedPreferenceUtils.SPINNER_FLOAT;
				type.setSelection(SharedPreferenceUtils.SPINNER_FLOAT);
			} else if (valueType.equalsIgnoreCase(SharedPreferenceUtils.BOOLEAN)) {
				typePosition = SharedPreferenceUtils.SPINNER_BOOLEAN;
				type.setSelection(SharedPreferenceUtils.SPINNER_BOOLEAN);
			} else if (valueType.equalsIgnoreCase(SharedPreferenceUtils.STRING)) {
				typePosition = SharedPreferenceUtils.SPINNER_STRING;
				type.setSelection(SharedPreferenceUtils.SPINNER_STRING);
			}

			final AlertDialog dialog =
					builder.setTitle("Change Value").setView(editView).setPositiveButton("Set", null).setNegativeButton(cancel, listener2)
					       .setNeutralButton("Clear", clearListener).create();

			dialog.setOnShowListener(new DialogInterface.OnShowListener() {

				@Override
				public void onShow(DialogInterface dialog1) {

					Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View view) {

							storeOriginal(keyValue);
							Editable text = et_value.getText();

							switch (typePosition) {
								case SharedPreferenceUtils.SPINNER_STRING:
									preferenceUtils.putString(keyValue.first, String.valueOf(text));
									dialog.dismiss();
									break;
								case SharedPreferenceUtils.SPINNER_INT:
									int number = SharedPreferenceUtils.getNumber(text);
									preferenceUtils.putInt(keyValue.first, number);
									dialog.dismiss();
									break;
								case SharedPreferenceUtils.SPINNER_LONG:
									long numberLong = SharedPreferenceUtils.getNumberLong(text);
									preferenceUtils.putLong(keyValue.first, numberLong);
									dialog.dismiss();
									break;
								case SharedPreferenceUtils.SPINNER_BOOLEAN:
									boolean value = booleanSwitch.isChecked();
									preferenceUtils.putBoolean(keyValue.first, value);
									dialog.dismiss();
									break;
								case SharedPreferenceUtils.SPINNER_FLOAT:
									float numberFloat = SharedPreferenceUtils.getNumberFloat(text);
									preferenceUtils.putFloat(keyValue.first, numberFloat);
									dialog.dismiss();
									break;
							}
							refreshKeyValues();
						}
					});
				}
			});

			dialog.show();
		} else {
			AlertDialog.Builder builder = new Builder(getActivity());
			builder.setTitle("Test mode not enabled")
			       .setMessage("If you want to edit value for testing, testing mode should be enabled. It's available in options menu")
			       .setPositiveButton("Enable test mode", new OnClickListener() {
				       @Override
				       public void onClick(DialogInterface dialog, int which) {
					       getActivity().supportInvalidateOptionsMenu();
					       changeTestMode();
					       onItemClick(parent, view, position, id);
				       }
			       }).setNegativeButton(cancel, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					dialog.cancel();
				}
			}).show();
		}
	}

	/**
	 * Store original value before it's changed in test mode. It will take care not to over write if original value is already stored.
	 *
	 * @param keyValue
	 * 		: Pair of key and original value.
	 */
	private void storeOriginal(Pair<String, Object> keyValue) {
		String key = SharedPreferenceUtils.keyTestMode + keyValue.first;

		if (!preferenceUtils.isValueExistForKey(key)) {
			preferenceUtils.put(key, keyValue.second);

		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		String name = getArguments().getString("name");

		ListView preferencesList = getListView();

		if (SharedPreferenceUtils.isEmptyString(name)) {
			preferenceUtils = SharedPreferenceUtils.initWith(getActivity(), null);
		} else {
			preferenceUtils = SharedPreferenceUtils.initWith(getActivity(), name);
		}
		ArrayList<Pair<String, ?>> keyValPair = getKeyValues();

		prefsAdapter = new PrefsAdapter(getActivity(), keyValPair);
		setListAdapter(prefsAdapter);
		preferencesList.setOnItemClickListener(this);
	}


}
