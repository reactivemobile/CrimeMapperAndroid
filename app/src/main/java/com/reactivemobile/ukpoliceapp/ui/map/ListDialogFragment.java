package com.reactivemobile.ukpoliceapp.ui.map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.reactivemobile.ukpoliceapp.R;
import com.reactivemobile.ukpoliceapp.map.StreetLevelCrimeMapItem;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelCrime;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * DialogFragment for showing details of one or more crimes
 */
public class ListDialogFragment extends DialogFragment {

    public static final String PARAM_CRIME_LIST = "PARAM_CRIME_LIST";
    public static final String PARAM_CATEGORY_MAP = "PARAM_CATEGORY_MAP";

    public static final String TAG = ListDialogFragment.class.getName();

    public ListDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_list, container, false);
        RecyclerView listView = ButterKnife.findById(rootView, R.id.crime_list);
        List<StreetLevelCrimeMapItem> listItems = Parcels.unwrap(getArguments().getParcelable(PARAM_CRIME_LIST));
        HashMap<String, String> categoryMap = Parcels.unwrap(getArguments().getParcelable(PARAM_CATEGORY_MAP));
        CrimeListAdapter crimeListAdapter = new CrimeListAdapter(getActivity(), listItems, categoryMap);
        listView.setAdapter(crimeListAdapter);
        TextView crimeLocationTextView = ButterKnife.findById(rootView, R.id.crime_category);
        String crimeLocation = listItems.get(0).getCrime().getLocation().getStreet().getName();
        crimeLocationTextView.setText(crimeLocation);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private static class CrimeListAdapter extends RecyclerView.Adapter<StreetLevelCrimeMapItemViewHolder> {

        private final List<StreetLevelCrimeMapItem> mListItems;
        private final Context mContext;
        private final HashMap<String, String> mCategoryMap;

        CrimeListAdapter(Context context, List<StreetLevelCrimeMapItem> listItems, HashMap<String, String> categoryMap) {
            mContext = context;
            mListItems = listItems;
            mCategoryMap = categoryMap;
        }

        @Override
        public StreetLevelCrimeMapItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.view_list_item_crime, parent, false);
            return new StreetLevelCrimeMapItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(StreetLevelCrimeMapItemViewHolder holder, int position) {
            StreetLevelCrimeMapItem item = mListItems.get(position);
            StreetLevelCrime crime = item.getCrime();
            String outcome = crime.getOutcome_status() != null ? crime.getOutcome_status().getCategory() : mContext.getString(R.string.no_outcome_available);
            holder.statusTextView.setText(outcome);
            holder.categoryTextView.setText(mContext.getString(R.string.category, mCategoryMap.get(crime.getCategory())));
        }

        @Override
        public int getItemCount() {
            return mListItems.size();
        }
    }


    static class StreetLevelCrimeMapItemViewHolder extends RecyclerView.ViewHolder {
        final TextView categoryTextView;
        final TextView statusTextView;

        StreetLevelCrimeMapItemViewHolder(View itemView) {
            super(itemView);
            categoryTextView = ButterKnife.findById(itemView, R.id.text_crime_category);
            statusTextView = ButterKnife.findById(itemView, R.id.text_crime_outcome_status);
        }
    }
}
