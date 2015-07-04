package com.ceelites.sharedpreferenceinspector.fragments;


import android.content.Context;
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
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
	private final String[] trueFalse = new String[]{"true", "false"};
	private SharedPreferenceUtils preferenceUtils;
	private String prefName;
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String cancel = "Cancel";
		/**
		 * Checks if entered in test mode. If not, clicking on preferences will prompt user to enter test mode first. If already entered in
		 * test mode,
		 * it will present the UI to change the value. Once it's changed, it will store original value
		 */
		if (preferenceUtils.getBoolean(testModeOpened, false)) {

            final Pair<String, Object> keyValue = (Pair<String, Object>) parent.getItemAtPosition(position);
            final String valueType = keyValue.second.getClass().getSimpleName();

			AlertDialog.Builder builder = new Builder(getActivity());
			View editView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_mode, null);
			final EditText et_value = (EditText) editView.findViewById(R.id.value);
			Spinner type = (Spinner) editView.findViewById(R.id.type);

            et_value.setText(keyValue.second.toString());

			OnItemSelectedListener listener = new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if (typePosition != position) {
						et_value.setText("");
						typePosition = position;
					}

					et_value.setFocusable(true);

					switch (typePosition) {
						case SharedPreferenceUtils.SPINNER_STRING:
							et_value.setInputType(InputType.TYPE_CLASS_TEXT);
							break;
						case SharedPreferenceUtils.SPINNER_INT:
						case SharedPreferenceUtils.SPINNER_LONG:
							et_value.setInputType(InputType.TYPE_CLASS_NUMBER);
							break;
						case SharedPreferenceUtils.SPINNER_FLOAT:
							et_value.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							break;
						case SharedPreferenceUtils.SPINNER_BOOLEAN:
							et_value.setFocusable(false);

							InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(et_value.getWindowToken(), 0);

							et_value.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									AlertDialog.Builder trueFalseBuilder = new Builder(getActivity());
									trueFalseBuilder.setTitle("Choose Option").setItems(trueFalse, new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											et_value.setText(trueFalse[which]);
										}
									}).show();
								}
							});
							break;
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

			if(valueType.equalsIgnoreCase(SharedPreferenceUtils.INT)) {
                typePosition = SharedPreferenceUtils.SPINNER_INT;
				type.setSelection(SharedPreferenceUtils.SPINNER_INT);
			} else if(valueType.equalsIgnoreCase(SharedPreferenceUtils.LONG)){
                typePosition = SharedPreferenceUtils.SPINNER_LONG;
				type.setSelection(SharedPreferenceUtils.SPINNER_LONG);
			} else if(valueType.equalsIgnoreCase(SharedPreferenceUtils.FLOAT)){
                typePosition = SharedPreferenceUtils.SPINNER_FLOAT;
				type.setSelection(SharedPreferenceUtils.SPINNER_FLOAT);
			} else if(valueType.equalsIgnoreCase(SharedPreferenceUtils.BOOLEAN)){
                typePosition = SharedPreferenceUtils.SPINNER_BOOLEAN;
				type.setSelection(SharedPreferenceUtils.SPINNER_BOOLEAN);
			} else if(valueType.equalsIgnoreCase(SharedPreferenceUtils.STRING)){
                typePosition = SharedPreferenceUtils.SPINNER_STRING;
				type.setSelection(SharedPreferenceUtils.SPINNER_STRING);
			}

            final AlertDialog dialog = builder.setTitle("Change Value").setView(editView).setPositiveButton("Set", null).setNegativeButton(cancel, listener2)
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
                                    try {
                                        int number = SharedPreferenceUtils.getNumber(text);
                                        preferenceUtils.putInt(keyValue.first, number);
                                        dialog.dismiss();
                                    } catch(Exception e) {
                                        showToast(SharedPreferenceUtils.INT);
                                    }
                                    break;
                                case SharedPreferenceUtils.SPINNER_LONG:
                                    try {
                                        long numberLong = SharedPreferenceUtils.getNumberLong(text);
                                        preferenceUtils.putLong(keyValue.first, numberLong);
                                        dialog.dismiss();
                                    } catch(Exception e) {
                                        showToast(SharedPreferenceUtils.LONG);
                                    }
                                    break;
                                case SharedPreferenceUtils.SPINNER_BOOLEAN:
                                    boolean value = false;
                                    if (!SharedPreferenceUtils.isEmptyString(text)) {
                                        value = text.toString().equalsIgnoreCase("true");
                                    }
                                    preferenceUtils.putBoolean(keyValue.first, value);
                                    dialog.dismiss();
                                    break;
                                case SharedPreferenceUtils.SPINNER_FLOAT:
                                    try {
                                        float numberFloat = SharedPreferenceUtils.getNumberFloat(text);
                                        preferenceUtils.putFloat(keyValue.first, numberFloat);
                                        dialog.dismiss();
                                    } catch(Exception e) {
                                        showToast(SharedPreferenceUtils.FLOAT);
                                    }
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
					       changeTestMode();
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
			prefName = name;
			preferenceUtils = SharedPreferenceUtils.initWith(getActivity(), name);
		}
		ArrayList<Pair<String, ?>> keyValPair = getKeyValues();

		prefsAdapter = new PrefsAdapter(getActivity(), keyValPair);
		setListAdapter(prefsAdapter);
		preferencesList.setOnItemClickListener(this);
	}

    void showToast(String type) {

        if(toast != null)
            toast.cancel();

        toast = Toast.makeText(getActivity(), "Please enter a valid " + type + ".", Toast.LENGTH_SHORT);
        toast.show();
    }

}
