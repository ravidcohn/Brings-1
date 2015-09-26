package backend;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.some_lie.backend.eventApi.EventApi;
import com.example.some_lie.backend.eventApi.model.Event;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Ravid on 25/09/2015.
 */
public class Event_AsyncTask extends AsyncTask<String, Void, Event> {
    private static EventApi myApiService = null;
    private Context context;

    public Event_AsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Event doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            EventApi.Builder builder = new EventApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    //  .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setRootUrl("https://encoded-keyword-106406.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            return myApiService.insert(params[0],params[1]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Event result) {
        //Toast.makeText(context, result.getName(), Toast.LENGTH_LONG).show();
        //Toast.makeText(context,result.getMessage(),Toast.LENGTH_LONG).show();
        /*Toast.makeText(context,result.getFrom(),Toast.LENGTH_LONG).show();
        */if(result != null) {
            Toast.makeText(context, "הודעה נשלחה", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"ההודעה לא נשלחה",Toast.LENGTH_LONG).show();
        }
    }
}