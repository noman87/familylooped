package com.familylooped.slideShow;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.logger.Log;
import com.familylooped.photos.ModelMyPhoto;
import com.google.gson.Gson;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Noman on 5/29/2015.
 */
public class DownloadService extends Service {
    private int mDownloadIndex =0;
    public ArrayList<ModelMyPhoto> mList, mDownloadList;
    private ThinDownloadManager downloadManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
        downloadManager = new ThinDownloadManager();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        download_photos();
        //downloadFile(new ModelMyPhoto("1","http://www.familylooped.com/app/uploads/gallery/1432874085_1.jpg","Noman","12121212"));

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }

    private void download_photos() {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("userId", Utilities.getSaveData(this, Utilities.USER_ID));
        if (Utilities.getSaveData(this, Utilities.PHOTO_TIME) != null) {
            urlParams.put("dateTime", Utilities.getSaveData(this, Utilities.PHOTO_TIME));
        } else {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.ENGLISH);
            String timeString = time.format(c.getTime());
            StringBuffer currentTime = new StringBuffer(timeString);
            currentTime.insert(timeString.indexOf("+"), " ");
            Log.e("formatted string: ", "" + currentTime);
            Utilities.saveData(this, Utilities.PHOTO_TIME, "" + currentTime);
            urlParams.put("dateTime", "2015-05-01 11:01:04 +0500");

        }


        AsyncHttpRequest request = new AsyncHttpRequest(this, "getPictures", Utilities.BASE_URL + "getPictures", urlParams,true, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    mDownloadList = new ArrayList<>();
                    if (TextUtils.equals(object.getString("status"), Utilities.SUCCESS)) {
                        JSONArray data = object.getJSONArray("photos");
                        Gson gson = new Gson();
                        for (int i = 0; i < data.length(); i++) {
                            mDownloadList.add(gson.fromJson(data.getJSONObject(i).toString(), ModelMyPhoto.class));
                        }
                        downloadQueue();
                    } else {

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

        AppController.getInstance().addToRequestQueue(request);
    }

    private void downloadQueue() {
        downloadFile(mDownloadList.get(mDownloadIndex));
    }
    private void downloadFile(final ModelMyPhoto photo) {
        Log.e("Path ","is "+photo.getImage());

        Uri downloadUri = Uri.parse(photo.getImage());
        final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + Utilities.DIR_NAME + System.currentTimeMillis() + "_.jpg";
        Uri destinationUri = Uri.parse(path);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)

                //.addCustomHeader("Auth-Token", "YourTokenApiKey")
                //.setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Log.e("STATS ", "Download complete Success " + id);
                       // mList.add(photo);
                        mDownloadIndex++;
                        if (mDownloadIndex == mDownloadList.size()) {
                            mDownloadIndex = 0;
                        } else {
                            downloadQueue();
                        }
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String message) {
                        Log.e("STATS ", "Download Failed " + id);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, int progress) {
                        Log.e("OnProgress ", "id " + id + " progress " + progress);

                    }
                });
        int downloadId = downloadManager.add(downloadRequest);
    }
}
