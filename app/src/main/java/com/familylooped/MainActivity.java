package com.familylooped;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.familylooped.auth.InvitePeople;
import com.familylooped.common.activities.BaseActionBarActivity;
import com.familylooped.common.logger.Log;
import com.familylooped.dashboard.Dashboard;
import com.familylooped.photos.MyPhotos;


public class MainActivity extends BaseActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();*/
            if (getIntent().getExtras() != null) {
                if (getIntent().hasExtra("is_notification")) {
                    boolean is_notification = getIntent().getExtras().getBoolean("is_notification");
                    if (is_notification) {
                        setFragment(MyPhotos.newInstance(true));
                    } else {
                        setFragment(Dashboard.newInstance());
                    }

                } else if (getIntent().hasExtra("json")) {
                    String json = getIntent().getExtras().getString("json");
                    setFragment(InvitePeople.newInstance(true, json));
                }


            } else
                setFragment(Dashboard.newInstance());
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            return rootView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().findFragmentById(R.id.container).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("Config", "MainActiviy");
    }
}
