package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ShopGoodsGridAdapter;
import com.ftofs.twant.adapter.ShopGoodsListAdapter;
import com.ftofs.twant.adapter.StoreCategoryListAdapter;
import com.ftofs.twant.adapter.VideoListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.GoodsPair;
import com.ftofs.twant.entity.VideoItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ApiUtil;
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
public class ShopCommodityFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    public static final int ANIM_COUNT = 2;
    ShopMainFragment parentFragment;

    RecyclerView rvGoodsList;
    ImageView imgPriceOrderIndicator;

    ShopGoodsListAdapter shopGoodsListAdapter;
    ShopGoodsGridAdapter shopGoodsGridAdapter;
    LinearLayoutManager layoutManager;


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
    int currentViewStyle = VIEW_STYLE_GRID;
    private String title;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_commodity, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();

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

        SimpleTabManager simpleTabManager = new SimpleTabManager(0) {
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
        imgPriceOrderIndicator = view.findViewById(R.id.img_price_order_indicator);

        layoutManager = new CustomerLinearLayoutManager(_mActivity);//防止原生閃退
        rvGoodsList.setLayoutManager(layoutManager);
        rvGoodsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (parentFragment != null) {
                        parentFragment.onCbStopNestedScroll();
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (parentFragment != null) {
                        parentFragment.onCbStartNestedScroll();
                    }
                }
            }
        });
        shopGoodsGridAdapter = new ShopGoodsGridAdapter(_mActivity, goodsPairList);
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
                        Util.showLoginFragment();
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
                        Util.showLoginFragment();
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

        rvGoodsList.setAdapter(shopGoodsGridAdapter);

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
            loadStoreGoods(paramsOriginal, EasyJSONObject.generate("sort", "default_desc"), 1);
        }

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

        RecyclerView rvCategoryList = view.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(_mActivity));
        storeCategoryListAdapter = new StoreCategoryListAdapter(_mActivity, R.layout.store_category_list_item, shopStoreLabelList, this);
        storeCategoryListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int prevSelectedItemIndex = storeCategoryListAdapter.getPrevSelectedItemIndex();

                SLog.info("prevSelectedItemIndex[%d]", prevSelectedItemIndex);
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
                loadCategoryGoods(storeLabel.getStoreLabelId());
            }
        });
        rvCategoryList.setAdapter(storeCategoryListAdapter);

        loadVideoCover(currPage + 1);
        loadShopCategoryData();

        btnChangeViewStyle = view.findViewById(R.id.btn_change_view_style);
        btnChangeViewStyle.setOnClickListener(this);
    }

    private void clearAdapter() {
        if (currAnimIndex == VIEW_STYLE_GRID) {
            goodsList.clear();
            shopGoodsListAdapter.notifyDataSetChanged();
        }else{
            goodsPairList.clear();
            shopGoodsGridAdapter.notifyDataSetChanged();
        }
        currPage = 0;
    }

    /**
     * 加載指定分類的產品
     * @param labelId  分類Id
     */
    private void loadCategoryGoods(int labelId) {
        SLog.info("loadCategoryGoods");
        goodsList.clear();
        goodsPairList.clear();
        currPage = 0;

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
            Api.getUI(Api.PATH_SEARCH_GOODS_IN_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    loadingPopup.dismiss();

                    ToastUtil.showNetworkError(_mActivity, e);
                    shopGoodsGridAdapter.loadMoreFail();
                    shopGoodsListAdapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    SLog.info("responseStr[%s]", responseStr);
                    try {
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
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
                        for (Object object : goodsArray) {
                            EasyJSONObject goodsObject = (EasyJSONObject) object;

                            int id = goodsObject.getInt("commonId");
                            // 產品圖片
                            String goodsImageUrl = goodsObject.getSafeString("imageSrc");
                            // 產品名稱
                            String goodsName = goodsObject.getSafeString("goodsName");
                            // 賣點
                            String jingle = goodsObject.getSafeString("jingle");
                            // 獲取價格
                            double price = Util.getSpuPrice(goodsObject);

                            Goods goods = new Goods(id, goodsImageUrl, goodsName, jingle, price);
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

//                            GoodsPair loadEndGoodsPair = new GoodsPair();
//                            loadEndGoodsPair.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
//                            goodsPairList.add(loadEndGoodsPair);
                        }
                        if (goodsPairList != null && goodsPairList.size() > 0) {
                            SLog.info("當前list Size %d,type %d,title %s",goodsPairList.size(),goodsPairList.get(0).getItemType(),title);

                            if(goodsPairList.get(0).getItemType() != Constant.ITEM_TYPE_TITLE&&currPage==0){

                                GoodsPair titleGoodsPair = new GoodsPair();
                                titleGoodsPair.itemType = Constant.ITEM_TYPE_TITLE;
                                if (!StringUtil.isEmpty(title)) {
                                    titleGoodsPair.setItemTitle(title);
                                    title = "";
                                    goodsPairList.add(0, titleGoodsPair);
                                }

                            };
                        }

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
        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId
        );

        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_STORE_CATEGORY, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
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
                        if (storeLabelList != null && storeLabelList.length() > 0) {
                            List<StoreLabel> storeLabels = new ArrayList<>();
                            for (Object object2 : storeLabelList) {
                                EasyJSONObject easyJSONObject2 = (EasyJSONObject) object2;
                                StoreLabel storeLabel2 = new StoreLabel();
                                storeLabel2.setStoreLabelId(easyJSONObject2.getInt("storeLabelId"));
                                storeLabel2.setStoreLabelName(easyJSONObject2.getSafeString("storeLabelName"));
                                storeLabel2.setParentId(easyJSONObject2.getInt("parentId"));
                                storeLabel2.setStoreId(easyJSONObject2.getInt("storeId"));
                                storeLabel2.setIsFold(Constant.TRUE_INT);

                                storeLabels.add(storeLabel2);
                            }

                            storeLabel.setStoreLabelList(storeLabels);
                        }

                        if (storeLabel.getStoreLabelList() == null) { // 保證不為null
                            storeLabel.setStoreLabelList(new ArrayList<>());
                        }

//                        goodsCountTotal += goodsCount;
                        shopStoreLabelList.add(storeLabel);
                    }
                    goodsCountTotal = responseObj.getInt("datas.storeGoodsCount");
                    shopStoreLabelList.get(0).setGoodsCount(goodsCountTotal);  // 添加【全部產品】的項數
                    title = String.format("%s(%d)", shopStoreLabelList.get(0).getStoreLabelName(), goodsCountTotal);
                    if (goodsPairList != null && goodsPairList.size() > 0) {
                        if (goodsPairList.get(0).getItemType() != Constant.ITEM_TYPE_TITLE) {
                            //這裏是僅僅針對初始默認情況
                            if (currPage == 1) {
                                GoodsPair titleItem = new GoodsPair();
                                titleItem.itemType = Constant.ITEM_TYPE_TITLE;
                                titleItem.setItemTitle(title);
                                title = "";
                                goodsPairList.add(0,titleItem);
//                                int s = shopGoodsGridAdapter.getData().size();
                                shopGoodsGridAdapter.notifyItemInserted(0);
                                shopGoodsListAdapter.notifyItemInserted(0);
                            }

                        }
                    }

                    storeCategoryListAdapter.setNewData(shopStoreLabelList);
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
                ToastUtil.showNetworkError(_mActivity, e);
                videoListAdapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                try {
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
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
    }

    private void showSpecSelectPopup(int commonId) {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, Constant.ACTION_ADD_TO_CART, commonId, null, null, null, 1, null, null, 0,2, null))
                .show();
    }
}

