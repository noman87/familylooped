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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.MainActivity;
import com.familylooped.R;
import com.familylooped.auth.invitePeople.FragmentManuallyAddContact;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.activities.BaseActionBarActivity;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private static final String IS_UPDATE = "is_update";
    public static String TAG = "invite_people";
    private static String IS_ADD_NEW = "is_add_new";
    private ListView mListView;
    private boolean mIsUpdate;

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
    private boolean mIsAddNew;


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

    public static InvitePeople newInstance(String name, String lastName, String email, boolean isAddnew) {
        InvitePeople fragment = new InvitePeople();
        Bundle args = new Bundle();
        args.putString(FIRST_NAME, name);
        args.putString(LAST_NAME, lastName);
        args.putString(EMAIL, email);
        args.putBoolean(IS_ADD_NEW, isAddnew);
        fragment.setArguments(args);
        return fragment;
    }


    public static InvitePeople newInstance(boolean isUpdate) {
        InvitePeople fragment = new InvitePeople();
        Bundle args = new Bundle();
        args.putBoolean(IS_UPDATE, isUpdate);
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
            mIsUpdate = getArguments().getBoolean(IS_UPDATE);
            mIsAddNew = getArguments().getBoolean(IS_ADD_NEW);
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
        mListView = (ListView) view.findViewById(R.id.list_view);
        ((ImageButton) view.findViewById(R.id.btn_submit)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_invite)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_save)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        if (mIsUpdate) {
            ((ImageButton) view.findViewById(R.id.btn_submit)).setVisibility(View.GONE);
            ((ImageButton) view.findViewById(R.id.btn_save)).setVisibility(View.VISIBLE);
            getContactsFromServer();
        } else if (mIsAddNew) {
            ((ImageButton) view.findViewById(R.id.btn_submit)).setVisibility(View.GONE);
            ((ImageButton) view.findViewById(R.id.btn_save)).setVisibility(View.VISIBLE);
            setUpAdapter(mContactList);

        } else {
            getContacts();
        }
    }

    private void getContactsFromServer() {
        Map<String, String> params = new HashMap();
        params.put("userId", Utilities.getSaveData(getActivity(), Utilities.USER_ID));

        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "getContacts", Utilities.BASE_URL + "getContacts", params, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        JSONArray data = object.getJSONArray("data");
                        Gson gson = new Gson();
                        mContactList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            mContactList.add(gson.fromJson(data.getJSONObject(i).toString(), ModelInvitePeople.class));
                        }
                        setUpAdapter(mContactList);

                    } else {
                        showDialog(object.getString("msg"), "Ok", "cancel", new DialogClickListener() {
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
                if (error.getMessage() != null) {

                }
            }
        });

        AppController.getInstance().addToRequestQueue(request, "getContacts");

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
            case R.id.btn_back:
                ((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();
                break;
            case R.id.btn_save:
                sendUpdatedContactToServer();
                break;
        }
    }

    private void sendUpdatedContactToServer() {
        Map<String, String> params = new HashMap();
        params.put("userId", Utilities.getSaveData(getActivity(), Utilities.USER_ID));
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(mContactList).getAsJsonArray();
        params.put("contacts", jsonArray.toString());
        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "updateContacts", Utilities.BASE_URL + "updateContacts", params, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        Utilities.toast(getActivity(), "Your contacts has been saved");

                    } else {
                        showDialog(object.getString("msg"), "Ok", "cancel", new DialogClickListener() {
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
                if (error.getMessage() != null) {

                }
            }
        });

        AppController.getInstance().addToRequestQueue(request, "updateContacts");


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
        Signup.urlParams.put("contacts", jsonArray.toString());
        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "signUp", Utilities.BASE_URL + "signUp", Signup.urlParams, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        //changeActivity(MainActivity.class);


                        JSONObject data = object.getJSONObject("userData");
                        Utilities.saveData(getActivity(), Utilities.USER_ID, data.getString("id"));
                        Utilities.saveData(getActivity(), Utilities.USER_FIRST_NAME, data.getString("firstName"));
                        Utilities.saveData(getActivity(), Utilities.USER_LAST_NAME, data.getString("lastName"));
                        Utilities.saveData(getActivity(), Utilities.USER_PASSWORD, data.getString("password"));
                        Utilities.saveData(getActivity(), Utilities.USER_EMAIL, data.getString("email"));

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
        setUpAdapter(mContactList);

    }

    private void setUpAdapter(ArrayList<ModelInvitePeople> contactList) {
        mAdapter = new AdapterInvitePeople(getActivity(), contactList, mIsUpdate);
        mListView.setAdapter(mAdapter);
    }
}