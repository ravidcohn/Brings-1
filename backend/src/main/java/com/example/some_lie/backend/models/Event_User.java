package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 09/10/2015.
 */
@Entity
public class Event_User {
    @Id
    long id;
    private String Event_ID;
    private String User_ID;
    private String Attending;
    private String Permission;

    public Event_User(){}

    public Event_User(String event_ID, String user_ID, String attending, String permission) {
        Event_ID = event_ID;
        User_ID = user_ID;
        Attending = attending;
        Permission = permission;
    }

    public String getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(String event_ID) {
        Event_ID = event_ID;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getAttending() {
        return Attending;
    }

    public void setAttending(String attending) {
        Attending = attending;
    }

    public String getPermission() {
        return Permission;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }
}
