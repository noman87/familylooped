package com.familylooped.photos;

import android.support.v4.app.Fragment;

import com.familylooped.slideShow.FragmentSlideShow;

/**
 * Created by Noman on 4/26/2015.
 */
public class ModelMyPhoto {


    String image;

    public ModelMyPhoto(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Fragment createFragment() {
        return FragmentSlideShow.newInstance(image);
    }
}
