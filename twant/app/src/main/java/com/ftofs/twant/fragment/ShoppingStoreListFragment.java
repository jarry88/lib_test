package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.ShoppingStoreListAdapter;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.StoreItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.NewShoppingSpecialFragment;
import com.ftofs.twant.util.Util;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 購物專場店鋪列表子頁面
 *
 * @author gzp
 */
public class ShoppingStoreListFragment extends BaseFragment {

    private NewShoppingSpecialFragment parentFragment;
    private RecyclerView rvStoreList;
    ShoppingStoreListAdapter storeListAdapter;
    List<StoreItem> storeItems=new ArrayList<>();
    private EasyJSONArray zoneStoreVoList;

    public static ShoppingStoreListFragment newInstance() {
        ShoppingStoreListFragment fragment = new ShoppingStoreListFragment();
        return fragment;
    }
    public static ShoppingStoreListFragment newInstance(ShoppingSpecialFragment shoppingSpecialFragment) {
        ShoppingStoreListFragment fragment = new ShoppingStoreListFragment();
        fragment.parentFragment = null;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_rv_list, container, false);
        return view;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");
        if (zoneStoreVoList != null) {
            if (storeListAdapter == null) {
                storeListAdapter = new ShoppingStoreListAdapter(R.layout.store_view, storeItems);
            }
            updateStoreList(zoneStoreVoList);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvStoreList = view.findViewById(R.id.rv_simple);
        storeListAdapter = new ShoppingStoreListAdapter(R.layout.shopping_store_view, storeItems);
        storeListAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            int id = view1.getId();
            if (id == R.id.goods_image_left_container || id == R.id.goods_image_middle_container || id == R.id.goods_image_right_container) {
                int commonId;
                if (id == R.id.goods_image_middle_container) {
                    commonId = storeItems.get(position).goodsList.get(0).id;
                } else if (id == R.id.goods_image_left_container) {
                    commonId = storeItems.get(position).goodsList.get(1).id;
                } else {
                    commonId = storeItems.get(position).goodsList.get(2).id;
                }

                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            }
        });
        storeListAdapter.setOnItemClickListener((adapter, view1, position)->{
            int storeId = storeItems.get(position).storeId;
            if (Config.PROD) {
                HashMap<String, Object> analyticsDataMap = new HashMap<>();
                analyticsDataMap.put("storeId", storeId);
                MobclickAgent.onEventObject(TwantApplication.getInstance(), UmengAnalyticsActionName.ACTIVITY_STORE, analyticsDataMap);
            }
            Util.startFragment(ShopMainFragment.newInstance(storeId));
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        rvStoreList.setLayoutManager(linearLayoutManager);
        addOnNestedScroll();
        rvStoreList.setAdapter(storeListAdapter);
    }

    public void setStoreList(EasyJSONArray zoneStoreVoList) {
        this.zoneStoreVoList = zoneStoreVoList;
    }
    private void updateStoreList(EasyJSONArray zoneStoreVoList) {
        storeItems.clear();
        SLog.info(" updateStoreList");
        try {
            for (Object object : zoneStoreVoList) {
                EasyJSONObject store = (EasyJSONObject) object;
                StoreItem storeItem =new StoreItem();
                storeItem.storeFigureImage=store.getSafeString("storeFigureImage");
                storeItem.storeAvatar=store.getSafeString("storeAvatar");
                storeItem.storeName=store.getSafeString("storeName");
                storeItem.storeId=store.getInt("storeId");
                storeItem.storeClass = store.getSafeString("className");
                EasyJSONArray zoneGoodsVoList = store.getSafeArray("zoneGoodsVoList");
                for (Object object1 : zoneGoodsVoList) {
                    EasyJSONObject goods = (EasyJSONObject) object1;
                    storeItem.goodsList.add(Goods.parse(goods));
                }
                storeItems.add(storeItem);
            }
            storeListAdapter.setNewData(storeItems);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

    }

    public void setNestedScrollingEnabled(boolean b) {
        if (rvStoreList != null) {
            rvStoreList.setNestedScrollingEnabled(b);
            SLog.info("設置店鋪列表頁面滾動[%s]",b);
        }

    }

    public void addOnNestedScroll() {
        rvStoreList.setNestedScrollingEnabled(false);
        rvStoreList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {

                    parentFragment.onCbStartNestedScroll();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    parentFragment.onCbStopNestedScroll();

                }
            }
        });
    }

    public void setOnNestedScroll(NewShoppingSpecialFragment newShoppingSpecialFragment) {
        this.parentFragment = newShoppingSpecialFragment;
    }

    public void scrollToTop() {
        if (rvStoreList != null) {
            rvStoreList.scrollToPosition(0);
            rvStoreList.setNestedScrollingEnabled(false);
        }
    }
}
