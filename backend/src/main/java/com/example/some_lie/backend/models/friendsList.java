package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;

/**
 * Created by pinhas on 15/10/2015.
 */
@Entity
public class friendsList {
    @Id
    long id;
    RegistrationRecord[] friends_reg;
    public friendsList(){}

    public void setList(RegistrationRecord[] list){
        this.friends_reg = list;
    }

    public RegistrationRecord[] getFriends_reg(){
        return friends_reg;
    }

}
