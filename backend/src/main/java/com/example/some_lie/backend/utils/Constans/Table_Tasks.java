package com.example.some_lie.backend.utils.Constans;

/**
 * Created by Ravid on 28/10/2015.
 */
public final class Table_Tasks {
    public final static String Table_Name = "Tasks";

    public final static String Event_ID = "Event_ID";
    public final static String Task_ID_Number = "Task_ID_Number";
    public final static String Task_Name = "Task_Name";
    public final static String Description = "Description";
    public final static String User_ID = "User_ID";

    public final static String[] getAllFields(){
        return new String[]{Event_ID, Task_ID_Number, Task_Name, Description, User_ID};
    }

    public static int Size(){
        return getAllFields().length;
    }

}

