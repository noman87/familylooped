package com.familylooped.slideShow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.familylooped.R;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.AlertDialogFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;
import com.familylooped.photos.ModelMyPhoto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ActivitySlideShow extends FragmentActivity implements View.OnClickListener {

    private String mParam1;
    private String mParam2;
    private ViewPager mViewPager;
    private ArrayList<ModelMyPhoto> mList;
    private AdapterSlideShow mAdapter;
    private Timer mTimer;
    private int period, delay;
    private int currentIndex = 0;
    private Handler mHandler = new Handler();
    private Runnable mUpdateResults;
    private ArrayList<Object> photosUri;
    private LinearLayout layoutButtons;
    private int mRotationValue = 0;
    private ImageView mPhoto;
    private ImageButton mBtnStop;
    private LinearLayout mLayoutStopButon;
    private TextView mTxtName;
    private String TAG = "Activity Slide Show";
    Gson gson = new Gson();
    private int mSelectedPosition;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        period = Utilities.getSavedInt(this, Utilities.SLIDER_TIME);
        layoutButtons = (LinearLayout) findViewById(R.id.layout_buttons);
        mLayoutStopButon = (LinearLayout) findViewById(R.id.layout_stop_button);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ((ImageButton) findViewById(R.id.btn_replay)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.btn_delete)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.btn_rotate)).setOnClickListener(this);
        mTxtName = (TextView) findViewById(R.id.txt_name);
        mBtnStop = (ImageButton) findViewById(R.id.btn_stop);
        mBtnStop.setOnClickListener(this);

        //mPhoto = (ImageView) mViewPager.getFocusedChild();
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int visibility = layoutButtons.getVisibility();
                    if (visibility == View.VISIBLE) {
                        layoutButtons.setVisibility(View.INVISIBLE);
                        mLayoutStopButon.setVisibility(View.INVISIBLE);

                    } else if (visibility == View.INVISIBLE) {
                        layoutButtons.setVisibility(View.VISIBLE);
                        mLayoutStopButon.setVisibility(View.VISIBLE);
                        stopSlideShow();
                        timer(10000);
                    }
                }

                return false;

            }
        });


        mViewPager.setPageTransformer(false, new ReaderViewPagerTransformer(ReaderViewPagerTransformer.TransformType.FLOW));

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mRotationValue = 0;
            }

            @Override
            public void onPageSelected(int position) {
                /// currentIndex = position;
                //mCurrentPagerIndex = position;
                mTxtName.setText("From: " + mList.get(position).getFrom());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //getPhotos();
        showPhotos();


        mUpdateResults = new Runnable() {
            public void run() {
                currentIndex++;
                if (currentIndex >= mList.size()) {
                    currentIndex = 0;
                }
                if (currentIndex == 0)
                    mViewPager.setAdapter(mAdapter);
                else
                    mViewPager.setCurrentItem(currentIndex);
            }
        };
    }

    private void timer(int delay) {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mHandler.post(mUpdateResults);
            }
        }, delay, period);
    }

    public void stopSlideShow() {
        if (mTimer != null)
            mTimer.cancel();
    }


    private void showPhotos() {
        mList = new ArrayList<>();
        if (Utilities.getUsersPhotoJson(this) != null) {
            parseData(Utilities.getUsersPhotoJson(this));
        }
        mAdapter = new AdapterSlideShow(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);
        if (getIntent().getExtras() != null) {
            mSelectedPosition = getIntent().getExtras().getInt("position");
            mViewPager.setCurrentItem(mSelectedPosition);
        }
        if (mList.size() > 0)
            mTxtName.setText("From: " + mList.get(0).getFrom());

        timer(period);


    }

    private void parseData(String json) {
        Gson gson = new Gson();
        int days_preference = Utilities.getSavedInt(this, Utilities.PHOTO_PERIOD);
        if (days_preference < 0) {
            days_preference = Utilities.PHOTO_DAY;
        }

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                long timeDifference = Utilities.timeDiff(object.getString("timestamp"), Utilities.getData(System.currentTimeMillis(), Utilities.DATE_FORMAT));
                if (timeDifference < days_preference)
                    mList.add(gson.fromJson(object.toString(), ModelMyPhoto.class));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getPhotos() {


        Map<String, String> urlParams = new HashMap<>();
        mList = new ArrayList<>();
        urlParams.put("userId", Utilities.getSaveData(this, Utilities.USER_ID));

        AsyncHttpRequest request = new AsyncHttpRequest(this, "get_images", Utilities.BASE_URL + "photos", urlParams, new AsyncHttpRequest.HttpResponseListener() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data = object.getJSONArray("data");
                    Gson gson = new Gson();
                    for (int i = 0; i < data.length(); i++) {
                        mList.add(gson.fromJson(data.getJSONObject(i).toString(), ModelMyPhoto.class));

                    }

                    //timer();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(request, "get_photos");
    }

    private void saveRotateImage(String fileName, String orientation) {
        Log.e(TAG, "rOTATION " + orientation);
        final ProgressDialog dialog = new ProgressDialog(this);
        class SavingFile extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialog.setTitle("Saving");
                dialog.setMessage("Image is saving..");
                dialog.setCancelable(false);
                dialog.show();

            }

            @Override
            protected String doInBackground(String... params) {


                String dir = Utilities.getPhotoPath(ActivitySlideShow.this);
                String completeName = dir + "/" + params[0];

                File file = new File(completeName);
                Log.e("Is exist", "Value is " + file.exists());

                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(completeName);
                    Matrix matrix = new Matrix();
                    int rotation = Integer.parseInt(params[1]);
                    matrix.postRotate(rotation);
                    file.delete();
                    File file_new = new File(completeName);
                    Bitmap bmp2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    FileOutputStream fOut = new FileOutputStream(file_new);
                    bmp2.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                    bitmap.recycle();
                    mList.get(mViewPager.getCurrentItem()).setRotationValue(rotation);
                    Utilities.saveUsersPhotoJson(ActivitySlideShow.this, gson.toJson(mList));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                timer(5000);
                dialog.hide();
            }
        }
        new SavingFile().execute(fileName, orientation);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_rotate:
                stopSlideShow();

                //FrameLayout layout = (FrameLayout) mViewPager.getChildAt(mViewPager.getCurrentItem());
                //ImageView imageView = (ImageView) layout.getChildAt(0);
                AdapterSlideShow adapter = ((AdapterSlideShow) mViewPager.getAdapter());

                FragmentSlideShow fragment = (FragmentSlideShow) adapter.getRegisteredFragment(mViewPager.getCurrentItem());
                View view = fragment.getView();


                mRotationValue = mRotationValue + 90;
                fragment.photo.setRotation(mRotationValue);
                String fileName[] = mList.get(mViewPager.getCurrentItem()).getImage().split("/");
                saveRotateImage(fileName[fileName.length - 1], "" + mRotationValue);
                if (mRotationValue == 360)
                    mRotationValue = 0;

                break;

            case R.id.btn_delete:
                stopSlideShow();
                showDialog("Are you sure you want to delete this photo from loop?", "Yes", "No", new DialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        //File file = new File(mList.get(mViewPager.getCurrentItem()).getImage());
                        //boolean deleted = file.delete();
                        mAdapter.removeView(mViewPager, mViewPager.getCurrentItem());
                        reInitViewPager(true);

                    }
                });
                break;

            case R.id.btn_replay:
                //playSlideShow();
                stopSlideShow();
                if (!TextUtils.equals(mList.get(mViewPager.getCurrentItem()).getFrom(), "gallery"))
                    showPopup();
                else
                    showDialog("This photo has no email address attached", "OK", "Cancel", new DialogClickListener() {
                        @Override
                        public void onPositiveButtonClick() {

                        }
                    });

                break;
            case R.id.btn_stop:
                Log.e(TAG, "cLICK");
                //stopSlideShow();
                finish();
                break;
        }

    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send message");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.invite_popup_view, null);
        final TextView textView = (TextView) view.findViewById(R.id.txt_email);
        builder.setView(view);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // mEmailAddress = textView.getText().toString();
                timer(10000);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                timer(10000);
            }
        });
        builder.create().show();

    }

    private void reInitViewPager(boolean is_increment) {

        mAdapter = new AdapterSlideShow(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);
        int position = 0;
        if (is_increment)
            position = mViewPager.getCurrentItem() + 1;
        else
            position = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(position);


    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mPhotoReceiver, new IntentFilter("my-event"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSlideShow();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPhotoReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("BackPRess", "buttons press");
        finish();
    }

    public void showDialog(String message, String done, String cancel, DialogClickListener listener) {

        AlertDialogFragment fragment = AlertDialogFragment.newInstance(message, done, cancel, listener);
        fragment.show(getSupportFragmentManager(), "Dialog");
    }


    private BroadcastReceiver mPhotoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonString = intent.getExtras().getString("json");
            ModelMyPhoto photo = gson.fromJson(jsonString, ModelMyPhoto.class);
            mList.add(photo);
            mAdapter.notifyDataSetChanged();

        }
    };
}



