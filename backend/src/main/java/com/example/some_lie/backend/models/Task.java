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
    private String subTask_ID_Number;
    private String Task_Type;
    private String Description;
    private String User_ID;

    public Task() {}

    public Task(String event_ID, String task_ID_Number, String subTask_ID_Number, String task_Type, String description, String user_ID) {
        Event_ID = event_ID;
        Task_ID_Number = task_ID_Number;
        this.subTask_ID_Number = subTask_ID_Number;
        Task_Type = task_Type;
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

    public String getSubTask_ID_Number() {
        return subTask_ID_Number;
    }

    public void setSubTask_ID_Number(String subTask_ID_Number) {
        this.subTask_ID_Number = subTask_ID_Number;
    }

    public String getTask_Type() {
        return Task_Type;
    }

    public void setTask_Type(String task_Type) {
        Task_Type = task_Type;
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
