package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 25/09/2015.
 */
@Entity
public class Event {
    @Id
    private String id;
    private String name;
    private String location;
    private String vote_location;
    private String start_date;
    private String end_date;
    private String all_day_time;
    private String start_time;
    private String end_time;
    private String vote_time;
    private String description;
    private String image_url;
    private String update_time;

    public Event() {}

    public Event(String id, String name, String location, String vote_location, String start_date, String end_date, String all_day_time, String start_time, String end_time, String vote_time, String description, String image_url, String update_time) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.vote_location = vote_location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.all_day_time = all_day_time;
        this.start_time = start_time;
        this.end_time = end_time;
        this.vote_time = vote_time;
        this.description = description;
        this.image_url = image_url;
        this.update_time = update_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVote_location() {
        return vote_location;
    }

    public void setVote_location(String vote_location) {
        this.vote_location = vote_location;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getAll_day_time() {
        return all_day_time;
    }

    public void setAll_day_time(String all_day_time) {
        this.all_day_time = all_day_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getVote_time() {
        return vote_time;
    }

    public void setVote_time(String vote_time) {
        this.vote_time = vote_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}

