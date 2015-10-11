package server;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Event;

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
            return myApiService.eventGet(params[0]).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected Event onPostExecute(Event result) {
        //Toast.makeText(context,result.getMessage(),Toast.LENGTH_LONG).show();
        /*Toast.makeText(context,result.getFrom(),Toast.LENGTH_LONG).show();
        */
        Event event = null;
        if(result != null) {
            event = new Event();
            Toast.makeText(context, "הודעה נשלחה", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"ההודעה לא נשלחה",Toast.LENGTH_LONG).show();
        }
        return new Event();
    }
}