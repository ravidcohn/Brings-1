package some_lie.brings;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import sliding_tab.SlidingTabs;

public class tab extends AppCompatActivity {
    private String KEY;
    private FragmentTransaction transaction;
    private SlidingTabs fragment;

    private int cItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        Bundle b = getIntent().getExtras();
        KEY = b.getString("KEY");

        if (savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            fragment = new SlidingTabs();
            fragment.setArguments(b);
            transaction.replace(R.id.fl_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    private void refresh(){

        transaction = getSupportFragmentManager().beginTransaction();
        int src_todo = fragment.getArguments().getInt("todo");
        if(src_todo == 2){
            cItem = src_todo;
        }
        fragment = new SlidingTabs();
        Bundle b = new Bundle();
        b.putString("KEY", KEY);
        b.putInt("view_num",cItem);

        fragment.setArguments(b);
        transaction.replace(R.id.fl_fragment, fragment);
        transaction.commit();
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
        boolean sItem = true;
        switch (item.getItemId()) {
            case R.id.action_home: {
                final Intent home = new Intent(this, MainActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
                break;
            }
            case R.id.action_edit: {
                final Intent edit = new Intent(this, edit_event.class);
                Bundle b = new Bundle();
                b.putString("KEY" ,KEY);
                cItem = fragment.getCurrentItem();
                edit.putExtras(b);
                startActivityForResult(edit, 1);
                break;
            }
            default: {
                sItem = super.onOptionsItemSelected(item);
                break;
            }
        }
        return sItem;
    }

}