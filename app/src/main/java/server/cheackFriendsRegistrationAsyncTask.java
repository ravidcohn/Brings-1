package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.RegistrationRecord;
import com.example.some_lie.backend.brings.model.RegistrationRecordCollection;

import java.util.ArrayList;

import utils.Constants;
import utils.sqlHelper;

/**
 * Created by pinhas on 14/10/2015.
 */
public class cheackFriendsRegistrationAsyncTask extends AsyncTask<ArrayList<String>, Void, RegistrationRecordCollection> {
    private static Brings myApiService = null;
    private Context context;
    private ArrayList<String> phones;
    public cheackFriendsRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected RegistrationRecordCollection doInBackground(ArrayList<String>... params) {
        if (myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
        try {
            String new_regId = null;
            String old_regId = null;
            if(params[1] != null){
                new_regId = params[1].get(0);
                old_regId = params[1].get(1);
            }
            this.phones = params[0];
            return myApiService.checkfriendsRegistration(new_regId, old_regId, Constants.Password, phones, Constants.User_Name).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(RegistrationRecordCollection result) {
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                sqlHelper.update("Friends",new String[]{"email","regester"},new String[]{result.getItems().get(i).getMail(),Constants.Yes},new String[]{"Phone"},new String[]{phones.get(i)});
            }
        }
    }
}
