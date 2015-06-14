package com.familylooped.photos;

import android.support.v4.app.Fragment;

import com.familylooped.slideShow.FragmentSlideShow;

/**
 * Created by Noman on 4/26/2015.
 */
public class ModelMyPhoto {


    String id, image, from, timestamp, subject;
    boolean check, show;
    int rotationValue;

    public ModelMyPhoto(String id, String image, String from, String timestamp, boolean check, boolean show) {
        this.id = id;
        this.image = image;
        this.from = from;
        this.timestamp = timestamp;
        this.check = check;
        this.show = show;
        this.rotationValue = 0;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ModelMyPhoto(String id, String image, String from, String timestamp,String subject) {
        this.id = id;
        this.image = image;
        this.from = from;
        this.timestamp = timestamp;
        this.check = check;
        this.show = show;
        this.show = false;
        this.check = false;
        this.rotationValue = 0;
        this.subject = subject;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRotationValue() {
        return rotationValue;
    }

    public void setRotationValue(int rotationValue) {
        this.rotationValue = rotationValue;
    }

    public Fragment createFragment() {
        return FragmentSlideShow.newInstance(image, rotationValue);
    }
}
