package com.familylooped.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.familylooped.R;
import com.familylooped.common.Utilities;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.photos.MyPhotos;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "Settings";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadioGroup mRadionGroup;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Settings newInstance() {
        Settings fragment = new Settings();
        return fragment;
    }

    public Settings() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        ((ImageButton) view.findViewById(R.id.btn_myphoto)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_restart_pass)).setOnClickListener(this);
        mRadionGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        mRadionGroup.setOnCheckedChangeListener(this);
        int sliderTime = Utilities.getSavedInt(getActivity(), Utilities.SLIDER_TIME);
        if (sliderTime < 0) {
            Utilities.saveInt(getActivity(), Utilities.SLIDER_TIME, 3000);
        }
        switch (sliderTime) {
            case 3000:
                ((RadioButton) view.findViewById(R.id.radio_3_sec)).setChecked(true);
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


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_myphoto:
                changeFragment(MyPhotos.newInstance(), MyPhotos.TAG);
                break;

            case R.id.btn_restart_pass:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int value = 0;
        switch (checkedId) {
            case R.id.radio_3_sec:
                value = 3;
                break;

            case R.id.radio_5_sec:
                value = 5;
                break;

            case R.id.radio_10_sec:
                value = 10;
                break;

            case R.id.radio_15_sec:
                value = 15;
                break;
        }

        Utilities.saveInt(getActivity(), Utilities.SLIDER_TIME, value * 1000);
    }
}
