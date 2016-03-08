package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 06/03/2016.
 */
@Entity
public class Vote_Date {

    @Id
    private String Event_ID;
    private String Vote_ID;
    private String Start_Date;
    private String End_Date;
    private String All_Day_Time;
    private String Start_Time;
    private String End_Time;
    private String User_ID;

    public Vote_Date() {}

    public Vote_Date(String event_ID, String vote_ID, String start_Date, String end_Date, String all_Day_Time, String start_Time, String end_Time, String user_ID) {
        Event_ID = event_ID;
        Vote_ID = vote_ID;
        Start_Date = start_Date;
        End_Date = end_Date;
        All_Day_Time = all_Day_Time;
        Start_Time = start_Time;
        End_Time = end_Time;
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

    public String getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(String start_Date) {
        Start_Date = start_Date;
    }

    public String getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(String end_Date) {
        End_Date = end_Date;
    }

    public String getAll_Day_Time() {
        return All_Day_Time;
    }

    public void setAll_Day_Time(String all_Day_Time) {
        All_Day_Time = all_Day_Time;
    }

    public String getStart_Time() {
        return Start_Time;
    }

    public void setStart_Time(String start_Time) {
        Start_Time = start_Time;
    }

    public String getEnd_Time() {
        return End_Time;
    }

    public void setEnd_Time(String end_Time) {
        End_Time = end_Time;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }
}
