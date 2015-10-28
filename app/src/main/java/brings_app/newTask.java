package brings_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import server.Task_AsyncTask_insert;
import utils.Constans.Constants;
import utils.Constans.Table_Tasks;
import utils.Helper;
import utils.sqlHelper;

/**
 * Created by pinhas on 08/09/2015.
 */
public class newTask extends AppCompatActivity {

    private EditText et_nt_task_ui;
    private EditText et_nt_description_ui;
    private Button bt_nt_create_task_ui;
    private String Event_ID;
    private int Task_ID_Number;
    private  String Task_Name;
    private String Description;
    private String Friend_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        et_nt_task_ui = (EditText) findViewById(R.id.et_nt_task_ui);
        et_nt_description_ui = (EditText) findViewById(R.id.et_nt_description_ui);
        bt_nt_create_task_ui = (Button) findViewById(R.id.bt_nt_create_task_ui);
        Bundle b = getIntent().getExtras();
        final Context context = this;

        Event_ID = b.getString("Event_ID");
        bt_nt_create_task_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean ok = saveData();
                if (ok) {
                    String message = Constants.New_Task + "|" + Event_ID + "^" + Task_ID_Number;
                    Helper.Send_Message_To_All_My_Friend_By_Event(context, Event_ID, message);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "only description can by empty..", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private boolean saveData() {
        boolean ok = false;
        if (et_nt_task_ui.getText().length() > 0) {
            ok = true;
            Task_ID_Number = 0;
            ArrayList<Integer> allIDS = new ArrayList<>();
            ArrayList<String>[] dbResult = sqlHelper.select(null, Table_Tasks.Table_Name, new String[]{Table_Tasks.Event_ID}, new String[]{Event_ID}, null);
            for (String t_id : dbResult[1]) {
                allIDS.add(Integer.parseInt(t_id));
            }
            while (allIDS.contains(Task_ID_Number)) {
                Task_ID_Number++;
            }
            Task_Name = et_nt_task_ui.getText().toString();
            Description = et_nt_description_ui.getText().toString();
            sqlHelper.insert(Table_Tasks.Table_Name, new String[]{Event_ID, Task_ID_Number + "", Task_Name, Description, Constants.UnCheck});
            new Task_AsyncTask_insert(this).execute(Event_ID, Task_ID_Number + "", Task_Name, Description, Constants.UnCheck);
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
