package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;

/**
 * Created by Ravid on 11/10/2015.
 */
public class EventFriend_AsyncTask_insert extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public EventFriend_AsyncTask_insert(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            myApiService.eventFriendInsert(params[0],params[1]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}