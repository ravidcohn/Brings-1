package brings_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import server.SendMessage_AsyncTask;
import utils.Constants;
import utils.sqlHelper;

/**
 * Created by pinhas on 19/09/2015.
 */
public class AddFriend extends AppCompatActivity {

    private TextView Name;
    private EditText input;
    private EditText input2;
    private Button add;
    private String KEY;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        Name = (TextView)findViewById(R.id.tv_addFriend);
        input = (EditText)findViewById(R.id.et_addFriend);
        input2 = (EditText)findViewById(R.id.et_addFriend2);
        add = (Button)findViewById(R.id.bt_addFriend);
        Bundle b = getIntent().getExtras();
        final Context context = this;
        email = "";
        final Intent friendList = new Intent(this, FriendSelctor.class);

        input.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(friendList,0);
            }

        });

        KEY = b.getString("KEY");
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = input2.getText().toString();
                boolean ok = saveData();
                //new SendMessage_AsyncTask(context).execute("h", Constants.New_Event + "|" + KEY, "ravidcohn@gmail.com");
                if (ok) {
                    new SendMessage_AsyncTask(context).execute(Constants.User_Name, Constants.New_Event + "|" + KEY, email);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "only description can by empty..", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Bundle b = data.getExtras();
            email = b.getString("email");
            input.setText(b.getString("name"));
        }
    }

    private boolean saveData(){
        boolean ok = false;
        if(email.length() > 0) {
            ArrayList<String>[] list = sqlHelper.select(null,Constants.Table_Events_Friends,new String[]{"Event_ID","Friend_ID"},new String[]{KEY,email},null);
            if(sqlHelper.select(null,Constants.Table_Events_Friends,new String[]{"Event_ID","Friend_ID"},new String[]{KEY,email},null)[0].isEmpty()){
                sqlHelper.insert(Constants.Table_Events_Friends, new String[]{KEY, email,Constants.No});
                ok = true;
            }
        }
        return ok;
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

}
