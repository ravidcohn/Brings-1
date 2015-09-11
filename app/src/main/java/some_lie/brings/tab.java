package some_lie.brings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by pinhas on 09/09/2015.
 */
public class tab extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_tab);


        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);


        mTabHost.addTab(mTabHost.newTabSpec("Main").setIndicator("Main"),
                mainTab.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Attending").setIndicator("Attending"),
                attendingTab.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("To Do").setIndicator("To Do"),
                todoTab.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Chat").setIndicator("Chat"),
                chatTab.class, null);
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