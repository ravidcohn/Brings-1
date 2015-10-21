package server;

import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.RegistrationRecord;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pinhas on 21/10/2015.
 */
public class add_logAsyncTask extends AsyncTask<String, Void, Void>{
    private static Brings myApiService = null;

    @Override
    protected Void doInBackground(String... params) {
        if (myApiService == null) {
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }

        return null;
    }
}
