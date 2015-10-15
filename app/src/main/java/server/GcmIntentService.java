package server;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Event;
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
        String message = "";
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
                    message = extras.getString("message").split("\\|")[1];
                }
                switch (action){
                    case Constants.New_Event: {
                        String[] event = getEvent(message);
                        ArrayList<String[]> allAttending = getAllAttending(message);
                        if(sqlHelper.select(null,Constants.Table_Events,new String[]{"ID"},new String[]{event[0]},null)[0].isEmpty()){
                            sqlHelper.insert(Constants.Table_Events, event);
                            for(String[] attending:allAttending){
                                sqlHelper.insert(Constants.Table_Events_Friends, attending);
                            }
                        }
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