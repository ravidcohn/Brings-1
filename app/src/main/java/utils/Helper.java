package utils;

import java.util.ArrayList;

/**
 * Created by Ravid on 23/10/2015.
 */
public class Helper {

    public static String Clean_Event_ID(String Event_ID){
        String[] toReplace = new String[]{"_"," - ","@","\\."};
        String[] replaceWith = new String[]{"$","_","_","_"};
        return replaceAll(Event_ID,toReplace,replaceWith);
    }

    private static String replaceAll(String str,String[] toReplace, String[] replaceWith){
        for(int i=0;i<toReplace.length;i++){
            str = str.replaceAll(toReplace[i],replaceWith[i]);
        }
        return str;
    }

    public static String getMyPermission(String Event_ID){
        ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0],Constants.Table_Events_Friends_Fields[1]},
                new String[]{Event_ID,Constants.User_Name}, null);
        return dbResult[3].get(0);
    }
}
