package utils.Constans;

/**
 * Created by Ravid on 28/10/2015.
 */
public final class Table_Users {
    public final static String Table_Name = "Users";

    public final static String User_ID = "User_ID";
    private final static String User_ID_SQL_Params = "varchar NOT NULL";
    public final static String Phone = "Phone";
    private final static String Phone_SQL_Params = "varchar NOT NULL";
    public final static String Nickname = "Nickname";
    private final static String Nickname_SQL_Params = "varchar NOT NULL";
    public final static String Register = "Register";
    private final static String Register_SQL_Params = "varchar NOT NULL";

    public final static String[] getAllFields(){
        return new String[]{User_ID, Phone, Nickname, Register};
    }

    public final static String[] getAllSqlParams(){
        return new String[]{User_ID_SQL_Params, Phone_SQL_Params, Nickname_SQL_Params, Register_SQL_Params};
    }

    public static int Size(){
        return getAllFields().length;
    }
}
