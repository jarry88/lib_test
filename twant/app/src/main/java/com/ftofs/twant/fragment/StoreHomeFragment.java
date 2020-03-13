package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.FeaturesGoodsAdapter;
import com.ftofs.twant.adapter.StoreGoodsListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.entity.StoreGoodsItem;
import com.ftofs.twant.entity.StoreGoodsPair;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.LockableNestedScrollView;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * @author gzp
 */
public class StoreHomeFragment extends ScrollableBaseFragment {
    List<StoreGoodsItem> storeGoodsItemList;
    RecyclerView rvFeaturesGoodsList;
    LinearLayout llFeaturesGoodsContainer;
    PagerSnapHelper pagerSnapHelper;
    LinearLayoutManager featuresGoodsLayoutManager;

    LinearLayout llNewInItemList;
    LinearLayout llHotItemList;
    List<StoreGoodsPair> storeHotItemList = new ArrayList<>();
    List<StoreGoodsPair> storeNewInItemList = new ArrayList<>();
    private LockableNestedScrollView llContainer;

    public static StoreHomeFragment newInstance() {
        StoreHomeFragment fragment = new StoreHomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_home_item, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llContainer = view.findViewById(R.id.ll_container);
        llFeaturesGoodsContainer = view.findViewById(R.id.ll_features_goods_container);
        rvFeaturesGoodsList = view.findViewById(R.id.rv_features_goods_list);
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvFeaturesGoodsList);
        SLog.info("__featuresGoodsLayoutManager");
        featuresGoodsLayoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvFeaturesGoodsList.setLayoutManager(featuresGoodsLayoutManager);
        SLog.info("__featuresGoodsLayoutManager");

