package backend;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Event;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Ravid on 25/09/2015.
 */
public class Event_AsyncTask_insert extends AsyncTask<String, Void, Event> {
    private static Brings myApiService = null;
    private Context context;

    public Event_AsyncTask_insert(Context context) {
        this.context = context;
    }

    @Override
    protected Event doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
                       myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            return myApiService.insert(params[0],params[1],params[2],params[3],params[4],params[5],params[6]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

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