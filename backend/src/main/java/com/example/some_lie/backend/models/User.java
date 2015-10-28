package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 25/09/2015.
 */
@Entity
public class User {
    @Id
    private String Friend_ID;
    private String Phone;
    private String Nickname;

    public User() {
    }

    public User(String friend_ID, String phone, String nickname) {
        Friend_ID = friend_ID;
        Phone = phone;
        Nickname = nickname;
    }

    public String getFriend_ID() {
        return Friend_ID;
    }

    public void setFriend_ID(String friend_ID) {
        Friend_ID = friend_ID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }
}
