package brings_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import utils.sqlHelper;

/**
 * Created by pinhas on 08/09/2015.
 */
public class Task extends AppCompatActivity {

    private TextView tv_et_task_info;
    private TextView tv_et_description_info;
    private String KEY;
    private int ID;
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
        KEY = b.getString("KEY");
        ID = b.getInt("taskID");

        ArrayList<String>[] db = sqlHelper.select(null,"Tasks",new String[]{"ID","TaskNumber"},new String[]{KEY,ID+""},new int[]{1});
        tv_et_task_info.setText(db[2].get(0));
        tv_et_description_info.setText(db[3].get(0));

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
