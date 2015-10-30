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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import server.EventFriend_AsyncTask_insert;
import server.SendMessage_AsyncTask;
import utils.Constans.Constants;
import utils.Helper;

/**
 * Created by pinhas on 19/09/2015.
 */
public class AddFriend extends AppCompatActivity {

    private TextView Name;
    private EditText input;
    private EditText input2;
    private Button add;
    private Spinner permission_spinner;
    private String Event_ID;
    private String Friend_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        Name = (TextView)findViewById(R.id.tv_addFriend);
        input = (EditText)findViewById(R.id.et_addFriend);
        input2 = (EditText)findViewById(R.id.et_addFriend2);
        add = (Button)findViewById(R.id.bt_addFriend);
        permission_spinner = (Spinner)findViewById(R.id.spin_af_permission_ui);

        Bundle b = getIntent().getExtras();
        final Context context = this;
        Friend_ID = "";
        final Intent friendList = new Intent(this, friendSelector.class);

        input.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(friendList,0);
            }

        });

        Event_ID = b.getString("Event_ID");
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Friend_ID = input2.getText().toString();
                boolean ok = saveData();
                if (ok) {
                    new SendMessage_AsyncTask(context).execute(Constants.MY_User_ID, Constants.New_Event + "|" + Event_ID, Friend_ID);
                    String message = Constants.New_Attending + "|" + Event_ID + "^" + Friend_ID;
                    Helper.Send_Message_To_Friend_By_Event_Except_One(context, Event_ID, Friend_ID, message);
                    Helper.User_Insert_MySQL(Friend_ID);
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
            Friend_ID = b.getString("Friend_ID");
            input.setText(b.getString("name"));
        }
    }

    private boolean saveData(){
        boolean ok = false;
        if(Friend_ID.length() > 0) {
           // ArrayList<String>[] list = sqlHelper.select(null, Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0]
                //    , Constants.Table_Events_Friends_Fields[1]}, new String[]{Event_ID, Friend_ID}, null);
            if(!Helper.isFriend_Exist_in_Event_MySql(Event_ID, Friend_ID)){
                String permission = Helper.getMyPermission(Event_ID);
                String Permission_Value = permission_spinner.getSelectedItem().toString();
                if(!(permission.equals(Constants.Editor) && Permission_Value.equals(Constants.Manager))) {
                    Helper.Event_Friend_Insert_MySQL(Event_ID, Friend_ID, Constants.UnCheck, Permission_Value);
                    new EventFriend_AsyncTask_insert(this).execute(Event_ID, Friend_ID, Constants.UnCheck, Permission_Value);
                    ok = true;
                }else{
                    Toast.makeText(this, "Editor can't give Manage permission", Toast.LENGTH_LONG).show();
                }
            }
        }
        return ok;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
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
