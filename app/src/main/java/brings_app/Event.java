package brings_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import server.Messageing.GcmIntentService;
import server.ServerAsyncResponse;
import utils.Constans.Constants;
import utils.Constans.Table_Chat;
import utils.Constans.Table_Events;
import utils.Event_Helper_Package.Contacts_List;
import utils.Event_Helper_Package.Event_Helper;
import utils.Event_Helper_Package.Task_Helper;
import utils.Event_Helper_Package.Vote_Date_Helper;
import utils.Helper;
import utils.sqlHelper;

public class Event extends AppCompatActivity implements ServerAsyncResponse {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static String my_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        GcmIntentService.delegate = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle b = getIntent().getExtras();
        Event_Helper.load_event(b.getString("Event_ID"));
        TextView event_name = (TextView) findViewById(R.id.event_name);
        my_permission = Helper.getMyPermission(Event_Helper.details[Table_Events.Event_ID_num]);
        if (!Event_Helper.details[Table_Events.Name_num].equals("")) {
            event_name.setText(Event_Helper.details[Table_Events.Name_num]);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ImageButton edit = (ImageButton) findViewById(R.id.edit);
        if (!my_permission.equals(Constants.Owner)) {
            edit.setVisibility(View.GONE);
        } else {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), New_Event.class);
                    Bundle data = new Bundle();
                    data.putString("Event_ID", Event_Helper.details[Table_Events.Event_ID_num]);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //Bundle b = getIntent().getExtras();
        //Event_Helper.load_event(b.getString("Event_ID"));
        int CurrentItem = mViewPager.getCurrentItem();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(CurrentItem);
        TextView event_name = (TextView) findViewById(R.id.event_name);
        if (!Event_Helper.details[Table_Events.Name_num].equals("")) {
            event_name.setText(Event_Helper.details[Table_Events.Name_num]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
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
    public void processFinish(String... output) {
        if (output[0].equals(Constants.Update_Activity)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Event_Helper.load_event(Event_Helper.details[Table_Events.Event_ID_num]);
                    int CurrentItem = mViewPager.getCurrentItem();
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    mViewPager.setCurrentItem(CurrentItem);
                    TextView event_name = (TextView) findViewById(R.id.event_name);
                    if (!Event_Helper.details[Table_Events.Name_num].equals("")) {
                        event_name.setText(Event_Helper.details[Table_Events.Name_num]);
                    }
                }
            });

        }
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Details";
                //case 1:
                //return "Attending";
                case 1:
                    return "Tasks";
                case 2:
                    return "Chat";
            }
            return null;
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1: {
                    View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
                    CardView CardView_description = (CardView) rootView.findViewById(R.id.CardView_description);
                    TextView description = (TextView) rootView.findViewById(R.id.description);
                    final TextView date = (TextView) rootView.findViewById(R.id.date);
                    final TextView location = (TextView) rootView.findViewById(R.id.location);
                    final Switch switcher_time = (Switch) rootView.findViewById(R.id.switcher_time);
                    Switch switcher_location = (Switch) rootView.findViewById(R.id.switcher_location);
                    final RecyclerView recyclerView_date = (RecyclerView) rootView.findViewById(R.id.recyclerView_date);
                    final RelativeLayout relativeLayout_date = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_date);
                    final RelativeLayout relativeLayout_titles = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_date_titles);
                    RelativeLayout relativeLayout_date_vote = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_date_vote);
                    //Set description.
                    if (Event_Helper.details[Table_Events.Description_num].equals("")) {
                        CardView_description.setVisibility(View.GONE);
                    } else {
                        description.setText(Event_Helper.details[Table_Events.Description_num]);
                    }
                    //Set date.
                    if (Event_Helper.details[Table_Events.Vote_Time_num].equals(Constants.Yes))
                        switcher_time.setChecked(true);
                    setSwitcher_time_view(switcher_time, recyclerView_date, relativeLayout_titles, relativeLayout_date, date);
                    if (my_permission.equals(Constants.Owner)) {
                        switcher_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                setSwitcher_time_view(switcher_time, recyclerView_date, relativeLayout_titles, relativeLayout_date, date);
                            }
                        });
                    } else {
                        relativeLayout_date_vote.setVisibility(View.GONE);
                    }
                    //Set location.
                    if (Event_Helper.details[Table_Events.Vote_Location_num].equals(Constants.No)) {
                        if (Event_Helper.details[Table_Events.Location_num].equals("")) {
                            location.setText(R.string.location_not_set);
                        } else {
                            location.setText(Event_Helper.details[Table_Events.Location_num]);
                        }
                    } else {
                        location.setText(R.string.press_for_vote);
                        switcher_location.setChecked(true);
                    }
                    if (my_permission.equals(Constants.Owner)) {
                        switcher_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    Helper.update_Event_details_field(getContext(), Event_Helper.details[Table_Events.Event_ID_num], Table_Events.Vote_Location, Constants.Yes);
                                    location.setText(R.string.press_for_vote);

                                } else {
                                    Helper.update_Event_details_field(getContext(), Event_Helper.details[Table_Events.Event_ID_num], Table_Events.Vote_Location, Constants.No);
                                    location.setText(Event_Helper.details[Table_Events.Location_num]);

                                }
                            }
                        });
                    } else {
                        switcher_location.setVisibility(View.GONE);
                    }

                    final RecyclerView recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                    recyclerview.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                    final ExpandableListAdapter_Event_Friends expandableListAdapter_friends = new ExpandableListAdapter_Event_Friends(null);
                    create_attending_list(expandableListAdapter_friends, recyclerview);
                    recyclerview.setAdapter(expandableListAdapter_friends);
                    recyclerview.setNestedScrollingEnabled(false);
                    RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
                    radioGroup.clearCheck();
                    switch (Event_Helper.friends.get(Constants.MY_User_ID).getAttending()) {
                        case Constants.Yes: {
                            radioGroup.check(R.id.radioGroup_yes);
                            break;
                        }
                        case Constants.Maybe: {
                            radioGroup.check(R.id.radioGroup_maybe);
                            break;
                        }
                        case Constants.No: {
                            radioGroup.check(R.id.radioGroup_no);
                            break;
                        }
                    }
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.radioGroup_yes: {
                                    Event_Helper.friends.get(Constants.MY_User_ID).setAttending(Constants.Yes);
                                    Helper.update_attending(getContext(), Event_Helper.details[Table_Events.Event_ID_num], Constants.MY_User_ID, Constants.Yes);
                                    create_attending_list(expandableListAdapter_friends, recyclerview);
                                    break;
                                }
                                case R.id.radioGroup_maybe: {
                                    Event_Helper.friends.get(Constants.MY_User_ID).setAttending(Constants.Maybe);
                                    Helper.update_attending(getContext(), Event_Helper.details[Table_Events.Event_ID_num], Constants.MY_User_ID, Constants.Maybe);
                                    create_attending_list(expandableListAdapter_friends, recyclerview);
                                    break;
                                }
                                case R.id.radioGroup_no: {
                                    Event_Helper.friends.get(Constants.MY_User_ID).setAttending(Constants.No);
                                    Helper.update_attending(getContext(), Event_Helper.details[Table_Events.Event_ID_num], Constants.MY_User_ID, Constants.No);
                                    create_attending_list(expandableListAdapter_friends, recyclerview);
                                    break;
                                }
                            }
                            expandableListAdapter_friends.notifyDataSetChanged();
                        }
                    });

                    return rootView;
                }
                case 2: {
                    View rootView = inflater.inflate(R.layout.fragment_event_tasks, container, false);

                    RecyclerView recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                    recyclerview.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                    List<ExpandableListAdapter_Event_Tasks.Item> data = new ArrayList<>();

                    ExpandableListAdapter_Event_Tasks.Item task;
                    for (int task_id : Event_Helper.task.keySet()) {
                        Task_Helper task_helper = Event_Helper.task.get(task_id);
                        if (task_helper.getType().equals(Constants.Personal_Task)) {
                            task = new ExpandableListAdapter_Event_Tasks.Item(ExpandableListAdapter_Event_Tasks.Personal_Task, task_id, 0, task_helper.getDescription());
                        } else {
                            task = new ExpandableListAdapter_Event_Tasks.Item(ExpandableListAdapter_Event_Tasks.Group_Task, task_id, 0, task_helper.getDescription());
                        }
                        for (int subTask_id : task_helper.getSubTasks().keySet()) {
                            if (task.invisibleChildren == null) {
                                task.invisibleChildren = new ArrayList<>();
                            }
                            task.invisibleChildren.add(new ExpandableListAdapter_Event_Tasks.Item(ExpandableListAdapter_Event_Tasks.SubTask,
                                    task_id, subTask_id, task_helper.getSubTasks().get(subTask_id)[0]));

                        }
                        data.add(task);
                    }

                    recyclerview.setAdapter(new ExpandableListAdapter_Event_Tasks(data, recyclerview));
                    return rootView;
                }
                case 3: {
                    View rootView = inflater.inflate(R.layout.fragment_event_chat, container, false);

                    final RecyclerView recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                    recyclerview.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

                    final String Chat_ID = Table_Chat.Table_Name + Helper.Clean_Event_ID(Event_Helper.details[Table_Events.Event_ID_num]);
                    final ArrayList<String>[] dbChat = sqlHelper.select(null, Chat_ID, null, null, null);

                    final List<ExpandableListAdapter_Event_Chat.Item> data = new ArrayList<>();
                    for (int i = 0; i < dbChat[0].size(); i++) {
                        if (dbChat[Table_Chat.User_ID_num].get(i).equals(Constants.MY_User_ID)) {
                            data.add(new ExpandableListAdapter_Event_Chat.Item(ExpandableListAdapter_Event_Chat.Chat_My));
                        } else {
                            data.add(new ExpandableListAdapter_Event_Chat.Item(ExpandableListAdapter_Event_Chat.Chat_Friend));
                        }

                    }
                    final ExpandableListAdapter_Event_Chat expandableListAdapter_event_chat = new ExpandableListAdapter_Event_Chat(data, dbChat, Chat_ID);
                    recyclerview.setAdapter(expandableListAdapter_event_chat);
                    recyclerview.scrollToPosition(data.size() - 1);
                    ImageButton send = (ImageButton) rootView.findViewById(R.id.send);
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText editText = (EditText) getActivity().findViewById(R.id.editText);
                            //Get all my Chat message.
                            ArrayList<String>[] MyDbChat = sqlHelper.select(null, Chat_ID, new String[]{Table_Chat.User_ID}, new String[]{Constants.MY_User_ID}, null);
                            //Generate message id.
                            int max_message_id = 0;
                            for (String tmp_message_id : MyDbChat[Table_Chat.Message_ID_num]) {
                                max_message_id = Math.max(max_message_id, Integer.parseInt(tmp_message_id));
                            }
                            max_message_id++;
                            String message_id = max_message_id + "";
                            //Set all chat values.
                            String date = Helper.getCurrentDate();
                            String time = Helper.getCurrentTime();
                            String message = editText.getText().toString();
                            editText.setText("");
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            String chat[] = new String[]{message_id, Constants.MY_User_ID, message, date, time};
                            sqlHelper.insert(Chat_ID, chat);
                            //Update the dcChat and the adapter.
                            dbChat[Table_Chat.Message_ID_num].add(message_id);
                            dbChat[Table_Chat.User_ID_num].add(Constants.MY_User_ID);
                            dbChat[Table_Chat.Message_num].add(message);
                            dbChat[Table_Chat.Date_num].add(date);
                            dbChat[Table_Chat.Time_num].add(time);
                            expandableListAdapter_event_chat.setDbChat(dbChat);
                            data.add(new ExpandableListAdapter_Event_Chat.Item(ExpandableListAdapter_Event_Chat.Chat_My));
                            expandableListAdapter_event_chat.notifyItemInserted(data.size() - 1);
                            recyclerview.scrollToPosition(data.size() - 1);
                            //Send to all users.
                            Helper.Send_Chat_Message_ServerSQL(getContext(), Chat_ID, chat);
                        }
                    });
                    return rootView;
                }
            }

            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return null;
        }

        private void setSwitcher_time_view(Switch switcher_time, RecyclerView recyclerView_date, RelativeLayout relativeLayout_titles, RelativeLayout relativeLayout_date, TextView date) {
            if (switcher_time.isChecked()) {
                Helper.update_Event_details_field(getContext(), Event_Helper.details[Table_Events.Event_ID_num], Table_Events.Vote_Time, Constants.Yes);
                relativeLayout_date.setVisibility(View.GONE);
                recyclerView_date.setVisibility(View.VISIBLE);
                relativeLayout_titles.setVisibility(View.VISIBLE);
                recyclerView_date.setLayoutManager(new LinearLayoutManager(getContext()));
                List<ExpandableListAdapter_Event_Vote_Date.Item> data = new ArrayList<>();
                ExpandableListAdapter_Event_Vote_Date.Item vote;
                for (int vote_id : Event_Helper.vote_date.keySet()) {
                    vote = new ExpandableListAdapter_Event_Vote_Date.Item(ExpandableListAdapter_Event_Vote_Date.Vote_Date_Parent, vote_id, Constants.UnCheck);
                    Vote_Date_Helper vote_date_helper = Event_Helper.vote_date.get(vote_id);
                    for (String User_ID : vote_date_helper.getVotes().keySet()) {
                        if (vote.invisibleChildren == null)
                            vote.invisibleChildren = new ArrayList<>();
                        vote.invisibleChildren.add(new ExpandableListAdapter_Event_Vote_Date.Item(ExpandableListAdapter_Event_Vote_Date.Vote_Date_Child, vote_id,
                                vote_date_helper.getVotes().get(User_ID)));
                    }
                    data.add(vote);
                }
                int recyclerView_height_dp = (data.size() * 55);//40.
                recyclerView_date.setAdapter(new ExpandableListAdapter_Event_Vote_Date(data, recyclerView_date));
                int recyclerView_height_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, recyclerView_height_dp, getResources().getDisplayMetrics());
                recyclerView_date.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
            } else {
                recyclerView_date.setVisibility(View.GONE);
                relativeLayout_titles.setVisibility(View.GONE);
                relativeLayout_date.setVisibility(View.VISIBLE);
                Helper.update_Event_details_field(getContext(), Event_Helper.details[Table_Events.Event_ID_num], Table_Events.Vote_Time, Constants.No);
                String date_text = Helper.date_text_view(Event_Helper.details[Table_Events.Start_Date_num], Event_Helper.details[Table_Events.End_Date_num],
                        Event_Helper.details[Table_Events.All_Day_Time_num], Event_Helper.details[Table_Events.Start_Time_num], Event_Helper.details[Table_Events.End_Time_num]);
                date.setText(date_text);
            }
        }

        public void create_attending_list(ExpandableListAdapter_Event_Friends expandableListAdapter_friends, RecyclerView recyclerView) {
            int yes_num = 0, no_num = 0, maybe_num = 0, not_replay_num = 0;
            List<ExpandableListAdapter_Event_Friends.Item> data = new ArrayList<>();
            ExpandableListAdapter_Event_Friends.Item user_yes = new ExpandableListAdapter_Event_Friends.Item(ExpandableListAdapter_Event_Friends.User);
            ExpandableListAdapter_Event_Friends.Item user_no = new ExpandableListAdapter_Event_Friends.Item(ExpandableListAdapter_Event_Friends.User);
            ExpandableListAdapter_Event_Friends.Item user_maybe = new ExpandableListAdapter_Event_Friends.Item(ExpandableListAdapter_Event_Friends.User);
            ExpandableListAdapter_Event_Friends.Item user_not_replay = new ExpandableListAdapter_Event_Friends.Item(ExpandableListAdapter_Event_Friends.User);
            for (String User_ID : Event_Helper.friends.keySet()) {
                switch (Event_Helper.friends.get(User_ID).getAttending()) {
                    case Constants.Yes: {
                        yes_num++;
                        create_friends_items(user_yes, User_ID);
                        break;
                    }
                    case Constants.Maybe: {
                        maybe_num++;
                        create_friends_items(user_maybe, User_ID);
                        break;
                    }
                    case Constants.No: {
                        no_num++;
                        create_friends_items(user_no, User_ID);
                        break;
                    }
                    case Constants.Did_Not_Replay: {
                        not_replay_num++;
                        create_friends_items(user_not_replay, User_ID);
                        break;
                    }
                }
            }

            user_yes.text = yes_num + " Going";
            user_maybe.text = maybe_num + " Maybe";
            user_no.text = no_num + " Not Going";
            user_not_replay.text = not_replay_num + " Did Not Replay";

            data.add(user_yes);
            data.add(user_maybe);
            data.add(user_no);
            data.add(user_not_replay);

            expandableListAdapter_friends.setData(data, recyclerView);
        }

        public void create_friends_items(ExpandableListAdapter_Event_Friends.Item item, String User_ID) {
            if (item.invisibleChildren == null) {
                item.invisibleChildren = new ArrayList<>();
            }
            ExpandableListAdapter_Event_Friends.Item temp = new ExpandableListAdapter_Event_Friends.Item(ExpandableListAdapter_Event_Friends.User_Child);
            temp.text = User_ID;
            item.invisibleChildren.add(temp);
        }
    }
}

