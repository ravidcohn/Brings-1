package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 25/09/2015.
 */
@Entity
public class User {
    @Id
    private String User_ID;
    private String Phone;
    private String Nickname;

    public User() {
    }

    public User(String user_ID, String phone, String nickname) {
        User_ID = user_ID;
        Phone = phone;
        Nickname = nickname;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
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
