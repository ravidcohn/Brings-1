package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 21/10/2015.
 */
@Entity
public class Chat {
    @Id
    private String Message_ID;
    private String Friend_ID_Sender;
    private String Message;
    private String Date;
    private String Time;

    public Chat() {}

    public Chat(String message_ID, String friend_ID_Sender, String message, String date, String time) {
        Message_ID = message_ID;
        Friend_ID_Sender = friend_ID_Sender;
        Message = message;
        Date = date;
        Time = time;
    }

    public String getMessage_ID() {
        return Message_ID;
    }

    public void setMessage_ID(String message_ID) {
        Message_ID = message_ID;
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
