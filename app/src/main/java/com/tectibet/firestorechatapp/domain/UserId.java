package com.tectibet.firestorechatapp.domain;

import androidx.annotation.NonNull;

/**
 * Created by kharag on 20-05-2020.
 */
public class UserId {
    public String userId;
    public <T extends UserId> T withId(@NonNull final String id){
        this.userId=id;
        return  (T) this;
    }
}
