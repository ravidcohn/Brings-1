package server;

import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.User;

import utils.Constants;
import utils.sqlHelper;

/**
 * Created by Ravid on 19/10/2015.
 */
public class User_AsyncTask_get extends AsyncTask<String, Void, User> {
    private static Brings myApiService = null;

    public User_AsyncTask_get() {
    }

    @Override
    protected User doInBackground(String... params) {
        if (myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
        try {
            return myApiService.userGet(params[0]).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(User result) {
        sqlHelper.insert(Constants.Table_Users, new String[]{result.getEmail(), result.getName()});
    }
}