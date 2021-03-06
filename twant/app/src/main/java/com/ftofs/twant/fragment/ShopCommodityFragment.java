package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ShopGoodsGridAdapter;
import com.ftofs.twant.adapter.ShopGoodsListAdapter;
import com.ftofs.twant.adapter.StoreCategoryListAdapter;
import com.ftofs.twant.adapter.VideoListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.lib_net.model.StoreLabel;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.entity.GoodsPair;
import com.ftofs.twant.entity.VideoItem;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.CustomerLinearLayoutManager;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商店產品Fragment
 * @author zwm
 */
public class ShopCommodityFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    public static final int ANIM_COUNT = 2;
    ShopMainFragment parentFragment;

    RecyclerView rvGoodsList;
    ImageView imgPriceOrderIndicator;

    ShopGoodsListAdapter shopGoodsListAdapter;
    ShopGoodsGridAdapter shopGoodsGridAdapter;
    LinearLayoutManager layoutManager;
    boolean firstShow = true;


    float lastX = 0;
    float lastY = 0;
    boolean isUp;

    StoreCategoryListAdapter storeCategoryListAdapter;

    List<Goods> goodsList = new ArrayList<>();  // 每行顯示一個產品
    List<GoodsPair> goodsPairList = new ArrayList<>();  // 每行顯示兩個產品
    GoodsPair currGoodsPair;  // 當前處理的goodsPair，考慮到分頁時，加載到奇數個產品，所以要預存一下GoodsPair

    List<StoreLabel> shopStoreLabelList = new ArrayList<>();

    boolean isStandalone;
    int storeId;
    EasyJSONObject paramsOriginal; // 傳進來的參數
    EasyJSONObject mExtra;
    boolean priceAsc;

    // 當前加載第幾頁
    int currPage = 0;
    boolean hasMore;

    int currVideoPage = 0;
    boolean videoHasMore;

    ScaledButton btnChangeViewStyle;

    LinearLayout llPage1;
    LinearLayout llPage2;
    private int currAnimIndex;
    VideoListAdapter videoListAdapter;
    List<VideoItem> videoItemList = new ArrayList<>();


    public static final int VIEW_STYLE_LIST = 0;  // 以列表形式查看
    public static final int VIEW_STYLE_GRID = 1;  // 以網格形式查看
    int currentViewStyle = VIEW_STYLE_LIST;
    private String title;
    private boolean isSlidingUpward;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvCategoryList;
    private int lastVisibleItemPosition;
    private int firstVisibleItemPosition;
    private boolean categoryToNext;
    private int currStoreLabelId;
    private StoreLabel currStoreLabel;
    private SimpleTabManager simpleTabManager;
    private boolean videoLoaded;

    /**
     * 新建一個實例
     * @param isStandalone
     * @param paramsStr JSON字符串格式參數  必傳，最少要傳一個storeId
     * @return
     */
    public static ShopCommodityFragment newInstance(boolean isStandalone, String paramsStr) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
        args.putString("paramsStr", paramsStr);
        SLog.info("paramsStr[%s]", paramsStr);

        ShopCommodityFragment fragment = new ShopCommodityFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_commodity, container, false);
        return view;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        hideSoftInput();

        if (!videoLoaded) {
            loadVideoCover(currPage + 1);
        }
        loadShopCategoryData();
        if (paramsOriginal.exists("sort")) {
            String sort = null;
            try {
                sort = paramsOriginal.getSafeString("sort");
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

            if ("new_desc".equals(sort)) {  // 最新產品
                simpleTabManager.performClick(2);
            } else if ("sale_desc".equals(sort)) { // 商店熱賣
                simpleTabManager.performClick(1);
            }
        } else {
//            loadStoreGoods(paramsOriginal, EasyJSONObject.generate("sort", "default_desc"), 1);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(false);
        // Vertical

        Bundle args = getArguments();
        isStandalone = args.getBoolean("isStandalone");
        String paramsStr = args.getString("paramsStr");
        paramsOriginal = EasyJSONObject.parse(paramsStr);
        try {
            storeId = paramsOriginal.getInt("storeId");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        if (isStandalone) {
            view.findViewById(R.id.tool_bar).setVisibility(View.VISIBLE);
            Util.setOnClickListener(view, R.id.btn_search_goods, this);
            Util.setOnClickListener(view, R.id.btn_back, this);
        } else {
            view.findViewById(R.id.tool_bar).setVisibility(View.GONE);
        }

        // 獲取屏幕寬度
        int screenWidth = Util.getScreenDimension(_mActivity).first;
        llPage1 = view.findViewById(R.id.ll_page_1);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llPage1.getLayoutParams();
        layoutParams.width = screenWidth;
        llPage1.setLayoutParams(layoutParams);

        llPage2 = view.findViewById(R.id.ll_page_2);
        layoutParams = (LinearLayout.LayoutParams) llPage2.getLayoutParams();
        layoutParams.width = screenWidth;
        llPage2.setLayoutParams(layoutParams);

        simpleTabManager  = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                if (isRepeat && id != R.id.btn_order_price) {
                    SLog.info("重復點擊");
                    return;
                }

                imgPriceOrderIndicator.setVisibility(View.GONE);
                if (id == R.id.btn_order_general) { // 綜合
                    SLog.info("btn_order_general");
                    clearAdapter();
                    mExtra =EasyJSONObject.generate("sort", "default_desc");;
                    loadStoreGoods(paramsOriginal, mExtra, 1);
                } else if (id == R.id.btn_order_sale) { // 銷量
                    SLog.info("btn_order_sale");
                    clearAdapter();
                    mExtra = EasyJSONObject.generate("sort", "sale_desc");
                    loadStoreGoods(paramsOriginal, mExtra, 1);
                } else if (id == R.id.btn_order_new) { // 上新
                    SLog.info("btn_order_new");
                    clearAdapter();

                    mExtra = EasyJSONObject.generate("sort", "new_desc");
                    loadStoreGoods(paramsOriginal, mExtra, 1);
                } else if (id == R.id.btn_order_price) { // 價格
                    if (isRepeat) {
                        // 如果是再次點擊的話，切換排序順序
                        priceAsc = !priceAsc;
                    } else {
                        // 如果是首次點擊的話，默認升序排序
                        priceAsc = true;
                    }
                    String sort;
                    if (priceAsc) { // 價格降序
                        sort = "price_desc";
                        imgPriceOrderIndicator.setImageResource(R.drawable.icon_price_sort_desc);
                    } else { // 價格升序
                        sort = "price_asc";
                        imgPriceOrderIndicator.setImageResource(R.drawable.icon_price_sort_asc);
                    }
                    imgPriceOrderIndicator.setVisibility(View.VISIBLE);

                    SLog.info("btn_order_price");
                    clearAdapter();

                    mExtra = EasyJSONObject.generate("sort", sort);
                    loadStoreGoods(paramsOriginal, mExtra, 1);
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_order_general));
        simpleTabManager.add(view.findViewById(R.id.btn_order_sale));
        simpleTabManager.add(view.findViewById(R.id.btn_order_new));
        simpleTabManager.add(view.findViewById(R.id.btn_order_price));

        rvGoodsList = view.findViewById(R.id.rv_goods_list);
//        OverScrollDecoratorHelper.setUpOverScroll(rvGoodsList, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        imgPriceOrderIndicator = view.findViewById(R.id.img_price_order_indicator);

        layoutManager = new CustomerLinearLayoutManager(_mActivity);//防止原生閃退
        rvGoodsList.setLayoutManager(layoutManager);

        rvGoodsList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                if (layoutManager instanceof LinearLayoutManager) {
                    //得到当前界面，最后一个子视图对应的position
                    lastVisibleItemPosition = layoutManager
                            .findLastVisibleItemPosition();

                    //得到当前界面，第一个子视图的position
                    firstVisibleItemPosition = layoutManager
                            .findFirstVisibleItemPosition();
                }

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                //得到当前界面可见数据的大小
                int visibleItemCount = layoutManager.getChildCount();

                //得到RecyclerView对应所有数据的大小
                int totalItemCount = layoutManager.getItemCount();

                //判断条件可按实际需要调整
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleItemCount > 0) {

                    //最后视图对应的position等于总数-1时，说明上一次滑动结束时，触底了
                    if (lastVisibleItemPosition == totalItemCount - 1){
                        //按需进行业务
//                        if (storeCategoryListAdapter.getSelectedItemCount() < visibleItemCount) {
//                            SLog.info("分不清");
                        SLog.info("isUp,[%s]",isUp);
                            if (isUp) {
                                return;
                            }
//                        }

                        int preIndex = storeCategoryListAdapter.getPrevSelectedItemIndex();
                        SLog.info("触底了 %s",categoryToNext);
                        if (!categoryToNext) {
                            if (!storeCategoryListAdapter.hasNextSubItem(true)) {
                                if (preIndex < storeCategoryListAdapter.getItemCount()-1) {

                                    categoryToNext = true;
                                categoryOnItemClick(preIndex+1);
                                }

                            }else {
                                categoryOnSubItemClick(true);
                            }
                        }

                        //第一个视图的position等于0，说明上一次滑动结束时，触顶了
                    } else if (firstVisibleItemPosition == 0) {
//                        ToastUtil.success(_mActivity,"触顶了");

                    }
                }

                if (parentFragment == null) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        parentFragment.onCbStopNestedScroll();
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        parentFragment.onCbStartNestedScroll();

                }
            }
        });
        shopGoodsGridAdapter = new ShopGoodsGridAdapter(_mActivity, goodsPairList, false);
        shopGoodsGridAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsPair goodsPair = goodsPairList.get(position);
                // padding忽略點擊
                if (goodsPair.getItemType() == Constant.ITEM_TYPE_LOAD_END_HINT) {
                    ApiUtil.addPost(_mActivity,false);
                    return;
                }

                Goods goods;
                int commonId = -1;
                int id = view.getId();
                if (id == R.id.img_left_goods || id == R.id.btn_add_to_cart_left) {
                    goods = goodsPair.leftGoods;
                    commonId = goods.id;
                } else if (id == R.id.img_right_goods || id == R.id.btn_add_to_cart_right) {
                    goods = goodsPair.rightGoods;
                    commonId = goods.id;
                }

                int userId = User.getUserId();

                if (id == R.id.btn_add_to_cart_left || id == R.id.btn_add_to_cart_right) {
                    if (userId > 0) {
                        showSpecSelectPopup(commonId);
                    } else {
                        Util.showLoginFragment(requireContext());
                    }
                    return;
                }
                if (commonId != -1) {
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                }
            }
        });
        shopGoodsGridAdapter.setEnableLoadMore(true);
        shopGoodsGridAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                SLog.info("onLoadMoreRequested");

                if (!hasMore) {
                    shopGoodsGridAdapter.setEnableLoadMore(false);
                    return;
                }
                loadStoreGoods(paramsOriginal, mExtra, currPage + 1);
            }
        }, rvGoodsList);

        shopGoodsListAdapter = new ShopGoodsListAdapter(_mActivity, goodsList);
        shopGoodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Goods goods = goodsList.get(position);
                SLog.info(title +String.valueOf(position));
                int commonId = goods.id;

                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            }
        });
        shopGoodsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                Goods goods = goodsList.get(position);
                int commonId = goods.id;
                int userId = User.getUserId();
                if (id == R.id.btn_add_to_cart) {
                    if (userId > 0) {
                        showSpecSelectPopup(commonId);
                    } else {
                        Util.showLoginFragment(requireContext());
                    }
                }
            }
        });

        shopGoodsListAdapter.setEnableLoadMore(true);
        shopGoodsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                SLog.info("onLoadMoreRequested");

                if (!hasMore) {
                    shopGoodsListAdapter.setEnableLoadMore(false);
                    return;
                }
                loadStoreGoods(paramsOriginal, mExtra, currPage + 1);
            }
        }, rvGoodsList);

        rvGoodsList.setAdapter(currentViewStyle==VIEW_STYLE_LIST?shopGoodsListAdapter:shopGoodsGridAdapter);
        rvGoodsList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = e.getRawX();
                    lastY = e.getRawY();
                } else if (action == MotionEvent.ACTION_UP) {
                    float x = e.getX();
                    float y = e.getY();
                    float deltaY = y - lastY;
                    isUp = deltaY > 0;
                    SLog.info("isUp[%s]", isUp);
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        RecyclerView rvVideoList = view.findViewById(R.id.rv_video_list);
        rvVideoList.setLayoutManager(new LinearLayoutManager(_mActivity));
        videoListAdapter = new VideoListAdapter(videoItemList);

        // 設置空頁面
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.layout_placeholder_no_data, null, false);
        // 設置空頁面的提示語
        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
        tvEmptyHint.setText(R.string.no_data_hint);
        videoListAdapter.setEmptyView(emptyView);

        videoListAdapter.setEnableLoadMore(true);
        videoListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                SLog.info("onLoadMoreRequested");

                if (!videoHasMore) {
                    videoListAdapter.setEnableLoadMore(false);
                    return;
                }
                loadVideoCover(currPage + 1);
            }
        }, rvVideoList);

        videoListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_play) { // 點擊播放按鈕
                    VideoItem videoItem = videoItemList.get(position);
                    Util.playYoutubeVideo(_mActivity, videoItem.videoId);
                }
            }
        });
        rvVideoList.setAdapter(videoListAdapter);

        rvCategoryList = view.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(_mActivity));
        storeCategoryListAdapter = new StoreCategoryListAdapter(_mActivity, R.layout.store_category_list_item, shopStoreLabelList, this);
        storeCategoryListAdapter.setOnItemClickListener((adapter, view1, position) -> categoryOnItemClick(position));
        rvCategoryList.setAdapter(storeCategoryListAdapter);


        btnChangeViewStyle = view.findViewById(R.id.btn_change_view_style);
        btnChangeViewStyle.setOnClickListener(this);
    }

    private boolean isRecyclerBottom(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return false;
        }
        //得到当前显示的最后一个item的view
        View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
        //得到lastChildView的bottom坐标值
        int lastChildBottom = lastChildView.getBottom();
        //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
        int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
        //通过这个lastChildView得到这个view当前的position值
        int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);

        //判断lastChildView的bottom值跟recyclerBottom
        //判断lastPosition是不是最后一个position
        //如果两个条件都满足则说明是真正的滑动到了底部
        if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
            // Toast.makeText(_mActivity, "滑动到底了", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void categoryOnItemClick(int position) {
