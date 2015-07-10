package com.familylooped.auth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.R;
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
 * Use the {@link Signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signup extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "signUp";
    public static Map<String, String> urlParams;
    private static String IS_UPDATE = "is_update";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean mIsUpdate;

    private String[] validateStrings = {"firstName", "lastName", "userName", "password",
            "confirm_password"};

    private String[] postStrings = {"firstName", "lastName", "userName", "password",
            "confirm_password"};

    private HashMap<String, EditText> editTextMap = new HashMap<String, EditText>();
    private EditText txt_email/*txt_alternate_email*/;
    private boolean valid_email;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Signup.
     */
    // TODO: Rename and change types and number of parameters
    public static Signup newInstance(String param1, String param2) {
        Signup fragment = new Signup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static Signup newInstance(boolean is_update) {
        Signup fragment = new Signup();
        Bundle args = new Bundle();
        args.putBoolean(IS_UPDATE, is_update);
        fragment.setArguments(args);
        return fragment;
    }

    public static Signup newInstance() {
        Signup fragment = new Signup();
        return fragment;
    }


    public Signup() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsUpdate = getArguments().getBoolean(IS_UPDATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);


    }

    private void checkUserName() {
        Map<String, String> params = new HashMap();
        params.put("userName", txt_email.getText().toString());
        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "checkUserName", Utilities.BASE_URL + "checkUserName2", params, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        ProceedToRegistration();
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

        AppController.getInstance().addToRequestQueue(request, "checkUserName");
    }


    private void init(View view) {
        ((ImageButton) view.findViewById(R.id.btn_next)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_change_password)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_question)).setOnClickListener(this);
        txt_email = (EditText) view.findViewById(R.id.txt_email);
        if (mIsUpdate) {
            ((EditText) view.findViewById(R.id.txt_password)).setVisibility(View.GONE);
            ((EditText) view.findViewById(R.id.txt_confirm_password)).setVisibility(View.GONE);
            ((EditText) view.findViewById(R.id.txt_first_name)).setText(Utilities.getSaveData(getActivity(), Utilities.USER_FIRST_NAME));
            ((EditText) view.findViewById(R.id.txt_last_name)).setText(Utilities.getSaveData(getActivity(), Utilities.USER_LAST_NAME));
            String email[] = Utilities.getSaveData(getActivity(), Utilities.USER_EMAIL).split("@");
            ((EditText) view.findViewById(R.id.txt_email)).setText(email[0]);
            ((EditText) view.findViewById(R.id.txt_first_name)).setKeyListener(null);
            ((EditText) view.findViewById(R.id.txt_last_name)).setKeyListener(null);
            ((EditText) view.findViewById(R.id.txt_first_name)).setKeyListener(null);
            ((EditText) view.findViewById(R.id.txt_email)).setKeyListener(null);
            ((ImageButton) view.findViewById(R.id.btn_change_password)).setVisibility(View.VISIBLE);
            ((ImageButton) view.findViewById(R.id.btn_question)).setVisibility(View.GONE);

        } else {
            LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
            for (int i = 0; i < mainLayout.getChildCount(); i++) {
                if (mainLayout.getChildAt(i) instanceof EditText) {

                    editTextMap.put(mainLayout.getChildAt(i).getTag().toString(),
                            (EditText) mainLayout.getChildAt(i));
                } else if (mainLayout.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout layout = (LinearLayout) mainLayout.getChildAt(i);
                    if (layout.getChildAt(0) instanceof EditText) {
                        editTextMap.put(layout.getChildAt(0).getTag().toString(),
                                (EditText) layout.getChildAt(0));
                    }
                }
            }
        }


    }


    private void validate() {
        View viewFocus = null;
        boolean cancel = false;
        for (int i = validateStrings.length - 1; i > -1; i--) {
            EditText editText = editTextMap.get(validateStrings[i]);
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                editText.setError("This is required field");
                viewFocus = editText;
                cancel = true;
            }
        }
        if (cancel) {
            viewFocus.requestFocus();
            return;
        } else if (!passwordValidation()) {
            return;
        } /*else if (!Utilities.isValidEmail(txt_alternate_email.getText().toString())) {
            txt_alternate_email.setError("Please enter correct email address");
            txt_alternate_email.setText("");
            return;
        }*/ else {
            checkUserName();
        }


    }

    private void ProceedToRegistration() {
        urlParams = new HashMap<>();
        urlParams.put("deviceToken", Utilities.getSaveData(getActivity(), Utilities.REG_ID));
        for (int i = 0; i < postStrings.length; i++) {
            urlParams.put(postStrings[i], editTextMap
                    .get(postStrings[i]).getText().toString());


        }
        changeFragment(SecretQuestion.newInstance(), SecretQuestion.TAG);
    }

    private boolean passwordValidation() {
        EditText txt_password = editTextMap.get("password");
        EditText txt_confirm_password = editTextMap.get("confirm_password");
        if (!TextUtils.equals(txt_password.getText().toString(),
                txt_confirm_password.getText().toString())) {
            txt_confirm_password.setError("Passwords do not match");
            txt_password.setText("");
            txt_confirm_password.setText("");

            return false;
        } else if (txt_password.getText().length() <= 2) {
            txt_password.setError("Passwords should be more than 2 character");
            txt_password.setText("");
            txt_confirm_password.setText("");
            return false;
        } else
            return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (mIsUpdate)
                    changeFragment(SecretQuestion.newInstance(true), SecretQuestion.TAG);
                else
                    validate();
                break;
            case R.id.btn_back:
                ((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();
                break;
            case R.id.btn_change_password:
                changeFragment(ChangePassword.newInstance(), ChangePassword.TAG);
                break;
            case R.id.btn_question:
                showDialog("Choose your preferred username", "OK", "Cancel", new DialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {

                    }

                    @Override
                    public void onDismiss() {

                    }
                });
                break;


        }
    }


}
