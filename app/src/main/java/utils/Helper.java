package utils;

/**
 * Created by Ravid on 23/10/2015.
 */
public class Helper {

    public static String Clean_Event_ID(String Event_ID){
        return Event_ID.replaceAll(" - ","_").replaceAll("@","_");
    }

}
