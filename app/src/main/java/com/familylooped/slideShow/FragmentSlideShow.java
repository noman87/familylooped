package com.familylooped.slideShow;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.familylooped.R;
import com.familylooped.common.AppController;
import com.familylooped.common.fragments.BaseFragment;
import com.familylooped.common.fragments.DialogClickListener;
import com.familylooped.common.logger.Log;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class FragmentSlideShow extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String IMAGE_PATH = "image_path";
    private static String ROTATION_VALUE = "rotation_value";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mImagePath;
    ImageView photo;
    private ProgressBar progressBar;
    private LinearLayout layoutButtons;
    private int mRotationValue = 0;
    private int mWidth, mHeight;
    private Bitmap mBitmap;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSlideShow.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSlideShow newInstance(String param1, String param2) {
        FragmentSlideShow fragment = new FragmentSlideShow();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSlideShow newInstance(String imagePath, int rotationValue) {
        FragmentSlideShow fragment = new FragmentSlideShow();
        Bundle args = new Bundle();
        args.putString(IMAGE_PATH, imagePath);
        args.putInt(ROTATION_VALUE, rotationValue);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSlideShow() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImagePath = getArguments().getString(IMAGE_PATH);
            mRotationValue = getArguments().getInt(ROTATION_VALUE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_slide_show, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getScreenSize();

        photo = (ImageView) view.findViewById(R.id.photo);

        int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

// Loads given image
        Picasso.with(getActivity())
                .load(Uri.parse(mImagePath))
                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .skipMemoryCache()
                .resize(size, size)
                .centerInside()
                .into(photo);

    }

    @Override
    public void onClick(View v) {

    }

    public void getScreenSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth();  // deprecated
        mHeight = display.getHeight();  // deprecated
    }

    public class BitmapTransform implements Transformation {

        int maxWidth;
        int maxHeight;

        public BitmapTransform(int maxWidth, int maxHeight) {
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth, targetHeight;
            double aspectRatio;

            if (source.getWidth() > source.getHeight()) {
                targetWidth = maxWidth;
                aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                targetHeight = (int) (targetWidth * aspectRatio);
            } else {
                targetHeight = maxHeight;
                aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                targetWidth = (int) (targetHeight * aspectRatio);
            }

            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return maxWidth + "x" + maxHeight;
        }

    }

    ;
}
