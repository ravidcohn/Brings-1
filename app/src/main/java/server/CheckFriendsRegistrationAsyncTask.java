package server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.CollectionResponseRegistrationRecord;
import com.example.some_lie.backend.brings.model.RegistrationRecord;

import java.util.ArrayList;
import java.util.List;

import utils.Constans.Constants;
import utils.Constans.Table_Users;
import utils.sqlHelper;

/**
 * Created by pinhas on 14/10/2015.
 */
public class CheckFriendsRegistrationAsyncTask extends AsyncTask<ArrayList<String>, Void, List<RegistrationRecord>> {
    private static Brings myApiService = null;
    private Context context;
    private ArrayList<String> phones;
    public CheckFriendsRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<RegistrationRecord> doInBackground(ArrayList<String>... params) {
        if (myApiService == null) { // Only do this once
            myApiService = CloudEndpointBuilderHelper.getEndpoints();
        }
        try {
            String new_regId = "!";
            String old_regId = "!";
            if(params[1] != null){
                new_regId = params[1].get(0);
                old_regId = params[1].get(1);
            }
            this.phones = params[0];
            CollectionResponseRegistrationRecord result = myApiService.checkfriendsRegistration(new_regId, old_regId, Constants.Password, phones, Constants.MY_User_ID).execute();
            return result.getItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(List<RegistrationRecord> result) {
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                sqlHelper.update(Table_Users.Table_Name,new String[]{Table_Users.Friend_ID, Table_Users.Register},
                        new String[]{result.get(i).getMail(),Constants.Yes},new String[]{Table_Users.Phone},new String[]{result.get(i).getPhone()});
            }
        }
    }
}
