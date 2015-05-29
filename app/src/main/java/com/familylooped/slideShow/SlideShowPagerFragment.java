package com.familylooped.slideShow;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
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
import com.familylooped.MainActivity;
import com.familylooped.R;
import com.familylooped.common.AppController;
import com.familylooped.common.Utilities;
import com.familylooped.common.async.AsyncHttpRequest;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;
import com.familylooped.photos.AdapterMyPhoto;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideShowPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideShowPagerFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "Slide Show";
    private String mDateFormat = "dd/M/yyyy hh:mm:ss";
    private static int mCurrentPagerIndex;

    // TODO: Rename and change types of parameters
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SlideShowPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SlideShowPagerFragment newInstance(String param1, String param2) {
        SlideShowPagerFragment fragment = new SlideShowPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SlideShowPagerFragment newInstance() {
        SlideShowPagerFragment fragment = new SlideShowPagerFragment();

        return fragment;
    }

    public SlideShowPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        period = Utilities.getSavedInt(getActivity(), Utilities.SLIDER_TIME);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide_show_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutButtons = (LinearLayout) view.findViewById(R.id.layout_buttons);
        mLayoutStopButon = (LinearLayout) view.findViewById(R.id.layout_stop_button);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ((ImageButton) view.findViewById(R.id.btn_replay)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_delete)).setOnClickListener(this);
        ((ImageButton) view.findViewById(R.id.btn_rotate)).setOnClickListener(this);
        mTxtName = (TextView) view.findViewById(R.id.txt_name);
        mBtnStop = (ImageButton) view.findViewById(R.id.btn_stop);
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

            }

            @Override
            public void onPageSelected(int position) {
                /// currentIndex = position;
                mCurrentPagerIndex = position;
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
        mTimer.cancel();
    }


    private void showPhotos() {
        mList = new ArrayList<>();
        if (Utilities.getSaveData(getActivity(), Utilities.PHOTO_JSON) != null) {
            parseData(Utilities.getSaveData(getActivity(), Utilities.PHOTO_JSON));
        }
        mAdapter = new AdapterSlideShow(getChildFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);
        if (mList.size() > 0)
            mTxtName.setText("From: " + mList.get(0).getFrom());

        timer(period);


    }

    private void parseData(String json) {
        Gson gson = new Gson();
        int days_preference = Utilities.getSavedInt(getActivity(), Utilities.PHOTO_PERIOD);
        if (days_preference < 0) {
            days_preference = Utilities.PHOTO_DAY;
        }

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                long timeDifference = Utilities.timeDiff(object.getString("time"), Utilities.getData(System.currentTimeMillis(), mDateFormat));
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
        urlParams.put("userId", Utilities.getSaveData(getActivity(), Utilities.USER_ID));

        AsyncHttpRequest request = new AsyncHttpRequest(getActivity(), "get_images", Utilities.BASE_URL + "photos", urlParams, new AsyncHttpRequest.HttpResponseListener() {
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

    private void saveRotateImage(String fileName, int orientation) {
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + Utilities.DIR_NAME;
        String completeName = dir +"/"+ fileName;

        File file = new File(completeName);
        Log.e("Is exist", "Value is " + file.exists());

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(completeName);
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            file.delete();
            File file_new = new File(completeName);
            Bitmap bmp2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            FileOutputStream fOut = new FileOutputStream(file_new);
            bmp2.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_rotate:
                stopSlideShow();
                timer(5000);



                FrameLayout layout = (FrameLayout) mViewPager.getChildAt(mViewPager.getCurrentItem());
                ImageView imageView = (ImageView) layout.getChildAt(0);
               // ImageView imageView = (ImageView) mViewPager.findViewWithTag(mList.get(mViewPager.getCurrentItem()));

                mRotationValue = mRotationValue + 90;
                imageView.setRotation(mRotationValue);
                String fileName[] = mList.get(mViewPager.getCurrentItem()).getImage().split("/");
                saveRotateImage(fileName[fileName.length - 1], mRotationValue);
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
                showPopup();
                stopSlideShow();

                break;
            case R.id.btn_stop:
                Log.e(TAG, "cLICK");
                stopSlideShow();
                getActivity().finish();
                break;
        }

    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Send message");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.invite_popup_view, null);
        final TextView textView = (TextView) view.findViewById(R.id.txt_email);
        builder.setView(view);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

        mAdapter = new AdapterSlideShow(getChildFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);
        int position = 0;
        if (is_increment)
            position = mViewPager.getCurrentItem() + 1;
        else
            position = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(position);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopSlideShow();
    }
}
