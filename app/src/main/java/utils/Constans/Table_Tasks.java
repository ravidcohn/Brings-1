package utils.Constans;

/**
 * Created by Ravid on 28/10/2015.
 */
public final class Table_Tasks {
    public final static String Table_Name = "Tasks";

    public final static String Event_ID = "Event_ID";
    private final static String Event_ID_SQL_Params = "varchar NOT NULL";
    public final static String Task_ID_Number = "Task_ID_Number";
    private final static String Task_ID_Number_SQL_Params = "varchar NOT NULL";
    public final static String Task_Name = "Task_Name";
    private final static String Task_Name_SQL_Params = "varchar NOT NULL";
    public final static String Description = "Description";
    private final static String Description_SQL_Params = "varchar NOT NULL";
    public final static String Friend_ID = "Friend_ID";
    private final static String Friend_ID_SQL_Params = "varchar NOT NULL";

    public final static String[] getAllFields(){
        return new String[]{Event_ID, Task_ID_Number, Task_Name, Description, Friend_ID};
    }

    public final static String[] getAllSqlParams(){
        return new String[]{Event_ID_SQL_Params, Task_ID_Number_SQL_Params, Task_Name_SQL_Params, Description_SQL_Params, Friend_ID_SQL_Params};
    }

}

