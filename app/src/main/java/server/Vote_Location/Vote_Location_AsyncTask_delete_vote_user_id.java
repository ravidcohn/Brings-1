package server.Vote_Location;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;

import server.CloudEndpointBuilderHelper;

/**
 * Created by Ravid on 08/03/2016.
 */
public class Vote_Location_AsyncTask_delete_vote_user_id extends AsyncTask<String, Void, Void> {
    private static Brings myApiService = null;
    private Context context;

    public Vote_Location_AsyncTask_delete_vote_user_id(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        try {
            myApiService.voteLocationDelete(params[0], params[1], params[2]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}