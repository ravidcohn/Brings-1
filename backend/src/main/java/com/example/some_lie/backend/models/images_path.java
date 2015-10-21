package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by pinhas on 21/10/2015.
 */
@Entity
public class images_path {
    @Id
    private long id;
    private String path;

public images_path(){}

    public images_path(String path){
        this.path = path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

}

