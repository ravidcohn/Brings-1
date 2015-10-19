/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sliding_tab;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import brings_app.AddFriend;
import brings_app.R;
import brings_app.Task;
import brings_app.newTask;
import server.EventFriend_AsyncTask_Update;
import server.EventFriend_AsyncTask_delete;
import server.SendMessage_AsyncTask;
import utils.Constants;
import utils.sqlHelper;

public class SlidingTabs extends Fragment {


    private int layouts[] = {R.layout.event_main, R.layout.event_attending, R.layout.event_todo, R.layout.event_chat};
    private String[] tabName= {"MAIN", "ATTENDING", "TODO", "CHAT"};



    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    public int getCurrentItem(){
        return mViewPager.getCurrentItem();
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)
        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        Bundle b = getArguments();
        int currentView = b.getInt("view_num");
        mViewPager.setCurrentItem(currentView);
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {

        private String KEY = "";
        ArrayList<Integer> Tasks_keys = new ArrayList<>();
        ArrayList<String> members_keys = new ArrayList<>();
        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return layouts.length;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabName[position];
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a add_friend from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new add_friend from our resources
            View view = getActivity().getLayoutInflater().inflate(layouts[position],
                    container, false);
            if(KEY.equals("")){
                Bundle b = getArguments();
                KEY = b.getString("KEY");
            }
            switch(position){
                case 0:{
                    setMainTab(view);
                    break;
                }
                case 1:{
                    setAttendingTab(view);
                    break;
                }
                case 2:{
                    setTodoTab(view);
                    break;
                }
                case 3:{
                    setChatTab(view);
                    break;
                }
            }

            // Add the newly created View to the ViewPager
            container.addView(view);

            return view;
        }
        private void setMainTab(View view){
            ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Events,new String[]{"ID"},new String[]{KEY},new int[]{1});
            TextView name = (TextView) view.findViewById(R.id.tv_em_name_ui);
            TextView place = (TextView) view.findViewById(R.id.tv_em_place_ui);
            TextView start = (TextView) view.findViewById(R.id.tv_em_start_ui);
            TextView end = (TextView) view.findViewById(R.id.tv_em_end_ui);
            TextView description = (TextView) view.findViewById(R.id.tv_em_description_ui);
            name.setText(dbResult[1].get(0));
            place.setText(dbResult[2].get(0));
            start.setText(dbResult[3].get(0));
            end.setText(dbResult[4].get(0));
            description.setText(dbResult[5].get(0));
            }
        private void setAttendingTab(final View view){
            ImageButton addFriend = (ImageButton) view.findViewById(R.id.ib_ea_add_friend);
            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent addFriend = new Intent(getActivity(), AddFriend.class);
                    Bundle data = new Bundle();
                    data.putString("KEY", KEY);
                    addFriend.putExtras(data);
                    getArguments().putInt("from", 1);
                    startActivityForResult(addFriend, 1);
                }
            });
            setAttendinglist(view);

        }

        private void setTodoTab(View view){

            setTodoList(view);
            ImageButton bt_etd_add_task = (ImageButton) view.findViewById(R.id.bt_etd_add_task);
            bt_etd_add_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent task = new Intent(getActivity().getApplicationContext(), newTask.class);
                    Bundle data = new Bundle();
                    data.putString("KEY", KEY);
                    task.putExtras(data);
                    getArguments().putInt("from", 2);
                    startActivityForResult(task, 2);
                }
            });

        }
        private void setChatTab(View view){

        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        private void setAttendinglist(final View view){
            members_keys.clear();
            sqlAttending();

            final Context context = getActivity();
            ListView listview = (ListView) view.findViewById(R.id.lvAttending);
            StableArrayAdapterAttending adapter = new StableArrayAdapterAttending(getActivity()  ,members_keys ,KEY);
            listview.setAdapter(adapter);

            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
                                               final int pos, final long id) {
                    // TODO Auto-generated method stub

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Delete " + members_keys.get(pos) + "?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String Friend_ID = members_keys.get(pos);
                                    sqlHelper.delete(Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0], Constants.Table_Events_Friends_Fields[1]}, new String[]{KEY, Friend_ID}, new int[]{1});
                                    new EventFriend_AsyncTask_delete(context).execute(KEY, Friend_ID);
                                    ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0]}, new String[]{KEY}, null);
                                    for(String to:dbResult[1]) {
                                        if(!to.equals(KEY)&&!to.equals(Constants.User_Name) && !to.equals(Friend_ID)) {
                                            new SendMessage_AsyncTask(context).execute(Constants.User_Name, Constants.Delete_Attending + "|" + KEY + "^" + Friend_ID, to);
                                        }
                                    }
                                    new SendMessage_AsyncTask(context).execute(Constants.User_Name, Constants.Delete_Event + "|" + KEY, Friend_ID);
                                    setAttendinglist(view);
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
        }
        private void setTodoList(final View rootView) {
            Tasks_keys.clear();
            sqlTodo();

            final Context context = getActivity();
            ListView listview = (ListView) rootView.findViewById(R.id.lv_etd);
            listview.setClickable(true);
            final Intent task = new Intent(getActivity().getApplicationContext(), Task.class);

            StableArrayAdapterTodo adapter = new StableArrayAdapterTodo(getActivity().getApplicationContext(),Tasks_keys,KEY);
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
                                    int task_key = Tasks_keys.get(pos);
                                    sqlHelper.delete(Constants.Table_Tasks, new String[]{"ID", "TaskNumber"}, new String[]{KEY, task_key + ""}, new int[]{1});
                                    setTodoList(rootView);
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
                    data.putInt("taskID", Tasks_keys.get(position));
                    data.putString("KEY", KEY);
                    task.putExtras(data);
                    startActivity(task);
                }
            });
        }

        private void sqlTodo() {
            final Context context = getActivity().getApplicationContext();
            ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Tasks, new String[]{Constants.Table_Tasks_Fields[0]}, new String[]{KEY}, null);
            for (String val: dbResult[1]){
                Tasks_keys.add(Integer.parseInt(val));
            }
        }


        private void sqlAttending() {
            final Context context = getActivity().getApplicationContext();
            ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Events_Friends,
                    new String[]{Constants.Table_Events_Friends_Fields[0]}, new String[]{KEY}, null);
            for (String val : dbResult[1]){
                members_keys.add(val);
            }
        }
    }
}

