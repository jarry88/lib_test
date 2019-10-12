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
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.BitmapUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 分類Fragment
 * @author zwm
 */
public class CategoryFragment extends BaseFragment implements View.OnClickListener {
    public static final int TAB_STORE = 0;
    public static final int TAB_GOODS = 1;
    public static final int TAB_BRAND = 2;

    /**
     * 當前選中哪個Tab
     */
    int currTabIndex;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();


    public static CategoryFragment newInstance(int currTabIndex) {
        Bundle args = new Bundle();

        args.putInt("currTabIndex", currTabIndex);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        currTabIndex = args.getInt("currTabIndex");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_search, this);
        Util.setOnClickListener(view, R.id.btn_message, this);

        TabLayout tabLayout = view.findViewById(R.id.category_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.category_viewpager);

        titleList.add(getResources().getString(R.string.category_shop));
        titleList.add(getResources().getString(R.string.category_commodity));
        titleList.add(getResources().getString(R.string.category_brand));

        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));

        fragmentList.add(CategoryShopFragment.newInstance());
        fragmentList.add(CategoryCommodityFragment.newInstance());
        fragmentList.add(CategoryBrandFragment.newInstance());

        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        tabLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(currTabIndex);
            }
        }, 250);
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_search:
                Util.startFragment(SearchFragment.newInstance(SearchType.ALL));
                break;
            case R.id.btn_message:
                if (User.getUserId() > 0) {
                    Util.startFragment(MessageFragment.newInstance(true));
                } else {
                    Util.showLoginFragment();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
