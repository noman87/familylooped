package com.familylooped.settings.playSettings;


import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.familylooped.MainActivity;
import com.familylooped.R;
import com.familylooped.common.Utilities;
import com.familylooped.common.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaySettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaySettings extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Play Settings";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadioGroup mRadionGroup, mPhotoRadioGroup;
    private int mValue = 0;
    private int photo_period = 0;
    private TextView mTxtHeadingOne, mTxtHeadingTwo;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaySettings.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaySettings newInstance(String param1, String param2) {
        PlaySettings fragment = new PlaySettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static PlaySettings newInstance() {
        PlaySettings fragment = new PlaySettings();

        return fragment;
    }

    public PlaySettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtHeadingOne = (TextView) view.findViewById(R.id.txt_heading_one);
        mTxtHeadingTwo = (TextView) view.findViewById(R.id.txt_heading_two);
        setText();
        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_save)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        mRadionGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        mRadionGroup.setOnCheckedChangeListener(this);

        mPhotoRadioGroup = (RadioGroup) view.findViewById(R.id.photo_radio_group);
        mPhotoRadioGroup.setOnCheckedChangeListener(this);

        int sliderTime = Utilities.getSavedInt(getActivity(), Utilities.SLIDER_TIME);
        if (sliderTime < 0) {
            sliderTime = 3000;
            Utilities.saveInt(getActivity(), Utilities.SLIDER_TIME, sliderTime);
        }
        switch (sliderTime) {
            case 3000:
                ((RadioButton) view.findViewById(R.id.radio_3_sec)).setChecked(true);
                break;
            case 7000:
                ((RadioButton) view.findViewById(R.id.radio_7_sec)).setChecked(true);
                break;

            case 5000:
                ((RadioButton) view.findViewById(R.id.radio_5_sec)).setChecked(true);
                break;
            case 10000:
                ((RadioButton) view.findViewById(R.id.radio_10_sec)).setChecked(true);
                break;
            case 15000:
                ((RadioButton) view.findViewById(R.id.radio_15_sec)).setChecked(true);
                break;
        }
        int photoPeriod = Utilities.getSavedInt(getActivity(), Utilities.PHOTO_PERIOD);
        if (photoPeriod < 0) {
            Utilities.saveInt(getActivity(), Utilities.PHOTO_PERIOD, Utilities.PHOTO_DAY);
        }
        switch (photoPeriod) {
            case Utilities.PHOTO_DAY:
                ((RadioButton) view.findViewById(R.id.radio_day)).setChecked(true);
                break;
            case Utilities.PHOTO_WEEK:
                ((RadioButton) view.findViewById(R.id.radio_week)).setChecked(true);
                break;
            case Utilities.PHOTO_MONTH:
                ((RadioButton) view.findViewById(R.id.radio_month)).setChecked(true);
                break;
            case Utilities.PHOTO_EVERY_THING:
                ((RadioButton) view.findViewById(R.id.radio_evey_thing)).setChecked(true);
                break;
        }
    }

    private void setText() {
        mTxtHeadingOne.setText(getResources().getString(R.string.setting_photo_top));
        mTxtHeadingTwo.setText(getResources().getString(R.string.setting_photo_bottom));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {


        switch (checkedId) {
            case R.id.radio_3_sec:
                mValue = 3;
                break;

            case R.id.radio_7_sec:
                mValue = 7;
                break;
            case R.id.radio_5_sec:
                mValue = 5;
                break;

            case R.id.radio_10_sec:
                mValue = 10;
                break;

            case R.id.radio_15_sec:
                mValue = 15;
                break;


            case R.id.radio_day:
                photo_period = Utilities.PHOTO_DAY;
                break;


            case R.id.radio_week:
                photo_period = Utilities.PHOTO_WEEK;
                break;


            case R.id.radio_month:
                photo_period = Utilities.PHOTO_MONTH;
                break;

            case R.id.radio_evey_thing:
                photo_period = Utilities.PHOTO_EVERY_THING;
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ((MainActivity) getActivity()).popFragmentIfStackExist();
                break;
            case R.id.btn_save:
                Utilities.saveInt(getActivity(), Utilities.SLIDER_TIME, mValue * 1000);
                Utilities.saveInt(getActivity(), Utilities.PHOTO_PERIOD, photo_period);
                Utilities.toast(getActivity(), "You settings has been saved");
                ((MainActivity) getActivity()).popFragmentIfStackExist();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Utilities.getSaveData(getActivity(), Utilities.USER_LANGUAGE) != null) {
            restartInLocale(Utilities.getSaveData(getActivity(), Utilities.USER_LANGUAGE));
        }
        setText();
    }
}
