package com.example.some_lie.backend.utils.Constans;

/**
 * Created by Ravid on 28/10/2015.
 */
public final class Table_Chat {
    public final static String Table_Name = "Chat_";//The full name is: "Chat_"+EventID".

    public final static String Message_ID = "Message_ID";
    private final static String Message_ID_SQL_Params = "VARCHAR(30) NOT NULL";
    public final static String User_ID = "User_ID";
    private final static String User_ID_SQL_Params = "VARCHAR(30) NOT NULL";
    public final static String Message = "Message";
    private final static String Message_SQL_Params = "LONGTEXT NOT NULL";
    public final static String Date = "Date";
    private final static String Date_SQL_Params = "VARCHAR(30) NOT NULL";
    public final static String Time = "Time";
    private final static String Time_SQL_Params = "VARCHAR(30) NOT NULL";

    public final static String[] getAllFields(){
        return new String[]{Message_ID, User_ID, Message, Date, Time};
    }

    public final static String[] getAllSqlParams(){
        return new String[]{Message_ID_SQL_Params, User_ID_SQL_Params, Message_SQL_Params, Date_SQL_Params, Time_SQL_Params};
    }

    public static int Size(){
        return getAllFields().length;
    }
}
