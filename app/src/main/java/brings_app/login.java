package brings_app;

import android.content.ContentResolver;
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

import com.example.some_lie.backend.brings.model.Event;

import java.util.ArrayList;

import server.LoginAsyncTask;
import server.ServerAsyncResponse;
import utils.Constants;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sqlHelper.setContext(this);
        sqlHelper.createALLTabels();
        login();
        tvName = (TextView) findViewById(R.id.tv_login_name);
        tvPass = (TextView) findViewById(R.id.tv_login_password);
        etName = (EditText) findViewById(R.id.et_login_name);
        etPass = (EditText) findViewById(R.id.et_login_password);
        login = (Button) findViewById(R.id.b_login_login);
        register = (Button) findViewById(R.id.b_login_register);
    }

    public void signIn(View view) {
        new LoginAsyncTask(this,this).execute(etName.getText().toString(), etPass.getText().toString());
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
            Constants.User_Name = prefs.getString("Name", "No name defined");//"No name defined" is the default value.
            Constants.Password = prefs.getString("Pass", "No name defined");
            getFriends();
            //TODO get updates from server
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }

    private void getFriends(){

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
        int count = 0;
        while (emails.moveToNext() )
        {
          //  String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        //    String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            String name2 = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME));
            if(name2 != null)
            Toast.makeText(this,name2+": "+email,Toast.LENGTH_LONG).show();

        }
        //Toast.makeText(this,count+"",Toast.LENGTH_LONG).show();


        phones.close();
    }

    @Override
    public void processFinish(String output) {
        String mail = output;
        String password = etPass.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("USER", "R-USER");
        editor.putString("Name", mail);
        editor.putString("Pass", password);
        editor.commit();
        login();
    }

    @Override
    public void EventProcessFinish(Event output) {
    }
}
