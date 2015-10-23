package utils;

import android.content.Context;

import java.util.ArrayList;

import server.SendMessage_AsyncTask;

/**
 * Created by Ravid on 23/10/2015.
 */
public class SendMessageHelper {

    public static void SendMessageToAllMyFriendByEvent(Context context, String Event_ID, String message){
        ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0]}, new String[]{Event_ID}, null);
        for(String to:dbResult[1]) {
            if(!to.equals(Constants.User_Name)) {
                new SendMessage_AsyncTask(context).execute(Constants.User_Name, message, to);
            }
        }
    }
}
