package com.familylooped.auth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.MainActivity;
import com.familylooped.R;
import com.familylooped.auth.forgotPassword.ForgotPassword;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String TAG = "Login";
    private EditText txtEmail, txtPassword;
    private String mEmail, mPassword;
    private CheckBox mRememberMe;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Login newInstance() {
        Login fragment = new Login();
        return fragment;
    }

    public Login() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        Utilities.hideKeyboard(getActivity(),view);
    }

    private void init(View view) {
        ((ImageButton) view.findViewById(R.id.btn_reg)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_login)).setOnClickListener(this);
        ((TextView)view.findViewById(R.id.forgot_password)).setOnClickListener(this);
        txtEmail = (EditText) view.findViewById(R.id.txt_email);
        txtPassword = (EditText) view.findViewById(R.id.txt_password);
        mRememberMe = (CheckBox) view.findViewById(R.id.remember_me);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reg:
                changeFragment(Signup.newInstance(), Signup.TAG);
                break;
            case R.id.forgot_password:
                changeFragment(ForgotPassword.newInstance(), ForgotPassword.TAG);
                break;
            case R.id.btn_login:
                mEmail = txtEmail.getText().toString();
                mPassword = txtPassword.getText().toString();
                if (!is_empty()) {
                    proceedToLogin();
                } else {
                    showDialog("Email and Password are required", "Ok", "Cancel", new DialogClickListener() {
                        @Override
                        public void onPositiveButtonClick() {

                        }
                    });
                }

                break;
        }
    }

    private void proceedToLogin() {

        Map<String, String> params = new HashMap();
        params.put("userName", mEmail);
        params.put("password", mPassword);
        params.put("deviceToken",Utilities.getSaveData(getActivity(),Utilities.REG_ID));


        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "Login", Utilities.BASE_URL + "login", params, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        JSONObject data = object.getJSONObject("data");
                        Utilities.saveData(getActivity(), Utilities.USER_ID, data.getString("id"));
                        Utilities.saveData(getActivity(), Utilities.USER_FIRST_NAME, data.getString("firstName"));
                        Utilities.saveData(getActivity(), Utilities.USER_LAST_NAME, data.getString("lastName"));
                        Utilities.saveData(getActivity(), Utilities.USER_PASSWORD, data.getString("password"));
                        Utilities.saveData(getActivity(), Utilities.USER_EMAIL, data.getString("email"));
                        Utilities.saveData(getActivity(), Utilities.USER_NAME, data.getString("userName"));



                        Utilities.saveBoolean(getActivity(), Utilities.IS_REMEMBER, mRememberMe.isChecked());

                        changeActivity(MainActivity.class);
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

        AppController.getInstance().addToRequestQueue(request, "Login");
    }

    private void isRememberMe() {


    }

    private boolean is_empty() {

        if (!TextUtils.isEmpty(mEmail) || !TextUtils.isEmpty(mPassword)) {
            return false;
        } else {
            return true;
        }

    }
}
