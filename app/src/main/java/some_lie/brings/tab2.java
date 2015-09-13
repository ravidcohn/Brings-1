package some_lie.brings;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class tab2 extends AppCompatActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    static int layouts[] = {R.layout.event_main,R.layout.event_attending,R.layout.event_todo,R.layout.event_chat};

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    //private static int ID;
    //private static String USERNAME;
    private static String KEY;
    private static TextView name;
    private static TextView place;
    private static TextView start;
    private static TextView end;
    private static TextView description;
    private static ArrayList<String> Tasks_keys;
    private static SQLiteDatabase db;
    final private static String path = "/data/data/some_lie.brings/databases/_edata";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2);
        Bundle b = getIntent().getExtras();
        //ID = b.getInt("ID");
        //USERNAME = b.getString("USERNAME");
        KEY = b.getString("KEY");
        Tasks_keys = new ArrayList<>();




        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_home:
                final Intent home =  new Intent(this,MainActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);

            //    return true;
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                return true;
            case R.id.action_edit:
                final Intent edit =  new Intent(this,edit_event.class);
                Bundle b = new Bundle();
                //b.putInt("ID", ID);
                //b.putString("USERNAME",USERNAME);
                b.putString("KEY", KEY);
                edit.putExtras(b);
                startActivityForResult(edit, 1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

                @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                SQLiteDatabase db = openOrCreateDatabase("_edata", MODE_PRIVATE, null);
                //Cursor c = db.rawQuery("select * from Events where ID = '" + USERNAME + " - " + ID + "';", null);
                Cursor c = db.rawQuery("select * from Events where ID = '" + KEY + "';", null);
                c.moveToFirst();
                name.setText(c.getString(1));
                place.setText(c.getString(2));
                start.setText(c.getString(3));
                end.setText(c.getString(4));
                description.setText(c.getString(5));
                c.close();
                db.close();
            }
    }

                /**
                 * A placeholder fragment containing a simple view.
                 */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(layouts[this.getArguments().getInt(ARG_SECTION_NUMBER) - 1], container, false);//R.layout.fragment_tab2
            db =  SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            //Cursor c = db.rawQuery("select * from Events where ID = '" + USERNAME + " - " + ID + "';", null);
            Cursor c = db.rawQuery("select * from Events where ID = '" + KEY + "';", null);
            c.moveToFirst();
            switch(this.getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:{
                    name = (TextView)rootView.findViewById(R.id.tv_em_name_ui);
                    place = (TextView)rootView.findViewById(R.id.tv_em_place_ui);
                    start = (TextView)rootView.findViewById(R.id.tv_em_start_ui);
                    end = (TextView)rootView.findViewById(R.id.tv_em_end_ui);
                    description = (TextView)rootView.findViewById(R.id.tv_em_description_ui);
                    name.setText(c.getString(1));
                    place.setText(c.getString(2));
                    start.setText(c.getString(3));
                    end.setText(c.getString(4));
                    description.setText(c.getString(5));
                    break;
                }
                case 2:{
                    break;
                }
                case 3:{
                    setList(rootView);
                    ImageButton bt_etd_add_task = (ImageButton) rootView.findViewById(R.id.bt_etd_add_task);
                    bt_etd_add_task.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent task = new Intent(getActivity().getApplicationContext(), newTask.class);
                            Bundle data = new Bundle();
                            //data.putInt("ID", ID);
                            //data.putString("USERNAME", USERNAME);
                            data.putString("KEY", KEY);
                            task.putExtras(data);
                            startActivity(task);

                        }
                    });
                    setList(rootView);
                    break;
                }
                case 4:{

                    break;
                }
            }
            c.close();
            db.close();
            return rootView;
        }

        private void setList(final View rootView) {
            Tasks_keys.clear();
            sql();

            final Context context = getActivity().getApplicationContext();
            final ListView listview = (ListView)rootView.findViewById(R.id.lv_etd);
            listview.setClickable(true);
            final Intent task =  new Intent(getActivity().getApplicationContext(),Task.class);

            StableArrayAdapter adapter = new StableArrayAdapter(getActivity().getApplicationContext());
            listview.setAdapter(adapter);

            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
                                               final int pos, final long id) {
                    // TODO Auto-generated method stub

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Delete Task?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db =  SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                                    String task_key = Tasks_keys.get(pos);
                                    db.execSQL("delete from Tasks where ID = '" + task_key + "';");
                                    setList(rootView);
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
                    //data.putInt("ID", IDS.get(position));
                    //data.putString("USERNAME", users_names.get(position));
                    data.putString("KEY", Tasks_keys.get(position));
                    task.putExtras(data);
                    startActivityForResult(task, 1);
                }
            });
        }
        private class StableArrayAdapter extends BaseAdapter implements View.OnClickListener {

            private Context context;

            public StableArrayAdapter(Context context) {
                this.context = context;
            }

            public View getView(int position, View convertView, ViewGroup viewGroup) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.event_tasks_list_item, null);

                TextView task_tit = (TextView) convertView.findViewById(R.id.tv_etd_list_item_task_tit);
                TextView task_friend = (TextView) convertView.findViewById(R.id.tv_etd_list_item_frind_tit);
                CheckBox task_do = (CheckBox) convertView.findViewById(R.id.cb_etd_list_item_task);

                db =  SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                Cursor c = db.rawQuery("select * from Tasks where ID = '" + Tasks_keys.get(position) + "';", null);
                c.moveToFirst();
                task_tit.setText(c.getString(1));
                task_friend.setText(c.getString(3));
                if (c.getString(3).equals("")) {
                    task_do.setChecked(false);
                }else{
                    task_do.setChecked(true);
                }
                c.close();
                db.close();

                return convertView;
            }

            public int getCount() {
                //return IDS.size();
                return 0;
            }

            @Override
            public Object getItem(int position) {
                //String s = users_names.get(position)+" - "+IDS.get(position);
                //return s;
                return "";
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public void onClick(View v) {

            }
        }

        private void sql() {
            final Context context = getActivity().getApplicationContext();
            db =  SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            //db.execSQL("DROP TABLE Events");
            db.execSQL("create table if not exists Tasks(ID varchar NOT NULL primary key,Task varchar NOT NULL,Description varchar, Name varchar)");
            Cursor c = db.rawQuery("select * from Tasks;", null);
            while (c.moveToNext()) {
                String task_key = c.getString(0);
                Tasks_keys.add(task_key);
            }
            c.close();
            db.close();
        }

    }

}
