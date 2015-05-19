package com.familylooped.slideShow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.familylooped.common.logger.Log;
import com.familylooped.photos.ModelMyPhoto;

import java.util.ArrayList;

/**
 * Created by Noman on 5/5/2015.
 */
public class AdapterSlideShow extends FragmentStatePagerAdapter {
    // private ArrayList<View> views = new ArrayList<>();
    private ArrayList<ModelMyPhoto> list;
    private long baseId = 0;

    public AdapterSlideShow(FragmentManager fm, ArrayList<ModelMyPhoto> list) {
        super(fm);
        this.list = list;
        Log.e("list size", "" + list.size());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position).createFragment();
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
        list.remove(position);

//
//        //  notifyChangeInPosition(position);
//        //    pager.removeViewAt(position);
//        notifyDataSetChanged();
//        pager.getAdapter().notifyDataSetChanged();

        return position;
    }
}
