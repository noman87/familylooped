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
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private String[] mQuestions;
    private EditText txt_alternate_email;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<ModelSecretQuestion> mQuestionList;
    public static final String TAG = "secret_question";
    private Spinner spinner, spinner2, spinner3;
    private EditText txt_ans1, txt_ans2, txt_ans3;
    private String mQuestionsId;
    private String mAns;


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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        txt_alternate_email = (EditText) view.findViewById(R.id.txt_alternate_email);
        spinner = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        spinner3 = (Spinner) view.findViewById(R.id.spinner3);
        txt_ans1 = (EditText) view.findViewById(R.id.txt_ans1);
        txt_ans2 = (EditText) view.findViewById(R.id.txt_ans2);
        txt_ans3 = (EditText) view.findViewById(R.id.txt_ans3);
        getSecretQuestions();

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

    private void setupAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spiner_view, mQuestions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner2.setSelection(1);
        spinner3.setAdapter(adapter);
        spinner3.setSelection(2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                validation();
                break;
            case R.id.btn_back:
                ((AuthActivity)getActivity()).popFragmentIfStackExist();
                break;
        }
    }

    private void validation() {
        ArrayList<EditText> editTexts = new ArrayList<>();
        editTexts.add(txt_ans1);
        editTexts.add(txt_ans2);
        editTexts.add(txt_ans3);
        int count = 0;
        for (int i = 0; i < editTexts.size(); i++) {
            if (TextUtils.isEmpty(editTexts.get(i).getText().toString())) {
                count++;
                if (count == 2) {
                    showDialog("At least 2 security question is to be answered is must", "OK", "Cancel", new DialogClickListener() {
                        @Override
                        public void onPositiveButtonClick() {

                        }
                    });
                    return;
                }
            }


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

    private void proceedToSignUp() {
        mQuestionsId = getId(spinner.getSelectedItem().toString());
        mQuestionsId = mQuestionsId + "," + getId(spinner2.getSelectedItem().toString());
        mQuestionsId = mQuestionsId + "," + getId(spinner3.getSelectedItem().toString());
        mAns = txt_ans1.getText().toString();
        mAns = mAns + "," + txt_ans2.getText().toString();
        mAns = mAns + "," + txt_ans3.getText().toString();
        Signup.urlParams.put("secretQuestions ", mQuestionsId);
        Signup.urlParams.put("secretAnswers", mAns);
        Log.e("ID ", mQuestionsId);
        Log.e("ANS ", mAns);
        changeFragment(InvitePeople.newInstance(), InvitePeople.TAG);

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
