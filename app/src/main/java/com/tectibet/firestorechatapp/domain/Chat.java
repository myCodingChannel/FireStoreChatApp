package com.tectibet.firestorechatapp.domain;

import com.google.firebase.Timestamp;

/**
 * Created by kharag on 23-05-2020.
 */
public class Chat {
String from;
String to;
Timestamp time_stamp;
String message;

    public Chat(String from, String to, Timestamp time_stamp, String message) {
        this.from = from;
        this.to = to;
        this.time_stamp = time_stamp;
        this.message = message;
    }

    public Chat() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Timestamp getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Timestamp time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
