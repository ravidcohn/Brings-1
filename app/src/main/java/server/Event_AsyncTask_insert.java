package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.ImagesPath;

/**
 * Created by Ravid on 25/09/2015.
 */
public class Event_AsyncTask_insert extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public Event_AsyncTask_insert(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
                       myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            ImagesPath ipath = myApiService.eventInsert(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7]).execute();
            String url = ipath.getPath();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}