class ExpandableListAdapter_Event_Vote_Date extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int Vote_Date_Parent = 0;
    public static final int Vote_Date_Child = 1;

    private List<Item> data;
    private int recyclerView_height_px;
    private RecyclerView recyclerView;

    private int Vote_ID;
    private String User_ID;

    public List<Item> getData() {
        return data;
    }

    private int parent_height;
    private int child_height;


    public ExpandableListAdapter_Event_Vote_Date(List<Item> data, RecyclerView recyclerView) {
        this.data = data;
        this.recyclerView = recyclerView;
        parent_height = 164;//px
        child_height = 140;//px
        recyclerView_height_px = data.size() * parent_height;
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater;
        switch (viewType) {
            case Vote_Date_Parent: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_detail_vote_date_parent, parent, false);
                ViewHolder_Vote_Date_Parent viewHolder_vote_date_parent = new ViewHolder_Vote_Date_Parent(view);
                /*
                final ViewTreeObserver observer = view.getViewTreeObserver();
                final View finalView = view;
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (parent_height == 0) {
                            parent_height = finalView.getHeight();
                            recyclerView_height_dp = (data.size() - 1) * parent_height;
                            int recyclerView_height_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, recyclerView_height_dp, finalView.getContext().getResources().getDisplayMetrics());
                            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, parent_height));
                        }
                        finalView.getViewTreeObserver().addOnGlobalLayoutListener(this);
                    }
                });*/
                return viewHolder_vote_date_parent;
            }
            case Vote_Date_Child: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_detail_vote_child, parent, false);
                ViewHolder_Vote_Date_Child viewHolder_vote_date_child = new ViewHolder_Vote_Date_Child(view);
                return viewHolder_vote_date_child;
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        final View view = holder.itemView;
        switch (item.type) {
            case Vote_Date_Parent: {
                final ViewHolder_Vote_Date_Parent itemController = (ViewHolder_Vote_Date_Parent) holder;
                itemController.refferalItem = item;
                Vote_ID = itemController.refferalItem.Vote_ID;
                //Set values.
                itemController.count.setText(Event_Helper.vote_date.get(Vote_ID).getVotes().size() + "");
                if (Event_Helper.vote_date.get(Vote_ID).getVotes().get(Constants.MY_User_ID) != null) {
                    itemController.checkBox.setChecked(true);
                } else {
                    itemController.checkBox.setChecked(false);
                }
                itemController.option.setText("Option " + Vote_ID);
                //Set CheckBox
                itemController.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Vote_ID = itemController.refferalItem.Vote_ID;
                        int pos = data.indexOf(itemController.refferalItem);
                        if (itemController.checkBox.isChecked()) {
                            Event_Helper.vote_date.get(Vote_ID).getVotes().put(Constants.MY_User_ID, Constants.MY_User_ID);
                            Helper.add_vote_date_User_ID(v.getContext(), Vote_ID, Constants.MY_User_ID);
                            if (itemController.refferalItem.invisibleChildren == null) {
                                itemController.refferalItem.invisibleChildren = new ArrayList<>();
                                itemController.refferalItem.invisibleChildren.add(new ExpandableListAdapter_Event_Vote_Date.Item(Vote_Date_Child, Vote_ID, Constants.MY_User_ID));
                            } else {
                                data.add(pos + 1, new ExpandableListAdapter_Event_Vote_Date.Item(Vote_Date_Child, Vote_ID, Constants.MY_User_ID));
                                notifyItemInserted(pos + 1);
                                recyclerView_height_px += child_height;
                                recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                            }
                        } else {
                            Event_Helper.vote_date.get(Vote_ID).getVotes().remove(Constants.MY_User_ID);
                            Helper.delete_vote_date_User_ID(v.getContext(), Vote_ID, Constants.MY_User_ID);
                            if (itemController.refferalItem.invisibleChildren == null) {
                                int i = 0;
                                while (!data.get(pos + i).User_ID.equals(Constants.MY_User_ID))
                                    i++;
                                data.remove(data.get(pos + i));
                                notifyItemRemoved(pos + i);
                                recyclerView_height_px -= child_height;
                                recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                            } else {
                                int i = 0;
                                while (!itemController.refferalItem.invisibleChildren.get(i).User_ID.equals(Constants.MY_User_ID))
                                    i++;
                                itemController.refferalItem.invisibleChildren.remove(itemController.refferalItem.invisibleChildren.get(i));
                                if (itemController.refferalItem.invisibleChildren.size() == 0)
                                    itemController.refferalItem.invisibleChildren = null;
                            }
                        }
                        itemController.count.setText(Event_Helper.vote_date.get(Vote_ID).getVotes().size() + "");

                    }
                });
                //Set expand view.
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int pos = data.indexOf(itemController.refferalItem);
                            int count = 0;
                            while (data.size() > pos + 1 && data.get(pos + 1).type == Vote_Date_Child) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            if (count > 0) {
                                notifyItemRangeRemoved(pos + 1, count);
                                itemController.expand_arrow.setImageResource(R.mipmap.ic_expand_arrow);
                                recyclerView_height_px -= child_height * count;
                                recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                            }
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.expand_arrow.setImageResource(R.mipmap.ic_collapse_arrow);
                            item.invisibleChildren = null;
                            recyclerView_height_px += child_height * (index - pos - 1);
                            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                        }

                    }
                });
                break;
            }
            case Vote_Date_Child: {
                final ViewHolder_Vote_Date_Child itemController = (ViewHolder_Vote_Date_Child) holder;
                itemController.refferalItem = item;
                Vote_ID = itemController.refferalItem.Vote_ID;
                User_ID = itemController.refferalItem.User_ID;
                //Set values.
                if (User_ID.equals(Constants.MY_User_ID))
                    itemController.textView.setText(Constants.User_Nickname);
                else
                    itemController.textView.setText(Contacts_List.contacts.get(User_ID));
                itemController.textView_phone.setText(User_ID);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        this.Vote_ID = data.get(position).Vote_ID;
        this.User_ID = data.get(position).User_ID;
        return data.get(position).type;
    }


    private static class ViewHolder_Vote_Date_Parent extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public TextView option;
        public TextView date;
        public TextView count;
        public CheckBox checkBox;
        public ImageView expand_arrow;


        public ViewHolder_Vote_Date_Parent(View itemView) {
            super(itemView);
            option = (TextView) itemView.findViewById(R.id.option);
            date = (TextView) itemView.findViewById(R.id.date);
            count = (TextView) itemView.findViewById(R.id.count);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            expand_arrow = (ImageView) itemView.findViewById(R.id.expand_arrow);

        }
    }

    private static class ViewHolder_Vote_Date_Child extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public TextView textView;
        public TextView textView_phone;

        public ViewHolder_Vote_Date_Child(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView_phone = (TextView) itemView.findViewById(R.id.textView_phone);
        }
    }

    public static class Item {
        public int type;
        public int Vote_ID;
        public String User_ID;

        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, int Vote_ID, String User_ID) {
            this.type = type;
            this.Vote_ID = Vote_ID;
            this.User_ID = User_ID;
        }
    }
}

