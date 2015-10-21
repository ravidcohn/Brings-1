package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 21/10/2015.
 */
@Entity
public class Chat {
    @Id
    private String Event_ID;
    private String Friend_ID_Sender;
    private String Message;
    private String Date;
    private String Time;

    public Chat() {}


    public Chat(String event_ID, String friend_ID_Sender, String message, String date, String time) {
        Event_ID = event_ID;
        Friend_ID_Sender = friend_ID_Sender;
        Message = message;
        Date = date;
        Time = time;
    }

    public String getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(String event_ID) {
        Event_ID = event_ID;
    }

    public String getFriend_ID_Sender() {
        return Friend_ID_Sender;
    }

    public void setFriend_ID_Sender(String friend_ID_Sender) {
        Friend_ID_Sender = friend_ID_Sender;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
