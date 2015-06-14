package com.familylooped.slideShow;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.MainActivity;
import com.familylooped.R;
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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Noman on 5/29/2015.
 */
public class DownloadService extends Service {
    private int mDownloadIndex = 0;
    public ArrayList<ModelMyPhoto> mList, mDownloadList;
    private ThinDownloadManager downloadManager;
    private NotificationManager mNotifyManager;
    private Builder mBuilder;
    private int mNotificationId = 1;
    Gson gson = new Gson();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
        downloadManager = new ThinDownloadManager();
        initializePhotoList();


    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        //Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        showNotification();
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
        String url = "getPictures?userId=" + Utilities.getSaveData(this, Utilities.USER_ID) + "&";
        if (Utilities.getSaveData(this, Utilities.PHOTO_TIME) != null) {
            urlParams.put("dateTime", Utilities.getEncodedString(Utilities.getSaveData(this, Utilities.PHOTO_TIME)));
            url = url + "dateTime=" + Utilities.getEncodedString(Utilities.getSaveData(this, Utilities.PHOTO_TIME));
        } else {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.ENGLISH);
            String timeString = time.format(c.getTime());
            StringBuffer currentTime = new StringBuffer(timeString);
            currentTime.insert(timeString.indexOf("+"), " ");
            Log.e("formatted string: ", "" + currentTime);
            Utilities.saveData(this, Utilities.PHOTO_TIME, "" + currentTime);
            //urlParams.put("dateTime", Utilities.getEncodedString("2015-05-01 11:01:04 +0500"));
            url = url + "dateTime=" + Utilities.getEncodedString("2015-05-01 11:01:04 +0500");
            // urlParams.put("dateTime", "2015-06-01 11:01:04 +0500");

        }


        AsyncHttpRequest request = new AsyncHttpRequest(this, "getPictures", Utilities.BASE_URL + url, null, true, new AsyncHttpRequest.HttpResponseListener() {
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
                        mBuilder.setContentText("Download in progress " + mDownloadIndex + " / " + mDownloadList.size());
                        updateDate();

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

    private void updateDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.ENGLISH);
        String timeString = time.format(c.getTime());
        StringBuffer currentTime = new StringBuffer(timeString);
        currentTime.insert(timeString.indexOf("+"), " ");
        Log.e("formatted string: ", "" + currentTime);
        Utilities.saveData(this, Utilities.PHOTO_TIME, "" + currentTime);
    }

    private void downloadQueue() {
        downloadFile(mDownloadList.get(mDownloadIndex));
    }

    private void downloadFile(final ModelMyPhoto photo) {
        //Log.e("Path ", "is " + photo.getImage());

        Uri downloadUri = Uri.parse(photo.getImage());
        final String path = Utilities.getPhotoPath(this) + "/" + System.currentTimeMillis() + "_.jpg";

        final Uri destinationUri = Uri.parse(path);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)

                //.addCustomHeader("Auth-Token", "YourTokenApiKey")
                //.setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Log.e("STATS ", "Download complete Success " + id);
                        long time = Long.parseLong(photo.getTimestamp())*1000l;
                        ModelMyPhoto downloadedPhoto = new ModelMyPhoto(photo.getId(),
                                "file:" + destinationUri.toString(), photo.getFrom(),
                                Utilities.getData(time,
                                        Utilities.DATE_FORMAT), photo.getSubject());
                        mList.add(downloadedPhoto);
                        savePhotoListJson();
                        Intent intent = new Intent("my-event");
                        intent.putExtra("json", gson.toJson(downloadedPhoto));
                        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(new Intent(intent));

                        mDownloadIndex++;
                        mBuilder.setContentText(("Download in progress " + mDownloadIndex + " / " + mDownloadList.size()));
                        mNotifyManager.notify(mNotificationId, mBuilder.build());
                        if (mDownloadIndex == mDownloadList.size()) {
                            mDownloadIndex = 0;
                            //downloadManager.release();
                            mBuilder.setContentText("Download completed");
                            // Removes the progress bar
                            mBuilder.setProgress(0, 0, false);
                            mNotifyManager.notify(mNotificationId, mBuilder.build());

                        } else {
                            downloadQueue();
                        }
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String message) {
                        Log.e("STATS ", "Download Failed " + id);
                        downloadQueue();

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, int progress) {
                        //  Log.e("OnProgress ", "id " + id + " progress " + progress);

                        mBuilder.setProgress(100, progress, false);
                        mNotifyManager.notify(mNotificationId, mBuilder.build());

                    }
                });
        int downloadId = downloadManager.add(downloadRequest);
    }

    private void savePhotoListJson() {
        Utilities.saveUsersPhotoJson(this, gson.toJson(mList));
    }

    public void showNotification() {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Download")

                .setSmallIcon(R.drawable.ic_launcher);

    }

    private void initializePhotoList() {
        mList = new ArrayList<>();
        if (Utilities.getUsersPhotoJson(this) != null) {
            parseData(Utilities.getUsersPhotoJson(this));
        }
    }

    private void parseData(String json) {
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                mList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), ModelMyPhoto.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
