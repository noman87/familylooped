package com.familylooped.common.async;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.familylooped.R;
import com.familylooped.common.Utilities;
import com.familylooped.common.logger.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class AsyncHttpRequest extends Request<String> {


    public static final int TIMEOUT_MS = 300000;
    private static final String PARAMS = " PARAMS";
    private static final String RESPONSE = " RESPONSE";
    private static final String ERROR = " ERROR";
    private static final String URL = " URL";
    private final Map<String, String> params;
    private final HttpResponseListener listener;
    private final String TAG;
    private Dialog mProgressDialog;
    private Context mContext;


    /**
     * Creates a new request with the given method (the Context of the calling object,
     * the tag for printing,
     * the url to hit,
     * the parameters to send {set @null if no parameters},
     * Response Listener,
     * Error Listener.
     * <p/>
     * Note that the normal response listener is not provided here as
     * delivery of responses is provided by subclasses, who have a better idea of how to deliver
     * an already-parsed response.
     *
     * @param mContext        The Context
     * @param TAG             the tag for printing purposes
     * @param url             the url
     * @param params          the parameters
     * @param runInBackground true if you want this to run in background with no progress dialog, false otherwise.
     * @param listener        Response Listener
     * @param errorListener   Error Listener
     */
    public AsyncHttpRequest(Context mContext, String TAG, String url, Map<String, String> params, boolean runInBackground,
                            HttpResponseListener listener, Response.ErrorListener errorListener) {
        super(params != null ? Request.Method.POST : Request.Method.GET, url, errorListener);
        this.params = params;
        this.listener = listener;
        this.mContext = mContext;
        if (!runInBackground) {
            mProgressDialog = createProgressDialog(mContext);
            mProgressDialog.show();
        }

        if (params != null)
            setDefaultParams();
        this.TAG = TAG;

        Log.w(TAG + URL, url);

        if (params != null)
            try {
                printHashMap();
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }


    /**
     * Creates a new request with the given method (the Context of the calling object,
     * the tag for printing,
     * the url to hit,
     * the parameters to send {set @null if no parameters},
     * Response Listener,
     * Error Listener.
     * <p/>
     * Note that the normal response listener is not provided here as
     * delivery of responses is provided by subclasses, who have a better idea of how to deliver
     * an already-parsed response.
     *
     * @param mContext      The Context
     * @param TAG           the tag for printing purposes
     * @param url           the url
     * @param params        the parameters
     * @param listener      Response Listener
     * @param errorListener Error Listener
     */
    public AsyncHttpRequest(Context mContext, String TAG, String url, Map<String, String> params,
                            HttpResponseListener listener, Response.ErrorListener errorListener) {
        super(params != null ? Request.Method.POST : Request.Method.GET, url, errorListener);
        this.params = params;
        this.mContext = mContext;
        this.listener = listener;
        mProgressDialog = createProgressDialog(mContext);
        mProgressDialog.show();

        if (params != null)
            setDefaultParams();
        this.TAG = TAG;

        Log.w(TAG + URL, url);

        if (params != null)
            try {
                printHashMap();
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    private void printHashMap() throws JSONException {
        JSONArray paramArray = new JSONArray();
        JSONObject param;
        for (String key : params.keySet()) {
            param = new JSONObject();
            param.put(key, params.get(key));
            paramArray.put(param);
        }
        Gson gson = new Gson();
        String jsonStr = gson.toJson(paramArray);
        Log.w(TAG + PARAMS, jsonStr);
    }

    private void setDefaultParams() {
        params.put("deviceId", Utilities.getDeviceId(mContext));
        params.put("deviceType", "ANDROID");


    }


    private Dialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        dialog.setProgress(ProgressDialog.STYLE_SPINNER);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
        dialog.setContentView(R.layout.progressdialog);
        dialog.setCancelable(false);
            return  dialog;
        //return new TransparentProgressDialog(mContext);
    }

    /**
     * Subclasses must implement this to parse the raw network response
     * and return an appropriate response type. This method will be
     * called from a worker thread.  The response will not be delivered
     * if you return null.
     *
     * @param response Response from the network
     * @return The parsed response, or null in the case of an error
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    /**
     * Subclasses must implement this to perform delivery of the parsed
     * response to their listeners.  The given response is guaranteed to
     * be non-null; responses that fail to parse are not delivered.
     *
     * @param response The parsed response returned by
     *                 {@link #parseNetworkResponse(com.android.volley.NetworkResponse)}
     */
    @Override
    protected void deliverResponse(String response) {
        try {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            Utilities.printJSON(TAG + RESPONSE, response);
            listener.onResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delivers error message to the ErrorListener that the Request was
     * initialized with.
     *
     * @param error Error details
     */
    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        try {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null) {
                String responseErrorData = new String(response.data);
                displayErrorMessage(responseErrorData);
            }
            if (error instanceof NoConnectionError) {
                if(!error.getMessage().contains("javax.net.ssl.SSLHandshakeException")) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns a Map of parameters to be used for a POST or PUT request.  Can throw
     * {@link com.android.volley.AuthFailureError} as authentication may be required to provide these values.
     * <p/>
     * <p>Note that you can directly override {@link #getBody()} for custom data.</p>
     *
     * @throws com.android.volley.AuthFailureError in the event of auth failure
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }


    //Somewhere that has access to a context
    public void displayErrorMessage(String str) {
        Log.w(TAG + ERROR, str);
    }

    public interface HttpResponseListener extends Response.Listener<String> {
        /**
         * Called when a response is received.
         *
         * @param response
         */
        @Override
        void onResponse(String response);
    }


}
