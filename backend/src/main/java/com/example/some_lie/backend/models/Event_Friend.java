package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;

/**
 * Created by Ravid on 09/10/2015.
 */
@Entity
public class Event_Friend {
    private String event_name;
    private String friend_name;

    public Event_Friend(String event_name, String friend_name) {
        this.event_name = event_name;
        this.friend_name = friend_name;
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
}
