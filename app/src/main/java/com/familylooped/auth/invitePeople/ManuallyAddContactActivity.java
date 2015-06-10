package com.familylooped.auth.invitePeople;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.familylooped.MainActivity;
import com.familylooped.R;
import com.familylooped.auth.InvitePeople;
import com.familylooped.auth.ModelInvitePeople;
import com.familylooped.common.Utilities;
import com.familylooped.common.activities.BaseActionBarActivity;

import java.util.ArrayList;

public class ManuallyAddContactActivity extends BaseActionBarActivity implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "FragmentManuallyAddContact";
    private String mParam1;
    private String mParam2;
    private ListView mListView;
    private ArrayList<ModelManuallyContact> mDataList;
    private AdapterContactManually mAdapter;
    private EditText mTxtFirstName, mTxtLastName, mTxtEmail;
    private String name = "", lastName = "", email = "";
    private static java.lang.String CONTACT_LIST = "contact_list";
    private ArrayList<ModelInvitePeople> mList;
    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manually_add_contact);
        if (getIntent().getExtras().getString("json") != null) {
            mJsonString = getIntent().getExtras().getString("json");
        }
        init();


    }

    private void init() {
        ((ImageButton) findViewById(R.id.btn_add)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.btn_back)).setOnClickListener(this);
        mTxtFirstName = (EditText) findViewById(R.id.txt_first_name);
        mTxtLastName = (EditText) findViewById(R.id.txt_last_name);
        mTxtEmail = (EditText) findViewById(R.id.txt_email);
        if (mJsonString != null) {
            mList = Utilities.getArrayListFromGSON(mJsonString);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                add();
                break;
            case R.id.btn_back:
                finish();
                // changeFragmentWithoutBackStack(InvitePeople.newInstance(),InvitePeople.TAG);
                //((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();
                break;
        }
    }

    private void add() {
        name = mTxtFirstName.getText().toString();
        lastName = mTxtLastName.getText().toString();
        email = mTxtEmail.getText().toString();
        mList.add(0, new ModelInvitePeople(name + " " + lastName, email));
        Bundle bundle = new Bundle();
        bundle.putString("json", Utilities.getJSON(mList));
        changeActivity(MainActivity.class, bundle);

        //((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();

        //changeFragmentWithoutBackStack(InvitePeople.newInstance(name, lastName, email, true,Utilities.getJSON(mList)), InvitePeople.TAG);
    }
}