class ExpandableListAdapter_Event_Vote_Location extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int Vote_Location_Parent = 0;
    public static final int Vote_Location_Child = 1;

    private List<Item> data;
    private int recyclerView_height_px;
    private RecyclerView recyclerView;

    private int Vote_ID;
    private String User_ID;

    public List<Item> getData() {
        return data;
    }

    private int parent_height;
    private int child_height;


    public ExpandableListAdapter_Event_Vote_Location(List<Item> data, RecyclerView recyclerView) {
        this.data = data;
        this.recyclerView = recyclerView;
        parent_height = 164;//px
        child_height = 140;//px
        recyclerView_height_px = data.size() * parent_height;
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater;
        switch (viewType) {
            case Vote_Location_Parent: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_detail_vote_location_parent, parent, false);
                ViewHolder_Vote_Location_Parent viewHolder_vote_location_parent = new ViewHolder_Vote_Location_Parent(view);
                /*
                final ViewTreeObserver observer = view.getViewTreeObserver();
                final View finalView = view;
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (parent_height == 0) {
                            parent_height = finalView.getHeight();
                            recyclerView_height_dp = (data.size() - 1) * parent_height;
                            int recyclerView_height_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, recyclerView_height_dp, finalView.getContext().getResources().getDisplayMetrics());
                            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, parent_height));
                        }
                        finalView.getViewTreeObserver().addOnGlobalLayoutListener(this);
                    }
                });*/
                return viewHolder_vote_location_parent;
            }
            case Vote_Location_Child: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_detail_vote_child, parent, false);
                ViewHolder_Vote_Location_Child viewHolder_vote_location_child = new ViewHolder_Vote_Location_Child(view);
                return viewHolder_vote_location_child;
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        final View view = holder.itemView;
        switch (item.type) {
            case Vote_Location_Parent: {
                final ViewHolder_Vote_Location_Parent itemController = (ViewHolder_Vote_Location_Parent) holder;
                itemController.refferalItem = item;
                Vote_ID = itemController.refferalItem.Vote_ID;
                //Set values.
                itemController.count.setText(Event_Helper.vote_location.get(Vote_ID).getVotes().size() + "");
                if (Event_Helper.vote_location.get(Vote_ID).getVotes().get(Constants.MY_User_ID) != null) {
                    itemController.checkBox.setChecked(true);
                } else {
                    itemController.checkBox.setChecked(false);
                }
                itemController.option.setText("Option " + Vote_ID);
                //Set CheckBox
                itemController.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Vote_ID = itemController.refferalItem.Vote_ID;
                        int pos = data.indexOf(itemController.refferalItem);
                        if (itemController.checkBox.isChecked()) {
                            Event_Helper.vote_location.get(Vote_ID).getVotes().put(Constants.MY_User_ID, Constants.MY_User_ID);
                            Helper.add_vote_date_User_ID(v.getContext(), Vote_ID, Constants.MY_User_ID);
                            if (itemController.refferalItem.invisibleChildren == null) {
                                itemController.refferalItem.invisibleChildren = new ArrayList<>();
                                itemController.refferalItem.invisibleChildren.add(new ExpandableListAdapter_Event_Vote_Location.Item(Vote_Location_Child, Vote_ID, Constants.MY_User_ID));
                            } else {
                                data.add(pos + 1, new ExpandableListAdapter_Event_Vote_Location.Item(Vote_Location_Child, Vote_ID, Constants.MY_User_ID));
                                notifyItemInserted(pos + 1);
                                recyclerView_height_px += child_height;
                                recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                            }
                        } else {
                            Event_Helper.vote_location.get(Vote_ID).getVotes().remove(Constants.MY_User_ID);
                            Helper.delete_vote_date_User_ID(v.getContext(), Vote_ID, Constants.MY_User_ID);
                            if (itemController.refferalItem.invisibleChildren == null) {
                                int i = 0;
                                while (!data.get(pos + i).User_ID.equals(Constants.MY_User_ID))
                                    i++;
                                data.remove(data.get(pos + i));
                                notifyItemRemoved(pos + i);
                                recyclerView_height_px -= child_height;
                                recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                            } else {
                                int i = 0;
                                while (!itemController.refferalItem.invisibleChildren.get(i).User_ID.equals(Constants.MY_User_ID))
                                    i++;
                                itemController.refferalItem.invisibleChildren.remove(itemController.refferalItem.invisibleChildren.get(i));
                                if (itemController.refferalItem.invisibleChildren.size() == 0)
                                    itemController.refferalItem.invisibleChildren = null;
                            }
                        }
                        itemController.count.setText(Event_Helper.vote_location.get(Vote_ID).getVotes().size() + "");

                    }
                });
                //Set expand view.
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int pos = data.indexOf(itemController.refferalItem);
                            int count = 0;
                            while (data.size() > pos + 1 && data.get(pos + 1).type == Vote_Location_Child) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            if (count > 0) {
                                notifyItemRangeRemoved(pos + 1, count);
                                itemController.expand_arrow.setImageResource(R.mipmap.ic_expand_arrow);
                                recyclerView_height_px -= child_height * count;
                                recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                            }
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.expand_arrow.setImageResource(R.mipmap.ic_collapse_arrow);
                            item.invisibleChildren = null;
                            recyclerView_height_px += child_height * (index - pos - 1);
                            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                        }

                    }
                });
                break;
            }
            case Vote_Location_Child: {
                final ViewHolder_Vote_Location_Child itemController = (ViewHolder_Vote_Location_Child) holder;
                itemController.refferalItem = item;
                Vote_ID = itemController.refferalItem.Vote_ID;
                User_ID = itemController.refferalItem.User_ID;
                //Set values.
                if (User_ID.equals(Constants.MY_User_ID))
                    itemController.textView.setText(Constants.User_Nickname);
                else
                    itemController.textView.setText(Contacts_List.contacts.get(User_ID));
                itemController.textView_phone.setText(User_ID);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        this.Vote_ID = data.get(position).Vote_ID;
        this.User_ID = data.get(position).User_ID;
        return data.get(position).type;
    }


    private static class ViewHolder_Vote_Location_Parent extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public TextView option;
        public TextView location;
        public TextView count;
        public CheckBox checkBox;
        public ImageView expand_arrow;


        public ViewHolder_Vote_Location_Parent(View itemView) {
            super(itemView);
            option = (TextView) itemView.findViewById(R.id.option);
            location = (TextView) itemView.findViewById(R.id.location);
            count = (TextView) itemView.findViewById(R.id.count);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            expand_arrow = (ImageView) itemView.findViewById(R.id.expand_arrow);

        }
    }

    private static class ViewHolder_Vote_Location_Child extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public TextView textView;
        public TextView textView_phone;

        public ViewHolder_Vote_Location_Child(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView_phone = (TextView) itemView.findViewById(R.id.textView_phone);
        }
    }

    public static class Item {
        public int type;
        public int Vote_ID;
        public String User_ID;

        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, int Vote_ID, String User_ID) {
            this.type = type;
            this.Vote_ID = Vote_ID;
            this.User_ID = User_ID;
        }
    }
}