        llNewInItemList = view.findViewById(R.id.ll_new_in_item_list);
        llHotItemList = view.findViewById(R.id.ll_hot_item_list);
        // rvFeaturesGoodsList.setAdapter(new TestAdapter());
        // 使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示
        // https://www.jianshu.com/p/e54db232df62
    }

    public void setGoodsListDate(EasyJSONArray featuresGoodsVoList) {
        //顯示鎮店之寶
        List<StoreGoodsItem> storeGoodsItemList = new ArrayList<>();
        for (Object object : featuresGoodsVoList) {
            EasyJSONObject featuresGoodsVo = (EasyJSONObject) object;
            StoreGoodsItem storeGoodsItem = new StoreGoodsItem();

            try {
                storeGoodsItem.commonId = featuresGoodsVo.getInt("commonId");
                storeGoodsItem.imageSrc = featuresGoodsVo.getSafeString("imageSrc");
                storeGoodsItem.goodsName = featuresGoodsVo.getSafeString("goodsName");
                storeGoodsItem.jingle = featuresGoodsVo.getSafeString("jingle");
                storeGoodsItem.price = Util.getSpuPrice(featuresGoodsVo);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

            storeGoodsItemList.add(storeGoodsItem);
        }
        llFeaturesGoodsContainer.setVisibility(View.VISIBLE);
        FeaturesGoodsAdapter featuresGoodsAdapter = new FeaturesGoodsAdapter(_mActivity, storeGoodsItemList);
        rvFeaturesGoodsList.setAdapter(featuresGoodsAdapter);

        rvFeaturesGoodsList.postDelayed(new Runnable() {
            @Override
            public void run() {
                int targetPosition = Integer.MAX_VALUE / 2;
                rvFeaturesGoodsList.scrollToPosition(targetPosition);
                                    /*
                                    解決PagerSnapHelper的scrollToPosition不能居中的問題
                                    https://stackoverflow.com/questions/42988016/how-to-programmatically-snap-to-position-on-recycler-view-with-linearsnaphelper
                                     */
                rvFeaturesGoodsList.post(new Runnable() {
                    @Override
                    public void run() {
                        SLog.info("featuresGoodsLayoutManager[%s]",featuresGoodsLayoutManager.toString());

                        View view = featuresGoodsLayoutManager.findViewByPosition(targetPosition);

                        SLog.info("featuresGoodsLayoutManagerView[%s]",featuresGoodsLayoutManager.toString());
                        if (view == null) {
                            SLog.info("Error!Cant find target View for initial Snap");
                            return;
                        }

                        int[] snapDistance = pagerSnapHelper.calculateDistanceToFinalSnap(featuresGoodsLayoutManager, view);
                        if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                            rvFeaturesGoodsList.scrollBy(snapDistance[0], snapDistance[1]);
                        }
                    }
                });
            }
        }, 50);

    }



    public void newGoodsListData(EasyJSONArray newGoodsVoList) {
        if (newGoodsVoList!= null && newGoodsVoList.length() > 0) {
            int index = 0;
            StoreGoodsPair storeGoodsPair = null;
            for (Object object : newGoodsVoList) {
                EasyJSONObject easyJSONObject = (EasyJSONObject) object;

                StoreGoodsItem storeGoodsItem = new StoreGoodsItem();
                try {
                    storeGoodsItem.commonId = easyJSONObject.getInt("commonId");
                    storeGoodsItem.imageSrc = easyJSONObject.getSafeString("imageSrc");
                    storeGoodsItem.goodsName = easyJSONObject.getSafeString("goodsName");
                    storeGoodsItem.jingle = easyJSONObject.getSafeString("jingle");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
                storeGoodsItem.price = Util.getSpuPrice(easyJSONObject);

                if (index % 2 == 0) {
                    storeGoodsPair = new StoreGoodsPair(StoreGoodsPair.TYPE_NEW);
                    storeNewInItemList.add(storeGoodsPair);

                    storeGoodsPair.leftItem = storeGoodsItem;
                } else {
                    storeGoodsPair.rightItem = storeGoodsItem;
                }

                ++index;
            }
            StoreGoodsListAdapter newInGoodsAdapter = new StoreGoodsListAdapter(_mActivity, llNewInItemList, R.layout.store_goods_list_item);
            newInGoodsAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
                @Override
                public void onClick(ViewGroupAdapter adapter, View view, int position) {
                    SLog.info("onClick");
                    StoreGoodsPair pair = storeNewInItemList.get(position);
                    int commonId;
                    int id = view.getId();
                    if (id == R.id.ll_left_item_container) {
                        if (pair.leftItem == null) {
                            return;
                        }
                        commonId = pair.leftItem.commonId;
                    } else {
                        if (pair.rightItem == null) {
                            return;
                        }
                        commonId = pair.rightItem.commonId;
                    }

                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                }
            });
            newInGoodsAdapter.setData(storeNewInItemList);
        }

    }

    public void hotGoodsListData(EasyJSONArray hotGoodsVoList) {
        int index = 0;
        StoreGoodsPair storeGoodsPair = null;
        for (Object object : hotGoodsVoList) {
            EasyJSONObject easyJSONObject = (EasyJSONObject) object;

            StoreGoodsItem storeGoodsItem = new StoreGoodsItem();
            try {
                storeGoodsItem.commonId = easyJSONObject.getInt("commonId");
                storeGoodsItem.imageSrc = easyJSONObject.getSafeString("imageSrc");
                storeGoodsItem.goodsName = easyJSONObject.getSafeString("goodsName");
                storeGoodsItem.jingle = easyJSONObject.getSafeString("jingle");
                storeGoodsItem.price = Util.getSpuPrice(easyJSONObject);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

            if (index % 2 == 0) {
                storeGoodsPair = new StoreGoodsPair(StoreGoodsPair.TYPE_HOT);
                storeHotItemList.add(storeGoodsPair);

                storeGoodsPair.leftItem = storeGoodsItem;
            } else {
                storeGoodsPair.rightItem = storeGoodsItem;
            }

            ++index;
        }

        StoreGoodsListAdapter hotGoodsAdapter = new StoreGoodsListAdapter(_mActivity, llHotItemList, R.layout.store_goods_list_item);
        hotGoodsAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                SLog.info("onClick");
                StoreGoodsPair pair = storeHotItemList.get(position);
                int commonId;
                int id = view.getId();
                if (id == R.id.ll_left_item_container) {
                    if (pair.leftItem == null) {
                        return;
                    }
                    commonId = pair.leftItem.commonId;
                } else {
                    if (pair.rightItem == null) {
                        return;
                    }
                    commonId = pair.rightItem.commonId;
                }

                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            }
        });
        hotGoodsAdapter.setData(storeHotItemList);
    }

    @Override
    public void setScrollable(boolean scrollable) {
        llContainer.setScrollable(scrollable);
    }
}
