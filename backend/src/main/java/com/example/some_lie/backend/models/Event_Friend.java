package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 09/10/2015.
 */
@Entity
public class Event_Friend {
    @Id
    long id;
    private String event_name;
    private String friend_name;
    private String attending;
    private String permission;

    public Event_Friend(){}

    public Event_Friend(String event_name, String friend_name, String attending, String permission) {
        this.event_name = event_name;
        this.friend_name = friend_name;
        this.attending = attending;
        this.permission = permission;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getAttending() {
        return attending;
    }

    public void setAttending(String attending) {
        this.attending = attending;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