class ExpandableListAdapter_Event_Friends extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int User = 0;
    public static final int User_Child = 1;
    private String text_view;

    private List<Item> data;
    private RecyclerView recyclerView;
    private int recyclerView_height_dp;

    public void setData(List<Item> data, RecyclerView recyclerView) {
        this.data = data;
        this.recyclerView = recyclerView;
        recyclerView_height_dp = 134;//The size of all parent (23 + 8.5) * 4.
    }

    public ExpandableListAdapter_Event_Friends(List<Item> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        LayoutInflater inflater;
        switch (type) {
            case User: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_friend_user, parent, false);
                ViewHolder_User viewHolder_user = new ViewHolder_User(view);
                viewHolder_user.textView.setText(text_view);
                return viewHolder_user;
            }
            case User_Child: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_friend_user_child, parent, false);
                ViewHolder_User_Child viewHolder_user_child = new ViewHolder_User_Child(view);
                set_nickname(viewHolder_user_child);
                return viewHolder_user_child;
            }
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        final View view = holder.itemView;
        switch (item.type) {
            case User: {
                final ViewHolder_User itemController = (ViewHolder_User) holder;
                itemController.refferalItem = item;
                itemController.textView.setText(item.text);
                if (item.invisibleChildren == null) {
                    itemController.expand_arrow.setImageResource(R.mipmap.ic_collapse_arrow);
                } else {
                    itemController.expand_arrow.setImageResource(R.mipmap.ic_expand_arrow);
                }
                //itemController.expand_arrow.setOnClickListener(new View.OnClickListener() {
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int pos = data.indexOf(itemController.refferalItem);
                            int count = 0;
                            while (data.size() > pos + 1 && data.get(pos + 1).type != User) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.expand_arrow.setImageResource(R.mipmap.ic_expand_arrow);
                            recyclerView_height_dp -= 38 * count;
                            int recyclerView_height_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, recyclerView_height_dp, view.getResources().getDisplayMetrics());
                            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);

                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.expand_arrow.setImageResource(R.mipmap.ic_collapse_arrow);
                            item.invisibleChildren = null;
                            recyclerView_height_dp += 38 * (index - pos - 1);
                            int recyclerView_height_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, recyclerView_height_dp, view.getResources().getDisplayMetrics());
                            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerView_height_px));
                        }
                    }
                });
                break;
            }
            case User_Child: {
                final ViewHolder_User_Child itemController = (ViewHolder_User_Child) holder;
                itemController.refferalItem = item;
                set_nickname(itemController);
                String my_permission = Helper.getMyPermission(Event_Helper.details[Table_Events.Event_ID_num]);
                if (my_permission.equals(Constants.Owner)) {
                    itemController.textView_permission.setText("Owner");
                }
                break;
            }
        }
    }

    public void set_nickname(ViewHolder_User_Child viewHolder_user_child) {
        if (text_view.equals(Constants.MY_User_ID)) {
            viewHolder_user_child.textView.setText(Constants.User_Nickname);
        } else {
            viewHolder_user_child.textView.setText(Contacts_List.contacts.get(text_view));
        }
    }

    @Override
    public int getItemViewType(int position) {
        text_view = data.get(position).text;
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private static class ViewHolder_User extends RecyclerView.ViewHolder {
        public Item refferalItem;
        TextView textView;
        ImageView expand_arrow;

        public ViewHolder_User(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            expand_arrow = (ImageView) itemView.findViewById(R.id.expand_arrow);
        }
    }

    private static class ViewHolder_User_Child extends RecyclerView.ViewHolder {
        public Item refferalItem;
        TextView textView;
        TextView textView_permission;

        public ViewHolder_User_Child(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView_permission = (TextView) itemView.findViewById(R.id.textView_permission);
        }
    }

    public static class Item {
        public int type;
        public List<Item> invisibleChildren;
        public String text;

        public Item() {
        }

        public Item(int type) {
            this.type = type;
        }
    }
}

