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

import server.SendMessage_AsyncTask;
import server.Task_AsyncTask_insert;
import utils.Constants;
import utils.sqlHelper;

/**
 * Created by pinhas on 08/09/2015.
 */
public class newTask extends AppCompatActivity {

    private EditText et_nt_task_ui;
    private EditText et_nt_description_ui;
    private Button bt_nt_create_task_ui;
    private String imagePath = "";
    private String USERNAME = "user 1";//TODO
    private String KEY;
    private int task_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        et_nt_task_ui = (EditText) findViewById(R.id.et_nt_task_ui);
        et_nt_description_ui = (EditText) findViewById(R.id.et_nt_description_ui);
        bt_nt_create_task_ui = (Button) findViewById(R.id.bt_nt_create_task_ui);
        Bundle b = getIntent().getExtras();
        final Context context = this;

        //ID = b.getInt("ID");
        //USERNAME = b.getString("USERNAME");
        KEY = b.getString("KEY");
        bt_nt_create_task_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean ok = saveData();
                if (ok) {
                    ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0]}, new String[]{KEY}, null);
                    for(String to:dbResult[1]) {
                        if(!to.equals(Constants.User_Name)) {
                            new SendMessage_AsyncTask(context).execute(Constants.User_Name, Constants.New_Task + "|" + KEY + "^" + task_id, to);
                        }
                    }
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
            task_id = 0;
            ArrayList<Integer> allIDS = new ArrayList<>();
            ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Tasks, new String[]{Constants.Table_Tasks_Fields[0]}, new String[]{KEY}, null);
            for (String t_id : dbResult[1]) {
                allIDS.add(Integer.parseInt(t_id));
            }
            while (allIDS.contains(task_id)) {
                task_id++;
            }
            String task_name = et_nt_task_ui.getText().toString();
            String description = et_nt_description_ui.getText().toString();
            sqlHelper.insert(Constants.Table_Tasks, new String[]{KEY, task_id + "", task_name, description, Constants.UnCheck});
            new Task_AsyncTask_insert(this).execute(KEY, task_id + "", task_name, description, Constants.UnCheck);

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
