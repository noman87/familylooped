package com.familylooped.slideShow;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.familylooped.common.Utilities;
import com.familylooped.common.logger.Log;
import com.familylooped.photos.ModelMyPhoto;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Noman on 5/5/2015.
 */
public class AdapterSlideShow extends FragmentStatePagerAdapter {
    // private ArrayList<View> views = new ArrayList<>();
    private ArrayList<ModelMyPhoto> list;
    private long baseId = 0;
    private Activity mActivity;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


    public AdapterSlideShow(Activity activity, FragmentManager fm, ArrayList<ModelMyPhoto> list) {
        super(fm);
        this.list = list;
        Log.e("list size", "" + list.size());
        mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position).createFragment();
    }

    public View getCurrentView(int position) {
        return getItem(position).getView();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

//    @Override
//    public int getItemPosition(Object object) {
//        if (list.contains((ModelMyPhoto) object)) {
//            return list.indexOf((ModelMyPhoto) object);
//        } else {
//            return POSITION_NONE;
//        }
//    }


//    public void removeView(int index) {
//        list.remove(index);
//        notifyDataSetChanged();
//    }

//    @Override
//    public long getItemId(int position) {
//        // give an ID different from position when position has been changed
//        return baseId + position;
//    }


    /**
     * Notify that the position of a fragment has been changed.
     * Create a new ID for each position to force recreation of the fragment
     *
     * @param n number of items which have been changed
     */
    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }

    public int removeView(ViewPager pager, int position) {
        Gson gson = new Gson();
        list.remove(position);
        Utilities.saveUsersPhotoJson(mActivity, gson.toJson(list));
//
//        //  notifyChangeInPosition(position);
//        //    pager.removeViewAt(position);
//        notifyDataSetChanged();
//        pager.getAdapter().notifyDataSetChanged();

        return position;
    }

    private void deletePhotoFromApp(int currentItem) {

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}
