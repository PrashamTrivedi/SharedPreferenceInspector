package com.ceelites.sharedpreferenceinspector;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.ceelites.sharedpreferenceinspector.Utils.SharedPreferenceUtils;
import java.util.ArrayList;

/**
 * Created by Prasham on 12-01-2015.
 */
public class PrefsAdapter
		extends BaseAdapter
		implements Filterable {

	private final LayoutInflater inflater;

	private ArrayList<Pair<String, ?>> keyValSet;

	private ArrayList<Pair<String, ?>> filteredCollection;

	public PrefsAdapter(Context context, ArrayList<Pair<String, ?>> keyValSet) {

		this.keyValSet = keyValSet;
		this.filteredCollection = keyValSet;
		inflater = LayoutInflater.from(context);
	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 *
	 * @return Count of items.
	 */
	@Override
	public int getCount() {
		return filteredCollection.size();
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
		return filteredCollection.get(position);
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
        String value = "";
        Object second = keyVal.second;
		if (second != null) {
			value = second.toString() + " (" + second.getClass().getSimpleName() + ")";
		}
		holder.value.setText(value);

		return convertView;
	}

	/**
	 * <p>Returns a filter that can be used to constrain data with a filtering
	 * pattern.</p>
	 *
	 * <p>This method is usually implemented by {@link android.widget.Adapter}
	 * classes.</p>
	 *
	 * @return a filter used to constrain data
	 */
	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				ArrayList<Pair<String, ?>> filteredPrefs = new ArrayList<Pair<String, ?>>();
				FilterResults filterFriends = new FilterResults();
				if (!SharedPreferenceUtils.isEmptyString(constraint)) {
					for (Pair<String, ?> keyValue : keyValSet) {
						String constraintString = constraint.toString();
						String prefixKey = "key: ";
						String prefixValue = "value: ";
						String first = keyValue.first;
						Object second = keyValue.second;
						if (constraintString.toLowerCase().startsWith(prefixKey)) {
							if (first.toLowerCase().contains(constraintString.toLowerCase().substring(prefixKey.length()))) {
								filteredPrefs.add(keyValue);
							}
						} else if (constraintString.toLowerCase().startsWith(prefixValue)) {
							if (second != null) {
								if (second.toString().contains(constraintString.toLowerCase().substring(prefixValue.length()))) {
									filteredPrefs.add(keyValue);
								}
							}
						} else {
							if (first.equalsIgnoreCase(constraintString) || (second != null && second.toString()
							                                                                         .equalsIgnoreCase(constraintString))) {
								filteredPrefs.add(keyValue);
							}
						}
					}
				} else {
					filteredPrefs = keyValSet;
				}
				filterFriends.count = filteredPrefs.size();
				filterFriends.values = filteredPrefs;
				return filterFriends;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				filteredCollection = (ArrayList<Pair<String, ?>>) results.values;

				notifyDataSetChanged();
			}
		};
		return filter;
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
