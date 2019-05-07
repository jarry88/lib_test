package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 主Fragment容器
 * @author zwm
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {
    /** 首頁 */
    public static final int HOME_FRAGMENT = 0;
    /** 消息 */
    public static final int MESSAGE_FRAGMENT = 1;
    /** 想要圈 */
    public static final int CIRCLE_FRAGMENT = 2;
    /** 購物車 */
    public static final int CART_FRAGMENT = 3;
    /** 我的 */
    public static final int MY_FRAGMENT = 4;

    private SupportFragment[] mFragments = new SupportFragment[5];
    private int[] bottomBarButtonIds = new int[] {R.id.btn_home, R.id.btn_message, R.id.btn_circle,
                                                    R.id.btn_cart, R.id.btn_my};

    /**
     * 當前正在顯示的Fragment的下標
     */
    private int selectedFragmentIndex = HOME_FRAGMENT;

    private static MainFragment instance;


    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        instance = fragment;
        return fragment;
    }


    public static MainFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int id : bottomBarButtonIds) {
            Util.setOnClickListener(view, id, this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportFragment homeFragment = findChildFragment(HomeFragment.class);

        if (homeFragment == null) {
            mFragments[HOME_FRAGMENT] = HomeFragment.newInstance();
            mFragments[MESSAGE_FRAGMENT] = MessageFragment.newInstance();
            mFragments[CIRCLE_FRAGMENT] = CircleFragment.newInstance();
            mFragments[CART_FRAGMENT] = CartFragment.newInstance();
            mFragments[MY_FRAGMENT] = MyFragment.newInstance();


            loadMultipleRootFragment(R.id.fl_tab_container, selectedFragmentIndex,
                    mFragments[HOME_FRAGMENT],
                    mFragments[MESSAGE_FRAGMENT],
                    mFragments[CIRCLE_FRAGMENT],
                    mFragments[CART_FRAGMENT],
                    mFragments[MY_FRAGMENT]);
        } else {
            mFragments[HOME_FRAGMENT] = homeFragment;
            mFragments[MESSAGE_FRAGMENT] = findChildFragment(MessageFragment.class);
            mFragments[CIRCLE_FRAGMENT] = findChildFragment(CircleFragment.class);
            mFragments[CART_FRAGMENT] = findChildFragment(CartFragment.class);
            mFragments[MY_FRAGMENT] = findChildFragment(MyFragment.class);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        int len = bottomBarButtonIds.length;
        // 想要选中的Fragment的下标
        int index = -1;
        for (int i = 0; i < len; i++) {
            if (id == bottomBarButtonIds[i]) {
                index = i;
            }
        }

        SLog.info("index[%d], selectedFragmentIndex[%d]", index, selectedFragmentIndex);
        // 如果index不等于-1，表示是按下BottomBar的按鈕
        if (index != -1) {
            if (index == selectedFragmentIndex) {
                // 已經是當前Fragment，返回
                return;
            }
            showHideFragment(mFragments[index], mFragments[selectedFragmentIndex]);
            selectedFragmentIndex = index;
        }
    }
}
