package server.Event;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;

import server.CloudEndpointBuilderHelper;

/**
 * Created by Ravid on 11/10/2015.
 */
public class Event_AsyncTask_delete  extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public Event_AsyncTask_delete(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            myApiService.eventDelete(params[0]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

