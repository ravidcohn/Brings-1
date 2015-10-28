package server;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Chat;
import com.example.some_lie.backend.brings.model.ChatCollection;
import com.example.some_lie.backend.brings.model.Event;
import com.example.some_lie.backend.brings.model.EventFriend;
import com.example.some_lie.backend.brings.model.EventFriendCollection;
import com.example.some_lie.backend.brings.model.Task;
import com.example.some_lie.backend.brings.model.TaskCollection;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Constans.Constants;
import utils.Constans.Table_Chat;
import utils.Constans.Table_Events;
import utils.Constans.Table_Events_Friends;
import utils.Constans.Table_Tasks;
import utils.Constans.Table_Users;
import utils.Helper;
import utils.sqlHelper;


/**
 * Created by pinhas on 24/09/2015.
 */
public class GcmIntentService extends IntentService{
    private static Brings myApiService = null;
    public static ServerAsyncResponse delegate = null;

    public GcmIntentService() {
        super("GcmIntentService");
        if (myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String details = "";
        String action = "";
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());
                if(extras.getString("message").split("\\|").length>1) {
                    action = extras.getString("message").split("\\|")[0].split(": ")[1];
                    details = extras.getString("message").split("\\|")[1];
                }
                switch (action){
                    case Constants.New_Event: {
                        String[] event = getEvent(details);
                        ArrayList<String[]> allAttending = getAllAttending(details);
                        ArrayList<String[]> allTasks = getAllTasks(details);
                        String Chat_ID = Table_Chat.Table_Name + Helper.Clean_Event_ID(details);
                        ArrayList<String[]> allChat = getAllChat(Chat_ID);
                        if(sqlHelper.select(null, Table_Events.Table_Name, new String[]{Table_Events.Event_ID},new String[]{event[0]},new int[1])[0].isEmpty()){
                            sqlHelper.insert(Table_Events.Table_Name, event);
                            for(String[] attending:allAttending){
                                sqlHelper.insert(Table_Events_Friends.Table_Name, attending);
                                String Friend_ID = attending[1];
                                getNickName(Friend_ID);
                            }
                            for(String[] task:allTasks){
                                sqlHelper.insert(Table_Tasks.Table_Name, task);
                            }
                            sqlHelper.Create_Table(Chat_ID, Table_Chat.getAllFields(), Table_Chat.getAllSqlParams());
                            for(String[] chat:allChat){
                                sqlHelper.insert(Chat_ID, chat);
                            }
                        }
                        break;
                    }
                    case Constants.Delete_Event:{
                        Helper.Delete_Event_MySQL(details);
                        break;
                    }
                    case Constants.Update_Event:{
                        String[] event = getEvent(details);
                        Helper.Event_Update_MySQL(event[0], event[1], event[2], event[3], event[4], event[5], event[6], event[7], event[8], event[9]);
                        break;
                    }
                    case Constants.New_Attending:{
                        String Event_ID = details.split("\\^")[0];
                        String Friend_ID = details.split("\\^")[1];
                        String[] event_friend = getEventFriend(Event_ID,Friend_ID);
                        sqlHelper.insert(Table_Events_Friends.Table_Name, event_friend);
                        getNickName(Friend_ID);
                        break;
                    }
                    case Constants.Delete_Attending:{
                        String Event_ID = details.split("\\^")[0];
                        String Friend_ID = details.split("\\^")[1];
                        if(Friend_ID.equals(Constants.User_Name)){
                            Helper.Delete_Event_MySQL(Event_ID);
                        }else {
                            sqlHelper.delete(Table_Events_Friends.Table_Name, new String[]{Table_Events_Friends.Event_ID, Table_Events_Friends.Friend_ID},
                                    new String[]{Event_ID, Friend_ID}, new int[]{1});
                        }
                        break;
                    }
                    case Constants.Update_Attending:{
                        String Event_ID = details.split("\\^")[0];
                        String Friend_ID = details.split("\\^")[1];
                        String attend = details.split("\\^")[2];
                        sqlHelper.update(Table_Events_Friends.Table_Name, new String[]{Table_Events_Friends.Attending}, new String[]{attend},
                                new String[]{Table_Events_Friends.Event_ID, Table_Events_Friends.Friend_ID}, new String[]{Event_ID, Friend_ID});
                        break;
                    }
                    case Constants.New_Task:{
                        String Event_ID = details.split("\\^")[0];
                        String Task_ID_Number = details.split("\\^")[1];
                        String[] task = getTask(Event_ID, Task_ID_Number);
                        if(sqlHelper.select(null,Table_Tasks.Table_Name,new String[]{Table_Tasks.Event_ID, Table_Tasks.Task_ID_Number},
                                new String[]{task[0], task[1]},null)[0].isEmpty()) {
                            sqlHelper.insert(Table_Tasks.Table_Name, task);
                        }
                        break;
                    }
                    case Constants.Delete_Task:{
                        String Event_ID = details.split("\\^")[0];
                        String Task_ID_Number = details.split("\\^")[1];
                        sqlHelper.delete(Table_Tasks.Table_Name, new String[]{Table_Tasks.Event_ID, Table_Tasks.Task_ID_Number},
                                new String[]{Event_ID, Task_ID_Number}, new int[]{1});
                        break;
                    }
                    case Constants.Update_Task:{
                        String Event_ID = details.split("\\^")[0];
                        String Task_ID_Number = details.split("\\^")[1];
                        String[] task = getTask(Event_ID, Task_ID_Number);
                        sqlHelper.update(Table_Tasks.Table_Name, new String[]{Table_Tasks.Task_Name, Table_Tasks.Description, Table_Tasks.Friend_ID},
                                new String[]{task[2],task[3],task[4]}, new String[]{Table_Tasks.Event_ID, Table_Tasks.Task_ID_Number}, new String[]{Event_ID, Task_ID_Number});
                        break;
                    }
                    case Constants.Update_Task_Friend_ID:{
                        String Event_ID = details.split("\\^")[0];
                        String Task_ID_Number = details.split("\\^")[1];
                        String Friend_ID = details.split("\\^")[2];
                        sqlHelper.update(Table_Tasks.Table_Name, new String[]{Table_Tasks.Friend_ID}, new String[]{Friend_ID},
                                new String[]{Table_Tasks.Event_ID, Table_Tasks.Task_ID_Number}, new String[]{Event_ID, Task_ID_Number});
                        break;
                    }
                    case Constants.New_Chat_Message:{
                        String Chat_ID = details.split("\\^")[0];
                        String Message_ID = details.split("\\^")[1];
                        String[] chat = getChat(Chat_ID, Message_ID);
                        if(sqlHelper.select(null,Chat_ID,new String[]{Table_Chat.Message_ID},new String[]{chat[0]},null)[0].isEmpty()) {
                            sqlHelper.insert(Chat_ID, chat);
                        }
                        break;
                    }
                    case Constants.Delete_Chat_Message:{
                        String Chat_ID = details.split("\\^")[0];
                        String Message_ID = details.split("\\^")[1];
                        sqlHelper.delete(Chat_ID, new String[]{Table_Chat.Message_ID},
                                new String[]{Message_ID}, new int[]{1});
                        break;
                    }
                    default: {
                        showToast(extras.getString("message"));
                        break;
                    }
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
        delegate.processFinish(Constants.Update_Activity);
    }

    private void getNickName(String Friend_ID) {
        ArrayList<String>[] dbUsers = sqlHelper.select(null, Table_Users.Table_Name, new String[]{Table_Users.Friend_ID}, new String[]{Friend_ID}, new int[]{1});
        if(dbUsers[0].isEmpty()) {
            try {
                String Nickname = myApiService.userGet(Friend_ID).execute().getFriendID();
                sqlHelper.insert(Table_Users.Table_Name,new String[]{Friend_ID, Nickname});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String[] getEvent(String event_id){
        Event event;
        String[] result = new String[8];
        try {
            event = myApiService.eventGet(event_id).execute();
            result[0] = event.getId();
            result[1] = event.getName();
            result[2] = event.getLocation();
            result[3] = event.getStartDate();
            result[4] = event.getEndDate();
            result[5] = event.getDescription();
            result[6] = event.getImageUrl();
            result[7] = event.getUpdateTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private String[] getEventFriend(String event_id, String friend_id){
        EventFriend eventFriend;
        String[] result = new String[4];
        try {
            eventFriend = myApiService.eventFriendGet(event_id,friend_id).execute();
            result[0] = eventFriend.getEventName();
            result[1] = eventFriend.getFriendName();
            result[2] = eventFriend.getAttending();
            result[3] = eventFriend.getPermission();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private String[] getTask(String event_id, String task_id) {
        Task task;
        String[] result = new String[5];
        try {
            task = myApiService.taskGet(event_id, task_id).execute();
            result[0] = task.getEventID();
            result[1] = task.getTaskIDNumber();
            result[2] = task.getTaskName();
            result[3] = task.getDescription();
            result[4] = task.getFriendID();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private String[] getChat(String chat_id, String message_id) {
        Chat chat;
        String[] result = new String[5];
        try {
            chat = myApiService.chatGet(chat_id, message_id).execute();
            result[0] = chat.getMessageID();
            result[1] = chat.getFriendIDSender();
            result[2] = chat.getMessage();
            result[3] = chat.getDate();
            result[4] = chat.getTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<String[]> getAllAttending(String event_id) {
        ArrayList<String[]>result = new ArrayList<>();
        EventFriendCollection eventFriendCollection;
        try {
            eventFriendCollection = myApiService.eventFriendGetFriends(event_id).execute();
            for(int i=0;i<eventFriendCollection.getItems().size();i++){
                result.add(new String[]{eventFriendCollection.getItems().get(i).getEventName(), eventFriendCollection.getItems().get(i).getFriendName(),
                        eventFriendCollection.getItems().get(i).getAttending(),eventFriendCollection.getItems().get(i).getPermission()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private ArrayList<String[]> getAllTasks(String event_id) {
        ArrayList<String[]>result = new ArrayList<>();
        TaskCollection taskCollection;
        try {
            taskCollection = myApiService.taskGetAll(event_id).execute();
            if(taskCollection.getItems()!=null) {
                for (int i = 0; i < taskCollection.getItems().size(); i++) {
                    result.add(new String[]{taskCollection.getItems().get(i).getEventID(), taskCollection.getItems().get(i).getTaskIDNumber(),
                            taskCollection.getItems().get(i).getTaskName(), taskCollection.getItems().get(i).getDescription(), taskCollection.getItems().get(i).getFriendID()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private ArrayList<String[]> getAllChat(String Chat_ID) {
        ArrayList<String[]>result = new ArrayList<>();
        ChatCollection chatCollection;
        try {
            chatCollection = myApiService.chatGetAll(Chat_ID).execute();
            if(chatCollection.getItems()!=null) {
                for (int i = 0; i < chatCollection.getItems().size(); i++) {
                    result.add(new String[]{chatCollection.getItems().get(i).getMessageID(), chatCollection.getItems().get(i).getFriendIDSender(),
                            chatCollection.getItems().get(i).getMessage(), chatCollection.getItems().get(i).getDate(), chatCollection.getItems().get(i).getTime()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}