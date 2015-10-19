package server;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Event;
import com.example.some_lie.backend.brings.model.Task;
import com.example.some_lie.backend.brings.model.EventFriendCollection;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Constants;
import utils.sqlHelper;


/**
 * Created by pinhas on 24/09/2015.
 */
public class GcmIntentService extends IntentService{
    private static Brings myApiService = null;
    public GcmIntentService() {
        super("GcmIntentService");
        if (myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String key = "";
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
                    key = extras.getString("message").split("\\|")[1];
                }
                switch (action){
                    case Constants.New_Event: {
                        String[] event = getEvent(key);
                        ArrayList<String[]> allAttending = getAllAttending(key);
                        if(sqlHelper.select(null,Constants.Table_Events,new String[]{"ID"},new String[]{event[0]},null)[0].isEmpty()){
                            sqlHelper.insert(Constants.Table_Events, event);
                            for(String[] attending:allAttending){
                                sqlHelper.insert(Constants.Table_Events_Friends, attending);
                            }
                        }
                        break;
                    }
                    case Constants.Delete_Event:{
                        sqlHelper.delete(Constants.Table_Events, new String[]{"id"}, new String[]{key}, new int[]{1});
                        sqlHelper.delete(Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0]}, new String[]{key}, new int[]{1});
                        break;
                    }
                    case Constants.Update_Event:{
                        String[] event = getEvent(key);
                        sqlHelper.update(Constants.Table_Events, new String[]{Constants.Table_Events_Fields[1], Constants.Table_Events_Fields[2], Constants.Table_Events_Fields[3], Constants.Table_Events_Fields[4],
                                        Constants.Table_Events_Fields[5], Constants.Table_Events_Fields[6], Constants.Table_Events_Fields[7]},
                                new String[]{event[1], event[2], event[3], event[4], event[5], event[6], event[7]}, new String[]{"id"}, new String[]{event[0]});
                        sqlHelper.delete(Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0]}, new String[]{key}, null);
                        break;
                    }
                    case Constants.New_Attending:{
                        String Event_ID = key.split("\\^")[0];
                        String Friend_ID = key.split("\\^")[1];
                        String attend = "";
                        sqlHelper.insert(Constants.Table_Events_Friends, new String[]{Event_ID, Friend_ID, attend});
                        ArrayList<String>[] dbUsers = sqlHelper.select(null, Constants.Table_Users, new String[]{Constants.Table_Users_Fields[0]}, new String[]{Friend_ID}, null);
                        if(dbUsers[0].isEmpty()) {
                            try {
                                String name = myApiService.userGet(Friend_ID).execute().getName();
                                sqlHelper.insert(Constants.Table_Users,new String[]{Friend_ID,name});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case Constants.Delete_Attending:{
                        String Event_ID = key.split("\\^")[0];
                        String Friend_ID = key.split("\\^")[1];
                        sqlHelper.delete(Constants.Table_Events_Friends,new String[]{Constants.Table_Events_Friends_Fields[0],Constants.Table_Events_Friends_Fields[0]},
                                new String[]{Event_ID,Friend_ID},new int[]{1});
                        break;
                    }
                    case Constants.Update_Attending:{
                        String Event_ID = key.split("\\^")[0];
                        String Friend_ID = key.split("\\^")[1];
                        String attend = key.split("\\^")[2];
                        sqlHelper.update(Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[2]}, new String[]{attend},
                                new String[]{Constants.Table_Events_Friends_Fields[0], Constants.Table_Events_Friends_Fields[1]}, new String[]{Event_ID, Friend_ID});
                        break;
                    }
                    case Constants.New_Task:{
                        String Event_ID = key.split("\\^")[0];
                        String Task_ID = key.split("\\^")[1];
                        String[] task = getTask(Event_ID, Task_ID);
                        if(sqlHelper.select(null,Constants.Table_Tasks,new String[]{"Event_ID", "Task_ID_Number"},new String[]{task[0], task[1]},null)[0].isEmpty()) {
                            sqlHelper.insert(Constants.Table_Tasks, task);
                        }
                        break;
                    }
                    case Constants.Delete_Task:{
                        /*
                        String Event_ID = key.split("\\^")[0];
                        String Friend_ID = key.split("\\^")[1];
                        sqlHelper.delete(Constants.Table_Events_Friends,new String[]{Constants.Table_Events_Friends_Fields[0],Constants.Table_Events_Friends_Fields[0]},
                                new String[]{Event_ID,Friend_ID},new int[]{1});
                                */
                        break;
                    }
                    case Constants.Update_Task:{
                        /*
                        String Event_ID = key.split("\\^")[0];
                        String Friend_ID = key.split("\\^")[1];
                        String attend = key.split("\\^")[2];
                        sqlHelper.update(Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[2]}, new String[]{attend},
                                new String[]{Constants.Table_Events_Friends_Fields[0], Constants.Table_Events_Friends_Fields[1]}, new String[]{Event_ID, Friend_ID});
                                */
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
    }



    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String[] getTask(String event_id, String task_id) {
        Task task;
        String[] result = new String[5];
        try {
            task = myApiService.taskGet(event_id, task_id).execute();
            result[0] = task.getEventID();
            result[1] = task.getTaskNumber();
            result[2] = task.getTaskName();
            result[3] = task.getDescription();
            result[4] = task.getWho();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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

    private ArrayList<String[]> getAllAttending(String event_id) {
        ArrayList<String[]>result = new ArrayList<>();
        EventFriendCollection eventFriendCollection;
        try {
            eventFriendCollection = myApiService.eventFriendGetFriends(event_id).execute();
            for(int i=0;i<eventFriendCollection.getItems().size();i++){
                result.add(new String[]{eventFriendCollection.getItems().get(i).getEventName(),
                        eventFriendCollection.getItems().get(i).getFriendName(),eventFriendCollection.getItems().get(i).getAttending()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}