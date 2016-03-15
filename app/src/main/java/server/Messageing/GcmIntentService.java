package server.Messageing;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Chat;
import com.example.some_lie.backend.brings.model.ChatCollection;
import com.example.some_lie.backend.brings.model.Event;
import com.example.some_lie.backend.brings.model.EventUser;
import com.example.some_lie.backend.brings.model.EventUserCollection;
import com.example.some_lie.backend.brings.model.Task;
import com.example.some_lie.backend.brings.model.TaskCollection;
import com.example.some_lie.backend.brings.model.VoteDateCollection;
import com.example.some_lie.backend.brings.model.VoteLocationCollection;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import brings_app.MainActivity;
import brings_app.R;
import server.CloudEndpointBuilderHelper;
import server.ServerAsyncResponse;
import server.cloudStorage;
import utils.Constans.Constants;
import utils.Constans.Table_Chat;
import utils.Constans.Table_Events;
import utils.Constans.Table_Events_Users;
import utils.Constans.Table_Tasks;
import utils.Constans.Table_Vote_Date;
import utils.Constans.Table_Vote_Location;
import utils.Helper;
import utils.sqlHelper;


/**
 * Created by pinhas on 24/09/2015.
 */
public class GcmIntentService extends IntentService {
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
                if (extras.getString(Constants.Message).split("\\|").length > 1) {
                    action = extras.getString(Constants.Message).split("\\|")[0].split(": ")[1];
                    details = extras.getString(Constants.Message).split("\\|")[1];
                }
                switch (action) {
                    case Constants.New_Event: {
                        String[] event = getEvent(details);
                        //String picName = event[Table_Events.Image_Path_num];
                        //event[Table_Events.Image_Path_num] = Constants.imageSaveLocation + "/" + picName;

                        //Check if Event exist.
                        if (sqlHelper.select(null, Table_Events.Table_Name, new String[]{Table_Events.Event_ID}, new String[]{event[Table_Events.Event_ID_num]}, new int[1])[0].isEmpty()) {
                            ArrayList<String[]> allUsers = getAllAttending(details);
                            ArrayList<String[]> allTasks = getAllTasks(details);
                            ArrayList<String[]> allVote_Date = getAllVotes_Date(details);
                            ArrayList<String[]> allVote_Location = getAllVotes_Location(details);

                            String Chat_ID = Table_Chat.Table_Name + Helper.Clean_Event_ID(details);
                            ArrayList<String[]> allChat = getAllChat(Chat_ID);
                            //Add event to my sql.
                            sqlHelper.insert(Table_Events.Table_Name, event);
                            for (String[] User : allUsers) {
                                //Add user to my sql.
                                sqlHelper.insert(Table_Events_Users.Table_Name, User);
                                //Add User to my sql Users (check inside if the user already exist).
                                String User_ID = User[Table_Events_Users.User_ID_num];
                                Helper.User_Insert_MySQL(User_ID);
                            }
                            //Add task to my sql.
                            for (String[] task : allTasks) {
                                sqlHelper.insert(Table_Tasks.Table_Name, task);
                            }
                            //Add vote_date to my sql.
                            for (String[] vote : allVote_Date) {
                                sqlHelper.insert(Table_Vote_Date.Table_Name, vote);
                            }
                            //Add vote_location to my sql.
                            for (String[] vote : allVote_Location) {
                                sqlHelper.insert(Table_Vote_Location.Table_Name, vote);
                            }
                            //Add all missing messages.
                            sqlHelper.Create_Table(Chat_ID, Table_Chat.getAllFields(), Table_Chat.getAllSqlParams());
                            for (String[] chat : allChat) {
                                sqlHelper.insert(Chat_ID, chat);
                            }
                            try {
                                //cloudStorage.downloadFile(Constants.bucket_name, picName, getApplicationContext());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            addNotification("New Event", "You got invite to new event: " + event[Table_Events.Name_num]);
                        }
                        break;
                    }
                    case Constants.Delete_Event: {
                        boolean close_event = false;
                        if(delegate != null){
                            String event_id = delegate.currentLocation();
                            if(event_id.contains(" - ")){
                                event_id = event_id.substring(Constants.event.length());
                                if(event_id.equals(details)){
                                    close_event = true;
                                }
                            }
                        }
                        Helper.Delete_Event_MySQL(details);
                        if(close_event)delegate.closeActivity();
                        break;
                    }
                    case Constants.Update_Event: {
                        String Event_ID = details.split("|")[0];
                        String[] update_section = new String[]{details.split("|")[1], details.split("|")[2], details.split("|")[3], details.split("|")[4], details.split("|")[5]};
                        //Update event details.
                        String[] event = getEvent(Event_ID);
                        if (update_section[0].equals(Constants.Yes)) {
                            Helper.Update_Event_details_MySQL(event);
                            //Helper.Update_Event_details_MySQL(event[0], event[1], event[2], event[3], event[4], event[5], event[6], event[7], Constants.imageSaveLocation + "/" + event[8], event[9]);
                        }
                        //Update event users.
                        if (update_section[1].equals(Constants.Yes)) {
                            //Delete all users from the event.
                            sqlHelper.delete(Table_Events_Users.Table_Name, new String[]{Table_Events_Users.Event_ID}, new String[]{Event_ID}, null);
                            //Add all Users.
                            ArrayList<String[]> allUsers = getAllAttending(details);
                            for (String[] User : allUsers) {
                                //Add user to my sql.
                                sqlHelper.insert(Table_Events_Users.Table_Name, User);
                                //Add User to my sql Users (check inside if the user already exist).
                                String User_ID = User[Table_Events_Users.User_ID_num];
                                Helper.User_Insert_MySQL(User_ID);
                            }
                        }
                        //Update event tasks.
                        if (update_section[2].equals(Constants.Yes)) {
                            //Delete all tasks from the event.
                            sqlHelper.delete(Table_Tasks.Table_Name, new String[]{Table_Events_Users.Event_ID}, new String[]{Event_ID}, null);
                            ArrayList<String[]> allTasks = getAllTasks(details);
                            //Add task to my sql.
                            for (String[] task : allTasks) {
                                sqlHelper.insert(Table_Tasks.Table_Name, task);
                            }
                        }
                        //Update event vote_date.
                        if (update_section[3].equals(Constants.Yes)) {
                            //Delete all tasks from the event.
                            sqlHelper.delete(Table_Vote_Date.Table_Name, new String[]{Table_Vote_Date.Event_ID}, new String[]{Event_ID}, null);
                            ArrayList<String[]> allVote_Date = getAllVotes_Date(details);
                            //Add task to my sql.
                            for (String[] vote : allVote_Date) {
                                sqlHelper.insert(Table_Vote_Date.Table_Name, vote);
                            }
                        }
                        //Update event vote_location.
                        if (update_section[4].equals(Constants.Yes)) {
                            //Delete all tasks from the event.
                            sqlHelper.delete(Table_Vote_Location.Table_Name, new String[]{Table_Vote_Location.Event_ID}, new String[]{Event_ID}, null);
                            ArrayList<String[]> allVote_Location = getAllVotes_Location(details);
                            //Add task to my sql.
                            for (String[] vote : allVote_Location) {
                                sqlHelper.insert(Table_Vote_Location.Table_Name, vote);
                            }
                        }
                        try {
                            cloudStorage.downloadFile(Constants.bucket_name, event[8], getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case Constants.New_User: {
                        String Event_ID = details.split("\\^")[0];
                        String USer_ID = details.split("\\^")[1];
                        String[] event_friend = getEventFriend(Event_ID, USer_ID);
                        sqlHelper.insert(Table_Events_Users.Table_Name, event_friend);
                        Helper.User_Insert_MySQL(USer_ID);
                        break;
                    }
                    case Constants.Delete_User: {
                        String Event_ID = details.split("\\^")[0];
                        String USer_ID = details.split("\\^")[1];
                        if (USer_ID.equals(Constants.MY_User_ID)) {
                            Helper.Delete_Event_MySQL(Event_ID);
                        } else {
                            sqlHelper.delete(Table_Events_Users.Table_Name, new String[]{Table_Events_Users.Event_ID, Table_Events_Users.User_ID},
                                    new String[]{Event_ID, USer_ID}, new int[]{1});
                        }
                        break;
                    }
                    case Constants.Update_User_Attending: {
                        String Event_ID = details.split("\\^")[0];
                        String USer_ID = details.split("\\^")[1];
                        String attend = details.split("\\^")[2];
                        sqlHelper.update(Table_Events_Users.Table_Name, new String[]{Table_Events_Users.Attending}, new String[]{attend},
                                new String[]{Table_Events_Users.Event_ID, Table_Events_Users.User_ID}, new String[]{Event_ID, USer_ID});
                        break;
                    }
                    case Constants.New_Task: {
                        String Event_ID = details.split("\\^")[0];
                        String Task_ID_Number = details.split("\\^")[1];
                        String subTask_ID_Number = details.split("\\^")[2];
                        String[] task = getTask(Event_ID, Task_ID_Number, subTask_ID_Number);
                        if (sqlHelper.select(null, Table_Tasks.Table_Name, new String[]{Table_Tasks.Event_ID, Table_Tasks.Task_ID_Number},
                                new String[]{task[0], task[1]}, null)[0].isEmpty()) {
                            sqlHelper.insert(Table_Tasks.Table_Name, task);
                        }
                        break;
                    }
                    case Constants.Delete_Task: {
                        String Event_ID = details.split("\\^")[0];
                        String Task_ID_Number = details.split("\\^")[1];
                        sqlHelper.delete(Table_Tasks.Table_Name, new String[]{Table_Tasks.Event_ID, Table_Tasks.Task_ID_Number},
                                new String[]{Event_ID, Task_ID_Number}, new int[]{1});
                        break;
                    }
                    case Constants.Update_Task: {
                        String Event_ID = details.split("\\^")[0];
                        String Task_ID_Number = details.split("\\^")[1];
                        String subTask_ID_Number = details.split("\\^")[2];
                        String[] task = getTask(Event_ID, Task_ID_Number, subTask_ID_Number);
                        sqlHelper.update(Table_Tasks.Table_Name, new String[]{Table_Tasks.Description, Table_Tasks.Description, Table_Tasks.User_ID},
                                new String[]{task[2], task[3], task[4]}, new String[]{Table_Tasks.Event_ID, Table_Tasks.Task_ID_Number}, new String[]{Event_ID, Task_ID_Number});
                        break;
                    }
                    case Constants.Update_Task_User_ID: {
                        String Event_ID = details.split("\\^")[0];
                        String Task_ID_Number = details.split("\\^")[1];
                        String User_ID = details.split("\\^")[2];
                        sqlHelper.update(Table_Tasks.Table_Name, new String[]{Table_Tasks.User_ID}, new String[]{User_ID},
                                new String[]{Table_Tasks.Event_ID, Table_Tasks.Task_ID_Number, Table_Tasks.subTask_ID_Number}, new String[]{Event_ID, Task_ID_Number, 0 + ""});
                        break;
                    }
                    case Constants.New_Chat_Message: {
                        String Chat_ID = details.split("\\^")[0];
                        String Message_ID = details.split("\\^")[1];
                        String User_ID = details.split("\\^")[2];
                        String[] chat = getChat(Chat_ID, Message_ID, User_ID);
                        if (sqlHelper.select(null, Chat_ID, new String[]{Table_Chat.Message_ID, Table_Chat.User_ID}, new String[]{Message_ID, User_ID}, null)[0].isEmpty()) {
                            sqlHelper.insert(Chat_ID, chat);
                        }
                        break;
                    }
                    case Constants.Delete_Chat_Message: {
                        String Chat_ID = details.split("\\^")[0];
                        String Message_ID = details.split("\\^")[1];
                        String User_ID = details.split("\\^")[2];
                        sqlHelper.delete(Chat_ID, new String[]{Table_Chat.Message_ID, Table_Chat.User_ID},
                                new String[]{Message_ID, User_ID}, new int[]{1});
                        break;
                    }
                    case Constants.Update_Event_Details_Filed: {
                        String EventID = details.split("\\^")[0];
                        String Filed = details.split("\\^")[1];
                        String Update = details.split("\\^")[2];
                        Helper.update_Event_details_field_MySQL(EventID, Filed, Update);
                        break;
                    }
                    case Constants.Insert_Vote_Date: {
                        String EventID = details.split("\\^")[0];
                        int Vote_ID = Integer.parseInt(details.split("\\^")[1]);
                        String User_ID = details.split("\\^")[2];
                        Helper.add_vote_date_User_ID_MySQL(EventID, Vote_ID, User_ID);
                        break;
                    }
                    case Constants.Delete_Vote_Date: {
                        String EventID = details.split("\\^")[0];
                        int Vote_ID = Integer.parseInt(details.split("\\^")[1]);
                        String User_ID = details.split("\\^")[2];
                        Helper.delete_vote_date_User_ID_MySQL(EventID, Vote_ID, User_ID);
                        break;
                    }
                    case Constants.Insert_Vote_Location: {
                        String EventID = details.split("\\^")[0];
                        int Vote_ID = Integer.parseInt(details.split("\\^")[1]);
                        String User_ID = details.split("\\^")[2];
                        Helper.add_vote_location_User_ID_MySQL(EventID, Vote_ID, User_ID);
                        break;
                    }
                    case Constants.Delete_Vote_Location: {
                        String EventID = details.split("\\^")[0];
                        int Vote_ID = Integer.parseInt(details.split("\\^")[1]);
                        String User_ID = details.split("\\^")[2];
                        Helper.delete_vote_location_User_ID_MySQL(EventID, Vote_ID, User_ID);
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


    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String[] getEvent(String event_id) {
        Event event;
        String[] result = new String[Table_Events.Size()];
        try {
            event = myApiService.eventGet(event_id).execute();
            result[Table_Events.Event_ID_num] = event.getId();
            result[Table_Events.Name_num] = event.getName();
            result[Table_Events.Location_num] = event.getLocation();
            result[Table_Events.Vote_Location_num] = event.getVoteLocation();
            result[Table_Events.Start_Date_num] = event.getStartDate();
            result[Table_Events.End_Date_num] = event.getEndDate();
            result[Table_Events.All_Day_Time_num] = event.getAllDayTime();
            result[Table_Events.Start_Time_num] = event.getStartTime();
            result[Table_Events.End_Time_num] = event.getEndTime();
            result[Table_Events.Vote_Time_num] = event.getVoteTime();
            result[Table_Events.Description_num] = event.getDescription();
            result[Table_Events.Image_Path_num] = event.getImageUrl();
            result[Table_Events.Update_Time_num] = event.getUpdateTime();
            //Clean unsigned filed.
            for (int i = 0; i < result.length; i++) {
                if (result[i] == null)
                    result[i] = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String[] getEventFriend(String event_id, String friend_id) {
        EventUser eventUser;
        String[] result = new String[Table_Events_Users.Size()];
        try {
            eventUser = myApiService.eventUserGet(event_id, friend_id).execute();
            result[Table_Events_Users.Event_ID_num] = eventUser.getEventID();
            result[Table_Events_Users.User_ID_num] = eventUser.getUserID();
            result[Table_Events_Users.Attending_num] = eventUser.getAttending();
            result[Table_Events_Users.Permission_num] = eventUser.getPermission();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String[] getTask(String event_id, String task_id, String subTask_id) {
        Task task;
        String[] result = new String[Table_Tasks.Size()];
        try {
            task = myApiService.taskGet(event_id, task_id, subTask_id).execute();
            result[Table_Tasks.Event_ID_num] = task.getEventID();
            result[Table_Tasks.Task_ID_Number_num] = task.getTaskIDNumber();
            result[Table_Tasks.subTask_ID_Number_num] = task.getSubTaskIDNumber();
            result[Table_Tasks.Task_Type_num] = task.getTaskType();
            result[Table_Tasks.Description_num] = task.getDescription();
            result[Table_Tasks.User_ID_num] = task.getUserID();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String[] getChat(String chat_id, String message_id, String user_id) {
        Chat chat;
        String[] result = new String[Table_Chat.Size()];
        try {
            chat = myApiService.chatGet(chat_id, message_id, user_id).execute();
            result[0] = chat.getMessageID();
            result[1] = chat.getUserIDSender();
            result[2] = chat.getMessage();
            result[3] = chat.getDate();
            result[4] = chat.getTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<String[]> getAllAttending(String event_id) {
        ArrayList<String[]> result = new ArrayList<>();
        EventUserCollection eventUserCollection;
        try {
            eventUserCollection = myApiService.eventUserGetUsers(event_id).execute();
            for (int i = 0; i < eventUserCollection.getItems().size(); i++) {
                result.add(new String[]{eventUserCollection.getItems().get(i).getEventID(), eventUserCollection.getItems().get(i).getUserID(),
                        eventUserCollection.getItems().get(i).getAttending(), eventUserCollection.getItems().get(i).getPermission()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<String[]> getAllTasks(String event_id) {
        ArrayList<String[]> result = new ArrayList<>();
        TaskCollection taskCollection;
        try {
            taskCollection = myApiService.taskGetAll(event_id).execute();
            if (taskCollection.getItems() != null) {
                String[] task;
                for (int i = 0; i < taskCollection.getItems().size(); i++) {
                    task = new String[Table_Tasks.Size()];
                    task[Table_Tasks.Event_ID_num] = taskCollection.getItems().get(i).getEventID();
                    task[Table_Tasks.Task_ID_Number_num] = taskCollection.getItems().get(i).getTaskIDNumber();
                    task[Table_Tasks.subTask_ID_Number_num] = taskCollection.getItems().get(i).getSubTaskIDNumber();
                    task[Table_Tasks.Task_Type_num] = taskCollection.getItems().get(i).getTaskType();
                    task[Table_Tasks.Description_num] = taskCollection.getItems().get(i).getDescription();
                    task[Table_Tasks.User_ID_num] = taskCollection.getItems().get(i).getUserID();
                    task[Table_Tasks.Mark_num] = taskCollection.getItems().get(i).getUserID();
                    result.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<String[]> getAllVotes_Date(String event_id) {
        ArrayList<String[]> result = new ArrayList<>();
        VoteDateCollection voteDateCollection;
        try {
            voteDateCollection = myApiService.voteDateGetAll(event_id).execute();
            if (voteDateCollection.getItems() != null) {
                String[] vote = new String[Table_Vote_Date.Size()];
                for (int i = 0; i < voteDateCollection.getItems().size(); i++) {
                    vote[Table_Vote_Date.Event_ID_num] = voteDateCollection.getItems().get(i).getEventID();
                    vote[Table_Vote_Date.Vote_ID_num] = voteDateCollection.getItems().get(i).getVoteID();
                    vote[Table_Vote_Date.Start_Date_num] = voteDateCollection.getItems().get(i).getStartDate();
                    vote[Table_Vote_Date.End_Date_num] = voteDateCollection.getItems().get(i).getEndDate();
                    vote[Table_Vote_Date.All_Day_Time_num] = voteDateCollection.getItems().get(i).getAllDayTime();
                    vote[Table_Vote_Date.Start_Time_num] = voteDateCollection.getItems().get(i).getStartTime();
                    vote[Table_Vote_Date.End_Time_num] = voteDateCollection.getItems().get(i).getEndTime();
                    vote[Table_Vote_Date.User_ID_num] = voteDateCollection.getItems().get(i).getUserID();
                    result.add(vote);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<String[]> getAllVotes_Location(String event_id) {
        ArrayList<String[]> result = new ArrayList<>();
        VoteLocationCollection voteLocationCollection;
        try {
            voteLocationCollection = myApiService.voteLocationGetAll(event_id).execute();
            if (voteLocationCollection.getItems() != null) {
                String[] vote = new String[Table_Vote_Location.Size()];
                for (int i = 0; i < voteLocationCollection.getItems().size(); i++) {
                    vote[Table_Vote_Location.Event_ID_num] = voteLocationCollection.getItems().get(i).getEventID();
                    vote[Table_Vote_Location.Vote_ID_num] = voteLocationCollection.getItems().get(i).getVoteID();
                    vote[Table_Vote_Location.Description_num] = voteLocationCollection.getItems().get(i).getDescription();
                    vote[Table_Vote_Location.User_ID_num] = voteLocationCollection.getItems().get(i).getUserID();
                    result.add(vote);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<String[]> getAllChat(String Chat_ID) {
        ArrayList<String[]> result = new ArrayList<>();
        ChatCollection chatCollection;
        try {
            chatCollection = myApiService.chatGetAll(Chat_ID).execute();
            if (chatCollection.getItems() != null) {
                for (int i = 0; i < chatCollection.getItems().size(); i++) {
                    result.add(new String[]{chatCollection.getItems().get(i).getMessageID(), chatCollection.getItems().get(i).getUserIDSender(),
                            chatCollection.getItems().get(i).getMessage(), chatCollection.getItems().get(i).getDate(), chatCollection.getItems().get(i).getTime()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Add app running notification

    private void addNotification(String title, String content) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    // Remove notification
    private void removeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(0);
    }

}