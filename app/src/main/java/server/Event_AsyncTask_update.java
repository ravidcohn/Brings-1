package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;

/**
 * Created by Ravid on 11/10/2015.
 */
public class Event_AsyncTask_update extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public Event_AsyncTask_update(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            myApiService.eventUpdate(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
