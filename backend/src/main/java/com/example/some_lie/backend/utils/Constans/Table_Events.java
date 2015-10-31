package com.example.some_lie.backend.utils.Constans;

/**
 * Created by Ravid on 28/10/2015.
 */
public final class Table_Events {

    public final static String Table_Name = "Events";

    public final static String Event_ID = "Event_ID";
    public final static String Name = "Name";
    public final static String Location = "Location";
    public final static String Start_Date = "Start_Date";
    public final static String Start_Time = "Start_Time";
    public final static String End_Date = "End_Date";
    public final static String End_Time = "End_Time";
    public final static String Description = "Description";
    public final static String Image_Path = "Image_Path";
    public final static String Update_Time = "Update_Time";


    public final static String[] getAllFields(){
        return new String[]{Event_ID, Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, Image_Path, Update_Time};
    }

    public final static String[] getAllFields_Except_Event_ID(){
        return new String[]{Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, Image_Path, Update_Time};
    }

    public static int Size(){
        return getAllFields().length;
    }

}
