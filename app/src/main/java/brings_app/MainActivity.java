package brings_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.some_lie.backend.brings.Brings;
import com.example.some_lie.backend.brings.model.Event;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;

import server.CloudEndpointBuilderHelper;
import server.EventFriend_AsyncTask_delete_by_event;
import server.Event_AsyncTask_delete;
import server.SendMessage_AsyncTask;
import server.Task_AsyncTask_deleteByEvent;
import utils.Constants;
import utils.Helper;
import utils.bitmapHelper;
import utils.sqlHelper;

public class MainActivity extends AppCompatActivity {

    /**
     * Time limit for the application to wait on a response from Play Services.
     */
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String MY_PREFS_NAME = "Brings";

    private ImageButton ibAdd;
    private TextView tvSearch;
    private SearchView search;
    private static ArrayList<String> users_names;
    private static ArrayList<Integer> IDS;
    private static final String PROPERTY_REG_ID = "registrationId";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private Brings BringsApi;
    private Event event;

    /**
     * Google Cloud Messaging API.
     */
    private GoogleCloudMessaging gcm;

    /**
     * The registration ID.
     */
    private String regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BringsApi = CloudEndpointBuilderHelper.getEndpoints();
        users_names = new ArrayList<>();
        IDS = new ArrayList<>();
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        ibAdd = (ImageButton) findViewById(R.id.ibAdd);
        search = (SearchView) findViewById(R.id.searchView);
        setOnClick();

        tvSearch.setText("Search  ");

        setList();

    }



    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     * @param applicationContext the Application context.
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(final Context applicationContext) {
        final SharedPreferences prefs = getGCMPreferences(getApplicationContext());
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
     //       Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs
                .getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(getApplicationContext());
        if (registeredVersion != currentVersion) {
       //     Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }


    /**
     * @param applicationContext the Application context.
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(final Context
                                                        applicationContext) {
        // This sample app persists the registration ID in shared preferences,
        // but how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Returns the application version.
     * @param context the Application context.
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(final Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private void sql() {


        ArrayList<String>[] sqlresult = sqlHelper.select(null, Constants.Table_Events, null, null, null);
        for (String str : sqlresult[0]){
            String[] s = str.split(" - ");
            users_names.add(s[0]);
            IDS.add(Integer.parseInt(s[1]));
        }

    }

    /**
     * Checks if Google Play Services are installed and if not it initializes
     * opening the dialog to allow user to install Google Play Services.
     * @return a boolean indicating if the Google Play Services are available.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void setOnClick() {

        final Intent new_event = new Intent(this, newEvent.class);

        ibAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new_event);
            }

        });

        tvSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        setList();
    }

    private void setList() {
        users_names.clear();
        IDS.clear();
        sql();

        final Context context = this;
        final ListView listview = (ListView) findViewById(R.id.lvMain);
        listview.setClickable(true);
        final Intent tabs =  new Intent(this,tab.class);

        StableArrayAdapter adapter = new StableArrayAdapter(this);
        listview.setAdapter(adapter);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
                                           final int pos, final long id) {
                // TODO Auto-generated method stub

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set dialog message
                alertDialogBuilder
                        .setMessage("Delete Event?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String key = users_names.get(pos) + " - " + IDS.get(pos);
                                ArrayList<String>[] attendingArray = sqlHelper.select(null, Constants.Table_Events_Friends, new String[]{"Event_ID"}, new String[]{key}, null);
                                sqlHelper.delete(Constants.Table_Events, new String[]{Constants.Table_Events_Fields[0]}, new String[]{key}, new int[]{1});
                                sqlHelper.delete(Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0]}, new String[]{key}, null);
                                sqlHelper.delete(Constants.Table_Tasks, new String[]{Constants.Table_Tasks_Fields[0]}, new String[]{key}, null);
                                new Event_AsyncTask_delete(context).execute(key);
                                new EventFriend_AsyncTask_delete_by_event(context).execute(key);
                                new Task_AsyncTask_deleteByEvent(context).execute(key);
                                for(String to:attendingArray[1]) {
                                    if(!to.equals(Constants.User_Name)) {
                                        new SendMessage_AsyncTask(context).execute(Constants.User_Name, Constants.Delete_Event + "|" + key, to);
                                    }
                                }
                                sqlHelper.Delete_Table(Constants.Table_Chat+ Helper.Clean_Event_ID(key));
                                setList();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             *  starts the Register class for specific course when clicked on in the list
             */
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                Bundle data = new Bundle();
                //data.putInt("ID", IDS.get(position));
                //data.putString("USERNAME", users_names.get(position));
                data.putString("KEY", users_names.get(position) + " - " + IDS.get(position));
                tabs.putExtras(data);
                startActivity(tabs);
            }
        });
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     * @param applicationContext application's context.
     * @param registrationId     registration ID
     */
    private void storeRegistrationId(final Context applicationContext,
                                     final String registrationId) {
        final SharedPreferences prefs = getGCMPreferences(applicationContext);
        int appVersion = getAppVersion(applicationContext);
     //   Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, registrationId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    private class StableArrayAdapter extends BaseAdapter implements View.OnClickListener {

        private Context context;

        public StableArrayAdapter(Context context) {
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_list_item, null);

            ImageView iv = (ImageView) convertView.findViewById(R.id.ivPic);
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Events, new String[]{Constants.Table_Events_Fields[0]}, new String[]{users_names.get(position) + " - " + IDS.get(position)}, new int[]{1});
            tvName.setText(dbResult[1].get(0));
            tvDate.setText(dbResult[3].get(0));
            iv.setImageBitmap(bitmapHelper.decodeSampledBitmapFromFile(dbResult[6].get(0), 100, 100));

            return convertView;
        }

        public int getCount() {
            return IDS.size();
        }

        @Override
        public Object getItem(int position) {
            String s = users_names.get(position)+" - "+IDS.get(position);
            return s;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onClick(View v) {

        }
    }

}
