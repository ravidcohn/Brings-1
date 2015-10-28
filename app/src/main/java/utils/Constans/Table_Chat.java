package utils.Constans;

/**
 * Created by Ravid on 28/10/2015.
 */
public final class Table_Chat {
    public final static String Table_Name = "Chat_";//The full name is: "Chat_"+EventID".

    public final static String[] Table_Chat_Fields = new String[]{"Message_ID","Friend_ID","Message","Date","Time"};

    public final static String Message_ID = "Message_ID";
    private final static String Message_ID_SQL_Params = "varchar NOT NULL";
    public final static String Friend_ID = "Friend_ID";
    private final static String Friend_ID_SQL_Params = "varchar NOT NULL";
    public final static String Message = "Message";
    private final static String Message_SQL_Params = "varchar NOT NULL";
    public final static String Date = "Date";
    private final static String Date_SQL_Params = "varchar NOT NULL";
    public final static String Time = "Time";
    private final static String Time_SQL_Params = "varchar NOT NULL";


    public final static String[] getAllFields(){
        return new String[]{Message_ID, Friend_ID, Message, Date, Time};
    }

    public final static String[] getAllSqlParams(){
        return new String[]{Message_ID_SQL_Params, Friend_ID_SQL_Params, Message_SQL_Params, Date_SQL_Params, Time_SQL_Params};
    }
}
