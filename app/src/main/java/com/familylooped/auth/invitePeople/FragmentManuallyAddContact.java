package com.familylooped.auth.invitePeople;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.familylooped.R;
import com.familylooped.common.fragments.BaseFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentManuallyAddContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentManuallyAddContact extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView mListView;
    private ArrayList<ModelManuallyContact> mDataList
            ;
    private AdapterContactManually mAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentManuallyAddContact.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentManuallyAddContact newInstance(String param1, String param2) {
        FragmentManuallyAddContact fragment = new FragmentManuallyAddContact();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentManuallyAddContact newInstance() {
        FragmentManuallyAddContact fragment = new FragmentManuallyAddContact();
        return fragment;
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
        return inflater.inflate(R.layout.fragment_manually_add_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        ((ImageButton)view.findViewById(R.id.btn_add)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_add_raw)).setOnClickListener(this);
        mListView = (ListView)view.findViewById(R.id.list_view);
        mDataList = new ArrayList<ModelManuallyContact>();
        mAdapter = new AdapterContactManually(getActivity(),mDataList);
        mDataList.add(new ModelManuallyContact("","",""));
        mListView.setAdapter(mAdapter);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_raw:
                addNewRaw();
                break;
        }
    }

    private void addNewRaw() {
        mDataList.add(0,new ModelManuallyContact("","",""));
        mAdapter.notifyDataSetChanged();
    }
}
