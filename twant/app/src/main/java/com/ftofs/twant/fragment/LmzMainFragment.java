package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.lmz.LmzNestedScrollView;
import com.ftofs.twant.widget.lmz.LmzNestedScrollingDemoFragment;
import com.ftofs.twant.widget.lmz.LmzNestedScrollingFragmentAdapter;
import com.ftofs.twant.widget.lmz.LmzNestedVerticalLinearLayout;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LmzMainFragment extends BaseFragment implements View.OnClickListener {
    int currIndex = -1;
    public static final int TAB_COUNT = 4;

    int[] tabIdArr = new int[] {
            R.id.tab_btn_1, R.id.tab_btn_2, R.id.tab_btn_3, R.id.tab_btn_4
    };

    View[] tabBtnArr = new View[TAB_COUNT];

    ViewPager viewPager;
    LmzNestedVerticalLinearLayout nestedVerticalLinearLayout;
    LmzNestedScrollView nestedScrollView;

    private List<LmzNestedScrollingDemoFragment> fragmentList;
    private LmzNestedScrollingFragmentAdapter fragmentAdapter;

    public static LmzMainFragment newInstance() {
        Bundle args = new Bundle();

        LmzMainFragment fragment = new LmzMainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lmz_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        for (int i = 0; i < TAB_COUNT; i++) {
            tabBtnArr[i] = view.findViewById(tabIdArr[i]);
            tabBtnArr[i].setOnClickListener(this);
        }

        viewPager = view.findViewById(R.id.view_pager);
        nestedVerticalLinearLayout = view.findViewById(R.id.ll_scroll_container);
        nestedScrollView = view.findViewById(R.id.lmz_nested_scroll_view);

        fragmentList = new ArrayList<>();
        for (int i = 0; i < TAB_COUNT; i++) {
            LmzNestedScrollingDemoFragment fragment = new LmzNestedScrollingDemoFragment();
            fragment.setIndex(i);
            fragmentList.add(fragment);
        }

        fragmentAdapter = new LmzNestedScrollingFragmentAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        nestedScrollView.setCurrentScrollableContainer(fragmentList.get(0));

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                nestedScrollView.setCurrentScrollableContainer(fragmentList.get(position));
            }
        });

        selectTab(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tab_btn_1) {
            selectTab(0);
        } else if (id == R.id.tab_btn_2) {
            selectTab(1);
        } else if (id == R.id.tab_btn_3) {
            selectTab(2);
        } else if (id == R.id.tab_btn_4) {
            selectTab(3);
        } else if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void selectTab(int index) {
        if (index == currIndex) {
            return;
        }
        // 取消前一次的选中状态
        if (currIndex >= 0) {
            tabBtnArr[currIndex].setBackground(null);
        }

        currIndex = index;
        tabBtnArr[currIndex].setBackgroundResource(R.drawable.border_type_d);

        viewPager.setCurrentItem(currIndex);
    }
}

