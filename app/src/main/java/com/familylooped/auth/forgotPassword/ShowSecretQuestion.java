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
import android.widget.TextView;

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
 * Use the {@link ShowSecretQuestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowSecretQuestion extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String JSON_STRING = "json_string";
    public static final String TAG = "show secret question";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mJsonString;
    private View mView;
    private String mQuestionId;
    private EditText mTxtAnswer;

    public static ShowSecretQuestion newInstance(String jsonString) {
        ShowSecretQuestion fragment = new ShowSecretQuestion();
        Bundle args = new Bundle();
        args.putString(JSON_STRING, jsonString);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowSecretQuestion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mJsonString = getArguments().getString(JSON_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_secret_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ImageButton) view.findViewById(R.id.btn_next)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_back)).setOnClickListener(this);
        mTxtAnswer = (EditText)view.findViewById(R.id.txt_ans);
        mView = view;
        parseJsonData();
    }

    private void sendServerRequest() {
        Map<String, String> params = new HashMap();
        params.put("questionId", mQuestionId);
        params.put("answer", mTxtAnswer.getText().toString());



        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "answerSecuriyQuestion", Utilities.BASE_URL + "answerSecuriyQuestion", params, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        changeFragment(ChangePassword.newInstance(true),ChangePassword.TAG);

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
        AppController.getInstance().addToRequestQueue(request, "answerSecuriyQuestion");
    }


    private void parseJsonData() {

        try {
            JSONObject object = new JSONObject(mJsonString);
            ((TextView) mView.findViewById(R.id.txt_sec_question)).setText(object.getString("title"));
            mQuestionId = object.getString("id");

        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (mTxtAnswer.getText().toString() != null)
                    sendServerRequest();
                else
                    showDialog("Please enter you answer", "Ok", "Cancel", new DialogClickListener() {
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
