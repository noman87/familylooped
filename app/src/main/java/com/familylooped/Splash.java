package com.familylooped;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.familylooped.auth.AuthActivity;
import com.familylooped.common.Utilities;
import com.familylooped.common.activities.SampleActivityBase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.File;
import java.io.IOException;


public class Splash extends SampleActivityBase {

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleCloudMessaging gcm;
    String regid;
    private String SENDER_ID = "1077724934121";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        createFolder();
        setUpUsersDefault();


        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);
            Log.d(TAG, "REG_ID--" + regid);
            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                splashThread();
            }
        } else {

            Log.i(TAG, "No valid Google Play Services APK found.");
        }


    }

    private void setUpUsersDefault() {
        if (Utilities.getSavedInt(this, Utilities.SLIDER_TIME) < 0)
            Utilities.saveInt(this, Utilities.SLIDER_TIME, 3000);
        if (Utilities.getSavedInt(this, Utilities.PHOTO_PERIOD) < 0)
            Utilities.saveInt(this, Utilities.PHOTO_PERIOD, Utilities.PHOTO_DAY);
    }


    private String getRegistrationId(Context context) {

        String registrationId = Utilities.getSaveData(this, Utilities.REG_ID);
        if (registrationId == null) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return registrationId;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    public void registerInBackground() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    //Constants.setRegId(MainActivity.this,regid);
                    Log.d(TAG, msg);

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    //sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;

            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                splashThread();

            }
        }.execute(null, null, null);
    }

    private void splashThread() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (Utilities.getSavedBoolean(Splash.this, Utilities.IS_REMEMBER))
                    intent = new Intent(Splash.this, MainActivity.class);
                else
                    intent = new Intent(Splash.this, AuthActivity.class);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    private void createFolder() {
        File folder = new File(Utilities.getPhotoPath(this));
        //File folder = new File(Environment.getExternalStorageDirectory() + "/FamilyLooped");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
            Log.e("Status,", "is " + success);
            Utilities.saveBoolean(this, Utilities.FOLDER, true);
        } else {
            if (!Utilities.getSavedBoolean(this, Utilities.FOLDER)) {
                if (folder.isDirectory()) {
                    String[] children = folder.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(folder, children[i]).delete();
                    }
                    boolean delete_status = folder.delete();

                    folder.mkdir();
                    Utilities.saveBoolean(this, Utilities.FOLDER, true);
                }
                Log.e("Path ", "is " + folder.getAbsolutePath());
            }
        }
    }

    private void storeRegistrationId(Context context, String regId) {

        Utilities.saveData(context, Utilities.REG_ID, regId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
