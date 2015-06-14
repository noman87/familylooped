package com.familylooped.common.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;

import com.familylooped.R;
import com.familylooped.common.Utilities;
import com.familylooped.common.logger.Log;

public abstract class BaseFragment extends Fragment {

    public static boolean sDisableFragmentAnimations = false;

    protected void setTitle(String title) {
       // getActionBar().setTitle(title);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utilities.hideKeyboard(getActivity(), view);
    }

    /**
     * Translate Animation between fragments.
     *
     * @param fragment
     * @param tag
     */
    protected void changeFragment(Fragment fragment, String tag) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, fragment, tag).addToBackStack("back." + tag).commit();
    }


    public void showDialog(String message, String done, String cancel, DialogClickListener listener) {

        AlertDialogFragment fragment = AlertDialogFragment.newInstance(message, done, cancel, listener);
        fragment.show(getFragmentManager(), "Dialog");
    }


    protected Fragment getAttachFragment() {
        return getFragmentManager().findFragmentById(R.id.container);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {}

    /**
     * Translate Animation between fragments.
     *
     * @param fragment
     * @param tag
     */
    protected void changeFragmentWithoutBackStack(Fragment fragment, String tag) {


        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, fragment, tag).commit();
    }


    /**
     * Translate Animation between Activities with data.
     *
     * @param cls
     * @param data
     */
    protected <T> void changeActivity(Class<T> cls, Bundle data) {
        Intent resultIntent = new Intent(getActivity(), cls);
        if (data != null)
            resultIntent.putExtras(data);
        startActivity(resultIntent);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    /**
     * Translate Animation between Activities.
     *
     * @param cls
     */
    protected <T> void changeActivity(Class<T> cls) {
        Intent resultIntent = new Intent(getActivity(), cls);
        startActivity(resultIntent);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        getActivity().finish();
    }

    protected <T> void changeActivity(Class<T> cls,boolean isRemoveAll) {
        Intent resultIntent = new Intent(getActivity(), cls);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(resultIntent);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        getActivity().finish();
    }

    public ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (sDisableFragmentAnimations) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /*protected void clearBackStack() {
        try {
            sDisableFragmentAnimations = true;
            getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            sDisableFragmentAnimations = false;
        }catch (Exception e){

        }
    } */


    protected void clearBackStack() {
        sDisableFragmentAnimations = true;
        int stack = getFragmentManager().getBackStackEntryCount();
        for (int i = 0; i <= stack; i++) {

            getFragmentManager().popBackStack();
        }
        sDisableFragmentAnimations = false;
    }

}
