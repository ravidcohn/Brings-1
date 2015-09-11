package some_lie.brings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton ibAdd;
    private TextView tvSearch;
    private SearchView search;
    private SQLiteDatabase db;
    private static ArrayList<String> users_names;
    private static ArrayList<Integer> IDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        users_names = new ArrayList<>();
        IDS = new ArrayList<>();
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        ibAdd = (ImageButton) findViewById(R.id.ibAdd);
        search = (SearchView) findViewById(R.id.searchView);
        setOnClick();

        tvSearch.setText("Search  ");

        setList();

    }

    private void sql() {

        db = openOrCreateDatabase("_edata", MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE Events");
        db.execSQL("create table if not exists Events(ID varchar NOT NULL primary key,Name varchar NOT NULL,Place VARCHAR NOT NULL,Start DATE not null,End Date not null,Description varchar,imagePath varchar)");
        Cursor c = db.rawQuery("select * from Events;", null);
        while (c.moveToNext()) {
            String[] s = c.getString(0).split(" - ");
            users_names.add(s[0]);
            IDS.add(Integer.parseInt(s[1]));
        }
        c.close();
        db.close();

    }

    private void setOnClick() {

        final Intent new_event = new Intent(this, newEvent.class);

        ibAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new_event);
            }

        });

        tvSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                search.setIconified(false);
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
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        setList();
    }

    private void setList() {
        users_names.clear();
        IDS.clear();
        sql();

        final Context context = this;
        final ListView listview = (ListView) findViewById(R.id.lvMain);
        listview.setClickable(true);
        final Intent tabs =  new Intent(this,tab2.class);

        StableArrayAdapter adapter = new StableArrayAdapter(this);
        listview.setAdapter(adapter);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
                                           final int pos, final long id) {
                // TODO Auto-generated method stub

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set dialog message
                alertDialogBuilder
                        .setMessage("Delete Event?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db = openOrCreateDatabase("_edata", MODE_PRIVATE, null);
                                String key = users_names.get(pos) + " - " + IDS.get(pos);
                                db.execSQL("delete from Events where ID = '" + key + "';");
                                setList();
                                db.close();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             *  starts the Register class for specific course when clicked on in the list
             */
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                Bundle data = new Bundle();
                data.putInt("ID", IDS.get(position));
                data.putString("USERNAME", users_names.get(position));
                tabs.putExtras(data);
                startActivityForResult(tabs, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        }
        else{
            finish();
        }
    }

    private class StableArrayAdapter extends BaseAdapter implements View.OnClickListener {

        private Context context;

        public StableArrayAdapter(Context context) {
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_list_item, null);

            ImageView iv = (ImageView) convertView.findViewById(R.id.ivPic);
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

            db = openOrCreateDatabase("_edata", MODE_PRIVATE, null);
            Cursor c = db.rawQuery("select * from Events where ID = '" + users_names.get(position) + " - " + IDS.get(position) + "';", null);
            c.moveToFirst();
            tvName.setText(c.getString(1));
            tvDate.setText(c.getString(3));
            iv.setImageBitmap(bitmapHelper.decodeSampledBitmapFromFile(c.getString(6), 100, 100));
            c.close();
            db.close();

            return convertView;
        }

        public int getCount() {
            return IDS.size();
        }

        @Override
        public Object getItem(int position) {
            String s = users_names.get(position)+" - "+IDS.get(position);
            return s;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
