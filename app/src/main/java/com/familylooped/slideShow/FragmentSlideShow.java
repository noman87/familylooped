package com.familylooped.slideShow;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
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

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSlideShow#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSlideShow extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String IMAGE_PATH = "image_path";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mImagePath;
    ImageView photo;
    private ProgressBar progressBar;
    private LinearLayout layoutButtons;
    private int mRotationValue = 0;


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

    public static FragmentSlideShow newInstance(String imagePath) {
        FragmentSlideShow fragment = new FragmentSlideShow();
        Bundle args = new Bundle();
        args.putString(IMAGE_PATH, imagePath);
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

        photo = (ImageView)view.findViewById(R.id.photo);

        Log.e("Path", "is " + mImagePath);
        File file = new File(mImagePath);
        Log.e("URI", Uri.fromFile(file).toString());
        photo.setImageURI(Uri.fromFile(file));

        /*ImageRequest imageRequest = new ImageRequest(mImagePath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                progressBar.setVisibility(View.GONE);
                photo.setImageBitmap(response);
                //frameAnimation.stop();
                Log.e("URL","is "+mImagePath);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        AppController.getInstance().addToRequestQueue(imageRequest, "Getting Image");*/
    }


    @Override
    public void onClick(View v) {

    }
}
