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
 * 店鋪主Fragment容器
 * @author zwm
 */
public class ShopMainFragment extends BaseFragment implements View.OnClickListener {
    /** 首頁 */
    public static final int HOME_FRAGMENT = 0;
    /** 商品 */
    public static final int COMMODITY_FRAGMENT = 1;
    /** 分類 */
    public static final int CATEGORY_FRAGMENT = 2;
    /** 活動 */
    public static final int ACTIVITY_FRAGMENT = 3;
    /** 客服  */
    public static final int CUSTOMER_SERVICE_FRAGMENT = 4;

    private SupportFragment[] mFragments = new SupportFragment[5];
    private int[] bottomBarButtonIds = new int[] {R.id.btn_home, R.id.btn_commodity, R.id.btn_category,
            R.id.btn_activity, R.id.btn_customer_service};

    /**
     * 當前正在顯示的Fragment的下標
     */
    private int selectedFragmentIndex = HOME_FRAGMENT;

    public static ShopMainFragment newInstance() {
        Bundle args = new Bundle();

        ShopMainFragment fragment = new ShopMainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_main, container, false);
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

        SupportFragment homeFragment = findChildFragment(ShopHomeFragment.class);

        if (homeFragment == null) {
            mFragments[HOME_FRAGMENT] = ShopHomeFragment.newInstance();
            mFragments[COMMODITY_FRAGMENT] = ShopCommodityFragment.newInstance();
            mFragments[CATEGORY_FRAGMENT] = ShopCategoryFragment.newInstance();
            mFragments[ACTIVITY_FRAGMENT] = ShopActivityFragment.newInstance();
            mFragments[CUSTOMER_SERVICE_FRAGMENT] = ShopCustomerServiceFragment.newInstance();


            loadMultipleRootFragment(R.id.fl_tab_container, selectedFragmentIndex,
                    mFragments[HOME_FRAGMENT],
                    mFragments[COMMODITY_FRAGMENT],
                    mFragments[CATEGORY_FRAGMENT],
                    mFragments[ACTIVITY_FRAGMENT],
                    mFragments[CUSTOMER_SERVICE_FRAGMENT]);
        } else {
            mFragments[HOME_FRAGMENT] = homeFragment;
            mFragments[COMMODITY_FRAGMENT] = findChildFragment(ShopCommodityFragment.class);
            mFragments[CATEGORY_FRAGMENT] = findChildFragment(ShopCategoryFragment.class);
            mFragments[ACTIVITY_FRAGMENT] = findChildFragment(ShopActivityFragment.class);
            mFragments[CUSTOMER_SERVICE_FRAGMENT] = findChildFragment(ShopCustomerServiceFragment.class);
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
