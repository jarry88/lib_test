package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.LoginFragmentPagerAdapter;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 登入
 * @author zwm
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_login, this);


        TabLayout tabLayout = view.findViewById(R.id.login_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.login_viewpager);

        titleList.add(_mActivity.getResources().getString(R.string.login_way_password));
        titleList.add(_mActivity.getResources().getString(R.string.login_way_dynamic_code));

        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));

        fragmentList.add(PasswordLoginFragment.newInstance());
        fragmentList.add(DynamicCodeLoginFragment.newInstance());

        LoginFragmentPagerAdapter adapter = new LoginFragmentPagerAdapter(_mActivity.getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {

        }
    }
}
