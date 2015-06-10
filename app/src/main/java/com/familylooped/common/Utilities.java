package com.familylooped.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.familylooped.common.logger.Log;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static final String TAG = Utilities.class.getPackage().getName();
    public static final String ACTION_CONNECT = TAG + ".connect";
    public static final int NOTIFICATION_ID = 1001;
    public static final int ERROR_CODE_BAD_REQUEST = 400;
    public static final int ERROR_CODE_FORBIDDEN = 403;
    public static final int ERROR_CODE_NOT_FOUND = 404;
    public static final String FREE_USER = "0";
    public static final String PAID_USER = "1";
    public static final String EXPIRED_USER = "2";


    public static final String TCP_PROTOCOL_NAME = "tcp";
    public static final String UDP_PROTOCOL_NAME = "udp";

    public static final String TCP_PROTOCOL_GET_FASTEST_SERVER = "80 tcp";
    public static final String UDP_PROTOCOL_GET_FASTEST_SERVER = "53 udp";
    public static final String URL_FASTEST_SERVER = "https://dialerxn.dialertoserver.com/dialer/select-location/Dialer_XMLS/oauth/cedexis-speed.php";

    public static final String BASE_URL = "http://www.familylooped.com/app/index.php/services/index/";

    public static final String GET_SERVER_URL = "https://dialerxn.dialertoserver.com/dialer/select-location/Dialer_XMLS/oauth/cedexis-speed.php?command=new_get_countries&device=android";
    public static final String S3_PROFILE_LINK = "https://d13ox8hzuvbzal.cloudfront.net/android/template.conf";

    public static final String URL_GET_IP = "https://dialerxn.dialertoserver.com/dialer/select-location/Dialer_XMLS/ip_location.php";
    public static final String ALLOW_USERS_URL = "http://d13ox8hzuvbzal.cloudfront.net/windows-software-files/allowed_user.txt";

    public static final String S3_PROFILE_VERSION = "https://d13ox8hzuvbzal.cloudfront.net/android/android_profile_ver.txt";


    public static final String SUPPORT_URL = "http://support.purevpn.com/";
    public static final String DID_YOU_KNOW = "http://support.purevpn.com/customer/portal/articles/1735155-did-you-know---android-";

    public static final String S3_FAIL_OVER_SERVER = "https://d2rku1c1b5jb5q.cloudfront.net/failover_grids_android.json";
    public static final String FAIL_OVER_VERSION_URL = "https://d2rku1c1b5jb5q.cloudfront.net/failover_grids_version.txt";


    // public static final String  S3_FAIL_OVER_SERVER = "https://s3.eu-central-1.amazonaws.com/amirqtest/failover_grids_android.json";
    //public static final String FAIL_OVER_VERSION_URL = "https://s3.eu-central-1.amazonaws.com/amirqtest/failover_grids_version.txt";

    public static final String SUPPORT_LINK = "http://purevpn.uservoice.com/forums/135248-service-improvement";


    public static final String USER_FB_LIKE = "USER_FB_LIKE";
    public static final String USER_TWEET = "USER_TWEET";
    public static final String USER_FB_SHARE = "USER_FB_SHARE";
    public static final String USER_G_PLUS_SHARE = "USER_G_PLUS_SHARE";
    public static final String USER_TWITTER_FOLLOW = "USER_TWITTER_FOLLOW";
    public static final String BASE_64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqR/udNYB3sTOJxKhC8yNlSBc/WAKWHfkzZPE37lJEH1TWgUxz4P0pc51+c5iAZfk9X5SJZJL10XH3UWDzG+M2FfS0bF/dW9JMeRbHKSGLs3Igseay7KSWQy2Q7vDJl5DTSn/NbJS/MwjuXIcfYGw8Jd8F8cNHN2skQvdvgB1nkHuyVKuJhg0LlSjAcQfjZpoSpiU7uZ1OlpRnkjLFxq9DDirKA8xTAlABYcMzbchPz5iPnhNTpdTD4LC7RMfqDidYqCLbui9IjOPdwd8yDMa1post6yGzhKPmFsl2ngIO8meJuPiZY7i48mjKmfaOzGhPAUtgJPA7e6Fi0PWCSgTZwIDAQAB";


    public static final String MCS = "mcs";
    public static final String VERSION = "2";
    public static final String AUTH_KEY = "aBbc123!@!";

    public static final String PURE_VPN_USERNAME = "pure_vpn_username";
    public static final String PURE_VPN_PASSWORD = "pure_vpn_password";
    public static final String VPN_PROFILE = "vpn_profile";

    public static final String DEVICE = "Android";


    public static final String KEY_USER_DATA = "usr_data";
    public static final String KEY_FROM_SPLASH = "from_splash";
    public static final String USER_API_TOKEN = "usr_api_token";
    public static final String KEY_USER__SYNC_SETTINGS = "usr_sync_settings";
    public static final String KEY_USER_FIRST_TIME = "usr_first_time";
    public static final String KEY_DNS_SERVERS = "dns_servers";
    public static final String NEW_RECLIC_KEY = "AA013db6e601c398e177e4a74ff3fc91e4e188afe5";

    public static final String ALLOW_USERS_STRING = "allow_users";


    public static final String VPN_PROFILE_VERSION = "vpn_profile_verison";
    public static final String IS_VPN_USERNAME_SAVED = "IS_VPN_USERNAME_SAVED";

    public static final String USER_ID = "user_id";
    public static final String CLIENT_ID = "client_id";
    public static final String TCP_PROTOCOL = "8";
    public static final String UDP_PROTOCOL = "9";
    public static final String SERVER_RESPONSE = "server_response";
    public static final String PROFILE_VERSION_CALL_TIME = "profile_version_time";
    public static final String FAIL_OVER_RESPONSE = "fail_over_response";
    public static final String FAIL_OVER_VERSION = "fail_over_version";
    public static final String FAIL_OVER_VERSION_TIME = "fail_over_version_time";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String DIR_NAME = "/FamilyLooped";
    public static final String PHOTO_JSON = "photo_json";
    public static String DATE_FORMAT = "dd/M/yyyy hh:mm:ss";


    public static final String S3_FAIL_OVER_VERSION = "s3_fail_over_version";

    public static final String SELECTED_SERVER = "selected_server";
    public static final String SERVER_CALL_TIME = "server_call_time";
    public static CharSequence SUCCESS = "SUCCESS";
    public static String IS_REMEMBER = "is_remember";
    public static String SLIDER_TIME = "slider_time";
    public static String REG_ID = "reg_id";
    public static String PHOTO_TIME = "photo_time";


    public static final String PHOTO_PERIOD = "photo_period";
    public static final int PHOTO_DAY = 1;
    public static final int PHOTO_WEEK = 7;
    public static final int PHOTO_MONTH = 30;
    public static final int PHOTO_EVERY_THING = 3000000;
    public static String FOLDER = "folder";

    public static <T> void printJSON(String TAG, Object object, Class<T> classOfT) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(object, classOfT);
        Log.i(TAG, jsonStr);
    }

    public static <T> String getJSON(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> String getJSON(Object object, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.toJson(object, classOfT);
    }

    public static <T> void printJSON(String TAG, Object object) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(object);
        Log.i(TAG, jsonStr);
    }

    public static <T> T getObjectFromGSON(String object, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(object, classOfT);
    }

    public static <T> ArrayList<T> getArrayListFromGSON(String object) {
        try {
            InputStream stream = new ByteArrayInputStream(object.getBytes("UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            Gson gson = new Gson();
            return gson.fromJson(br, new TypeToken<ArrayList<T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void saveData(Context context, String key, String value) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static int getSavedInt(Context context, String key) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(context.getPackageName(),


                Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    public static String getSaveData(Context context, String key) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static boolean getSavedBoolean(Context context, String key) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
        editText.requestFocus();
    }

    public static boolean isValidEmail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void toast(Context context, String text) {

        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static boolean isValidUserName(String username, Context context) {
        String expression = "^[0-9a-zA-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void underlineWidgetWithText(TextView widget, String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        widget.setText(content);
    }

    public static String getParams(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean firstIterate = true;
        for (String key : params.keySet()) {
            stringBuilder.append(firstIterate ? "?" : "&").append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            firstIterate = false;
        }
        return stringBuilder.toString();
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static long timeDiff(String startDate, String endDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        Date date1 = null, date2 = null;

        try {
            date1 = simpleDateFormat.parse(startDate);
            date2 = simpleDateFormat.parse(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //milliseconds
        long different = date2.getTime() - date1.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        Log.e("Days ", " is" + elapsedDays);
        return elapsedDays;
        /*if (elapsedDays > 0) {
            return true;
        } else if (elapsedHours > 0) {
            return true;
        } else if (elapsedMinutes > 30) {
            return true;
        } else {
            return false;
        }*/

    }

    public static String getData(long unixTime, String formate) {

        long unixSeconds = unixTime;
        Date date = new Date(unixSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static long dateToTimeStamp(String date, String dateFormat) {
        Date newDate = null;
        try {
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            newDate = (Date) formatter.parse(date);
            System.out.println("Today is " + newDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate.getTime();
    }

    public static String getPhotoPath(Context context) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + Utilities.DIR_NAME;
        /*if (Utilities.getSaveData(context, Utilities.USER_ID) != null)
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + Utilities.DIR_NAME + "/" + Utilities.getSaveData(context, Utilities.USER_ID);
        else {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + Utilities.DIR_NAME;
        }*/

    }

    public static String getUsersPhotoJson(Context context) {
        Log.e("PhotoJson", Utilities.getSaveData(context, Utilities.USER_ID + "_" + Utilities.PHOTO_JSON));
        return Utilities.getSaveData(context, Utilities.getSaveData(context,Utilities.USER_ID)+ "_" + Utilities.PHOTO_JSON);
    }

    public static void saveUsersPhotoJson(Context context, String json) {
        Utilities.saveData(context, Utilities.getSaveData(context,Utilities.USER_ID) + "_" + Utilities.PHOTO_JSON, json);
    }


}
