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
    private String Who;

    public String getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(String event_ID) {
        Event_ID = event_ID;
    }

    public String getTaskNumber() {
        return Task_ID_Number;
    }

    public void setTaskNumber(String taskNumber) {
        Task_ID_Number = taskNumber;
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

    public String getWho() {
        return Who;
    }

    public void setWho(String who) {
        Who = who;
    }

    public Task() {}

    public Task(String event_ID, String task_ID_Number, String task_Name, String description, String who) {
        Event_ID = event_ID;
        Task_ID_Number = task_ID_Number;
        Task_Name = task_Name;
        Description = description;
        Who = who;
    }

}
