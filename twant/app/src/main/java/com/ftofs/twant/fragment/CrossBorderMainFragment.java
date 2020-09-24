package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.CrossBorderActivityGoodsAdapter;
import com.ftofs.twant.adapter.CrossBorderCategoryListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.BargainItem;
import com.ftofs.twant.entity.CrossBorderActivityGoods;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderCategoryItem;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderNavPane;
import com.ftofs.twant.entity.CrossBorderShoppingZoneItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.Store;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.CrossBorderDrawView;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 跨城購承載頁
 * @author zwm
 */
public class CrossBorderMainFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvCategoryList;
    CrossBorderCategoryListAdapter categoryListAdapter;
    List<CrossBorderCategoryItem> categoryList = new ArrayList<>();

    ViewPager viewPager;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    LinearLayout llAppBar;
    View vwTopBg;
    CrossBorderDrawView vwBottomBg;
    public int homeBgColor; // 首頁頂部背景色

    View crossBorderCategoryListMask;
    View btnViewMoreCategory;

    public static CrossBorderMainFragment newInstance() {
        CrossBorderMainFragment fragment = new CrossBorderMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cross_border_main, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        crossBorderCategoryListMask = view.findViewById(R.id.cross_border_category_list_mask);
        btnViewMoreCategory = view.findViewById(R.id.btn_view_more_category);

        llAppBar = view.findViewById(R.id.ll_app_bar);
        vwTopBg = view.findViewById(R.id.vw_top_bg);
        vwBottomBg = view.findViewById(R.id.vw_bottom_bg);

        rvCategoryList = view.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        categoryListAdapter = new CrossBorderCategoryListAdapter(_mActivity, R.layout.cross_border_category_item, categoryList);
        categoryListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CrossBorderCategoryItem item = categoryList.get(position);
                SLog.info("position[%d], item[%s]", position, item);

                int prevSelectedIndex = categoryListAdapter.getSelectedIndex();
                if (position == prevSelectedIndex) {
                    return;
                }

                categoryListAdapter.setSelectedIndex(position);
                categoryListAdapter.notifyItemChanged(prevSelectedIndex);
                categoryListAdapter.notifyItemChanged(position);

                viewPager.setCurrentItem(position);

                changeBackgroundColor(Color.parseColor(item.backgroundColor));
            }
        });
        rvCategoryList.setAdapter(categoryListAdapter);


        viewPager = view.findViewById(R.id.vp_category_list);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CrossBorderCategoryItem item = categoryList.get(position);
                SLog.info("position[%d], item[%s]", position, item);

                if (position == 0) { // 如果是回到首頁，恢復原先的顏色
                    changeBackgroundColor(homeBgColor);
                } else {
                    changeBackgroundColor(Color.parseColor(item.backgroundColor));
                }


                int prevSelectedIndex = categoryListAdapter.getSelectedIndex();
                categoryListAdapter.setSelectedIndex(position);
                categoryListAdapter.notifyItemChanged(prevSelectedIndex);
                categoryListAdapter.notifyItemChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search, this);

        // 先設置默認顏色
        changeBackgroundColor(_mActivity.getColor(R.color.tw_cross_border_home_page_bg_color));

        loadData();
    }

    private void initViewPager(EasyJSONObject responseObj) {
        try {
            // 獲取Banner圖數據
            int index = 0;
            List<CrossBorderBannerItem> bannerItemList = new ArrayList<>();
            EasyJSONArray bannerArray = responseObj.getSafeArray("datas.bannerList");
            for (Object object : bannerArray) {
                CrossBorderBannerItem bannerItem = (CrossBorderBannerItem) EasyJSONBase.jsonDecode(CrossBorderBannerItem.class, object.toString());
                bannerItemList.add(bannerItem);
                if (index == 0) {
                    homeBgColor = Color.parseColor(bannerItem.backgroundColorApp);
                    changeBackgroundColor(homeBgColor);
                }

                index++;
            }

            // 獲取導航區數據
            CrossBorderNavPane navPane = null;
            List<CrossBorderNavPane> navPaneList = new ArrayList<>();
            EasyJSONArray navArray = responseObj.getSafeArray("datas.navList");
            int navItemCount = 0;
            for (Object object : navArray) {
                if (navItemCount % 10 == 0) {
                    navPane = new CrossBorderNavPane();
                    navPaneList.add(navPane);
                }

                CrossBorderNavItem navItem = (CrossBorderNavItem) EasyJSONBase.jsonDecode(CrossBorderNavItem.class, object.toString());
                navPane.crossBorderNavItemList.add(navItem);

                navItemCount++;
            }

            // 獲取購物專場數據
            List<CrossBorderShoppingZoneItem> shoppingZoneList = new ArrayList<>();
            EasyJSONArray shoppingZoneArray = responseObj.getSafeArray("datas.zoneList");
            for (Object object : shoppingZoneArray) {
                CrossBorderShoppingZoneItem shoppingZoneItem = (CrossBorderShoppingZoneItem) EasyJSONBase.jsonDecode(CrossBorderShoppingZoneItem.class, object.toString());
                shoppingZoneList.add(shoppingZoneItem);
            }

            // 獲取砍價數據
            List<CrossBorderActivityGoods> bargainGoodsList = new ArrayList<>();
            EasyJSONArray bargainGoodsArray = responseObj.getSafeArray("datas.bargainGoodsList");
            for (Object object : bargainGoodsArray) {
                EasyJSONObject bargainGoodsObject = (EasyJSONObject) object;

                CrossBorderActivityGoods bargainGoods = new CrossBorderActivityGoods(
                        Constant.PROMOTION_TYPE_BARGAIN,
                        bargainGoodsObject.optInt("goodsId"),
                        bargainGoodsObject.optInt("commonId"),
                        bargainGoodsObject.optString("imageSrc"),
                        bargainGoodsObject.optString("goodsName"),
                        bargainGoodsObject.optDouble("bottomPrice")
                );

                bargainGoods.bargainId = bargainGoodsObject.optInt("bargainId");
                bargainGoodsList.add(bargainGoods);
            }

            // 獲取拼團數據
            List<CrossBorderActivityGoods> groupGoodsList = new ArrayList<>();
            EasyJSONArray groupGoodsArray = responseObj.getSafeArray("datas.groupGoodsList");
            for (Object object : groupGoodsArray) {
                EasyJSONObject groupGoodsObject = (EasyJSONObject) object;

                CrossBorderActivityGoods groupGoods = new CrossBorderActivityGoods(
                        Constant.PROMOTION_TYPE_GROUP,
                        groupGoodsObject.optInt("goodsId"),
                        groupGoodsObject.optInt("commonId"),
                        groupGoodsObject.optString("imageName"),
                        groupGoodsObject.optString("goodsName"),
                        groupGoodsObject.optDouble("groupPrice")
                );

                groupGoods.groupId = groupGoodsObject.optInt("groupId");
                groupGoodsList.add(groupGoods);
            }

            // 獲取【優選好店】數據
            List<Store> storeList = new ArrayList<>();
            EasyJSONArray storeArray = responseObj.getSafeArray("datas.storeList");
            for (Object object : storeArray) {
                Store store = (Store) EasyJSONBase.jsonDecode(Store.class, object.toString());
                storeList.add(store);
            }

            titleList.add("首頁");
            fragmentList.add(CrossBorderHomeFragment.newInstance(bannerItemList, navItemCount, navPaneList, shoppingZoneList, bargainGoodsList, groupGoodsList, storeList));
            categoryList.add(new CrossBorderCategoryItem(0, "首頁", "#019AA7"));

            EasyJSONArray catList = responseObj.getSafeArray("datas.catList");
            SLog.info("catList.length[%d]", catList.length());
            viewPager.setOffscreenPageLimit(catList.length());  // 不要减1，因为额外加了【首页】
            for (Object object : catList) {
                CrossBorderCategoryItem item = (CrossBorderCategoryItem) EasyJSONBase.jsonDecode(CrossBorderCategoryItem.class, object.toString());

                titleList.add(item.catName);
                fragmentList.add(CrossBorderCategoryFragment.newInstance(item.categoryId, item.catName));
                categoryList.add(item);
            }
            categoryListAdapter.setShowViewMore(categoryList.size() > 5);
            showViewMoreCategoryIndicator(categoryList.size() > 5);
            categoryListAdapter.setNewData(categoryList);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);
    }

    private void showViewMoreCategoryIndicator(boolean show) {
        btnViewMoreCategory.setVisibility(show ? View.VISIBLE : View.GONE);
        crossBorderCategoryListMask.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void loadData() {
        String url = Api.PATH_TARIFF_BUY_INDEX;
        // String url = "https://test.weshare.team/tmp/test4.json";

        Api.getUI(url, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, "", "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    SLog.info("responseObj[%s]", responseObj);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, "", responseStr, "");
                        return;
                    }

                    initViewPager(responseObj);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_CROSS_BORDER_HOME_THEME_COLOR) {
            String colorStr = (String) message.data;
            SLog.info("colorStr[%s]", colorStr);
            homeBgColor = Color.parseColor(colorStr);
            changeBackgroundColor(homeBgColor);
        }
    }

    private void changeBackgroundColor(int color) {
        llAppBar.setBackgroundColor(color);
        vwTopBg.setBackgroundColor(color);
        btnViewMoreCategory.setBackgroundColor(color);
        vwBottomBg.setColor(color);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_test) {
            // initViewPager();
            // changeBackgroundColor(Color.RED);
        } else if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_search) {
            Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                    EasyJSONObject.generate("keyword", "", "isFromCrossBorderHome", true).toString()));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }
}
