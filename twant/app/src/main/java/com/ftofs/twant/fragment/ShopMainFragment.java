package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.lxj.xpopup.XPopup;

import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 店鋪主Fragment容器
 * @author zwm
 */
public class ShopMainFragment extends BaseFragment implements View.OnClickListener {
    // 店鋪Id
    int storeId;

    TextView tvShopTitle;

    // 店鋪頭像圓形按鈕
    ImageView imgBottomBarShopAvatar;

    // 店鋪名稱
    String storeName = "";

    public static final int FRAGMENT_COUNT = 5;

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
    private int[] bottomBarIconIds = new int[] {0, R.id.icon_shop_bottom_bar_commodity, R.id.icon_shop_bottom_bar_category,
            R.id.icon_shop_bottom_bar_activity, R.id.icon_shop_bottom_bar_customer_service};
    private ImageView[] bottomBarIcons = new ImageView[5];
    private int[] bottomBarIconResources = new int[] {0, R.drawable.icon_shop_bottom_bar_commodity, R.drawable.icon_shop_bottom_bar_category,
            R.drawable.icon_shop_bottom_bar_activity, R.drawable.icon_shop_bottom_bar_customer_service};
    private int[] bottomBarSelIconResources = new int[] {0, R.drawable.icon_shop_bottom_bar_commodity_sel, R.drawable.icon_shop_bottom_bar_category_sel,
            R.drawable.icon_shop_bottom_bar_activity_sel, R.drawable.icon_shop_bottom_bar_customer_service_sel};

    /**
     * 當前正在顯示的Fragment的下標
     */
    private int selectedFragmentIndex = HOME_FRAGMENT;

    public static ShopMainFragment newInstance(int shopId) {
        Bundle args = new Bundle();

        args.putInt("shopId", shopId);
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

        Bundle args = getArguments();
        storeId = args.getInt("shopId");

        tvShopTitle = view.findViewById(R.id.tv_shop_title);
        imgBottomBarShopAvatar = view.findViewById(R.id.img_bottom_bar_shop_avatar);

        for (int id : bottomBarButtonIds) {
            Util.setOnClickListener(view, id, this);
        }

        for (int i = 0; i < FRAGMENT_COUNT; i++) {
            if (i == HOME_FRAGMENT) {
                continue;
            }
            bottomBarIcons[i] = view.findViewById(bottomBarIconIds[i]);
        }


        Util.setOnClickListener(view, R.id.btn_menu, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportFragment homeFragment = findChildFragment(ShopHomeFragment.class);

        if (homeFragment == null) {
            mFragments[HOME_FRAGMENT] = ShopHomeFragment.newInstance();
            mFragments[COMMODITY_FRAGMENT] = ShopCommodityFragment.newInstance(EasyJSONObject.generate("storeId", storeId).toString());
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
        if (id == R.id.btn_back) {
            pop();
        } if (id == R.id.btn_menu) {
            SLog.info("here");
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 15))
                    .offsetY(-Util.dip2px(_mActivity, 9))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_STORE))
                    .show();
        } else { // 點擊底部導航欄
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
                onBottomBarClick(index);
            }
        }
    }

    public void onBottomBarClick(int index) {
        if (index == selectedFragmentIndex) {
            // 已經是當前Fragment，返回
            return;
        }

        // 切換底部工具欄圖標的選中狀態
        if (selectedFragmentIndex != HOME_FRAGMENT) {
            ImageView imgIcon = bottomBarIcons[selectedFragmentIndex];
            imgIcon.setImageResource(bottomBarIconResources[selectedFragmentIndex]);
        }

        if (index != HOME_FRAGMENT) {
            ImageView imgIcon = bottomBarIcons[index];
            imgIcon.setImageResource(bottomBarSelIconResources[index]);
        }

        if (index != CUSTOMER_SERVICE_FRAGMENT) {
            SLog.info("storeName[%s]", storeName);
            tvShopTitle.setText(storeName);
        }

        showHideFragment(mFragments[index], mFragments[selectedFragmentIndex]);
        selectedFragmentIndex = index;
    }

    public void setImgBottomBarShopAvatar(String url) {
        Glide.with(this).load(url).into(imgBottomBarShopAvatar);
    }

    public void setShopName(String storeName) {
        this.storeName = storeName;
        setFragmentTitle(storeName);
    }

    public void setFragmentTitle(String title) {
        tvShopTitle.setText(title);
    }

    public int getStoreId() {
        return storeId;
    }
}
