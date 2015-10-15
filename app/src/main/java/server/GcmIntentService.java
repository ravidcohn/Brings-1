package server;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Event;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
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
                        try {
                            Event result = myApiService.eventGet(message).execute();
                            String name = result.getName();
                            String id = result.getId();
                            String location = result.getLocation();
                            String start_date = result.getStartDate();
                            String end_date = result.getEndDate();
                            String description = result.getDescription();
                            String image_path = result.getImageUrl();
                            String update_time = result.getUpdateTime();
                            sqlHelper.insert(Constants.Table_Events, new String[]{"test"+Math.random(), name, location, start_date, end_date, description, image_path, update_time});
                            //TODO some shit..
                        } catch (IOException e) {
                            e.printStackTrace();
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

}