package com.ceelites.sharedpreferenceinspector;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Prasham on 12-01-2015.
 */
public class PrefsAdapter
		extends BaseAdapter {

	private final LayoutInflater inflater;
	private ArrayList<Pair<String, ?>> keyValSet;

	public PrefsAdapter(Context context, ArrayList<Pair<String, ?>> keyValSet) {

		this.keyValSet = keyValSet;
		inflater = LayoutInflater.from(context);
	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 *
	 * @return Count of items.
	 */
	@Override
	public int getCount() {
		return keyValSet.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 *
	 * @param position
	 * 		Position of the item whose data we want within the adapter's data set.
	 *
	 * @return The data at the specified position.
	 */
	@Override
	public Pair<String, ?> getItem(int position) {
		return keyValSet.get(position);
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 *
	 * @param position
	 * 		The position of the item within the adapter's data set whose row id we want.
	 *
	 * @return The id of the item at the specified position.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Get a View that displays the data at the specified position in the data set. You can either create a View manually or inflate it from an XML
	 * layout file. When the View is inflated, the parent View (GridView, ListView...) will apply default layout parameters unless you use {@link
	 * android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)} to specify a root view and to prevent attachment to the root.
	 *
	 * @param position
	 * 		The position of the item within the adapter's data set of the item whose view we want.
	 * @param convertView
	 * 		The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not
	 * 		possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of
	 * 		view types, so that this View is always of the right type (see {@link #getViewTypeCount()} and {@link #getItemViewType(int)}).
	 * @param parent
	 * 		The parent that this view will eventually be attached to
	 *
	 * @return A View corresponding to the data at the specified position.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Pair<String, ?> keyVal = getItem(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.preference_item, parent, false);
			holder.key = (TextView) convertView.findViewById(R.id.key);
			holder.value = (TextView) convertView.findViewById(R.id.value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.key.setText(keyVal.first);
		Object second = keyVal.second;
		holder.value.setText(second.toString() + " (" + second.getClass().getSimpleName() + ")");

		return convertView;
	}

	public void setKeyValues(ArrayList<Pair<String, ?>> keyValues) {
		this.keyValSet = keyValues;
		notifyDataSetChanged();
	}

	class ViewHolder {
		TextView key;
		TextView value;
	}
}
