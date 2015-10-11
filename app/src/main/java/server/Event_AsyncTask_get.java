package server;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Event;
import com.example.some_lie.backend.brings.model.EventFriendCollection;

/**
 * Created by Ravid on 26/09/2015.
 */
public class Event_AsyncTask_get extends AsyncTask<String, Void, Event> {
private static Brings myApiService = null;
private Context context;

    public Event_AsyncTask_get(Context context) {
        this.context = context;
    }

    @Override
    protected Event doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
        try {
            EventFriendCollection eventFriendCollection = myApiService.eventFriendGetEvents(params[0]).execute();

            Event event = myApiService.eventGet(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

            /*
            EventFriendCollection eventFriendCollection = myApiService.eventFriendGetEvents(params[0]).execute();
            EventFriend eventFriend;
            Brings.EventGetEvent event;
            for(int i = 0;i<eventFriendCollection.getItems().size();i++){
                eventFriend = eventFriendCollection.getItems().get(i);
                event = myApiService.eventGetEvent(eventFriend.getEventName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return null;

    }

    @Override
    protected void onPostExecute(Event result) {
        //Toast.makeText(context,result.getMessage(),Toast.LENGTH_LONG).show();
        /*Toast.makeText(context,result.getFrom(),Toast.LENGTH_LONG).show();
        */if(result != null) {
            Toast.makeText(context, "הודעה נשלחה", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"ההודעה לא נשלחה",Toast.LENGTH_LONG).show();
        }
    }
}