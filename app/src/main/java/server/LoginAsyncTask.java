package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.RegistrationRecord;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import utils.Constants;

/**
 * Created by pinhas on 12/10/2015.
 */
public class LoginAsyncTask extends AsyncTask<String, Void, RegistrationRecord> {
    private static Brings myApiService = null;
    private ServerAsyncResponse delegate;
    private GoogleCloudMessaging gcm;
    private Context context;

    public LoginAsyncTask(ServerAsyncResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected RegistrationRecord doInBackground(String... params) {
        if(myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
        try {
            String msg = "";
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            String regId = gcm.register(Constants.SENDER_ID);

            return myApiService.authentication(params[0], params[1], regId).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(RegistrationRecord result) {
        String message = "Cannot connect the server.. please try again later";
        if(result != null) {
            message = result.getMail();
        }
        delegate.processFinish(message);
    }
}

