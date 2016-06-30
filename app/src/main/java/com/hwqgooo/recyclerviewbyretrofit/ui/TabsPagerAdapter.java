package com.hwqgooo.recyclerviewbyretrofit.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hwqgooo.recyclerviewbyretrofit.view.IBaseFragment;

import java.util.List;

/**
 * Created by weiqiang on 2016/6/11.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> lists;

    public TabsPagerAdapter(FragmentManager fm, List<Fragment> lists) {
        super(fm);
        this.lists = lists;
    }

    @Override
    public Fragment getItem(int position) {
        return lists != null ? lists.get(position) : null;
    }

    @Override
    public int getCount() {
        return lists != null ? lists.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        try {
            IBaseFragment fragment = (IBaseFragment) lists.get(position);
            return fragment.getTitle();
        } catch (Exception e) {
            return super.getPageTitle(position);
        }
    }
}
