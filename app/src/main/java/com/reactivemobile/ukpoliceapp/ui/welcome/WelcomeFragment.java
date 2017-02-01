package com.reactivemobile.ukpoliceapp.ui.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.reactivemobile.ukpoliceapp.MainActivity;
import com.reactivemobile.ukpoliceapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment implementing WelcomeViewContract functionality
 */
public class WelcomeFragment extends Fragment implements WelcomeContract.WelcomeViewContract {

    public static final String TAG = WelcomeFragment.class.getName();

    private WelcomeContract.WelcomePresenterContract mWelcomePresenter;

    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        mWelcomePresenter = new WelcomePresenter(getActivity());

        mUnbinder = ButterKnife.bind(this, rootView);

        CheckBox disclaimer_checkbox = ButterKnife.findById(rootView, R.id.checkbox_disclaimer);
        final View proceedButton = ButterKnife.findById(rootView, R.id.button_use_current_location);

        disclaimer_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            proceedButton.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            mWelcomePresenter.saveDisclaimerState(isChecked);
        });

        disclaimer_checkbox.setChecked(mWelcomePresenter.isDisclaimerAccepted());

        return rootView;
    }

    @OnClick(R.id.button_use_current_location)
    void proceed() {
        ((MainActivity) getActivity()).showMap();
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
