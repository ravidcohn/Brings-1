package server.Task;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;

import server.CloudEndpointBuilderHelper;

/**
 * Created by Ravid on 20/10/2015.
 */
public class Task_AsyncTask_insert extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public Task_AsyncTask_insert(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            myApiService.taskInsert(params[0],params[1],params[2],params[3],params[4], params[5]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}