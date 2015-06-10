package com.familylooped.auth.forgotPassword;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.R;
import com.familylooped.auth.SecretQuestion;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.activities.BaseActionBarActivity;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.settings.personalData.changePassword.ChangePassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPassword extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "forgot_password";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText mTxtUsername;
    public static String FORGOT_PASSWORD;
    public static String USER_NAME;

    // TODO: Rename and change types and number of parameters
    public static ForgotPassword newInstance() {
        ForgotPassword fragment = new ForgotPassword();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ForgotPassword() {
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
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ImageButton) view.findViewById(R.id.btn_next)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        mTxtUsername = (EditText) view.findViewById(R.id.txt_username);

    }

    private void sendServerRequest() {
        Map<String, String> params = new HashMap();
        params.put("userName", mTxtUsername.getText().toString());


        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "checkUserName", Utilities.BASE_URL + "checkUserName", params, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        try {
                            JSONObject data = object.getJSONObject("data");
                            FORGOT_PASSWORD = object.getString("userId");
                            USER_NAME = mTxtUsername.getText().toString();
                            changeFragment(ShowSecretQuestion.newInstance(data.toString()), ShowSecretQuestion.TAG);
                        } catch (Exception e) {
                            ((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();
                            showDialog("Your password has been sent to your alternate email address", "Ok", "Cancel", new DialogClickListener() {
                                @Override
                                public void onPositiveButtonClick() {

                                }
                            });

                        }

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
        AppController.getInstance().addToRequestQueue(request, "checkUserName");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (mTxtUsername.getText().toString() != null)
                    sendServerRequest();
                else
                    showDialog("Please enter username", "Ok", "Cancel", new DialogClickListener() {
                        @Override
                        public void onPositiveButtonClick() {

                        }
                    });
                break;
            case R.id.btn_back:
                ((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();
                break;


        }
    }
}