class ExpandableListAdapter_Event_Tasks extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int Personal_Task = 0;
    public static final int Group_Task = 1;
    public static final int SubTask = 2;

    private List<Item> data;
    private RecyclerView recyclerView;
    private List<View> viewList;
    private String text;
    private int task_id;
    private int subTask_id;

    public ExpandableListAdapter_Event_Tasks(List<Item> data, RecyclerView recyclerView) {
        this.data = data;
        this.recyclerView = recyclerView;
        viewList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        LayoutInflater inflater;
        switch (type) {
            case Personal_Task:
            case Group_Task: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_task, parent, false);
                ViewHolder_Task viewHolder_task = new ViewHolder_Task(view);
                viewList.add(view);
                viewHolder_task.description.setText(text);
                Task_Helper task = Event_Helper.task.get(task_id);
                if (task.getMark().equals(Constants.Yes)) {
                    viewHolder_task.checkBox.setChecked(true);
                } else {
                    viewHolder_task.checkBox.setChecked(false);
                }
                if (type == Personal_Task) {
                    viewHolder_task.imageView.setImageResource(R.mipmap.ic_personal_gray);
                    viewHolder_task.type.setText("Personal Task");
                } else {
                    if (task.getUser_ID().equals(Constants.MY_User_ID)) {
                        viewHolder_task.imageView.setImageResource(R.mipmap.ic_group_green);
                        viewHolder_task.checkBox.setVisibility(View.VISIBLE);
                        viewHolder_task.user_nickname.setText(Constants.User_Nickname);

                    } else if (task.getUser_ID().equals(Constants.UnCheck)) {
                        viewHolder_task.imageView.setImageResource(R.mipmap.ic_group_gray1);
                        viewHolder_task.checkBox.setVisibility(View.GONE);

                    } else {
                        viewHolder_task.imageView.setImageResource(R.mipmap.ic_group_black);
                        viewHolder_task.checkBox.setVisibility(View.GONE);
                        viewHolder_task.user_nickname.setText(Contacts_List.contacts.get(task.getUser_ID()));
                    }
                }
                return viewHolder_task;
            }
            case SubTask: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_task_child, parent, false);
                ViewHolder_Task_Child viewHolder_task_child = new ViewHolder_Task_Child(view);
                viewList.add(view);
                viewHolder_task_child.description.setText(text);
                Task_Helper task = Event_Helper.task.get(task_id);
                if (task.getType().equals(Constants.Group_Task) && !Event_Helper.task.get(task_id).getUser_ID().equals(Constants.MY_User_ID)) {
                    viewHolder_task_child.checkBox.setVisibility(View.GONE);
                } else {
                    viewHolder_task_child.checkBox.setVisibility(View.VISIBLE);
                }
                if (task.getSubTasks().get(subTask_id)[1].equals(Constants.Yes)) {
                    viewHolder_task_child.checkBox.setChecked(true);
                } else {
                    viewHolder_task_child.checkBox.setChecked(false);
                }
                return viewHolder_task_child;
            }
        }
        return null;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Item item = data.get(position);
        final View view = holder.itemView;
        switch (item.type) {
            case SubTask: {
                final ViewHolder_Task_Child itemController = (ViewHolder_Task_Child) holder;
                itemController.refferalItem = item;
                //Refresh values and CheckBox appearance.
                itemController.description.setText(itemController.refferalItem.description);
                task_id = itemController.refferalItem.task_id;
                subTask_id = itemController.refferalItem.subTask_id;
                Task_Helper task = Event_Helper.task.get(task_id);
                if (task.getType().equals(Constants.Group_Task) && !Event_Helper.task.get(task_id).getUser_ID().equals(Constants.MY_User_ID)) {
                    itemController.checkBox.setVisibility(View.GONE);
                } else {
                    itemController.checkBox.setVisibility(View.VISIBLE);
                }
                if (task.getSubTasks().get(subTask_id)[1].equals(Constants.Yes)) {
                    itemController.checkBox.setChecked(true);
                } else {
                    itemController.checkBox.setChecked(false);
                }
                itemController.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task_id = itemController.refferalItem.task_id;
                        subTask_id = itemController.refferalItem.subTask_id;
                        if (itemController.checkBox.isChecked()) {
                            mark_child(task_id, subTask_id, Constants.Yes, itemController.refferalItem);
                        } else {
                            mark_child(task_id, subTask_id, Constants.No, itemController.refferalItem);
                        }
                    }
                });
                break;
            }
            case Personal_Task:
            case Group_Task: {
                final ViewHolder_Task itemController = (ViewHolder_Task) holder;
                itemController.refferalItem = item;
                task_id = itemController.refferalItem.task_id;
                Task_Helper task = Event_Helper.task.get(task_id);
                //Set arrow.
                if (item.invisibleChildren == null) {
                    itemController.expand_arrow.setImageResource(R.mipmap.ic_collapse_arrow);
                } else {
                    itemController.expand_arrow.setImageResource(R.mipmap.ic_expand_arrow);
                }
                //Set checkbox.
                if (Event_Helper.task.get(task_id).getMark().equals(Constants.Yes)) {
                    itemController.checkBox.setChecked(true);
                } else {
                    itemController.checkBox.setChecked(false);
                }
                //Set nickname;
                if (task.getUser_ID().equals(Constants.MY_User_ID)) {
                    itemController.user_nickname.setText(Constants.User_Nickname);
                } else if (!task.getUser_ID().equals(Constants.UnCheck)) {
                    itemController.user_nickname.setText(Contacts_List.contacts.get(task.getUser_ID()));
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int pos = data.indexOf(itemController.refferalItem);
                            int count = 0;
                            while (data.size() > pos + 1 && data.get(pos + 1).type == SubTask) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.expand_arrow.setImageResource(R.mipmap.ic_expand_arrow);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.expand_arrow.setImageResource(R.mipmap.ic_collapse_arrow);
                            item.invisibleChildren = null;
                        }
                    }
                });
                itemController.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task_id = itemController.refferalItem.task_id;
                        int pos = data.indexOf(itemController.refferalItem);
                        if (itemController.checkBox.isChecked()) {
                            mark(task_id, Constants.Yes);
                        } else {
                            mark(task_id, Constants.No);
                        }
                        refresh_children(pos);
                    }
                });
                //Take task.
                if (itemController.refferalItem.type == Group_Task) {
                    itemController.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = data.indexOf(itemController.refferalItem);
                            //Sign for the task.
                            task_id = itemController.refferalItem.task_id;
                            if (Event_Helper.task.get(itemController.refferalItem.task_id).getUser_ID().equals(Constants.UnCheck)) {
                                Event_Helper.task.get(itemController.refferalItem.task_id).setUser_ID(Constants.MY_User_ID);
                                Helper.set_task_user_ID(v.getContext(), Event_Helper.details[Table_Events.Event_ID_num], task_id, Constants.MY_User_ID);
                                itemController.user_nickname.setText(Constants.User_Nickname);
                                itemController.imageView.setImageResource(R.mipmap.ic_group_green);
                                itemController.checkBox.setVisibility(View.VISIBLE);
                                refresh_children(pos);
                            } else {//UnSign for the task (check first that the task was sign by the user).
                                if (Event_Helper.task.get(itemController.refferalItem.task_id).getUser_ID().equals(Constants.MY_User_ID)) {
                                    Event_Helper.task.get(itemController.refferalItem.task_id).setUser_ID(Constants.UnCheck);
                                    Helper.set_task_user_ID(v.getContext(), Event_Helper.details[Table_Events.Event_ID_num], task_id, Constants.UnCheck);
                                    itemController.user_nickname.setText("");
                                    itemController.imageView.setImageResource(R.mipmap.ic_group_gray1);
                                    itemController.checkBox.setVisibility(View.GONE);
                                    refresh_children(pos);
                                }
                            }
                        }
                    });
                }
                break;

            }
        }
    }

    private void mark_child(int task_id, int subTask_id, String mark, Item refferalItem) {
        Task_Helper task = Event_Helper.task.get(task_id);
        String description = task.getSubTasks().get(subTask_id)[0];
        task.getSubTasks().put(subTask_id, new String[]{description, mark});
        Helper.mark_one_task_MySQL(Event_Helper.details[Table_Events.Event_ID_num], task_id, subTask_id, mark);
        //Change parent mark according to the child mark. parent is marked iff all of is children's are marked.
        int num_of_mark_child = 0;
        int numb_of_childes = task.getSubTasks().size();
        for (String[] sub_task : task.getSubTasks().values()) {
            if (sub_task[1].equals(Constants.Yes)) {
                num_of_mark_child++;
            }
        }
        //Find the position of the parent.
        int pos = data.indexOf(refferalItem);
        while (data.get(pos).type == SubTask) {
            pos--;
        }
        if (num_of_mark_child == numb_of_childes) {
            task.setMark(mark);
            Helper.mark_one_task_MySQL(Event_Helper.details[Table_Events.Event_ID_num], task_id, 0, mark);
            notifyItemChanged(pos);
        } else if (num_of_mark_child == numb_of_childes - 1 && mark.equals(Constants.No)) {
            task.setMark(mark);
            Helper.mark_one_task_MySQL(Event_Helper.details[Table_Events.Event_ID_num], task_id, 0, mark);
            notifyItemChanged(pos);
        }

    }

    private void mark(int task_id, String mark) {
        Event_Helper.task.get(task_id).setMark(mark);
        for (int subTask_id : Event_Helper.task.get(task_id).getSubTasks().keySet()) {
            String description = Event_Helper.task.get(task_id).getSubTasks().get(subTask_id)[0];
            Event_Helper.task.get(task_id).getSubTasks().put(subTask_id, new String[]{description, mark});
        }
        //Save in sql.
        Helper.mark_task_and_subTasks_MySQL(Event_Helper.details[Table_Events.Event_ID_num], task_id, mark);
    }

    private void refresh_children(int position) {
        int index = 1;
        if (data.get(position).invisibleChildren == null) {
            while (data.size() > position + index && data.get(position + index).type == SubTask) {
                notifyItemChanged(position + index);
                index++;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        this.text = data.get(position).description;
        this.task_id = data.get(position).task_id;
        this.subTask_id = data.get(position).subTask_id;
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ViewHolder_Task extends RecyclerView.ViewHolder {
        public ImageView expand_arrow;
        public ImageView imageView;
        public TextView user_nickname;
        public TextView description;
        public TextView type;
        public Item refferalItem;
        public CheckBox checkBox;

        public ViewHolder_Task(View itemView) {
            super(itemView);
            expand_arrow = (ImageView) itemView.findViewById(R.id.expand_arrow);
            user_nickname = (TextView) itemView.findViewById(R.id.user_nickname);
            description = (TextView) itemView.findViewById(R.id.description);
            type = (TextView) itemView.findViewById(R.id.type);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

    private static class ViewHolder_Task_Child extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public TextView description;
        CheckBox checkBox;

        public ViewHolder_Task_Child(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }

    public static class Item {
        public int type;
        public int task_id;
        public int subTask_id;
        public String description;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, int task_id, int subTask_id, String description) {
            this.type = type;
            this.task_id = task_id;
            this.subTask_id = subTask_id;
            this.description = description;
        }
    }
}

class ExpandableListAdapter_Event_Chat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int Chat_My = 0;
    public static final int Chat_Friend = 1;

    private String Chat_ID;
    private List<Item> data;
    private ArrayList<String>[] dbChat;
    private int position;

    public ExpandableListAdapter_Event_Chat(List<Item> data, ArrayList<String>[] dbChat, String Chat_ID) {
        this.data = data;
        this.dbChat = dbChat;
        this.Chat_ID = Chat_ID;
    }

    public void setDbChat(ArrayList<String>[] dbChat) {
        this.dbChat = dbChat;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        LayoutInflater inflater;
        switch (type) {
            case Chat_My: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_chat_my, parent, false);
                ViewHolder_Chat_My viewHolder_chat_my = new ViewHolder_Chat_My(view);
                viewHolder_chat_my.textView.setText(chat_message_format(position));
                return viewHolder_chat_my;
            }
            case Chat_Friend: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_chat_user, parent, false);
                ViewHolder_Chat_Friend viewHolder_chat_friend = new ViewHolder_Chat_Friend(view);
                viewHolder_chat_friend.textView.setText(chat_message_format(position));
                return viewHolder_chat_friend;
            }
        }
        return null;
    }

    private String chat_message_format(int position) {
        String time = dbChat[Table_Chat.Time_num].get(position);
        String hour = time.split(":")[0];
        if (hour.length() == 1) {
            hour = 0 + hour;
        }
        String minute = time.split(":")[1];
        if (minute.length() == 1) {
            minute = 0 + hour;
        }
        String message = dbChat[Table_Chat.Message_num].get(position);
        String user = dbChat[Table_Chat.User_ID_num].get(position);
        if (user.equals(Constants.MY_User_ID)) {
            return "me, " + hour + ":" + minute + "\n" + message;
        } else {
            return Contacts_List.contacts.get(user) + " ," + hour + ":" + minute + " " + "\n" + message;
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Item item = data.get(position);
        View view = holder.itemView;
        switch (item.type) {
            case Chat_My: {
                final ViewHolder_Chat_My itemController = (ViewHolder_Chat_My) holder;
                itemController.refferalItem = item;
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Delete Message?")
                                .setMessage("Are you sure you want to Delete this Message?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int pos = data.indexOf(itemController.refferalItem);
                                        //Remove from adapter.
                                        data.remove(itemController.refferalItem);
                                        notifyItemRemoved(pos);
                                        //Remove from MySql + ServerSql;
                                        String Message_ID = dbChat[Table_Chat.Message_ID_num].get(pos);
                                        Helper.delete_chat_message(v.getContext(), Chat_ID, Message_ID, Constants.MY_User_ID);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return false;
                    }
                });
                break;
            }
            case Chat_Friend: {

                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        this.position = position;
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private static class ViewHolder_Chat_My extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public TextView textView;

        public ViewHolder_Chat_My(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    private static class ViewHolder_Chat_Friend extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public TextView textView;

        public ViewHolder_Chat_Friend(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);

        }
    }

    public static class Item {
        public int type;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type) {
            this.type = type;
        }
    }
}