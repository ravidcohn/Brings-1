package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;

/**
 * Created by Ravid on 18/10/2015.
 */
public class EventFriend_AsyncTask_delete extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public EventFriend_AsyncTask_delete(Context context) {
            this.context = context;
            }

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            myApiService.eventFriendDelete(params[0],params[1]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
