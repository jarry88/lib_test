package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.FeaturesGoodsAdapter;
import com.ftofs.twant.adapter.StoreGoodsListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.StoreGoodsItem;
import com.ftofs.twant.entity.StoreGoodsPair;
import com.ftofs.twant.entity.TimeInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.LockableNestedScrollView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * @author gzp
 */
public class StoreHomeFragment extends ScrollableBaseFragment implements View.OnClickListener {
    RecyclerView rvFeaturesGoodsList;
    FeaturesGoodsAdapter featuresGoodsAdapter;
    RelativeLayout rlFeaturesGoodsContainer;
    PagerSnapHelper pagerSnapHelper;
    LinearLayoutManager featuresGoodsLayoutManager;

    LinearLayout llNewInItemList;
    LinearLayout llHotItemList;
    List<StoreGoodsPair> storeHotItemList = new ArrayList<>();
    List<StoreGoodsPair> storeNewInItemList = new ArrayList<>();
    private LockableNestedScrollView llContainer;

    Timer timer;
    TimerHandler timerHandler;

    long featureGoodsNavClickTimestamp = 0; // 【鎮店之寶】最近一次點擊的時間戳
    public static final long FEATURE_GOODS_NAV_CLICK_INTERVAL = 500; // 【鎮店之寶】兩次點擊之間的最短時間間隔（毫秒）

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

        timerHandler = new TimerHandler(this);

        llContainer = view.findViewById(R.id.ll_container);


        rlFeaturesGoodsContainer = view.findViewById(R.id.rl_features_goods_container);
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

        Util.setOnClickListener(view, R.id.btn_view_prev_feature_goods, this);
        Util.setOnClickListener(view, R.id.btn_view_next_feature_goods, this);
    }

    public void setGoodsListDate(EasyJSONArray featuresGoodsVoList) {
        //顯示鎮店之寶
        List<Goods> storeGoodsItemList = new ArrayList<>();
        for (Object object : featuresGoodsVoList) {
            EasyJSONObject featuresGoodsVo = (EasyJSONObject) object;

            try {
                Goods storeGoodsItem = Goods.parse(featuresGoodsVo);
                storeGoodsItemList.add(storeGoodsItem);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

        }
        rlFeaturesGoodsContainer.setVisibility(View.VISIBLE);
        featuresGoodsAdapter = new FeaturesGoodsAdapter(_mActivity, storeGoodsItemList);
        rvFeaturesGoodsList.setAdapter(featuresGoodsAdapter);

        rvFeaturesGoodsList.postDelayed(new Runnable() {
            @Override
            public void run() {
                int targetPosition = Constant.INFINITE_LOOP_VALUE / 2;
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

    static class TimerHandler extends Handler {
        WeakReference<StoreHomeFragment> weakReference;

        public TimerHandler(StoreHomeFragment storeHomeFragment) {
            weakReference = new WeakReference<>(storeHomeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            StoreHomeFragment goodsDetailFragment = weakReference.get();
            goodsDetailFragment.scrollFeatureGoods(1);
        }
    }


    /**
     * 滾動鎮店之寶列表
     * @param direction -1 向前滾動  1 向後滾動
     */
    public void scrollFeatureGoods(int direction) {
        if (direction != -1 && direction != 1) { // 校驗取值是否有效
            return;
        }

        int position = featuresGoodsLayoutManager.findFirstCompletelyVisibleItemPosition();
        SLog.info("position[%d]", position);
        if (position == -1) {
            return;
        }
        rvFeaturesGoodsList.smoothScrollToPosition(position + direction);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        startTimer();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        stopTimer();
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }

        // 定时服务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long threadId = Thread.currentThread().getId();
                // SLog.info("threadId[%d]", threadId);
                Message message = new Message();

                if (timerHandler != null) {
                    timerHandler.sendMessage(message);
                }
            }
        }, 500, 3000);  // 0.5秒后启动，每隔3秒运行一次
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_view_prev_feature_goods || id == R.id.btn_view_next_feature_goods) {
            long now = System.currentTimeMillis();

            if (now - featureGoodsNavClickTimestamp < FEATURE_GOODS_NAV_CLICK_INTERVAL) {
                return;
            }
            featureGoodsNavClickTimestamp = now;
            if (id == R.id.btn_view_prev_feature_goods) {
                scrollFeatureGoods(-1);
            } else {
                scrollFeatureGoods(1);
            }
        }

    }
}
