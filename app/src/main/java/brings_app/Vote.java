package brings_app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import utils.Constans.Constants;
import utils.Constans.Table_Events;
import utils.Event_Helper_Package.Contacts_List;
import utils.Event_Helper_Package.Event_Helper;
import utils.Helper;

public class Vote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView event_name = (TextView) findViewById(R.id.event_name);
        //Get vote type.
        Bundle b = getIntent().getExtras();
        final String vote_type = b.getString("vote_type");
        //Set page title.
        if (vote_type.equals(Constants.Vote_Date)) {
            event_name.setText("Date Options");
        } else {
            event_name.setText("Location Options");
        }
        //Get permission;
        String permission = Helper.getMyPermission(Event_Helper.details[Table_Events.Event_ID_num]);
        //Set recyclerView.
        //Get all votes.
        /*
        ArrayList<String>[] dbVote_parents = sqlHelper.select(null, Table_Vote_Location.Table_Name + Event_Helper.details[Table_Events.Event_ID_num],
                new String[]{Table_Vote_Location.Type, Table_Vote_Location.User_ID}, new String[]{vote_type, ""}, null);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ExpandableListAdapter_Vote.Item> data = new ArrayList<>();
        ExpandableListAdapter_Vote.Item vote;
        if (dbVote_parents != null) {
            for (int i = 0; i < dbVote_parents[0].size(); i++) {
                String Vote_ID = dbVote_parents[Table_Vote_Location.Vote_ID_num].get(i);
                String Description = dbVote_parents[Table_Vote_Location.Description_num].get(i);
                ArrayList<String>[] dbVote_childes = sqlHelper.select(null, Table_Vote_Location.Table_Name + Event_Helper.details[Table_Events.Event_ID_num],
                        new String[]{Table_Vote_Location.Type, Table_Vote_Location.Vote_ID}, new String[]{vote_type, Vote_ID}, null);
                String Count = dbVote_childes[0].size() + "";
                if (vote_type.equals(Constants.Vote_Date)) {
                    vote = new ExpandableListAdapter_Vote.Item(ExpandableListAdapter_Vote.Vote_Date, Vote_ID, Description, dbVote_childes[Table_Vote_Location.User_ID_num].get(i), Count);
                } else {
                    vote = new ExpandableListAdapter_Vote.Item(ExpandableListAdapter_Vote.Vote_Location, Vote_ID, Description, dbVote_childes[Table_Vote_Location.User_ID_num].get(i), Count);
                }

                for (String User_ID : dbVote_childes[Table_Vote_Location.User_ID_num]) {
                    if (vote.invisibleChildren == null) {
                        vote.invisibleChildren = new ArrayList<>();
                    }
                    if (vote_type.equals(Constants.Vote_Date)) {
                        vote.invisibleChildren.add(new ExpandableListAdapter_Vote.Item(ExpandableListAdapter_Vote.Vote_Date, Vote_ID, "", User_ID, 0 + ""));
                    } else {
                        vote.invisibleChildren.add(new ExpandableListAdapter_Vote.Item(ExpandableListAdapter_Vote.Vote_Date, Vote_ID, "", User_ID, 0 + ""));
                    }
                }
                data.add(vote);
            }
        }
        recyclerView.setAdapter(new ExpandableListAdapter_Vote(data));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (permission.equals(Constants.Owner)) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    construct_dialog(vote_type, recyclerView);
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }*/
    }

    private void construct_dialog(String vote_type, RecyclerView recyclerView) {
        final ExpandableListAdapter_Vote expandableListAdapter_vote = (ExpandableListAdapter_Vote) recyclerView.getAdapter();
        final List<ExpandableListAdapter_Vote.Item> data = expandableListAdapter_vote.getData();
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        if (vote_type.equals(Constants.Vote_Location)) {
            // Include xml file
            dialog.setContentView(R.layout.dialog_vote_location);
            // Set dialog title
            dialog.setTitle("Add location option");
            // set values for custom dialog components - text, image and button
            final EditText editText = (EditText) dialog.findViewById(R.id.editText);
            TextView save = (TextView) dialog.findViewById(R.id.save);
            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
            dialog.show();
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Get next vote id.
                    int vote_id = 0;
                    for (int i = 0; i < data.size(); i++) {
                        vote_id = Math.max(vote_id, Integer.parseInt(data.get(i).Vote_ID));
                    }
                    vote_id++;
                    data.add(new ExpandableListAdapter_Vote.Item(ExpandableListAdapter_Vote.Vote_Location, vote_id + "", editText.getText().toString(), Constants.UnCheck, 0 + ""));
                    expandableListAdapter_vote.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {

        }

    }
}

