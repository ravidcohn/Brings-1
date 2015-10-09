package some_lie.brings;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import backend.Event_AsyncTask_insert;

/**
 * Created by Ravid on 08/10/2015.
 */
public class Registration extends AppCompatActivity {
    private EditText et_rg_mail_ui;
    private EditText et_rg_your_name_ui;
    private EditText et_rg_phone_ui;
    private EditText et_rg_password_ui;
    private EditText et_rg_confirm_password_ui;
    private Button bt_rg_register_ui;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        et_rg_mail_ui = (EditText)findViewById(R.id.et_rg_mail_ui);
        et_rg_your_name_ui = (EditText)findViewById(R.id.et_rg_your_name_ui);
        et_rg_phone_ui = (EditText)findViewById(R.id.et_rg_phone_ui);
        et_rg_password_ui = (EditText)findViewById(R.id.et_rg_password_ui);
        et_rg_confirm_password_ui = (EditText)findViewById(R.id.et_rg_confirm_password_ui);
        bt_rg_register_ui = (Button)findViewById(R.id.bt_rg_register_ui);

        bt_rg_register_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean ok = saveData();
                if (ok) {
                    finish();
                }
            }

        });
    }

    private boolean saveData(){
        boolean ok = false;
        String mail = et_rg_mail_ui.getText().toString();
        String name = et_rg_your_name_ui.getText().toString();
        String phone = et_rg_phone_ui.getText().toString();
        String password = et_rg_password_ui.getText().toString();
        String confirm_password = et_rg_confirm_password_ui.getText().toString();
        if(mail.length()>0 && name.length()>0 && phone.length()>0 && password.length()>0 && confirm_password.length()>0) {
            if(password.equals(confirm_password)) {
                new Registration_AsyncTask(this).execute(mail,name,phone,password,confirm_password);
                ok = true;
                db = openOrCreateDatabase("_edata", MODE_PRIVATE, null);
                int task_id = 0;
                ArrayList<Integer> allIDS = new ArrayList<>();
                Cursor c = db.rawQuery("select * from Tasks where ID = '" + KEY + "';", null);
                while (c.moveToNext()) {
                    int t_id = c.getInt(1);
                    allIDS.add(t_id);
                }
                while (allIDS.contains(task_id)) {
                    task_id++;
                }
                String task = et_nt_task_ui.getText().toString();
                String description = et_nt_description_ui.getText().toString();
                String name = "";

                db.execSQL("insert into Tasks values('" + KEY + "'," + task_id + ",'" + task + "','" + description + "','" + name + "');");
                c.close();
                db.close();
            }else {
                Toast.makeText(getApplicationContext(), "password are not much", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "All fields should be filed", Toast.LENGTH_SHORT).show();
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
