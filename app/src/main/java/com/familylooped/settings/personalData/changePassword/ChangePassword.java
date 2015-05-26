package com.familylooped.settings.personalData.changePassword;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.MainActivity;
import com.familylooped.R;
import com.familylooped.auth.Login;
import com.familylooped.auth.forgotPassword.ForgotPassword;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePassword extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Change Password";
    private static String IS_FORGOT_PASSWORD = "is_forgot_password";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText mOldPassword, mNewPassword, mConfirmPassword;
    private String mPassword = "";
    private boolean mIsForgotPassword;

    public static ChangePassword newInstance(boolean is_forgot_password) {
        ChangePassword fragment = new ChangePassword();
        Bundle args = new Bundle();
        args.putBoolean(IS_FORGOT_PASSWORD, is_forgot_password);
        fragment.setArguments(args);
        return fragment;
    }

    public static ChangePassword newInstance() {
        ChangePassword fragment = new ChangePassword();
        return fragment;
    }

    public ChangePassword() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mIsForgotPassword = getArguments().getBoolean(IS_FORGOT_PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chnage_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_save)).setOnClickListener(this);
        mOldPassword = (EditText) view.findViewById(R.id.txt_old_password);
        mNewPassword = (EditText) view.findViewById(R.id.txt_new_password);
        mConfirmPassword = (EditText) view.findViewById(R.id.txt_confirm_password);
        if (mIsForgotPassword) {
            mOldPassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ((MainActivity) getActivity()).popFragmentIfStackExist();
                break;
            case R.id.btn_save:
                if (!mIsForgotPassword) {
                    if (mOldPassword.getText().toString() != null) {
                        byte[] data = Base64.decode(Utilities.getSaveData(getActivity(), Utilities.USER_PASSWORD), Base64.DEFAULT);
                        try {
                            mPassword = new String(data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    validation();
                } else {
                    validation(true);
                }
                break;
        }
    }

    private String decodeString(String password) {
        // Sending side
        byte[] data = new byte[20];
        try {
            data = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT);

    }

    private void validation() {
        Log.e(TAG, "pASS " + mPassword);
        if (TextUtils.isEmpty(mOldPassword.getText().toString()) || TextUtils.isEmpty(mNewPassword.getText().toString()) || TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
            showDialog("All fields are mandatory to fill", "OK", "Cancel", new DialogClickListener() {
                @Override
                public void onPositiveButtonClick() {

                }
            });
            return;
        } else if (!TextUtils.equals(mOldPassword.getText().toString(), mPassword)) {
            showDialog("You have entered wrong old password.Please try again", "OK", "Cancel", new DialogClickListener() {
                @Override
                public void onPositiveButtonClick() {

                }
            });
            return;
        } else if (!TextUtils.equals(mNewPassword.getText().toString(), mConfirmPassword.getText().toString())) {

            showDialog("New password and old password does not match! please try again", "OK", "Cancel", new DialogClickListener() {
                @Override
                public void onPositiveButtonClick() {

                }
            });
            return;
        } else {
            proceedToChangePassword();
        }

    }

    private void validation(boolean isForgotPassword) {
        if (TextUtils.isEmpty(mNewPassword.getText().toString()) || TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
            showDialog("All fields are mandatory to fill", "OK", "Cancel", new DialogClickListener() {
                @Override
                public void onPositiveButtonClick() {

                }
            });
            return;
        } else if (!TextUtils.equals(mNewPassword.getText().toString(), mConfirmPassword.getText().toString())) {

            showDialog("New password and old password does not match! please try again", "OK", "Cancel", new DialogClickListener() {
                @Override
                public void onPositiveButtonClick() {

                }
            });
            return;
        } else {
            proceedToChangePassword();
        }

    }


    private void proceedToChangePassword() {
        Map<String, String> params = new HashMap();
        if (mIsForgotPassword)
            params.put("userId", ForgotPassword.FORGOT_PASSWORD);
        else
            params.put("userId", Utilities.getSaveData(getActivity(), Utilities.USER_ID));
        params.put("password", mConfirmPassword.getText().toString());
        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "resetPassword", Utilities.BASE_URL + "resetPassword", params, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        Utilities.saveData(getActivity(), Utilities.USER_PASSWORD, decodeString(mConfirmPassword.getText().toString()));
                        Utilities.toast(getActivity(), "Your password has been changed");
                        mConfirmPassword.setText("");
                        mOldPassword.setText("");
                        mNewPassword.setText("");
                        if (mIsForgotPassword) {
                            changeFragmentWithoutBackStack(Login.newInstance(), Login.TAG);
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
        AppController.getInstance().addToRequestQueue(request, "resetPassword");
    }
}
