package com.familylooped.photos;

/**
 * Created by Noman on 5/20/2015.
 */
public class ModelPhoto {
    String uri;
    boolean check;

    public ModelPhoto(String uri, boolean check) {
        this.uri = uri;
        this.check = check;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
