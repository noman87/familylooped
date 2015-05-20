package com.familylooped.auth;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.MainActivity;
import com.familylooped.R;
import com.familylooped.auth.invitePeople.FragmentManuallyAddContact;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvitePeople#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitePeople extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    public static String TAG = "invite_people";
    private ListView mListView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mFirstName, mLastName, mEmail;
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.DATA
    };
    private ArrayList<ModelInvitePeople> mContactList;
    private String mEmailAddress = "";
    private AdapterInvitePeople mAdapter;
    private ArrayList<ModelInvitePeople> mCheckedList;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvitePeople.
     */
    // TODO: Rename and change types and number of parameters
    public static InvitePeople newInstance(String param1, String param2) {
        InvitePeople fragment = new InvitePeople();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static InvitePeople newInstance(String name, String lastName, String email) {
        InvitePeople fragment = new InvitePeople();
        Bundle args = new Bundle();
        args.putString(FIRST_NAME, name);
        args.putString(LAST_NAME, lastName);
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    public static InvitePeople newInstance() {
        InvitePeople fragment = new InvitePeople();

        return fragment;
    }

    public InvitePeople() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactList = new ArrayList<ModelInvitePeople>();
        if (getArguments() != null) {
            mFirstName = getArguments().getString(FIRST_NAME);
            mLastName = getArguments().getString(LAST_NAME);
            mEmailAddress = getArguments().getString(EMAIL);
            mContactList.add(new ModelInvitePeople(mFirstName + " " + mLastName, mEmailAddress));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite_people, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        ((ImageButton) view.findViewById(R.id.btn_submit)).setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.list_view);
        ((ImageButton) view.findViewById(R.id.btn_invite)).setOnClickListener(this);
        getContacts();

    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite people");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.invite_popup_view, null);
        final TextView textView = (TextView) view.findViewById(R.id.txt_email);
        builder.setView(view);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEmailAddress = textView.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                proceedToSignUp();
                break;
            case R.id.btn_invite:
                changeFragment(FragmentManuallyAddContact.newInstance(), "");
                break;
        }
    }

    private void proceedToSignUp() {
        mCheckedList = new ArrayList<ModelInvitePeople>();
        Gson gson = new GsonBuilder().create();
        for (ModelInvitePeople item : mContactList) {
            if (item.isCheck())
                mCheckedList.add(item);
        }
        JsonArray jsonArray = gson.toJsonTree(mCheckedList).getAsJsonArray();
        Log.d(TAG, "LIST " + jsonArray);
        if (mEmailAddress.length() > 0)
            Signup.urlParams.put("invitedEmails", mEmailAddress.substring(0, mEmailAddress.length() - 1));
        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "signUp", Utilities.BASE_URL + "signUp", Signup.urlParams, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        //changeActivity(MainActivity.class);


                        JSONObject data = object.getJSONObject("userData");
                        Utilities.saveData(getActivity(), Utilities.USER_ID, data.getString("id"));
                        changeActivity(MainActivity.class);


                    } else {
                        showDialog(object.getString("msg"), "OK", "Cancel", new DialogClickListener() {
                            @Override
                            public void onPositiveButtonClick() {

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(request, "signUp");
    }

    private void getContacts() {

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            try {
                final int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
                final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                long contactId;
                String displayName, address;
                while (cursor.moveToNext()) {
                    contactId = cursor.getLong(contactIdIndex);
                    displayName = cursor.getString(displayNameIndex);
                    address = cursor.getString(emailIndex);
                    if (address != null || !TextUtils.isEmpty(address))
                        mContactList.add(new ModelInvitePeople(displayName, address));

                }
            } finally {
                cursor.close();
            }
        }
        mAdapter = new AdapterInvitePeople(getActivity(), mContactList);
        mListView.setAdapter(mAdapter);

    }
}