package brings_app;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import utils.Constans.Table_Tasks;
import utils.sqlHelper;

/**
 * Created by pinhas on 08/09/2015.
 */
public class Task extends AppCompatActivity {

    private TextView tv_et_task_info;
    private TextView tv_et_description_info;
    private String Event_ID;
    private int Task_ID_Number;
    private SQLiteDatabase db;
  //  private String task;
  //  private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_task);
        tv_et_task_info = (TextView)findViewById(R.id.tv_et_task_info);
        tv_et_description_info = (TextView)findViewById(R.id.tv_et_description_info);
        Bundle b = getIntent().getExtras();
        Event_ID = b.getString("Event_ID");
        Task_ID_Number = b.getInt("Task_ID_Number");

        ArrayList<String>[] dbTasks = sqlHelper.select(null, Table_Tasks.Table_Name,new String[]{Table_Tasks.Event_ID,
                Table_Tasks.Task_ID_Number},new String[]{Event_ID, Task_ID_Number +""},new int[]{1});
        tv_et_task_info.setText(dbTasks[2].get(0));
        tv_et_description_info.setText(dbTasks[3].get(0));

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