class ExpandableListAdapter_Vote extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int Vote_Date = 0;
    public static final int Vote_Location = 1;
    public static final int Vote_Child = 2;

    private List<Item> data;
    private String Vote_ID;
    private String Description;
    private String User_ID;
    private String Count;

    public List<Item> getData() {
        return data;
    }

    public ExpandableListAdapter_Vote(List<Item> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater;
        switch (viewType) {
            case Vote_Date:
            case Vote_Location: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.vote_parent, parent, false);
                ViewHolder_Vote viewHolder_vote = new ViewHolder_Vote(view);
                if (viewType == Vote_Date) {
                    viewHolder_vote.imageView.setImageResource(R.mipmap.ic_time);
                } else {
                    viewHolder_vote.imageView.setImageResource(R.mipmap.ic_location);
                }
                viewHolder_vote.description.setText(Description);
                viewHolder_vote.count.setText(Count);
                return viewHolder_vote;
            }
            case Vote_Child: {
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_detail_vote_child, parent, false);
                ViewHolder_Vote_Child viewHolder_vote_child = new ViewHolder_Vote_Child(view);
                String nickname = Contacts_List.contacts.get(User_ID);
                viewHolder_vote_child.textView.setText(nickname);
                viewHolder_vote_child.textView_phone.setText(Helper.Convert_User_ID_To_Phone(User_ID));
                return viewHolder_vote_child;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        final View view = holder.itemView;
        switch (item.type) {
            case Vote_Location: {
                final ViewHolder_Vote itemController = (ViewHolder_Vote) holder;
                itemController.refferalItem = item;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int pos = data.indexOf(itemController.refferalItem);
                            int count = 0;
                            while (data.size() > pos + 1 && data.get(pos + 1).type == Vote_Child) {
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
                        int count = Integer.parseInt(itemController.refferalItem.Count);
                        if (itemController.checkBox.isChecked()) {
                            itemController.refferalItem.User_ID = Constants.MY_User_ID;
                            //Add child.
                            if (itemController.refferalItem.invisibleChildren == null)
                                itemController.refferalItem.invisibleChildren = new ArrayList<Item>();
                            itemController.refferalItem.invisibleChildren.add(new ExpandableListAdapter_Vote.Item(ExpandableListAdapter_Vote.Vote_Child, Vote_ID, "", Constants.MY_User_ID, 0 + ""));
                            count++;
                        } else {
                            itemController.refferalItem.User_ID = Constants.UnCheck;
                            for (int i = 0; i < itemController.refferalItem.invisibleChildren.size(); i++) {
                                if (itemController.refferalItem.invisibleChildren.get(i).User_ID.equals(Constants.MY_User_ID)) {
                                    itemController.refferalItem.invisibleChildren.remove(itemController.refferalItem.invisibleChildren.get(i));
                                    break;
                                }
                            }
                            count--;
                        }
                        itemController.refferalItem.Count = count + "";
                        itemController.count.setText(count + "");
                        notifyDataSetChanged();
                    }
                });
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
        this.Description = data.get(position).Description;
        this.User_ID = data.get(position).User_ID;
        this.Count = data.get(position).Count;
        return data.get(position).type;
    }

    private static class ViewHolder_Vote extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public ImageView imageView;
        public TextView description;
        public TextView count;
        public ImageView expand_arrow;
        public CheckBox checkBox;


        public ViewHolder_Vote(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            description = (TextView) itemView.findViewById(R.id.description);
            count = (TextView) itemView.findViewById(R.id.count);
            expand_arrow = (ImageView) itemView.findViewById(R.id.expand_arrow);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

    private static class ViewHolder_Vote_Child extends RecyclerView.ViewHolder {
        public Item refferalItem;
        public TextView textView;
        public TextView textView_phone;

        public ViewHolder_Vote_Child(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView_phone = (TextView) itemView.findViewById(R.id.textView_phone);

        }
    }

    public static class Item {
        public int type;
        public String Vote_ID;
        public String Description;
        public String User_ID;
        public String Count;

        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, String Vote_ID, String Description, String User_ID, String Count) {
            this.type = type;
            this.Vote_ID = Vote_ID;
            this.Description = Description;
            this.User_ID = User_ID;
            this.Count = Count;
        }
    }
}