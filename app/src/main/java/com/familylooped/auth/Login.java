package com.familylooped.auth;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.familylooped.common.logger.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private Spinner mLanguageSpinner;
    private View mView;


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
        mView = view;
        init(view);
        Utilities.hideKeyboard(getActivity(), view);
    }

    private void init(View view) {
        ((ImageButton) view.findViewById(R.id.btn_reg)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_login)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.forgot_password)).setOnClickListener(this);
        txtEmail = (EditText) view.findViewById(R.id.txt_email);
        txtPassword = (EditText) view.findViewById(R.id.txt_password);
        mRememberMe = (CheckBox) view.findViewById(R.id.remember_me);
        mLanguageSpinner = (Spinner) view.findViewById(R.id.spinner_language);
        ArrayList<String> language = new ArrayList<>();
        language.add("Select Language");
        language.add("English");
        language.add("Dutch");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spiner_view, language);
        mLanguageSpinner.setPrompt(getResources().getString(R.string.select_language));
        mLanguageSpinner.setAdapter(spinnerAdapter);
        mLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1:
                        Utilities.saveData(getActivity(), Utilities.USER_LANGUAGE, "en_US");
                        restartInLocale("en_US");
                        break;
                    case 2:
                        Utilities.saveData(getActivity(), Utilities.USER_LANGUAGE, "nl");
                        restartInLocale("nl");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    showDialog(getResources().getString(R.string.email_pass_req), "Ok", "Cancel", new DialogClickListener() {
                        @Override
                        public void onPositiveButtonClick() {

                        }

                        @Override
                        public void onDismiss() {

                        }
                    });
                }

                break;
        }
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("onConfiguration", "Call");

        if (Utilities.getSaveData(getActivity(), Utilities.USER_LANGUAGE) != null) {

            restartInLocale(Utilities.getSaveData(getActivity(), Utilities.USER_LANGUAGE));
        }
        /*String local = Utilities.getSaveData(getActivity(), Utilities.USER_LANGUAGE);
        if (TextUtils.equals(local, "en_US")) {

        } else {

        }*/
        // reFrashFragment();
        // changeFragmentWithoutBackStack(Signup.newInstance(), Signup.TAG, false);

        setText();


    }

    private void setText() {
        txtEmail.setHint(getResources().getString(R.string.str_username));
        txtPassword.setHint(getResources().getString(R.string.str_password));
        ((TextView) mView.findViewById(R.id.forgot_password)).setText(getResources().getString(R.string.str_forgot_password));
        mRememberMe.setText(getResources().getString(R.string.remember_me));
    }

    private void proceedToLogin() {

        Map<String, String> params = new HashMap();
        params.put("userName", mEmail);
        params.put("password", mPassword);
        params.put("deviceToken", Utilities.getSaveData(getActivity(), Utilities.REG_ID));


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

                            @Override
                            public void onDismiss() {

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
