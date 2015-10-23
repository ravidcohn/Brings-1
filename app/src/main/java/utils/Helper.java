package utils;

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
}
