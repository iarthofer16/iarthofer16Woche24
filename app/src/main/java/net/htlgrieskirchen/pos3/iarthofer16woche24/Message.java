package net.htlgrieskirchen.pos3.iarthofer16woche24;

import android.support.annotation.NonNull;

import java.util.Date;

public class Message implements Comparable<Message> {

    private String message;
    private String user;
    private Date dateTime;
    private int id;

    public Message() {
    }

    public Message(String message, String user, int id) {
        this.message = message;
        this.user = user;
        this.dateTime = new Date();
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(@NonNull Message m) {
        return this.dateTime.compareTo(m.dateTime);
    }
}
