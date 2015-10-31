package com.example.some_lie.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Ravid on 19/10/2015.
 */
@Entity
public class Task {
    @Id
    private String Event_ID;
    private String Task_ID_Number;
    private String Task_Name;
    private String Description;
    private String User_ID;

    public Task() {}

    public Task(String event_ID, String task_ID_Number, String task_Name, String description, String user_ID) {
        Event_ID = event_ID;
        Task_ID_Number = task_ID_Number;
        Task_Name = task_Name;
        Description = description;
        User_ID = user_ID;
    }

    public String getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(String event_ID) {
        Event_ID = event_ID;
    }

    public String getTask_ID_Number() {
        return Task_ID_Number;
    }

    public void setTask_ID_Number(String task_ID_Number) {
        Task_ID_Number = task_ID_Number;
    }

    public String getTask_Name() {
        return Task_Name;
    }

    public void setTask_Name(String task_Name) {
        Task_Name = task_Name;
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
