package com.familylooped.common.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.familylooped.R;
import com.familylooped.common.logger.Log;
import com.familylooped.common.logger.LogWrapper;

public class BaseActionBarActivity extends FragmentActivity {
    private static final String TAG = "Base Activity";
    protected FragmentManager.OnBackStackChangedListener mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            setActionBarArrowDependingOnFragmentsBackStack();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
        initializeLogging();
    }

    private void setActionBarArrowDependingOnFragmentsBackStack() {
       /* int backStackEntryCount =
                getSupportFragmentManager().getBackStackEntryCount();
        getSupportActionBar().setDisplayHomeAsUpEnabled(backStackEntryCount > 0);*/
    }


    protected void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();



    }


    protected void setFragmentWithBackStack(Fragment fragment,String tag) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.container, fragment).addToBackStack(tag).commit();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            popFragmentIfStackExist();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void popFragmentIfStackExist() {
        int backStackEntryCount =
                getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0)
            getSupportFragmentManager().popBackStack();
    }

    /**
     * Set up targets to receive log data
     */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);
        Log.i(TAG, "Ready");
    }

    protected <T> void changeActivity(Intent intent) {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        startActivity(intent);
        finish();
    }
    protected <T> void changeActivity(Class<T> cls, Bundle data) {
        Intent resultIntent = new Intent(this, cls);
        if (data != null)
            resultIntent.putExtras(data);
        startActivity(resultIntent);
        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
