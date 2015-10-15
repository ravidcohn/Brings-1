package server;

import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Event;

/**
 * Created by Ravid on 26/09/2015.
 */
public class Event_AsyncTask_get extends AsyncTask<String, Void, Event> {
    private static Brings myApiService = null;
    private ServerAsyncResponse delegate;

    public Event_AsyncTask_get(ServerAsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Event doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
        try {
           return myApiService.eventGet(params[0]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Event result) {
        delegate.EventProcessFinish(result);
    }
}