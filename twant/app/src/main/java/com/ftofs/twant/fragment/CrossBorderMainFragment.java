package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.CrossBorderActivityGoodsAdapter;
import com.ftofs.twant.adapter.CrossBorderCategoryListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.BargainItem;
import com.ftofs.twant.entity.CrossBorderActivityGoods;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderCategoryItem;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderShoppingZoneItem;
import com.ftofs.twant.entity.Store;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;


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

        rvCategoryList = view.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        categoryListAdapter = new CrossBorderCategoryListAdapter(_mActivity, R.layout.cross_border_category_item, categoryList);
        categoryListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CrossBorderCategoryItem item = categoryList.get(position);
                SLog.info("position[%d], item[%s]", position, item);

                viewPager.setCurrentItem(position);
            }
        });
        rvCategoryList.setAdapter(categoryListAdapter);


        viewPager = view.findViewById(R.id.vp_category_list);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_back, this);

        loadData();
    }

    private void initViewPager(EasyJSONObject responseObj) {
        try {
            // 獲取Banner圖數據
            List<CrossBorderBannerItem> bannerItemList = new ArrayList<>();
            EasyJSONArray bannerArray = responseObj.getSafeArray("datas.bannerList");
            for (Object object : bannerArray) {
                CrossBorderBannerItem bannerItem = (CrossBorderBannerItem) EasyJSONBase.jsonDecode(CrossBorderBannerItem.class, object.toString());
                bannerItemList.add(bannerItem);
            }

            // 獲取導航區數據
            List<CrossBorderNavItem> navItemList = new ArrayList<>();
            EasyJSONArray navArray = responseObj.getSafeArray("datas.navList");
            for (Object object : navArray) {
                CrossBorderNavItem navItem = (CrossBorderNavItem) EasyJSONBase.jsonDecode(CrossBorderNavItem.class, object.toString());
                SLog.info("navItem[%s]", navItem);
                navItemList.add(navItem);
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
            fragmentList.add(CrossBorderHomeFragment.newInstance(bannerItemList, navItemList, shoppingZoneList, bargainGoodsList, groupGoodsList, storeList));
            categoryList.add(new CrossBorderCategoryItem(0, "首頁", "#019AA7"));

            EasyJSONArray catList = responseObj.getSafeArray("datas.catList");
            for (Object object : catList) {
                CrossBorderCategoryItem item = (CrossBorderCategoryItem) EasyJSONBase.jsonDecode(CrossBorderCategoryItem.class, object.toString());

                titleList.add(item.catName);
                fragmentList.add(CrossBorderCategoryFragment.newInstance(item.categoryId, item.catName));
                categoryList.add(item);
            }
            categoryListAdapter.setNewData(categoryList);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_test) {
            // initViewPager();
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
}
