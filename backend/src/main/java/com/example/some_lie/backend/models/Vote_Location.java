package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 08/03/2016.
 */
public class Vote_Location {

    @Id
    private String Event_ID;
    private String Vote_ID;
    private String Description;
    private String User_ID;

    public Vote_Location() {}

    public Vote_Location(String event_ID, String vote_ID, String description, String user_ID) {
        Event_ID = event_ID;
        Vote_ID = vote_ID;
        Description = description;
        User_ID = user_ID;
    }

    public String getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(String event_ID) {
        Event_ID = event_ID;
    }

    public String getVote_ID() {
        return Vote_ID;
    }

    public void setVote_ID(String vote_ID) {
        Vote_ID = vote_ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }
}
