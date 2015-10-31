package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The Objectify object model for device registrations we are persisting
 */
@Entity
public class RegistrationRecord {

    @Id
    private String User_ID;

    @Index
    private String Registration_ID;
    private String Phone;
    //private String password;
    private String Nickname;
    private String registration_message;


    public RegistrationRecord() {
    }

    public RegistrationRecord(String user_ID, String phone) {
        User_ID = phone;
        Phone = phone;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getRegistration_ID() {
        return Registration_ID;
    }

    public void setRegistration_ID(String registration_ID) {
        Registration_ID = registration_ID;
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

    public String getRegistration_message() {
        return registration_message;
    }

    public void setRegistration_message(String registration_message) {
        this.registration_message = registration_message;
    }
}