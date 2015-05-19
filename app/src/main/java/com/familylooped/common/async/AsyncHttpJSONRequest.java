package com.familylooped.common.async;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.familylooped.common.Utilities;
import com.familylooped.common.logger.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AsyncHttpJSONRequest extends JsonRequest<JSONObject> {


    public static final String BODY = " BODY";
    public static final String URL = " URL";
    public static final String RESPONSE = " RESPONSE";
    public static final String ERROR = " ERROR";
    private final String TAG;
    private ProgressDialog mProgressDialog;


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
     * @param jsonRequest     the parameters
     * @param runInBackground true if you want this to run in background with no progress dialog, false otherwise.
     * @param listener        Response Listener
     * @param errorListener   Error Listener
     */
    public AsyncHttpJSONRequest(Context mContext, String TAG, String url, JSONObject jsonRequest, boolean runInBackground,
                                HttpJSONResponseListener listener, Response.ErrorListener errorListener) {
        super(jsonRequest != null ? Method.POST : Method.GET, url,
                (jsonRequest == null) ? null : jsonRequest.toString(),
                listener, errorListener);
        if (!runInBackground) {
            mProgressDialog = createProgressDialog(mContext);
            mProgressDialog.show();
        }
        this.TAG = TAG;

        Log.i(TAG + URL, url);
        Utilities.printJSON(TAG + BODY, jsonRequest);

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
     * @param jsonRequest   the JSON parameters
     * @param listener      Response Listener
     * @param errorListener Error Listener
     */
    public AsyncHttpJSONRequest(Context mContext, String TAG, String url, JSONObject jsonRequest,
                                HttpJSONResponseListener listener, Response.ErrorListener errorListener) {
        super(jsonRequest != null ? Method.POST : Method.GET, url,
                (jsonRequest == null) ? null : jsonRequest.toString(),
                listener, errorListener);
        mProgressDialog = createProgressDialog(mContext);
        mProgressDialog.show();

        this.TAG = TAG;

        Log.i(TAG + URL, url);
        Utilities.printJSON(TAG + BODY, jsonRequest);

    }


    private ProgressDialog createProgressDialog(Context mContext) {
       ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
       // dialog.setContentView(R.layout.progressdialog);
        dialog.setCancelable(false);

        return dialog;

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
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
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
    protected void deliverResponse(JSONObject response) {
        try {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            Utilities.printJSON(TAG + RESPONSE, response);
            super.deliverResponse(response);
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
        try {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();

            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null) {
                String responseErrorData = new String(response.data);
                displayErrorMessage(responseErrorData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.deliverError(error);
    }


    //Somewhere that has access to a context
    public void displayErrorMessage(String str) {
        Log.i(TAG + ERROR, str);
    }

    public interface HttpJSONResponseListener extends Response.Listener<JSONObject> {
        /**
         * Called when a response is received.
         *
         * @param response
         */
        @Override
        void onResponse(JSONObject response);
    }
}
