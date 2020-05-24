package com.tectibet.firestorechatapp.domain;

/**
 * Created by kharag on 20-05-2020.
 */
public class Users extends UserId {
    String name;
    String fcm;
    Boolean login;

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public Users() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