//        SLog.bt();
        int prevSelectedItemIndex = storeCategoryListAdapter.getPrevSelectedItemIndex();

        SLog.info("prevSelectedItemIndex[%d],postion [%d]", prevSelectedItemIndex,position);
        if (prevSelectedItemIndex != -1) {
            StoreLabel prevSelectedItem = shopStoreLabelList.get(prevSelectedItemIndex);
            // 設置為收合狀態
            prevSelectedItem.setIsFold(1);

            prevSelectedItem.setIsFold(Constant.TRUE_INT);  // 設置為收合狀態
            storeCategoryListAdapter.notifyItemChanged(prevSelectedItemIndex);
        }

        StoreLabel storeLabel = shopStoreLabelList.get(position);
        storeLabel.setIsFold(Constant.FALSE_INT);  // 設置為展開狀態
        storeCategoryListAdapter.notifyItemChanged(position);

        storeCategoryListAdapter.setPrevSelectedItemIndex(position);
        title =String.format("%s(%d)",storeLabel.getStoreLabelName(),storeLabel.getGoodsCount());
        currStoreLabel = storeLabel;
        loadCategoryGoods(currStoreLabel.getStoreLabelId());
    }

    private void clearAdapter() {
        if (currentViewStyle == VIEW_STYLE_LIST) {
            goodsList.clear();
            if (shopGoodsGridAdapter != null) {
                shopGoodsListAdapter.notifyDataSetChanged();
            }
        }else{
            goodsPairList.clear();
            if (shopGoodsGridAdapter != null) {
                shopGoodsGridAdapter.notifyDataSetChanged();
            }
        }
        currPage = 0;
    }

    /**
     * 加載指定分類的產品
     * @param labelId  分類Id
     */
    private void loadCategoryGoods(int labelId) {
        SLog.info("loadCategoryGoods");
        clearAdapter();

        mExtra = EasyJSONObject.generate(
                "storeId", storeId);

        if (labelId != 0) { // labelId為0表示全部產品
            try {
                mExtra.set("labelId", labelId);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }
        SLog.info("mExtra[%s]", mExtra.toString());
        loadStoreGoods(paramsOriginal, mExtra, 1);
    }

    /**
     * 加載商店產品
     * @param paramsOriginal
     * @param extra 額外的搜索參數
     * @param page 第幾頁
     */
    private void loadStoreGoods(EasyJSONObject paramsOriginal, EasyJSONObject extra, int page) {
        try {
            EasyJSONObject params = EasyJSONObject.generate();
            for (Map.Entry<String, Object> param : paramsOriginal.entrySet()) {
                params.set(param.getKey(), param.getValue().toString());
            }

            // 合并參數
            if (extra != null) {
                for (Map.Entry<String, Object> param : extra.entrySet()) {
                    params.set(param.getKey(), param.getValue().toString());
                }
            }

            params.set("page", page);

            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
            SLog.info("商店內產品搜索,params[%s]", params.toString());
            String url = Api.PATH_SEARCH_GOODS_IN_STORE;
            Api.getUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    loadingPopup.dismiss();

                    ToastUtil.showNetworkError(_mActivity, e);
                    shopGoodsGridAdapter.loadMoreFail();
                    shopGoodsListAdapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    categoryToNext = false;
                    SLog.info("responseStr[%s]", responseStr);
                    try {
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            shopGoodsGridAdapter.loadMoreFail();
                            shopGoodsListAdapter.loadMoreFail();
                            return;
                        }

                        hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                        SLog.info("hasMore[%s]", hasMore);
                        if (!hasMore) {
                            shopGoodsGridAdapter.loadMoreEnd();
                            shopGoodsGridAdapter.setEnableLoadMore(false);
                            shopGoodsListAdapter.loadMoreEnd();
                            shopGoodsListAdapter.setEnableLoadMore(false);
                        }

                        EasyJSONArray goodsArray = responseObj.getSafeArray("datas.goodsCommonList");
                        if (page == 1) {

                        goodsList.clear();
                        }
                        for (Object object : goodsArray) {
                            EasyJSONObject goodsObject = (EasyJSONObject) object;
//
//                            int id = goodsObject.getInt("commonId");
//                            // 產品圖片
//                            String goodsImageUrl = goodsObject.getSafeString("imageSrc");
//                            // 產品名稱
//                            String goodsName = goodsObject.getSafeString("goodsName");
//                            // 賣點
//                            String jingle = goodsObject.getSafeString("jingle");
//                            // 獲取價格
//                            double price = Util.getSpuPrice(goodsObject);
//
//                            Goods goods = new Goods(id, goodsImageUrl, goodsName, jingle, price);
                            Goods goods = Goods.parse(goodsObject);
                            goodsList.add(goods);

                            if (currGoodsPair == null) {
                                currGoodsPair = new GoodsPair();
                            }

                            if (currGoodsPair.leftGoods == null) {
                                currGoodsPair.leftGoods = goods;
                            } else {
                                currGoodsPair.rightGoods = goods;
                                goodsPairList.add(currGoodsPair);

                                currGoodsPair = null;
                            }
                        }

                        // 如果剛好奇數個，可能沒添加到列表中
                        if (currGoodsPair != null) {
                            goodsPairList.add(currGoodsPair);
                            currGoodsPair = null;
                        }

                        if (!hasMore && goodsList.size() > 0) {
                            // 如果全部加載完畢，不添加加載完畢的提示
                            Goods goods = new Goods();
                            goodsList.add(goods);

                            GoodsPair loadEndGoodsPair = new GoodsPair();
                            loadEndGoodsPair.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
                            goodsPairList.add(loadEndGoodsPair);
                        }
                        if (goodsPairList != null && goodsPairList.size() > 0) {
                            SLog.info("當前list Size %d,type %d,title %s",goodsPairList.size(),goodsPairList.get(0).getItemType(),title);

                            if(goodsPairList.get(0).getItemType() != Constant.ITEM_TYPE_TITLE&&currPage==0){

                                GoodsPair titleGoodsPair = new GoodsPair();
                                titleGoodsPair.itemType = Constant.ITEM_TYPE_TITLE;
                                if (!StringUtil.isEmpty(title)) {
                                    titleGoodsPair.setItemTitle(title);
//                                    title = "";
                                    goodsPairList.add(0, titleGoodsPair);
//                                    goodsList.add(0, goods);
                                }

                            };

                        }

                        if (goodsList != null && goodsList.size() > 0) {
                            SLog.info("當前list Size %d,type %d,title %s",goodsList.size(),goodsList.get(0).getItemType(),title);

                            if(goodsList.get(0).getItemType() != Constant.ITEM_TYPE_TITLE&&currPage==0){
                                Goods goods =new Goods(Constant.ITEM_TYPE_TITLE);
                                if (!StringUtil.isEmpty(title)) {
                                    goods.name = title;
                                    title = "";
                                    goodsList.add(0, goods);
                                }

                            };

                        }
                        swipeRefreshLayout.setRefreshing(false);

                        shopGoodsGridAdapter.setNewData(goodsPairList);
                        shopGoodsGridAdapter.loadMoreComplete();
                        shopGoodsListAdapter.setNewData(goodsList);
                        shopGoodsListAdapter.loadMoreComplete();

                        currPage++;
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    /**
     * 加載商店分類產品數據
     */
    private void loadShopCategoryData() {
        String url = Api.PATH_STORE_CATEGORY;
        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId
        );

        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                shopStoreLabelList.clear();
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    return;
                }

                try {
                    // 添加一個【全部產品】的分類
                    StoreLabel storeLabel = new StoreLabel();
                    storeLabel.setStoreLabelId(0); // 0 表示全部產品
                    storeLabel.setStoreLabelName("全部產品");
                    storeLabel.setIsFold(Constant.FALSE_INT); //默認選中【全部產品】
                    storeLabel.setStoreLabelList(new ArrayList<>());
                    shopStoreLabelList.add(storeLabel);

                    int goodsCountTotal = 0;
                    EasyJSONArray storeCategoryList = responseObj.getSafeArray("datas.storeCategoryList");
                    for (Object object : storeCategoryList) {

                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        storeLabel = new StoreLabel();
                        storeLabel.setStoreLabelId(easyJSONObject.getInt("storeLabelId"));
                        int goodsCount = easyJSONObject.getInt("goodsCount");
                        storeLabel.setGoodsCount(goodsCount);
                        storeLabel.setStoreLabelName(easyJSONObject.getSafeString("storeLabelName"));
                        storeLabel.setParentId(easyJSONObject.getInt("parentId"));
                        storeLabel.setStoreId(easyJSONObject.getInt("storeId"));
                        storeLabel.setIsFold(Constant.TRUE_INT);

                        EasyJSONArray storeLabelList = easyJSONObject.getSafeArray("storeLabelList");
                        storeLabel.parseStoreLabelList(storeLabelList,1);
                        shopStoreLabelList.add(storeLabel);

                    }

                        if (storeLabel.getStoreLabelList() == null) { // 保證不為null
                            storeLabel.setStoreLabelList(new ArrayList<>());
                        }

//                        goodsCountTotal += goodsCount;
                        goodsCountTotal = responseObj.getInt("datas.storeGoodsCount");
//                    shopStoreLabelList.add(0,new StoreLabel());
                    shopStoreLabelList.get(0).setGoodsCount(goodsCountTotal);  // 添加【全部產品】的項數
                    title = String.format("%s(%d)", shopStoreLabelList.get(0).getStoreLabelName(), goodsCountTotal);
                    if (goodsPairList != null && goodsPairList.size() > 0) {

                        if (goodsPairList.get(0).getItemType() != Constant.ITEM_TYPE_TITLE) {
                            //這裏是僅僅針對初始默認情況
                            if (currPage == 1) {
                                GoodsPair titleItem = new GoodsPair();
                                titleItem.itemType = Constant.ITEM_TYPE_TITLE;
                                titleItem.setItemTitle(title);
                                goodsPairList.add(0,titleItem);
//                                int s = shopGoodsGridAdapter.getData().size();
                                shopGoodsGridAdapter.notifyItemInserted(0);
                                shopGoodsListAdapter.notifyItemInserted(0);
                            }

                        }
                    }
                    if (goodsList != null && goodsList.size() > 0) {
                        if(goodsList.get(0).getItemType() != Constant.ITEM_TYPE_TITLE){
                            //這裏是僅僅針對初始默認情況
                            if (currPage == 1) {
                                Goods goods =new Goods(Constant.ITEM_TYPE_TITLE);
                                goods.name = title;
                                title = "";
                                goodsList.add(0, goods);
                                shopGoodsGridAdapter.notifyItemInserted(0);
                                shopGoodsListAdapter.notifyItemInserted(0);
                            }
                        };
                    }

                    storeCategoryListAdapter.setNewData(shopStoreLabelList);
                    if (firstShow) {
                        firstShow = false;
                        loadStoreGoods(paramsOriginal, EasyJSONObject.generate("sort", "default_desc"), 1);
                    }

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadVideoCover(int page) {
        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId,
                "page", page);

        String url = Api.PATH_STORE_VIDEO_LIST + "/" + storeId;
        SLog.info("loadVideoCover, url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                videoListAdapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                try {
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        videoListAdapter.loadMoreFail();
                        return;
                    }

                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        videoListAdapter.loadMoreEnd();
                        videoListAdapter.setEnableLoadMore(false);
                    }

                    EasyJSONArray videoVoList = responseObj.getSafeArray("datas.videoVoList");
                    for (Object object : videoVoList) {
                        EasyJSONObject videoVo = (EasyJSONObject) object;

                        VideoItem videoItem = new VideoItem(Constant.ITEM_TYPE_NORMAL);
                        String videoUrl = videoVo.getSafeString("videoUrl");
                        videoItem.videoId = Util.getYoutubeVideoId(videoUrl);
                        videoItem.playCount = videoVo.getInt("playTimes");
                        videoItem.updateTime = videoVo.getSafeString("updateTime");

                        EasyJSONArray goodsCommonList = videoVo.getSafeArray("goodsCommonList");
                        for (Object object2 : goodsCommonList) {
                            EasyJSONObject goodsCommon = (EasyJSONObject) object2;
                            Goods goods = new Goods();
                            goods.id = goodsCommon.getInt("commonId");
                            goods.imageUrl = goodsCommon.getSafeString("goodsImage");

                            videoItem.goodsList.add(goods);
                        }

                        videoItemList.add(videoItem);
                    }

                    if (!videoHasMore && videoItemList.size() > 0) {
                        // 如果全部加載完畢，添加加載完畢的提示
                        videoItemList.add(new VideoItem(Constant.ITEM_TYPE_LOAD_END_HINT));

                        videoListAdapter.setNewData(videoItemList);
                        videoListAdapter.loadMoreComplete();

                        currVideoPage++;
                    }

                    SLog.info("videoItemList.size[%d]", videoItemList.size());
                    videoListAdapter.setNewData(videoItemList);
                    videoLoaded = true;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    public void switchTabPage() {
        SLog.info("currAnimIndex[%d]", currAnimIndex);
        if (currAnimIndex == 0) {
            llPage1.setVisibility(View.GONE);
            llPage2.setVisibility(View.VISIBLE);
        } else {
            llPage1.setVisibility(View.VISIBLE);
            llPage2.setVisibility(View.GONE);
        }

        currAnimIndex = (currAnimIndex + 1) % ANIM_COUNT;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_search_goods) {
            Util.startFragment(ShopSearchFragment.newInstance(storeId, null));
        } else if (id == R.id.btn_change_view_style) {
            changeViewStyle();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (parentFragment != null) {
            // 如果父Fragment不為空，表明是依附在父Fragment中的，pop出父Fragment
            parentFragment.pop();
        } else {
            hideSoftInputPop();
        }

        return true;
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("onSelected, type[%s], id[%d]", type, id);
        if (PopupType.DEFAULT == type) {
            storeCategoryListAdapter.notifyItemChanged(id);
            StoreLabel storeLabel = (StoreLabel) extra;
            int subCategoryId = storeLabel.getStoreLabelId();  // 二級分類Id
            title = storeLabel.getStoreLabelName();
            loadCategoryGoods(subCategoryId);
        }
    }

    private void changeViewStyle() {
        if (currentViewStyle == VIEW_STYLE_LIST) {
            btnChangeViewStyle.setIconResource(R.drawable.icon_store_goods_view_style_list);

            rvGoodsList.setAdapter(shopGoodsGridAdapter);
        } else {
            btnChangeViewStyle.setIconResource(R.drawable.icon_store_goods_view_style_grid);

            rvGoodsList.setAdapter(shopGoodsListAdapter);
        }
        currentViewStyle = 1 - currentViewStyle;
        if (currStoreLabel != null) {

            title =String.format("%s(%d)",currStoreLabel.getStoreLabelName(),currStoreLabel.getGoodsCount());
            loadCategoryGoods(currStoreLabel.getStoreLabelId());

        }


    }

    private void showSpecSelectPopup(int commonId) {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, Constant.ACTION_ADD_TO_CART, commonId, null, null, null, 1, null, null, 0,2, null))
                .show();
    }

    @Override
    public void onRefresh() {
        SLog.info("onRefresh");
        swipeRefreshLayout.setRefreshing(false);
        boolean orientationToDown = false;
        if (!storeCategoryListAdapter.hasNextSubItem(orientationToDown)) {
            int preIndex = storeCategoryListAdapter.getPrevSelectedItemIndex();
            SLog.info("prevSelectedItemIndex[%d]", preIndex);

            if (preIndex > 0) {
                categoryOnItemClick(preIndex - 1);
                currPage = 0;
            } else {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        } else {
            categoryOnSubItemClick(orientationToDown);
        }
    }

    private void categoryOnSubItemClick(boolean down) {
        try {
            int preIndex = storeCategoryListAdapter.getPrevSelectedItemIndex();
            BaseViewHolder holder=(BaseViewHolder)rvCategoryList.findViewHolderForAdapterPosition(preIndex);
            if (holder == null) {
                return;
            }
            LinearLayout llSubCategoryList = holder.getView(R.id.ll_sub_ategory_list);
            int subIndex=storeCategoryListAdapter.getPrevSelectedSubItemIndex()+(down?1:-1);
            SLog.info("SubIndex %d", subIndex);

            categoryToNext=false;
            llSubCategoryList.getChildAt(subIndex).performClick();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}

