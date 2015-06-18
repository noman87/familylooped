package com.familylooped.auth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.R;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.activities.BaseActionBarActivity;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecretQuestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecretQuestion extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String IS_UPDATE = "update";
    private String[] mQuestions;
    private EditText txt_alternate_email;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isUpdate;
    private ArrayList<ModelSecretQuestion> mQuestionList;
    public static final String TAG = "secret_question";
    private Spinner spinner, spinner2;
    private EditText txt_ans1, txt_ans2;
    private String mQuestionsId;
    private String mAns, mUsersSelectedQuestion1, mUsersSelectedQuestion2, mUsersSelectedAnswer1, mUsersSelectedAnswer2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecretQuestion.
     */
    // TODO: Rename and change types and number of parameters
    public static SecretQuestion newInstance(String param1, String param2) {
        SecretQuestion fragment = new SecretQuestion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static SecretQuestion newInstance(boolean isUpdate) {
        SecretQuestion fragment = new SecretQuestion();
        Bundle args = new Bundle();
        args.putBoolean(IS_UPDATE, isUpdate);
        fragment.setArguments(args);
        return fragment;
    }

    public static SecretQuestion newInstance() {
        SecretQuestion fragment = new SecretQuestion();

        return fragment;
    }

    public SecretQuestion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isUpdate = getArguments().getBoolean(IS_UPDATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_secret_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        ((ImageButton) view.findViewById(R.id.btn_next)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_save)).setOnClickListener(this);
        txt_alternate_email = (EditText) view.findViewById(R.id.txt_alternate_email);
        spinner = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        txt_ans1 = (EditText) view.findViewById(R.id.txt_ans1);
        txt_ans2 = (EditText) view.findViewById(R.id.txt_ans2);
        getSecretQuestions();
        if (isUpdate) {
            ((ImageButton) view.findViewById(R.id.btn_next)).setVisibility(View.GONE);
            ((ImageButton) view.findViewById(R.id.btn_save)).setVisibility(View.VISIBLE);
        }

    }

    private void getSecretQuestions() {

        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "secretQuestion", Utilities.BASE_URL + "securityQuestions", null, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONArray("data");
                    Gson gson = new Gson();
                    mQuestionList = new ArrayList<>();
                    mQuestions = new String[data.length()];
                    for (int i = 0; i < data.length(); i++) {
                        mQuestionList.add(gson.fromJson(data.getJSONObject(i).toString(), ModelSecretQuestion.class));
                        mQuestions[i] = data.getJSONObject(i).getString("title");

                    }
                    setupAdapter();
                    if (isUpdate)
                        getUsersSecretQuestions();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(request, "secretQuestion");
    }


    private void getUsersSecretQuestions() {

        HashMap<String, String> urlParams = new HashMap<>();
        urlParams.put("userId", Utilities.getSaveData(getActivity(), Utilities.USER_ID));
        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "getUsersSecretQuestions", Utilities.BASE_URL + "userProfile", urlParams, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    txt_alternate_email.setText(data.getString("alterEmail"));
                    JSONArray securityQuestions = data.getJSONArray("securityQuestions");
                    for (int i = 0; i <= 1; i++) {
                        mUsersSelectedQuestion1 = securityQuestions.getJSONObject(i).getString("title");
                        mUsersSelectedAnswer1 = securityQuestions.getJSONObject(i).getString("answer");
                        //mUsersSelectedQuestion2 = securityQuestions.getJSONObject(i).getString("title");

                        //mUsersSelectedAnswer2 = securityQuestions.getJSONObject(i).getString("answer");
                        if (i == 0) {
                            spinner.setSelection(getQuestionPosition(1, mUsersSelectedQuestion1));
                            txt_ans1.setText(mUsersSelectedAnswer1);
                        } else {
                            spinner2.setSelection(getQuestionPosition(2, mUsersSelectedQuestion1));
                            txt_ans2.setText(mUsersSelectedAnswer1);
                        }

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

        AppController.getInstance().addToRequestQueue(request, "getUsersSecretQuestions");


    }


    private int getQuestionPosition(int questionNumber, String strQuestion) {
        String questions[];
        if (questionNumber == 1) {
            questions = getResources().getStringArray(R.array.secret_question_1);
        } else {
            questions = getResources().getStringArray(R.array.secret_question_2);
        }
        for (int i = 0; i < questions.length; i++) {
            if (TextUtils.equals(questions[i], strQuestion)) {
                return i;
            }
        }
        return 0;
    }


    private void setupAdapter() {
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            list1.add(mQuestions[i]);
        }

        for (int i = 3; i <= 5; i++) {
            list2.add(mQuestions[i]);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                R.layout.spiner_view, list1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
                R.layout.spiner_view, list2);
        adapter1.setDropDownViewResource(R.layout.spiner_view);
        adapter2.setDropDownViewResource(R.layout.spiner_view);
        spinner.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                validation();
                break;
            case R.id.btn_back:
                ((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();
                break;
            case R.id.btn_save:
                validation();
                break;
        }
    }

    private void validation() {
        if (TextUtils.isEmpty(txt_ans1.getText().toString()) || TextUtils.isEmpty(txt_ans2.getText().toString())) {
            showDialog("Please provide all answers", "OK", "Cancel", new DialogClickListener() {
                @Override
                public void onPositiveButtonClick() {

                }
            });
            return;
        }


        if (!TextUtils.isEmpty(txt_alternate_email.getText().toString())) {
            if (!Utilities.isValidEmail(txt_alternate_email.getText().toString())) {
                txt_alternate_email.setError("Please enter correct email address");
                txt_alternate_email.setText("");
                return;
            } else {
                Signup.urlParams.put("alterNativeEmail", txt_alternate_email.getText().toString());
            }

        }

        proceedToSignUp();
    }

    private void update() {
        Map<String, String> params = new HashMap();
        params.put("userId", Utilities.getSaveData(getActivity(), Utilities.USER_ID));
        params.put("secretQuestions", mQuestionsId);
        params.put("secretAnswers", mAns);
        if (txt_alternate_email.getText().toString() != null)
            params.put("alterNativeEmail", txt_alternate_email.getText().toString());


        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "updateSecurityQuestions", Utilities.BASE_URL + "updateSecurityQuestions", params, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        Utilities.toast(getActivity(), "Save Successfully");
                        ((BaseActionBarActivity) getActivity()).popFragmentIfStackExist();
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

        AppController.getInstance().addToRequestQueue(request, "updateSecurityQuestions");

    }

    private void proceedToSignUp() {
        mQuestionsId = getId(spinner.getSelectedItem().toString());
        mQuestionsId = mQuestionsId + "," + getId(spinner2.getSelectedItem().toString());
        mAns = txt_ans1.getText().toString();
        mAns = mAns + "," + txt_ans2.getText().toString();

        Log.e("ID ", mQuestionsId);
        Log.e("ANS ", mAns);
        if (!isUpdate) {
            Signup.urlParams.put("secretQuestions", mQuestionsId);
            Signup.urlParams.put("secretAnswers", mAns);
            changeFragment(InvitePeople.newInstance(), InvitePeople.TAG);
        } else {
            update();
        }

    }

    private String getId(String question) {
        for (int i = 0; i < mQuestionList.size(); i++) {
            if (TextUtils.equals(question, mQuestionList.get(i).getTitle())) {
                return mQuestionList.get(i).getId();
            }
        }
        return null;
    }


}
