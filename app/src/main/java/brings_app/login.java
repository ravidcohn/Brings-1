package brings_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import server.LoginAsyncTask;
import server.ServerAsyncResponse;
import server.CheckFriendsRegistrationAsyncTask;
import utils.Constans.Constants;
import utils.Constans.Table_Users;
import utils.sqlHelper;

/**
 * Created by pinhas on 11/10/2015.
 */
public class login extends AppCompatActivity implements ServerAsyncResponse {
    private TextView tvName;
    private TextView tvPass;
    private EditText etName;
    private EditText etPass;
    private Button login;
    private Button register;
    private GoogleCloudMessaging gcm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sqlHelper.setContext(this);
        sqlHelper.createALLTables();
        login();
        tvName = (TextView) findViewById(R.id.tv_login_name);
        tvPass = (TextView) findViewById(R.id.tv_login_password);
        etName = (EditText) findViewById(R.id.et_login_name);
        etPass = (EditText) findViewById(R.id.et_login_password);
        login = (Button) findViewById(R.id.b_login_login);
        register = (Button) findViewById(R.id.b_login_register);
    }

    public void signIn(View view) {
        new LoginAsyncTask(this, this).execute(etName.getText().toString(), etPass.getText().toString());
    }

    public void register(View view) {
        Intent Register = new Intent(this, Registration.class);
        startActivity(Register);
        finish();
    }


    private void login() {

        SharedPreferences prefs = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("USER", null);
        if (restoredText != null) {
            Constants.User_nickName = prefs.getString("nickName", "No name defined");
            Constants.MY_User_ID = prefs.getString("Name", "No name defined");//"No name defined" is the default value.
            Constants.Password = prefs.getString("Pass", "No name defined");

            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(this);
            }
            String regId = null;
            try {
                regId = gcm.register(Constants.SENDER_ID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String gcmUpdate = null;
            if (regId != null) {
                gcmUpdate = prefs.getString("GCM", "NO Value");
                if (gcmUpdate != regId) {
                    SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("GCM", regId);
                    editor.apply();
                } else {
                    gcmUpdate = null;
                }

            }
            try {
                getFriends(regId, gcmUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO get updates from server
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }

    private void getFriends(String gcmUpdate, String oldGCM) {

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
        ArrayList<String>[] list;
        HashMap<String, String> data = new HashMap<>();
        ArrayList<String> ph = new ArrayList<>();
        while (phones.moveToNext()) {
            String Nickname = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("-", "").replaceAll(" ", "");
            if (data.get(Nickname) == null) {
                data.put(Nickname, phone);
                list = sqlHelper.select(null, Table_Users.Table_Name, new String[]{Table_Users.Phone}, new String[]{phone}, new int[]{1});
                if (list[0].isEmpty()) {
                    sqlHelper.insert(Table_Users.Table_Name, new String[]{"", phone, Nickname, Constants.No});
                }
                ph.add(phone);
            }
            //  String email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            //   String name2 = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME));
            //   if(name2 != null)
            //    Toast.makeText(this,name2+": "+email,Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(this,count+"",Toast.LENGTH_LONG).show();
        phones.close();
        ArrayList<String> gu = null;
        if (gcmUpdate != null) {
            gu = new ArrayList<>();
            gu.add(gcmUpdate);
            gu.add(oldGCM);
        }
        int size = ph.size() / 100 + 1;
        ArrayList<String>[] arrPh = new ArrayList[size];
        int count = 0;
        for (int i = 0; i < size && count < ph.size(); i++) {
            arrPh[i] = new ArrayList<>();
            for (int j = 0; j < 100 && count < ph.size(); j++, count++) {
                arrPh[i].add(ph.get(count));
            }
            new CheckFriendsRegistrationAsyncTask(this).execute(arrPh[i], gu);
        }

    }

    @Override
    public void processFinish(String... output) {
        if (output[0].contains("@")) {
            String mail = output[0];
            String password = etPass.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("USER", "R-USER");
            editor.putString("Name", mail);
            editor.putString("Pass", password);
            editor.putString("nickName", output[1]);
            editor.commit();
            login();
        } else {
            Toast.makeText(this, "User is not exist.. please register!", Toast.LENGTH_LONG).show();
        }
    }

}
