package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.CrossBorderCategoryListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.entity.CrossBorderActivityGoods;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderCategoryItem;
import com.ftofs.twant.entity.CrossBorderFloor;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderNavPane;
import com.ftofs.twant.entity.CrossBorderShoppingZoneItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.FloorItem;
import com.ftofs.twant.entity.Store;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UmengAnalytics;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    LinearLayout llMoreCategoryMenuContainer;
    LinearLayout llLessCategoryMenuContainer;

    ViewPager viewPager;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    LinearLayout llAppBar;

    View crossBorderCategoryListMask;
    View btnViewMoreCategory;
    String homeDefaultColorStr = "";

    int selCategoryMenuIndex = 0; // 當前選中的分類菜單的索引
    LinearLayout llTextRuler;

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

        llMoreCategoryMenuContainer = view.findViewById(R.id.ll_more_category_menu_container);
        llLessCategoryMenuContainer = view.findViewById(R.id.ll_less_category_menu_container);

        llTextRuler = view.findViewById(R.id.ll_text_ruler);

        crossBorderCategoryListMask = view.findViewById(R.id.cross_border_category_list_mask);
        btnViewMoreCategory = view.findViewById(R.id.btn_view_more_category);

        llAppBar = view.findViewById(R.id.ll_app_bar);

        rvCategoryList = view.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        categoryListAdapter = new CrossBorderCategoryListAdapter(_mActivity, R.layout.cross_border_category_item, categoryList);
        categoryListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("onItemClick, position[%d]", position);
                handleCategoryMenuSelected(position);
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
                    String colorStr = Hawk.get(SPField.FIELD_CURR_CROSS_BORDER_THEME_COLOR);
                    if (!StringUtil.isEmpty(colorStr)) {
                        changeBackgroundColor(Color.parseColor(colorStr));
                    }
                } else {
                    changeBackgroundColor(Color.parseColor(item.backgroundColor));
                }


                int prevSelectedIndex = categoryListAdapter.getSelectedIndex();
                categoryListAdapter.setSelectedIndex(position);
                categoryListAdapter.notifyItemChanged(prevSelectedIndex);
                categoryListAdapter.notifyItemChanged(position);

                TextView prevTextView = (TextView) llLessCategoryMenuContainer.getChildAt(selCategoryMenuIndex);
                prevTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                selCategoryMenuIndex = position;
                TextView currTextView = (TextView) llLessCategoryMenuContainer.getChildAt(selCategoryMenuIndex);
                currTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search, this);

        // 先設置默認顏色
        changeBackgroundColor(Util.getColor(R.color.tw_blue));

        loadData();
    }

    private void handleCategoryMenuSelected(int position) {
        CrossBorderCategoryItem item = categoryList.get(position);
        SLog.info("position[%d], item[%s]", position, item);

        HashMap<String, Object> analyticsDataMap = new HashMap<>();
        if (position == 0) {
            UmengAnalytics.onEventObject(UmengAnalyticsActionName.GO_TARIFF_BUY, analyticsDataMap);
        } else {
            analyticsDataMap.put("categoryId", item.categoryId);
            UmengAnalytics.onEventObject(UmengAnalyticsActionName.TARIFF_BUY_CATEGORY, analyticsDataMap);
        }

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
                    int color = Color.parseColor(bannerItem.backgroundColorApp);
                    changeBackgroundColor(color);
                    homeDefaultColorStr = bannerItem.backgroundColorApp;
                    SLog.info("homeDefaultColorStr[%s]", homeDefaultColorStr);
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

            // 獲取秒殺數據
            long secKillCountDown = responseObj.optLong("datas.secKillCountDown");
            List<CrossBorderActivityGoods> secKillGoodsList = new ArrayList<>();
            EasyJSONArray secKillGoodsArray = responseObj.getSafeArray("datas.secKillGoodsList");
            for (Object object : secKillGoodsArray) {
                EasyJSONObject secKillGoodsObject = (EasyJSONObject) object;

                CrossBorderActivityGoods secKillGoods = new CrossBorderActivityGoods(
                        Constant.PROMOTION_TYPE_SEC_KILL,
                        secKillGoodsObject.optInt("goodsId"),
                        secKillGoodsObject.optInt("commonId"),
                        secKillGoodsObject.optString("imageSrc"),
                        secKillGoodsObject.optString("goodsName"),
                        secKillGoodsObject.optDouble("bottomPrice")
                );

                secKillGoodsList.add(secKillGoods);
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

            // 獲取樓層數據
            List<CrossBorderFloor> floorList = new ArrayList<>();
            EasyJSONArray floorArray = responseObj.getSafeArray("datas.floorList");
            for (Object object : floorArray) {
                EasyJSONObject floorObject = (EasyJSONObject) object;
                CrossBorderFloor floor = new CrossBorderFloor();
                floor.floorId = floorObject.optInt("floorId");
                floor.floorHeadline = floorObject.optString("floorHeadline");
                floor.floorSubhead = floorObject.optString("floorSubhead");
                floor.floorType = floorObject.optString("floorType");

                EasyJSONArray imgVoList = floorObject.getSafeArray("imgVoList");
                for (Object object2 : imgVoList) {
                    FloorItem floorItem = (FloorItem) EasyJSONBase.jsonDecode(FloorItem.class, object2.toString());
                    floor.floorItemList.add(floorItem);
                }
                floorList.add(floor);
            }


            // 獲取【優選好店】數據
            List<Store> storeList = new ArrayList<>();
            EasyJSONArray storeArray = responseObj.getSafeArray("datas.storeList");
            for (Object object : storeArray) {
                Store store = (Store) EasyJSONBase.jsonDecode(Store.class, object.toString());
                storeList.add(store);
            }

            titleList.add("首頁");
            fragmentList.add(CrossBorderHomeFragment.newInstance(bannerItemList, homeDefaultColorStr, navItemCount, navPaneList, shoppingZoneList, secKillCountDown, secKillGoodsList, bargainGoodsList,
                    groupGoodsList, floorList, storeList));
            categoryList.add(new CrossBorderCategoryItem(0, "首頁", homeDefaultColorStr));

            EasyJSONArray catList = responseObj.getSafeArray("datas.catList");
            SLog.info("catList.length[%d]", catList.length());
            viewPager.setOffscreenPageLimit(catList.length());  // 不要减1，因为额外加了【首页】
            for (Object object : catList) {
                CrossBorderCategoryItem item = (CrossBorderCategoryItem) EasyJSONBase.jsonDecode(CrossBorderCategoryItem.class, object.toString());

                titleList.add(item.catName);
                fragmentList.add(CrossBorderCategoryFragment.newInstance(item.categoryId, item.catName));
                categoryList.add(item);

                if (BuildConfig.DEBUG) {
                    if (categoryList.size() >= 3) {
                        break;
                    }
                }
            }

            // int margin = Util.dip2px(_mActivity, 15); //
            for (int i = 0; i < categoryList.size(); i++) {
                CrossBorderCategoryItem item = categoryList.get(i);
                TextView textView = new TextView(_mActivity);
                textView.setTextSize(15);
                textView.setTextColor(Color.WHITE);
                textView.setText(item.catName);
                textView.setGravity(Gravity.CENTER);
                textView.setTag(R.id.data_index, i);
                if (i == 0) {
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag(R.id.data_index);
                        if (position == selCategoryMenuIndex) {
                            return;
                        }

                        TextView tvCatPrev = (TextView) llLessCategoryMenuContainer.getChildAt(selCategoryMenuIndex);
                        tvCatPrev.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                        SLog.info("selCategoryMenuIndex[%d], position[%d]", selCategoryMenuIndex, position);
                        selCategoryMenuIndex = position;
                        TextView tvCatCurr = (TextView) llLessCategoryMenuContainer.getChildAt(selCategoryMenuIndex);
                        tvCatCurr.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                        handleCategoryMenuSelected(position);
                    }
                });

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 1;

                llLessCategoryMenuContainer.addView(textView, layoutParams);

                /*
                int margin = Util.dip2px(_mActivity, 15);
                TextView tvRuler = new TextView(_mActivity);
                tvRuler.setTextSize(15);
                tvRuler.setText(item.catName);
                LinearLayout.LayoutParams rulerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rulerParams.leftMargin = margin;
                rulerParams.rightMargin = margin;

                llTextRuler.addView(textView, layoutParams);
                *
                 */
            }

            for (int i = 0; i < categoryList.size(); i++) {
                CrossBorderCategoryItem item = categoryList.get(i);
                int margin = Util.dip2px(_mActivity, 15);
                TextView tvRuler = new TextView(_mActivity);
                tvRuler.setTextSize(15);
                tvRuler.setText(item.catName);
                LinearLayout.LayoutParams rulerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rulerParams.leftMargin = margin;
                rulerParams.rightMargin = margin;

                llTextRuler.addView(tvRuler, rulerParams);
            }


            llTextRuler.post(new Runnable() {
                @Override
                public void run() {
                    Pair<Integer, Integer> dimension = Util.getScreenDimension(_mActivity);
                    int rulerWidth = llTextRuler.getWidth();
                    int screenWidth = dimension.first;
                    SLog.info("screenWidth[%d], rulerWidth[%d]", screenWidth, rulerWidth);

                    if (rulerWidth < screenWidth) {
                        llLessCategoryMenuContainer.setVisibility(View.VISIBLE);
                        llMoreCategoryMenuContainer.setVisibility(View.GONE);
                    } else {
                        llLessCategoryMenuContainer.setVisibility(View.GONE);
                        llMoreCategoryMenuContainer.setVisibility(View.VISIBLE);
                    }

                    llTextRuler.setVisibility(View.GONE);
                }
            });

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
        String apiUrl = Api.PATH_TARIFF_BUY_INDEX;
        if (BuildConfig.DEBUG) {
            apiUrl = "https://gogo.so/tmp/2.json";
        }
        String url = apiUrl;


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
            changeBackgroundColor(Color.parseColor(colorStr));
        }
    }

    private void changeBackgroundColor(int color) {
        llAppBar.setBackgroundColor(color);
        btnViewMoreCategory.setBackgroundColor(color);
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
