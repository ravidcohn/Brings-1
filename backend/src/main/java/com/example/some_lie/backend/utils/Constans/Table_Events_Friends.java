package com.example.some_lie.backend.utils.Constans;

/**
 * Created by Ravid on 28/10/2015.
 */
public final class Table_Events_Friends {
    public final static String Table_Name = "Events_Friends";

    public final static String Event_ID = "Event_ID";
    private final static String Event_ID_SQL_Params = "varchar NOT NULL";
    public final static String Friend_ID = "Friend_ID";
    private final static String Friend_ID_SQL_Params = "varchar NOT NULL";
    public final static String Attending = "Attending";
    private final static String Attending_SQL_Params = "varchar NOT NULL";
    public final static String Permission = "Permission";
    private final static String Permission_SQL_Params = "varchar NOT NULL";

    public final static String[] getAllFields(){
        return new String[]{Event_ID, Friend_ID, Attending, Permission};
    }

    public final static String[] getAllSqlParams(){
        return new String[]{Event_ID_SQL_Params, Event_ID_SQL_Params, Friend_ID_SQL_Params, Attending_SQL_Params, Permission_SQL_Params};
    }

    public static int Size(){
        return getAllFields().length;
    }
}