class StableArrayAdapterTodo extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private String path;
    private ArrayList<Integer> Tasks_keys;
    private String KEY;

    public StableArrayAdapterTodo(Context context, ArrayList<Integer> Tasks_keys, String KEY) {
        this.context = context;
        this.Tasks_keys = Tasks_keys;
        this.KEY = KEY;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.event_tasks_list_item, null);

        TextView task_tit = (TextView) convertView.findViewById(R.id.tv_etd_list_item_task_tit);
        TextView task_friend = (TextView) convertView.findViewById(R.id.tv_etd_list_item_frind_tit);
        CheckBox task_do = (CheckBox) convertView.findViewById(R.id.cb_etd_list_item_task);

        ArrayList<String>[] dbResult = sqlHelper.select(null,Constants.Table_Tasks,new String[]{"ID"},new String[]{KEY},null);
        task_tit.setText(dbResult[2].get(position));
        task_friend.setText(dbResult[3].get(position));
        if (dbResult[4].get(position).equals("")) {
            task_do.setChecked(false);
        } else {
            task_do.setChecked(true);
        }

        return convertView;
    }

    public int getCount() {
        //return IDS.size();
        return Tasks_keys.size();
    }

    @Override
    public Object getItem(int position) {
        //String s = users_names.get(position)+" - "+IDS.get(position);
        //return s;
        return Tasks_keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {

    }
}

class StableArrayAdapterAttending extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<String> members_keys;
    private String KEY;

    public StableArrayAdapterAttending(Context context, ArrayList<String> members_keys, String KEY) {
        this.context = context;
        this.members_keys = members_keys;
        this.KEY = KEY;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.event_attending_list_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.tv_ea_list_item);
        final RadioGroup radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ArrayList<String>[] dbResult = sqlHelper.select(null, Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[0]}, new String[]{KEY}, null);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_ea_list_yes: {
                        Update_Attending(dbResult, Constants.Yes, position);
                        break;
                    }
                    case R.id.rb_ea_list_maybe: {
                        Update_Attending(dbResult, Constants.Maybe, position);
                        break;
                    }
                    case R.id.rb_ea_list_no: {
                        Update_Attending(dbResult, Constants.No, position);
                        break;
                    }
                }
            }
        });
        ArrayList<String>[] dbResult = sqlHelper.select(null,Constants.Table_Events_Friends,new String[]{Constants.Table_Events_Friends_Fields[0]},new String[]{KEY},null);
        //name.setText(dbResult[1].get(position));
        name.setText(getName(dbResult[1].get(position)));
        switch (dbResult[2].get(position)) {
            case Constants.Yes: {
                radioGroup.check(R.id.rb_ea_list_yes);
                break;
            }
            case Constants.Maybe: {
                radioGroup.check(R.id.rb_ea_list_maybe);
                break;
            }
            case Constants.No: {
                radioGroup.check(R.id.rb_ea_list_no);
                break;
            }
            default:{
                break;
            }
        }
        if (!dbResult[1].get(position).equals(Constants.User_Name)) {
            for(int i=0;i<radioGroup.getChildCount();i++) {
                radioGroup.getChildAt(i).setEnabled(false);
                radioGroup.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        return convertView;
    }

    private String getName(String Friend_ID){
        ArrayList<String>[] dbFriends = sqlHelper.select(null,Constants.Table_Friends,new String[]{Constants.Table_Friends_Fields[2]},new String[]{Friend_ID},null);
        ArrayList<String>[] dbUsers = sqlHelper.select(null,Constants.Table_Users,new String[]{Constants.Table_Users_Fields[0]},new String[]{Friend_ID},null);
        if(!dbFriends[0].isEmpty()){
            return dbFriends[0].get(0);
        }else if(!dbUsers[0].isEmpty()){
            return dbUsers[1].get(0);
        }else if (Friend_ID.equals(Constants.User_Name)){
            return Constants.User_nickName;
        }else{
            return Friend_ID;
        }
    }

    private void Update_Attending(ArrayList<String>[] dbResult, String attend, int pos){
        if(!dbResult[2].get(pos).equals(attend)) {
            new EventFriend_AsyncTask_Update(context).execute(KEY, Constants.User_Name, attend);
            sqlHelper.update(Constants.Table_Events_Friends, new String[]{Constants.Table_Events_Friends_Fields[2]}, new String[]{attend},
                    new String[]{Constants.Table_Events_Friends_Fields[0], Constants.Table_Events_Friends_Fields[1]}, new String[]{KEY, Constants.User_Name});
            String message = Constants.Update_Attending + "|" + KEY + "^" + Constants.User_Name + "^" + attend;
            for (String to : dbResult[1]) {
                if (!to.equals(Constants.User_Name)) {
                    new SendMessage_AsyncTask(context).execute(Constants.User_Name, message, to);
                }
            }
        }
    }

    public int getCount() {
        //return IDS.size();
        return members_keys.size();
    }

    @Override
    public Object getItem(int position) {
        //String s = users_names.get(position)+" - "+IDS.get(position);
        //return s;
        return members_keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {

    }
}