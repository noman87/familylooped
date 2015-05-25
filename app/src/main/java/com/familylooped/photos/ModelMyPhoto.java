package com.familylooped.photos;

import android.support.v4.app.Fragment;

import com.familylooped.slideShow.FragmentSlideShow;

/**
 * Created by Noman on 4/26/2015.
 */
public class ModelMyPhoto {


    String id, image, from, time;
    boolean check, show;


    public ModelMyPhoto(String id, String image, String from, String time) {
        this.id = id;
        this.image = image;
        this.from = from;
        this.time = time;
    }


    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
