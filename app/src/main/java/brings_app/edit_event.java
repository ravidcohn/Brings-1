package brings_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import server.Event_AsyncTask_update;
import utils.Helper;
import utils.bitmapHelper;

/**
 * Created by pinhas on 08/09/2015.
 */
public class edit_event extends AppCompatActivity {

    private EditText et_ee_name_ui;
    private EditText et_ee_place_ui;
    private EditText et_ee_start_ui;
    private EditText et_ee_end_ui;
    private EditText et_ee_description_ui;

    private ImageButton ib_ee_pic_ui;
    private Button bt_ee_save_ui;
    private Button bt_ee_cancel_ui;
    private String Event_ID;
    private String ImagePath = "";
    private String Update_Time;
    private String Location;
    //private String USERNAME = "user 1";//TODO


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);
        et_ee_name_ui = (EditText)findViewById(R.id.et_ee_name_ui);
        et_ee_place_ui = (EditText)findViewById(R.id.et_ee_place_ui);
        et_ee_start_ui = (EditText)findViewById(R.id.et_ee_start_ui);
        et_ee_end_ui = (EditText)findViewById(R.id.et_ee_end_ui);
        et_ee_description_ui = (EditText)findViewById(R.id.et_ee_description_ui);
        ib_ee_pic_ui = (ImageButton)findViewById(R.id.ib_ee_pic_ui);
        bt_ee_save_ui = (Button)findViewById(R.id.bt_ee_save_ui);
        bt_ee_cancel_ui = (Button)findViewById(R.id.bt_ee_cancel_ui);
        //store value
        Bundle b = getIntent().getExtras();
        //ID = b.getInt("ID");
        //USERNAME = b.getString("USERNAME");
        Event_ID = b.getString("Event_ID");
        store_value();

        final Intent maps = new Intent(this, google_map_location.class);
        et_ee_place_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(maps, 26);
            }
        });

        //final Intent tabs = new Intent(this,tab.class);
        final Intent tabs2 = new Intent(this,tab.class);
        bt_ee_save_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean ok = saveData();
                if(ok) {
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"only description can by empty..",Toast.LENGTH_SHORT).show();
                }
            }

        });

        bt_ee_cancel_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        setDatePicker();
        setOnClick();
    }

    private void store_value() {


        SQLiteDatabase db =  db = openOrCreateDatabase("_edata", MODE_PRIVATE, null);
        //Cursor c = db.rawQuery("select * from Events where ID = '" + USERNAME + " - " + ID + "';", null);
        Cursor c = db.rawQuery("select * from Events where ID = '" + Event_ID + "';", null);
        c.moveToFirst();
        ImagePath = c.getString(6);
        et_ee_name_ui.setText(c.getString(1));
        et_ee_place_ui.setText(c.getString(2));
        et_ee_start_ui.setText(c.getString(3));
        et_ee_end_ui.setText(c.getString(4));
        et_ee_description_ui.setText(c.getString(5));
        ib_ee_pic_ui.setImageBitmap(bitmapHelper.decodeSampledBitmapFromFile(c.getString(6), 100, 100));
        c.close();
        db.close();
    }

    private boolean saveData(){
        boolean ok = false;
        if(et_ee_name_ui.getText().length() > 0 && et_ee_place_ui.length() > 0 && et_ee_start_ui.length() > 0 && et_ee_end_ui.length() > 0) {
            ok = true;
            String Name = et_ee_name_ui.getText().toString();
            String Start_Date = et_ee_start_ui.getText().toString();
            String Start_Time = "";
            String End_Date = et_ee_end_ui.getText().toString();
            String End_Time = "";
            String Description = et_ee_description_ui.getText().toString();
            Date time = Calendar.getInstance().getTime();
            Update_Time = time.toString();
            Helper.Event_Update_MySQL(Event_ID, Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, ImagePath, Update_Time);
            new Event_AsyncTask_update(this).execute(Event_ID, Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, ImagePath, Update_Time);
        }
        return ok;
    }

    private void setDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart(myCalendar);
            }
        };

        final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd(myCalendar);
            }

        };

        et_ee_start_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(edit_event.this, dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        et_ee_end_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(edit_event.this, dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
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
    private void updateLabelStart(Calendar myCalendar) {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_ee_start_ui.setText(sdf.format(myCalendar.getTime()));
        et_ee_end_ui.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabelEnd(Calendar myCalendar) {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_ee_end_ui.setText(sdf.format(myCalendar.getTime()));
    }


    private void setOnClick(){
        final Intent new_event = new Intent(this,newEvent.class);

        ib_ee_pic_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                ImagePath = picturePath;
                Bitmap thumbnail =  bitmapHelper.decodeSampledBitmapFromFile(picturePath, 100, 100);
                ib_ee_pic_ui.setImageBitmap(thumbnail);
            }
            else if(requestCode == 26){
                Bundle b = data.getExtras();
                Location = b.getString("Location");
                String locationTag = "";
                for (int i = 0; i < Location.length();i++){
                    if(Location.charAt(i)=='!'){
                        locationTag = Location.substring(i+1);
                        i = Location.length();
                    }
                }
                et_ee_place_ui.setText(locationTag);

            }
            else{
                finish();
            }
        }
        else{
            finish();
        }

    }



}
