package server;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.EventFriendCollection;

/**
 * Created by Ravid on 11/10/2015.
 */
public class EventFriend_AsyncTask_getFriend_by_event  extends AsyncTask<String, Void, EventFriendCollection> {
    private static Brings myApiService = null;
    private Context context;
    private EventFriendCollection eventFriendCollection;

    public EventFriendCollection getEventFriendCollection() {
        return eventFriendCollection;
    }

    public EventFriend_AsyncTask_getFriend_by_event(Context context) {
        this.context = context;
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
        //Toast.makeText(context,result.getMessage(),Toast.LENGTH_LONG).show();
        /*Toast.makeText(context,result.getFrom(),Toast.LENGTH_LONG).show();
        */
        if (result != null) {
            eventFriendCollection = result;
            Toast.makeText(context, "הודעה נשלחה", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "ההודעה לא נשלחה", Toast.LENGTH_LONG).show();
        }
    }
}