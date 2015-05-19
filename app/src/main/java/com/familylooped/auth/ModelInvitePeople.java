package com.familylooped.auth;

/**
 * Created by Noman on 4/30/2015.
 */
public class ModelInvitePeople {
    
    String name,email;
    boolean check;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public ModelInvitePeople(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
