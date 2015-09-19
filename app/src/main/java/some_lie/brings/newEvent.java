package some_lie.brings;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

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
    private SQLiteDatabase db;
    private int ID;
    private String imagePath = "";
    private String USERNAME = "user 1";//TODO


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);
        tv_ne_name_ui = (EditText)findViewById(R.id.tv_ne_name_ui);
        tv_ne_place_ui = (EditText)findViewById(R.id.tv_ne_place_ui);
        tv_ne_start_ui = (EditText)findViewById(R.id.tv_ne_start_ui);
        tv_ne_end_ui = (EditText)findViewById(R.id.tv_ne_end_ui);
        tv_ne_description_ui = (EditText)findViewById(R.id.tv_ne_description_ui);
        iv_ne_pic_ui = (ImageView)findViewById(R.id.iv_ne_pic_ui);
        bt_ne_pic_ui = (Button)findViewById(R.id.bt_ne_pic_ui);
        bt_ne_create_event_ui = (Button)findViewById(R.id.bt_ne_create_event_ui);

        //final Intent tabs = new Intent(this,tab.class);
        final Intent tabs2 = new Intent(this,tab.class);
        bt_ne_create_event_ui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean ok = saveData();
                if(ok) {
                    Bundle b = new Bundle();
                    b.putString("KEY", USERNAME+" - "+ID);
                 //   b.putString("USERNAME",USERNAME);
                    tabs2.putExtras(b);
                        // startActivity(tabs);
                        startActivity(tabs2);
                    finish();
                    }
                   else{
                        Toast.makeText(getApplicationContext(),"only description can by empty..",Toast.LENGTH_SHORT).show();
                    }
            }

        });
        setDatePicker();
        setOnClick();
    }

    private boolean saveData(){
        boolean ok = false;
        if(tv_ne_name_ui.getText().length() > 0 && tv_ne_place_ui.length() > 0 && tv_ne_start_ui.length() > 0 && tv_ne_end_ui.length() > 0) {
            ok = true;
            db = openOrCreateDatabase("_edata", MODE_PRIVATE, null);
            int id = 0;
            String key = USERNAME + " - "+id;
            ArrayList<String> allIDS = new ArrayList<>();
            Cursor c = db.rawQuery("select * from Events;", null);
            while (c.moveToNext()){
                String t_id = c.getString(0);
                allIDS.add(t_id);
            }
            while (allIDS.contains(key)){
                id++;
                key = USERNAME + " - "+id;
            }
            ID = id;
            String name = tv_ne_name_ui.getText().toString();
            String place = tv_ne_place_ui.getText().toString();
            String start = tv_ne_start_ui.getText().toString();
            String end = tv_ne_end_ui.getText().toString();
            String description = tv_ne_description_ui.getText().toString();

            db.execSQL("insert into Events values('" + USERNAME +" - " +id+"','" + name + "','" + place + "','" + start + "','" + end + "','" + description + "','"+imagePath+"');");
            c.close();
            db.close();
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


    private void setOnClick(){
        final Intent new_event = new Intent(this,newEvent.class);

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
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                imagePath = picturePath;
                Bitmap thumbnail =  bitmapHelper.decodeSampledBitmapFromFile(picturePath, 100, 100);//(BitmapFactory.decodeFile(picturePath));
                iv_ne_pic_ui.setImageBitmap(thumbnail);
            }
        }
    }



}
