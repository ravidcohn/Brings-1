package utils;

import android.content.Context;

import com.google.appengine.repackaged.org.joda.time.LocalDateTime;

import java.util.ArrayList;

import server.Chat_AsyncTask_CreateByEvent;
import server.Chat_AsyncTask_deleteByEvent;
import server.EventFriend_AsyncTask_delete_by_event;
import server.EventFriend_AsyncTask_insert;
import server.Event_AsyncTask_delete;
import server.Event_AsyncTask_insert;
import server.SendMessage_AsyncTask;
import server.Task_AsyncTask_deleteByEvent;
import server.User_AsyncTask_get;
import utils.Constans.Constants;
import utils.Constans.Table_Chat;
import utils.Constans.Table_Events;
import utils.Constans.Table_Events_Friends;
import utils.Constans.Table_Tasks;
import utils.Constans.Table_Users;

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
        ArrayList<String>[] dbResult = sqlHelper.select(null, Table_Events_Friends.Table_Name, new String[]{Table_Events_Friends.Event_ID,Table_Events_Friends.Friend_ID},
                new String[]{Event_ID,Constants.MY_User_ID}, null);
        return dbResult[3].get(0);
    }

    public static void Delete_Event_MySQL(String Event_ID){
        sqlHelper.delete(Table_Events.Table_Name, new String[]{Table_Events.Event_ID}, new String[]{Event_ID}, new int[]{1});
        sqlHelper.delete(Table_Events_Friends.Table_Name, new String[]{Table_Events_Friends.Event_ID}, new String[]{Event_ID}, null);
        sqlHelper.delete(Table_Tasks.Table_Name, new String[]{Table_Tasks.Event_ID}, new String[]{Event_ID}, null);
        sqlHelper.Delete_Table(Table_Chat.Table_Name + Helper.Clean_Event_ID(Event_ID));
    }
    public static void Create_Event_MySQL(String Event_ID,String  Name, String  Location,String  Start_Date,String  Start_Time,String End_Date,
                                          String  End_Time, String  Description, String  ImagePath, String  Update_Time){
        sqlHelper.insert(Table_Events.Table_Name, new String[]{Event_ID, Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, ImagePath, Update_Time});
        sqlHelper.insert(Table_Events_Friends.Table_Name, new String[]{Event_ID, Constants.MY_User_ID, Constants.Yes, Constants.Manager});
        String Chat_ID = Table_Chat.Table_Name + Helper.Clean_Event_ID(Event_ID);
        sqlHelper.Create_Table(Chat_ID, Table_Chat.getAllFields(), Table_Chat.getAllSqlParams());
    }
    public static void Delete_Event_ServerSQL(Context context, String Event_ID){
        new Event_AsyncTask_delete(context).execute(Event_ID);
        new EventFriend_AsyncTask_delete_by_event(context).execute(Event_ID);
        new Task_AsyncTask_deleteByEvent(context).execute(Event_ID);
        String Chat_ID = Table_Chat.Table_Name + Helper.Clean_Event_ID(Event_ID);
        new Chat_AsyncTask_deleteByEvent(context).execute(Chat_ID);
        String message = Constants.Delete_Event + "|" + Event_ID;
        Helper.Send_Message_To_All_My_Friend_By_Event(context, Event_ID, message);;
    }
    public static void Create_Event_ServerSQL(Context context, String Event_ID,String  Name, String  Location, String  Start_Date, String  Start_Time, String End_Date,
                                          String  End_Time, String  Description, String  ImagePath, String  Update_Time){
        new Event_AsyncTask_insert(context).execute(Event_ID, Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, ImagePath, Update_Time);
        new EventFriend_AsyncTask_insert(context).execute(Event_ID, Constants.MY_User_ID, Constants.Yes, Constants.Manager);
        String Chat_ID = Table_Chat.Table_Name + Helper.Clean_Event_ID(Event_ID);
        new Chat_AsyncTask_CreateByEvent(context).execute(Chat_ID);
    }


    public static ArrayList<String>[] getFriends_From_Event(String Event_ID){
        return sqlHelper.select(null, Table_Events_Friends.Table_Name, new String[]{Table_Events_Friends.Event_ID},
                new String[]{Event_ID}, null);
    }

    public static void Send_Message_To_All_My_Friend_By_Event(Context context, String Event_ID, String message){
        ArrayList<String>[] dbFriends = getFriends_From_Event(Event_ID);
        for(String to:dbFriends[1]) {
            if(!to.equals(Constants.MY_User_ID)) {
                new SendMessage_AsyncTask(context).execute(Constants.MY_User_ID, message, to);
            }
        }
    }
    public static void Send_Message_To_Friend_By_Event_Except_One(Context context, String Event_ID, String Friend_ID, String message){
        ArrayList<String>[] dbFriends = getFriends_From_Event(Event_ID);
        for(String to:dbFriends[1]) {
            if(!to.equals(Friend_ID) && !to.equals(Constants.MY_User_ID)) {
                new SendMessage_AsyncTask(context).execute(Constants.MY_User_ID, message, to);
            }
        }
    }

    public static String getCurrentTime(){
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHourOfDay();
        int minute = now.getMinuteOfHour();
        int second = now.getSecondOfMinute();
        String time = hour+":"+minute+":"+second;
        return time;
    }
    public static String getCurrentDate(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthOfYear();
        int day = now.getDayOfMonth();
        String date = day+"/"+month+"/"+year;
        return date;
    }

    public static boolean isFriend_Exist_in_Event_MySql(String Event_ID, String Friend_ID){
        return !(sqlHelper.select(null, Table_Events_Friends.Table_Name,new String[]{Table_Events_Friends.Event_ID,
                Table_Events_Friends.Friend_ID},new String[]{Event_ID, Friend_ID},null)[0].isEmpty());
    }

    public static void Event_Friend_Insert_MySQL(String Event_ID, String Friend_ID, String Attend, String Permission_Value){
        sqlHelper.insert(Table_Events_Friends.Table_Name, new String[]{Event_ID, Friend_ID, Attend, Permission_Value});
    }

    public static void User_Insert_MySQL(String Friend_ID){
        ArrayList<String>[] dbUsers = sqlHelper.select(null, Table_Users.Table_Name,new String[]{Table_Users.Friend_ID},new String[]{Friend_ID},null);
        if(dbUsers[0].isEmpty()){
            new User_AsyncTask_get().execute(Friend_ID);
        }
    }

    public static void Event_Update_MySQL(String Event_ID, String Name, String Location, String Start_Date, String Start_Time, String End_Date,
                                          String End_Time, String Description,String ImagePath,String Update_Time){
        sqlHelper.update(Table_Events.Table_Name,Table_Events.getAllFields_Except_Event_ID(),new String[]{Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, ImagePath, Update_Time},
                new String[]{Table_Events.Event_ID},new String[]{Event_ID});
    }
}
