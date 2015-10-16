package server;

import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.EventFriendCollection;

/**
 * Created by Ravid on 11/10/2015.
 */
public class EventFriend_AsyncTask_getFriend_by_event extends AsyncTask<String, Void, EventFriendCollection> {
    private static Brings myApiService = null;
    private ServerAsyncResponse delegate;


    public EventFriend_AsyncTask_getFriend_by_event(ServerAsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected EventFriendCollection doInBackground(String... params) {
        if (myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
        try {
           return myApiService.eventFriendGetEvents(params[0]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(EventFriendCollection result) {
        //delegate.EventFriendCollectionProcessFinish(result);
    }
}