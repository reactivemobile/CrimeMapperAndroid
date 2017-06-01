package com.reactivemobile.ukpoliceapp.ui.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.reactivemobile.ukpoliceapp.MainActivity;
import com.reactivemobile.ukpoliceapp.R;

import butterknife.BindView;
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

    @BindView(R.id.checkbox_disclaimer)
    CheckBox mCheckBox;

    @BindView(R.id.proceed_button)
    Button mProceedButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> mWelcomePresenter.setDisclaimerAccepted(isChecked));
        mWelcomePresenter = new WelcomePresenter(this);
        return rootView;
    }

    @Override
    public void disclaimerAccepted(boolean isAccepted) {
        mCheckBox.setChecked(isAccepted);
        mProceedButton.setVisibility(isAccepted ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.proceed_button)
    void proceed() {
        ((MainActivity) getActivity()).showMap();
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
