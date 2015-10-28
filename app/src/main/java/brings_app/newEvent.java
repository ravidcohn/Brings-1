package brings_app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import utils.Constans.Constants;
import utils.Constans.Table_Events;
import utils.Helper;
import utils.bitmapHelper;
import utils.sqlHelper;

/**
 * Created by pinhas on 08/09/2015.
 */
public class newEvent extends AppCompatActivity {

    private EditText tv_ne_name_ui;
    private EditText tv_ne_place_ui;
    private EditText tv_ne_start_ui;
    private EditText tv_ne_end_ui;
    private EditText tv_ne_description_ui;
    private ImageView iv_ne_pic_ui;
    private Button bt_ne_pic_ui;
    private Button bt_ne_create_event_ui;
    private String Event_ID;
    private String Name;
    private String Location;
    private String Start_Date;
    private String Start_Time = "";
    private String End_Date;
    private String End_Time = "";
    private String Description;
    private String ImagePath = "";
    private String Update_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);
        tv_ne_name_ui = (EditText) findViewById(R.id.tv_ne_name_ui);
        tv_ne_place_ui = (EditText) findViewById(R.id.tv_ne_place_ui);
        tv_ne_start_ui = (EditText) findViewById(R.id.tv_ne_start_ui);
        tv_ne_end_ui = (EditText) findViewById(R.id.tv_ne_end_ui);
        tv_ne_description_ui = (EditText) findViewById(R.id.tv_ne_description_ui);
        iv_ne_pic_ui = (ImageView) findViewById(R.id.iv_ne_pic_ui);
        bt_ne_pic_ui = (Button) findViewById(R.id.bt_ne_pic_ui);
        bt_ne_create_event_ui = (Button) findViewById(R.id.bt_ne_create_event_ui);

        // String url = "waze://?q=";
        // final Intent waze = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        //           startActivity( waze );

        final Intent maps = new Intent(this, google_map_location.class);
        tv_ne_place_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(maps, 26);
            }
        });


        //final Intent tabs = new Intent(this,tab.class);
        final Intent tabs2 = new Intent(this, tab.class);
        final Context context = this;
        bt_ne_create_event_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Name = tv_ne_name_ui.getText().toString();
                Location = tv_ne_place_ui.getText().toString();
                Start_Date = tv_ne_start_ui.getText().toString();
                Start_Time = "";
                End_Date = tv_ne_end_ui.getText().toString();
                End_Time = "";
                Description = tv_ne_description_ui.getText().toString();
                boolean ok = saveData();
                if (ok) {
                    Helper.Create_Event_ServerSQL(context, Event_ID, Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, ImagePath, Update_Time);
                    Bundle b = new Bundle();
                    b.putString(Table_Events.Event_ID, Event_ID);
                    //   b.putString("USERNAME",USERNAME);
                    tabs2.putExtras(b);
                    // startActivity(tabs);
                    startActivity(tabs2);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "only description can by empty..", Toast.LENGTH_SHORT).show();
                }
            }

        });
        setDatePicker();
        setOnClick();
    }

    private boolean saveData() {
        boolean ok = false;
        if (tv_ne_name_ui.getText().length() > 0 && tv_ne_place_ui.length() > 0 && tv_ne_start_ui.length() > 0 && tv_ne_end_ui.length() > 0) {
            ok = true;
            int id = 0;
            Event_ID = Constants.User_Name + " - " + id;
            ArrayList<String> allIDS = new ArrayList<>();
            ArrayList<String>[] dbResult = sqlHelper.select(null, Table_Events.Table_Name, null, null, null);
            for (String t_id : dbResult[0]) {
                allIDS.add(t_id);
            }
            while (allIDS.contains(Event_ID)) {
                id++;
                Event_ID = Constants.User_Name + " - " + id;
            }
            Date time = Calendar.getInstance().getTime();
            Update_Time = time.toString();
            Helper.Create_Event_MySQL(Event_ID, Name, Location, Start_Date, Start_Time, End_Date, End_Time, Description, ImagePath, Update_Time);
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

        tv_ne_start_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(newEvent.this, dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        tv_ne_end_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(newEvent.this, dateEnd, myCalendar
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

        tv_ne_start_ui.setText(sdf.format(myCalendar.getTime()));
        tv_ne_end_ui.setText(sdf.format(myCalendar.getTime()));
        tv_ne_description_ui.requestFocus();
    }

    private void updateLabelEnd(Calendar myCalendar) {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tv_ne_end_ui.setText(sdf.format(myCalendar.getTime()));
        tv_ne_description_ui.requestFocus();
    }


    private void setOnClick() {
        final Intent new_event = new Intent(this, newEvent.class);

        bt_ne_pic_ui.setOnClickListener(new View.OnClickListener() {

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
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                ImagePath = picturePath;
                Bitmap thumbnail = bitmapHelper.decodeSampledBitmapFromFile(picturePath, 100, 100);//(BitmapFactory.decodeFile(picturePath));
                iv_ne_pic_ui.setImageBitmap(thumbnail);
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
                tv_ne_place_ui.setText(locationTag);

            }
        }
    }


}
