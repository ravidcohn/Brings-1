package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;

/**
 * Created by Ravid on 23/10/2015.
 */
public class Chat_AsyncTask_CreateByEvent extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public Chat_AsyncTask_CreateByEvent(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            myApiService.chatCreateByEvent(params[0]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}