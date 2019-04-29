package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 登入頁面Adapter
 * @author zwm
 */
public class LoginFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
    private List<Fragment> fragmentList;

    public LoginFragmentPagerAdapter(FragmentManager fm, List<String> titleList, List<Fragment> fragmentList) {
        super(fm);

        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
