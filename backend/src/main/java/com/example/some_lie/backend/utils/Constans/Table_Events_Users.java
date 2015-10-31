package com.example.some_lie.backend.utils.Constans;

/**
 * Created by Ravid on 28/10/2015.
 */
public final class Table_Events_Users {
    public final static String Table_Name = "Events_Users";

    public final static String Event_ID = "Event_ID";
    public final static String User_ID = "User_ID";
    public final static String Attending = "Attending";
    public final static String Permission = "Permission";

    public final static String[] getAllFields(){
        return new String[]{Event_ID, User_ID, Attending, Permission};
    }

    public static int Size(){
        return getAllFields().length;
    }
}

