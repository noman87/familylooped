package com.familylooped.settings.personalData;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.familylooped.MainActivity;
import com.familylooped.R;
import com.familylooped.common.Utilities;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.settings.personalData.changePassword.ChangePassword;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalData extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "Personal Data";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalData.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalData newInstance(String param1, String param2) {
        PersonalData fragment = new PersonalData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static PersonalData newInstance() {
        PersonalData fragment = new PersonalData();

        return fragment;
    }

    public PersonalData() {
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
        return inflater.inflate(R.layout.fragment_personal_data, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.txt_name)).setText(Utilities.USER_FIRST_NAME);

        ((TextView) view.findViewById(R.id.txt_last_name)).setText(Utilities.USER_LAST_NAME);

        ((TextView) view.findViewById(R.id.txt_email)).setText(Utilities.USER_EMAIL);

        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_next)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.btn_change_password)).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ((MainActivity) getActivity()).popFragmentIfStackExist();
                break;
            case R.id.btn_next:
                break;
            case R.id.btn_change_password:
                changeFragment(ChangePassword.newInstance(),ChangePassword.TAG);
        }
    }
}
