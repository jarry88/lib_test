package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsSearchResultAdapter;
import com.ftofs.twant.adapter.StoreSearchResultAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.BizCircleId;
import com.ftofs.twant.entity.BizCircleItem;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.FilterCategoryGroup;
import com.ftofs.twant.entity.FilterCategoryItem;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.entity.JobInfoItem;
import com.ftofs.twant.entity.StoreSearchItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.orm.ImNameMap;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.SearchHistoryUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.GoodsFilterDrawerPopupView;
import com.ftofs.twant.widget.StoreFilterPopup;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 搜索结果Fragment
 * @author zwm
 */
public class SearchResultFragment extends BaseFragment implements View.OnClickListener,
        OnSelectedListener, BaseQuickAdapter.RequestLoadMoreListener {
    SearchType searchType;

    /**
     * 商店搜索結果的排序標準
     * "" 按【綜合】排序，服務器端返回什么就是什么
     * "collect_desc"  按【關注量】排序
     * "startBusiness_desc" 按【開店時長】排序
     */
    public static final String STORE_SORT_GENERAL = "";
    public static final String STORE_SORT_FOLLOW = "collect_desc";
    public static final String STORE_SORT_OPEN = "startBusiness_desc";


    /**
     * 當前選中的過濾條件的類型： 【所在地】和【商圈】只能二選一
     */
    /*
    int filterType = StoreFilterPopup.FILTER_TYPE_NONE;

    int storeDistrictAreaId; //  int  false  普通参数  0  商圈所在地區ID(選商圈所在地區時，只傳這個即可)
    int storeDistrictId;     //  int  false  普通参数  0  商圈ID
    int storeArea;           //  int  false  普通参数  0  地區ID
    */
    BizCircleId bizCircleId = new BizCircleId(BizCircleId.ID_TYPE_UNKNOWN, 0);

    /**
     * 商圈列表
     */
    List<BizCircleItem> bizCircleItemList;

    /**
     * 所在地列表
     */
    List<BizCircleItem> locationItemList;

    int twBlack;
    int twRed;
    int twBlue;

    int currSelId; // 表示商店列表當前選中哪個Tab
    int generalItemSelectedId = 1;

    StoreFilterPopup storeFilterPopup;

    boolean followStatus;
    TextView tvFollow;
    boolean hrStatus;
    TextView tvRecruitment;

    TextView tvSort;
    ImageView iconSort;
    TextView tvLocation;
    ImageView iconLocation;
    TextView tvBizCircle;
    ImageView iconBizCircle;

    ImageView btnPublishWantPost;

    RelativeLayout btnSort;
    RelativeLayout btnLocation;
    RelativeLayout btnBizCircle;

    RelativeLayout btnFollow;

    GoodsSearchResultAdapter mGoodsAdapter;
    StoreSearchResultAdapter mStoreAdapter;

    List<GoodsSearchItemPair> goodsItemPairList = new ArrayList<>();
    List<StoreSearchItem> storeItemList = new ArrayList<>();
    EditText etKeyword;
    EasyJSONObject paramsObj;
    EasyJSONObject currFilter;
    String keyword;

    RecyclerView rvSearchResultList;
    ImageView btnGotoTop;
    ImageView btnGotoCart;

    TextView btnPrevPage;
    TextView btnNextPage;

    /**
     * 標記分類篩選數據是否已經加載
     */
    boolean isFilterCategoryLoaded;
    List<FilterCategoryGroup> filterCategoryGroupList = new ArrayList<>();

    ImageView iconPriceOrder;
    boolean sortPriceAsc = true;  // 是否用升序來進行價格排序

    /**
     * 是否為活動專場促銷
     */
    boolean isActivityShopping = false;

    public static final int STORE_SEARCH_SORT_GENERAL = 0;
    public static final int STORE_SEARCH_SORT_STORE_OPEN = 1;
    public static final int STORE_SEARCH_SORT_FOLLOW = 2;
    int storeSortButtonIndex = STORE_SEARCH_SORT_GENERAL; // 商店搜索當前用哪種排序標準 0,1,2


    public static final int GOODS_SEARCH_SORT_GENERAL = 0;
    public static final int GOODS_SEARCH_SORT_SALE = 1;
    public static final int GOODS_SEARCH_SORT_PRICE = 2;
    int goodsSortButtonIndex = GOODS_SEARCH_SORT_GENERAL; // 產品搜索當前用哪種排序標準 0,1,2
    TextView[] goodsSortButtons = new TextView[3];

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示
    LinearLayout llFloatButtonContainer;
//    如果有商品結果，沒有店鋪結果 返回isHaveGoods為1
//
//    沒有商品結果，有店鋪結果
//    返回isHaveGoods為0，isHaveStore為1
//
//    沒有商品結果，沒有店鋪結果
//    返回isHaveGoods為0，isHaveStore為0
    private int isHaveGoods;
    private int isHaveStore;
    private int associateSearchTime;

    public static SearchResultFragment newInstance(String searchTypeStr, String paramsStr) {
        Bundle args = new Bundle();

        args.putString("searchTypeStr", searchTypeStr);
        args.putString("paramsStr", paramsStr);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String searchTypeStr = args.getString("searchTypeStr");
        searchType = SearchType.valueOf(searchTypeStr);
        String paramsStr = args.getString("paramsStr");
        paramsObj = EasyJSONObject.parse(paramsStr);

        twBlack = getResources().getColor(R.color.tw_black, null);
        twRed = getResources().getColor(R.color.tw_red, null);
        twBlue = getResources().getColor(R.color.tw_blue, null);

        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);

        if (searchTypeStr.equals(SearchType.GOODS.name())) {

        } else if (searchTypeStr.equals(SearchType.STORE.name())) {

        }

        Util.setOnClickListener(view, R.id.btn_clear_all, this);

        iconPriceOrder = view.findViewById(R.id.icon_price_order);

        goodsSortButtons[GOODS_SEARCH_SORT_GENERAL] = view.findViewById(R.id.tv_text_general);
        goodsSortButtons[GOODS_SEARCH_SORT_SALE] = view.findViewById(R.id.tv_text_sale);
        goodsSortButtons[GOODS_SEARCH_SORT_PRICE] = view.findViewById(R.id.tv_text_price);
        Util.setOnClickListener(view, R.id.btn_sort_goods_general, this);
        Util.setOnClickListener(view, R.id.btn_sort_goods_sale, this);
        Util.setOnClickListener(view, R.id.btn_sort_goods_price, this);

        btnPublishWantPost = view.findViewById(R.id.btn_publish_want_post);
        btnPublishWantPost.setOnClickListener(this);
        //Glide.with(_mActivity).load("file:///android_asset/christmas/publish_want_post_dynamic.gif").centerCrop().into(btnPublishWantPost);


        btnGotoTop = view.findViewById(R.id.btn_goto_top);
        btnGotoTop.setOnClickListener(this);
        btnGotoCart = view.findViewById(R.id.btn_goto_cart);
        btnGotoCart.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_goods_filter, this);
        etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = v.getText().toString().trim();
                    if (StringUtil.isEmpty(text)) {
                        ToastUtil.error(_mActivity, getString(R.string.input_search_keyword_hint));
                        return true;
                    }

                    keyword = text;
                    try {
                        paramsObj.set("keyword", keyword);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }

                    // 如果重新搜索，重置一下搜索排序條件
                    switchGoodsSortIndicator(R.id.btn_sort_goods_general);

                    currPage = 0;
                    doSearch(searchType, currPage + 1, text, null);
                    hideSoftInput();
                    return true;
                }

                return false;
            }
        });
        if (paramsObj.exists("keyword")) {
            try {
                keyword = paramsObj.getSafeString("keyword");
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
            etKeyword.setText(keyword);
        }

        EditTextUtil.cursorSeekToEnd(etKeyword);

        if (searchType == SearchType.GOODS) {
            view.findViewById(R.id.ll_goods_filter).setVisibility(View.VISIBLE);
        } else if (searchType == SearchType.STORE) {
            view.findViewById(R.id.ll_store_filter).setVisibility(View.VISIBLE);

            // 如果是搜索商店，隱藏【返回頂部】和【轉到購物袋】按鈕
            // btnGotoTop.setVisibility(View.GONE);
            // btnGotoCart.setVisibility(View.GONE);
        }

        rvSearchResultList = view.findViewById(R.id.rv_search_result_list);
        rvSearchResultList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideFloatButton();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showFloatButton();
                }

                if (!hasMore && !rvSearchResultList.canScrollVertically(1)) {
                    if (searchType == SearchType.GOODS) {
                        if (goodsItemPairList.size() > 0) {
                            int lastItemPos = goodsItemPairList.size() - 1;
                            GoodsSearchItemPair lastItem = goodsItemPairList.get(lastItemPos);
                            if (lastItem.animShowStatus == Constant.ANIM_NOT_SHOWN) {
                                lastItem.animShowStatus = Constant.ANIM_SHOWING;
                                mGoodsAdapter.notifyItemChanged(lastItemPos);
                                SLog.info("滑动到底了^________________^");
                            }
                        }
                    } else if (searchType == SearchType.STORE) {
                        if (storeItemList.size() > 0) {
                            int lastItemPos = storeItemList.size() - 1;
                            StoreSearchItem lastItem = storeItemList.get(lastItemPos);
                            if (lastItem.animShowStatus == Constant.ANIM_NOT_SHOWN) {
                                lastItem.animShowStatus = Constant.ANIM_SHOWING;
                                mStoreAdapter.notifyItemChanged(lastItemPos);
                                SLog.info("滑动到底了^________________^");
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        doSearch(searchType, currPage + 1, keyword, null);
    }

    private void initSearchStore(View view) {
        view.findViewById(R.id.ll_goods_filter).setVisibility(View.GONE);
        view.findViewById(R.id.ll_store_filter).setVisibility(View.VISIBLE);

        tvFollow = view.findViewById(R.id.tv_follow);
        tvRecruitment = view.findViewById(R.id.tv_recruitment);
        tvSort = view.findViewById(R.id.tv_sort);
        iconSort = view.findViewById(R.id.icon_sort);
        tvLocation = view.findViewById(R.id.tv_location);
        iconLocation = view.findViewById(R.id.icon_location);
        tvBizCircle = view.findViewById(R.id.tv_biz_circle);
        iconBizCircle = view.findViewById(R.id.icon_biz_circle);
        Util.setOnClickListener(view, R.id.btn_recruitment, this);

        btnFollow = view.findViewById(R.id.btn_follow);
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) { // 用戶未登錄，隱藏關注過濾按鈕
            btnFollow.setVisibility(View.GONE);
        } else {
            btnFollow.setOnClickListener(this);
        }

        btnSort = view.findViewById(R.id.btn_sort);
        btnSort.setOnClickListener(this);
        btnLocation = view.findViewById(R.id.btn_location);
        btnLocation.setOnClickListener(this);
        btnBizCircle = view.findViewById(R.id.btn_biz_circle);
        btnBizCircle.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearchResultList.setLayoutManager(layoutManager);
        mStoreAdapter = new StoreSearchResultAdapter(storeItemList);
        mStoreAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SLog.info("id[%d]", id);
                if (id == R.id.btn_shop_hr) {
                    boolean show = storeItemList.get(position).showJobInfo;
                    storeItemList.get(position).showJobInfo = !show;
                    mStoreAdapter.notifyItemChanged(position);
                } else if (id == R.id.btn_customer) {
                    CustomerServiceStaff staff = storeItemList.get(position).staff;
                    String memberName = staff.memberName;
                    String imName = staff.imName;
                    ImNameMap.saveMap(imName, memberName, storeItemList.get(position).storeId);

                    FriendInfo friendInfo = new FriendInfo();
                    friendInfo.memberName = memberName;
                    friendInfo.nickname = staff.staffName;
                    friendInfo.avatarUrl = staff.avatar;
                    friendInfo.role = ChatUtil.ROLE_CS_AVAILABLE;
                    FriendInfo.upsertFriendInfo(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);
                    EMConversation conversation = ChatUtil.getConversation(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);
                    Util.startFragment(ChatFragment.newInstance(conversation, friendInfo));
                }
            }
        });
        mStoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoreSearchItem storeSearchItem = storeItemList.get(position);
                if (storeSearchItem.getItemType() == Constant.ITEM_TYPE_NORMAL) {
                    Util.startFragment(ShopMainFragment.newInstance(storeSearchItem.storeId));
                } else {
                    Util.startFragment(AddPostFragment.newInstance(false));
                }
            }
        });

        mStoreAdapter.setEnableLoadMore(true);
        mStoreAdapter.setOnLoadMoreListener(this, rvSearchResultList);

        rvSearchResultList.setAdapter(mStoreAdapter);
    }

    private void initSearchGoods(View view) {
        view.findViewById(R.id.ll_store_filter).setVisibility(View.GONE);
        view.findViewById(R.id.ll_goods_filter).setVisibility(View.VISIBLE);
        try {
            if (paramsObj.exists("is_double_eleven") && paramsObj.getBoolean("is_double_eleven")) {
                // 如果是雙11活動，則將背景色設置為紅色
                isActivityShopping = true;
                view.findViewById(R.id.tool_bar).setVisibility(View.GONE);
                view.findViewById(R.id.ll_filter_bar).setVisibility(View.GONE);
                rvSearchResultList.setBackgroundColor(Color.parseColor("#FFF3004D"));

                // 如果是活動專場，採用【上一頁】、【下一頁】的分頁模式
                view.findViewById(R.id.ll_pagination_container).setVisibility(View.VISIBLE);
                btnPrevPage = view.findViewById(R.id.btn_prev_page);
                btnPrevPage.setOnClickListener(this);
                btnNextPage = view.findViewById(R.id.btn_next_page);
                btnNextPage.setOnClickListener(this);

                // 需禁用第1頁
                Util.setButtonStatus(_mActivity, btnPrevPage, false);
            }
        } catch (Exception e) {

        }

        rvSearchResultList.setLayoutManager(new LinearLayoutManager(_mActivity));
        mGoodsAdapter = new GoodsSearchResultAdapter(_mActivity, goodsItemPairList);
        mGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_goto_store_left || id == R.id.btn_goto_store_right) {
                    GoodsSearchItemPair pair = goodsItemPairList.get(position);
                    int storeId;
                    if (id == R.id.btn_goto_store_left) {
                        storeId = pair.left.storeId;
                    } else {
                        storeId = pair.right.storeId;
                    }

                    Util.startFragment(ShopMainFragment.newInstance(storeId));
                } else if (id == R.id.cl_container_left || id == R.id.cl_container_right) {
                    GoodsSearchItemPair pair = goodsItemPairList.get(position);
                    int commonId;
                    if (id == R.id.cl_container_left) {
                        commonId = pair.left.commonId;
                    } else {
                        commonId = pair.right.commonId;
                    }

                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                } else if (id == R.id.btn_play_game) {
//                    String url = Util.makeChristmasH5Url();
                    String url = Util.makeSpringH5Url();
                    SLog.info("sprint_url[%s]", url);
                    if (url == null) {
                        Util.showLoginFragment();
                        return;
                    }
                    start(H5GameFragment.newInstance(url, true));
                } else if (id == R.id.btn_back) {
                    hideSoftInputPop();
                }
            }
        });
        mGoodsAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                GoodsSearchItemPair pair = goodsItemPairList.get(position);
                int itemType = pair.getItemType();
                if (itemType == Constant.ITEM_TYPE_NORMAL) {
                    return 1;
                } else if (itemType == Constant.ITEM_TYPE_LOAD_END_HINT || itemType == Constant.ITEM_TYPE_DOUBLE_ELEVEN_BANNER) {
                    return 2;
                }
                return 1;
            }
        });

        if (!isActivityShopping) { // 活動專場採用點擊【上一頁】、【下一頁】的分頁方式
            mGoodsAdapter.setEnableLoadMore(true);
            mGoodsAdapter.setOnLoadMoreListener(this, rvSearchResultList);
        }
        rvSearchResultList.setAdapter(mGoodsAdapter);
    }


    private void doSearch(SearchType searchType, int page, String keyword, EasyJSONObject filter) {
        SLog.info("searchType[%s], keyword[%s]", searchType, keyword);
        this.searchType=searchType;
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        int searchTypeInt = searchType.ordinal();
        SearchHistoryUtil.saveSearchHistory(searchTypeInt, keyword);

        try {
            EasyJSONObject params = setSearchParams(page,filter);

            if (searchType == SearchType.GOODS) {
                SLog.info("params[%s]", params);
                String url = Api.PATH_SEARCH_GOODS;

                if (isActivityShopping) {
                    url = Api.PATH_SEARCH_PROMOTION;
                }

                Api.getUI(url, params, new UICallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        ToastUtil.showNetworkError(_mActivity, e);
                        loadingPopup.dismiss();
                        if (!isActivityShopping && mGoodsAdapter != null) {
                            mGoodsAdapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onResponse(Call call, String responseStr) throws IOException {
                        loadingPopup.dismiss();
                        try {
                            SLog.info("responseStr[%s]", responseStr);
                            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                            if (ToastUtil.checkError(_mActivity, responseObj)) {
                                if (!isActivityShopping) {
                                    mGoodsAdapter.loadMoreFail();
                                }
                                return;
                            }
                            hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                            if (associateSearchTime > 0) {
                                SLog.info("搜過商品，來搜商店了");
                            }
                            if (responseObj.exists("datas.isHaveStore")) {
                                SLog.info("沒有對應商品");
                                isHaveStore = responseObj.getInt("datas.isHaveStore");
                                if (isHaveStore > 0&&associateSearchTime++<1) {
                                    SLog.info("沒搜到商品，去搜商店了");
                                    doSearch(SearchType.STORE,currPage,keyword,filter);
                                    associateSearchTime=0;
                                    return;
                                }
                            }
                            initSearchGoods(getView());

                            SLog.info("page[%d], hasMore[%s]", page, hasMore);
                            if (!hasMore && !isActivityShopping) {
                                mGoodsAdapter.loadMoreEnd();
                                mGoodsAdapter.setEnableLoadMore(false);
                            }

                            // 如果是加載第一頁的數據，先清除舊數據
                            if (page == 1 || isActivityShopping) {
                                goodsItemPairList.clear();
                            }

                            if (isActivityShopping) {
                                goodsItemPairList.add(new GoodsSearchItemPair(Constant.ITEM_TYPE_DOUBLE_ELEVEN_BANNER));
                            }

                            EasyJSONArray easyJSONArray = responseObj.getSafeArray("datas.goodsList");
                            GoodsSearchItemPair pair = null;
                            for (Object object : easyJSONArray) {
                                EasyJSONObject goods = (EasyJSONObject) object;

                                String imageSrc = goods.getSafeString("imageSrc");
                                String storeAvatarUrl = goods.getSafeString("storeAvatar");
                                if (StringUtil.isEmpty(storeAvatarUrl)) {
                                    storeAvatarUrl = goods.getSafeString("storeAvatarUrl");
                                }
                                int storeId = goods.getInt("storeId");
                                String storeName = goods.getSafeString("storeName");
                                int commonId = goods.getInt("commonId");
                                String goodsName = goods.getSafeString("goodsName");
                                String jingle  = goods.getSafeString("jingle");
                                float price;
                                int appUsable = goods.getInt("appUsable");
                                if (appUsable > 0) {
                                    price = (float) goods.getDouble("appPrice0");
                                } else {
                                    price = (float) goods.getDouble("batchPrice2");
                                }


                                float extendPrice0 = (float) goods.getDouble("extendPrice0");
                                float batchPrice0 = (float) goods.getDouble("batchPrice0");

                                int promotionState = goods.getInt("promotionState");
                                int promotionType = goods.getInt("promotionType");

                                /*
                                    限時折扣條件
                                    promotionState = 1
                                    promotionType = 1
                                    appUsable = 1
                                 */
                                boolean showDiscountLabel = appUsable == 1 && promotionState == 1 && promotionType == 1;

                                String nationalFlag = StringUtil.normalizeImageUrl(goods.getSafeString("adminCountry.nationalFlag"));
                                GoodsSearchItem goodsSearchItem = new GoodsSearchItem(imageSrc, storeAvatarUrl, storeId,
                                        storeName, commonId, goodsName, jingle, price, nationalFlag);

                                goodsSearchItem.extendPrice0 = extendPrice0;
                                goodsSearchItem.batchPrice0 = batchPrice0;
                                goodsSearchItem.showDiscountLabel = showDiscountLabel;


                                int isPinkage = goods.getInt("isPinkage");
                                int isGift = goods.getInt("isGift");
                                goodsSearchItem.isFreightFree = (isPinkage == 1);
                                goodsSearchItem.hasGift = (isGift == 1);
                                goodsSearchItem.hasDiscount = (appUsable == 1);

                                if (pair == null) {
                                    pair = new GoodsSearchItemPair(Constant.ITEM_TYPE_NORMAL);
                                    pair.left = goodsSearchItem;
                                } else {
                                    pair.right = goodsSearchItem;
                                    goodsItemPairList.add(pair);
                                    pair = null;
                                }
                            }

                            if (pair != null) { // 如果是奇数项， 并且是最后一项，则满足这个条件
                                goodsItemPairList.add(pair);
                                pair = null;
                            }

                            if (!hasMore && !isActivityShopping) {
                                // 如果全部加載完畢，添加加載完畢的提示
                                goodsItemPairList.add(new GoodsSearchItemPair(Constant.ITEM_TYPE_LOAD_END_HINT));
                            }
                            SLog.info("goodsItemPairList.size[%d]", goodsItemPairList.size());
                            if (!isActivityShopping) {
                                mGoodsAdapter.loadMoreComplete();
                            }
                            mGoodsAdapter.setNewData(goodsItemPairList);
                            currPage++;

                            Util.setButtonStatus(_mActivity, btnPrevPage, currPage >= 2);
                            Util.setButtonStatus(_mActivity, btnNextPage, hasMore);

                            if (!isFilterCategoryLoaded) {
                                isFilterCategoryLoaded = true;

                                if (responseObj.exists("datas.filter.categoryNavVoList")) {
                                    // 讀取過濾條件數據
                                    EasyJSONArray categoryNavVoList = responseObj.getSafeArray("datas.filter.categoryNavVoList");
                                    for (Object object : categoryNavVoList) {
                                        EasyJSONObject categoryNavVo = (EasyJSONObject) object;
                                        int headId = categoryNavVo.getInt("categoryId");
                                        String headName = categoryNavVo.getSafeString("categoryName");
                                        FilterCategoryItem head = new FilterCategoryItem(headId, headName);

                                        FilterCategoryGroup group = new FilterCategoryGroup();
                                        group.head = head;
                                        EasyJSONArray categoryList = categoryNavVo.getSafeArray("categoryList");
                                        for (Object object2 : categoryList) {
                                            EasyJSONObject category = (EasyJSONObject) object2;

                                            int categoryId = category.getInt("categoryId");
                                            String categoryName = category.getSafeString("categoryName");

                                            FilterCategoryItem item = new FilterCategoryItem(categoryId, categoryName);
                                            group.list.add(item);
                                        }
                                        filterCategoryGroupList.add(group);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        }
                    }
                });
            } else if (searchType == SearchType.STORE) {
                params = Util.upLocation(params);
                SLog.info("params[%s]", params);
                Api.getUI(Api.PATH_SEARCH_STORE, params, new UICallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showNetworkError(_mActivity, e);
                        loadingPopup.dismiss();
                        if (mStoreAdapter != null) {
                            mStoreAdapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onResponse(Call call, String responseStr) throws IOException {
                        loadingPopup.dismiss();
                        try {
                            SLog.info("responseStr[%s]", responseStr);
                            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                            if (ToastUtil.checkError(_mActivity, responseObj)) {
                                mStoreAdapter.loadMoreFail();
                                return;
                            }

                            hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                            if (associateSearchTime > 0) {
                                SLog.info("搜過商店，來搜商品了");
                            }
                            if (responseObj.exists("datas.isHaveGoods")) {
                                isHaveGoods = responseObj.getInt("datas.isHaveGoods");
                                if (isHaveGoods > 0&&associateSearchTime++<1) {
                                    SLog.info("沒搜到商店，去搜商品了");

                                    doSearch(SearchType.GOODS,currPage,keyword,filter);
                                    associateSearchTime=0;
                                    return;
                                }
                            }

                            initSearchStore(getView());
                            SLog.info("hasMore[%s]", hasMore);
                            if (!hasMore) {
                                mStoreAdapter.loadMoreEnd();
                                mStoreAdapter.setEnableLoadMore(false);
                            }

                            // 首次初始化【所在地】列表
                            if (locationItemList == null) {
                                locationItemList = new ArrayList<>();

                                EasyJSONArray searchStoreAreaVoList = responseObj.getSafeArray("datas.searchStoreAreaVoList");
                                for (Object object : searchStoreAreaVoList) {
                                    EasyJSONObject searchStoreAreaVo = (EasyJSONObject) object;

                                    int id = searchStoreAreaVo.getInt("areaId");
                                    String name = searchStoreAreaVo.getSafeString("region");
                                    BizCircleItem bizCircleItem = new BizCircleItem(id, name, null); // 只有子項才可選，所以bizCircleId為null
                                    bizCircleItem.subItemList = new ArrayList<>();
                                    // 添加【全部】按鈕
                                    bizCircleItem.subItemList.add(new BizCircleItem(id, "全部", new BizCircleId(BizCircleId.ID_TYPE_AREA_ID, id)));

                                    EasyJSONArray areaList = searchStoreAreaVo.getSafeArray("areaList");
                                    for (Object object2 : areaList) {
                                        EasyJSONObject area = (EasyJSONObject) object2;
                                        int subId = area.getInt("areaId");
                                        String subName = area.getSafeString("areaName");

                                        bizCircleItem.subItemList.add(new BizCircleItem(subId, subName,  new BizCircleId(BizCircleId.ID_TYPE_AREA_ID, id)));
                                    }

                                    locationItemList.add(bizCircleItem);
                                }
                            }

                            // 首次初始化【商圈】列表
                            if (bizCircleItemList == null) {
                                bizCircleItemList = new ArrayList<>();

                                EasyJSONArray businessDistrictMap = responseObj.getSafeArray("datas.businessDistrictMap");
                                for (Object object : businessDistrictMap) {
                                    EasyJSONObject searchStoreAreaVo = (EasyJSONObject) object;

                                    int id = searchStoreAreaVo.getInt("districtAreaId");
                                    String name = searchStoreAreaVo.getSafeString("areaName");
                                    BizCircleItem bizCircleItem = new BizCircleItem(id, name, null); // 只有子項才可選，所以bizCircleId為null
                                    bizCircleItem.subItemList = new ArrayList<>();
                                    // 添加【全部】按鈕
                                    bizCircleItem.subItemList.add(new BizCircleItem(id, "全部", new BizCircleId(BizCircleId.ID_TYPE_CIRCLE_AREA_ID, id)));

                                    EasyJSONArray businessDistrictList = searchStoreAreaVo.getSafeArray("businessDistrictList");
                                    for (Object object2 : businessDistrictList) {
                                        EasyJSONObject area = (EasyJSONObject) object2;
                                        int subId = area.getInt("districtId");
                                        String subName = area.getSafeString("districtName");

                                        bizCircleItem.subItemList.add(new BizCircleItem(subId, subName, new BizCircleId(BizCircleId.ID_TYPE_CIRCLE_ID, id)));
                                    }

                                    bizCircleItemList.add(bizCircleItem);
                                }
                            }


                            if (page == 1) {
                                storeItemList.clear();
                            }
                            EasyJSONArray easyJSONArray = responseObj.getSafeArray("datas.storeList");
                            for (Object object : easyJSONArray) {
                                EasyJSONObject store = (EasyJSONObject) object;
                                SLog.info("storeInfo[%s]",store);
                                int storeId = store.getInt("storeId");
                                String storeAvatarUrl = store.getSafeString("storeAvatarUrl");
                                int storeView = store.getInt("storeView");
                                String storeName = store.getSafeString("storeName");
                                String className = store.getSafeString("className");
                                String mainBusiness = store.getSafeString("storeZy");
                                String storeFigureImage = store.getSafeString("storeFigureImage");
                                float distance = Float.valueOf(store.getSafeString("distance"));
                                String shopDay = store.getSafeString("shopDay");
                                int likeCount = store.getInt("likeCount");
                                int followCount=store.getInt("storeCollect");
                                int viewCount=store.getInt("likeCount");
                                EasyJSONArray servicesList = store.getSafeArray("storeServiceStaffVoList");
                                CustomerServiceStaff staff = new CustomerServiceStaff();
                                if (servicesList != null && servicesList.length() > 0) {
                                    Util.packStaffInfo(staff, (EasyJSONObject)servicesList.get(0));
                                }
                                int goodsCommonCount = store.getInt("goodsCommonCount");

                                // 獲取商店的前3個產品的圖片
                                List<String> goodsImageList = new ArrayList<>();
                                EasyJSONArray goodsCommonList = store.getSafeArray("goodsCommonList");
                                for (Object object2 : goodsCommonList) {
                                    EasyJSONObject goodsCommon = (EasyJSONObject) object2;
                                    goodsImageList.add(goodsCommon.getSafeString("imageSrc"));
                                }

                                List<JobInfoItem> jobList = new ArrayList<>();
                                if (store.exists("hrPostVoList")) {
                                    EasyJSONArray hrPostVoList = store.getSafeArray("hrPostVoList");

                                    int count = 0;
                                    for (Object object2 : hrPostVoList) {
                                        EasyJSONObject hrPostVo = (EasyJSONObject) object2;
                                        JobInfoItem jobInfoItem = new JobInfoItem();
                                        jobInfoItem.postName = hrPostVo.getSafeString("postTitle");
                                        jobInfoItem.salaryRange = hrPostVo.getSafeString("salaryRange");
                                        jobInfoItem.salaryType = hrPostVo.getSafeString("salaryType");

                                        jobList.add(jobInfoItem);
                                        count++;

                                        if (count >= 2) {
                                            break;
                                        }
                                    }
                                }


                                storeItemList.add(new StoreSearchItem(storeId, storeView, storeAvatarUrl, storeName, className, mainBusiness,
                                        storeFigureImage, distance, shopDay, likeCount,followCount,viewCount, goodsCommonCount, goodsImageList, jobList,staff));
                            }

                            if (!hasMore) {
                                // 如果全部加載完畢，添加加載完畢的提示
                                storeItemList.add(new StoreSearchItem());
                            }

                            SLog.info("storeItemList.size[%d]", storeItemList.size());
                            mStoreAdapter.loadMoreComplete();
                            mStoreAdapter.setNewData(storeItemList);
                            currPage++;
                        } catch (Exception e) {
                            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        }
                    }
                });
            } else {

            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    private EasyJSONObject setSearchParams(int page, EasyJSONObject filter) {
        EasyJSONObject params = EasyJSONObject.generate();
        String token = User.getToken();
        if (paramsObj.exists("is_double_eleven")) {
            isActivityShopping = true;
        }
        try {
            for (Map.Entry<String, Object> param : paramsObj.entrySet()) {

                    params.set(param.getKey(), param.getValue().toString());
                }
            // 合并參數
            if (filter != null) {
                for (Map.Entry<String, Object> param : filter.entrySet()) {
                    params.set(param.getKey(), param.getValue().toString());
                }
            }
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }
            params.set("page", page);

        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        return params;
    }

    private boolean doAssociateSearch(SearchType searchType, String keyword) {
        ++associateSearchTime;
        if (searchType == SearchType.GOODS) {
            if (isHaveGoods > 0) {
                return false;
            } else {
                if (isHaveStore > 0) {
                    return false;
                }
                searchType = SearchType.STORE;
            }

        } else if(searchType==SearchType.STORE) {
            if (isHaveStore > 0) {
                return false;
            } else {
                if (isHaveGoods > 0) {
                    return false;
                }
                searchType = SearchType.GOODS;
            }

        }
        SLog.info("執行關聯搜索%s",searchType.toString());
        doSearch(searchType,currPage,keyword,currFilter);
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_clear_all:
                etKeyword.setText("");
                break;
            case R.id.btn_goods_filter:
                showGoodsFilterPopup();
                break;
            case R.id.btn_goto_cart:
                if (User.isLogin()) {
                    Util.startFragment(CartFragment.newInstance(true));
                } else {
                    Util.showLoginFragment();
                }

                break;
            case R.id.btn_goto_top:
                rvSearchResultList.scrollToPosition(0);
                break;
                /*
            case R.id.btn_store_search_order_general:
                if (storeSortButtonIndex == STORE_SEARCH_SORT_GENERAL) {
                    return;
                }
                doSearch(searchType, keyword, null);
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlack);
                storeSortButtonIndex = STORE_SEARCH_SORT_GENERAL;
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlue);
                break;
            case R.id.btn_store_search_order_store_open:
                if (storeSortButtonIndex == STORE_SEARCH_SORT_STORE_OPEN) {
                    return;
                }
                doSearch(searchType, keyword, EasyJSONObject.generate("sort", "startBusiness_desc"));
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlack);
                storeSortButtonIndex = STORE_SEARCH_SORT_STORE_OPEN;
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlue);
                break;
            case R.id.btn_store_search_order_follow:
                if (storeSortButtonIndex == STORE_SEARCH_SORT_FOLLOW) {
                    return;
                }
                doSearch(searchType, keyword, EasyJSONObject.generate("sort", "collect_desc"));
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlack);
                storeSortButtonIndex = STORE_SEARCH_SORT_FOLLOW;
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlue);
                break;
                */
            case R.id.btn_sort_goods_general:
                if (goodsSortButtonIndex == GOODS_SEARCH_SORT_GENERAL) {
                    return;
                }
                currPage = 0;
                doSearch(searchType,currPage + 1, keyword, null);
                switchGoodsSortIndicator(id);
                break;
            case R.id.btn_sort_goods_sale:
                if (goodsSortButtonIndex == GOODS_SEARCH_SORT_SALE) {
                    return;
                }
                currPage = 0;
                currFilter = EasyJSONObject.generate("sort", "sale_desc");
                doSearch(searchType, currPage + 1,keyword, currFilter);
                switchGoodsSortIndicator(id);
                break;
            case R.id.btn_sort_goods_price:
                if (goodsSortButtonIndex == GOODS_SEARCH_SORT_PRICE) { // 再次點擊價格排序的話，切換升降序
                    SLog.info("HERE");
                    sortPriceAsc = !sortPriceAsc;
                } else {
                    SLog.info("HERE");
                    sortPriceAsc = true;
                }

                SLog.info("sortPriceAsc[%s]", sortPriceAsc);
                String priceSortStr = "price_desc";
                if (sortPriceAsc) {
                    priceSortStr = "price_asc";
                }

                currPage = 0;
                currFilter = EasyJSONObject.generate("sort", priceSortStr);
                doSearch(searchType, currPage + 1,keyword, currFilter);
                switchGoodsSortIndicator(id);
                break;
            case R.id.btn_follow:
                followStatus = !followStatus;
                if (followStatus) {
                    tvFollow.setTextColor(twBlue);
                } else {
                    tvFollow.setTextColor(twBlack);
                }
                int follow = 0;
                if (followStatus) {
                    follow = 1;
                }

                currPage = 0;
                currFilter = EasyJSONObject.generate("follow", follow);
                doSearch(SearchType.STORE, currPage + 1, keyword, currFilter);
                break;
            case R.id.btn_sort:
                togglePopup(PopupType.STORE_SORT_TYPE, v, id);
                break;
            case R.id.btn_location:
                togglePopup(PopupType.STORE_FILTER_LOCATION, v, id);
                break;
            case R.id.btn_biz_circle:
                togglePopup(PopupType.STORE_FILTER_BIZ_CIRCLE, v, id);
                break;
            case R.id.btn_recruitment:
                hrStatus = !hrStatus;
                if (hrStatus) {
                    tvRecruitment.setTextColor(twBlue);
                } else {
                    tvRecruitment.setTextColor(twBlack);
                }
                int hr = 0;
                if (hrStatus) {
                    hr = 1;
                }

                currPage = 0;
                currFilter = EasyJSONObject.generate("hr", hr);
                doSearch(SearchType.STORE, currPage + 1, keyword, currFilter);
                break;
            case R.id.btn_publish_want_post:
                Util.startFragment(AddPostFragment.newInstance(false));
                break;
            case R.id.btn_prev_page:
                if (currPage <= 1) {
                    return;
                }
                doSearch(searchType, currPage - 1, keyword, currFilter);
                break;
            case R.id.btn_next_page:
                if (!hasMore) {
                    return;
                }
                doSearch(searchType, currPage + 1, keyword, currFilter);
                break;
            default:
                break;
        }
    }

    private void togglePopup(PopupType popupType, View view, int id) {
        currSelId = id;
        if (storeFilterPopup == null) {
            storeFilterPopup = (StoreFilterPopup) new XPopup.Builder(_mActivity)
                    .atView(view)
                    .setPopupCallback(new XPopupCallback() {
                        @Override
                        public void onShow() {
                            SLog.info("显示了");
                            if (currSelId == R.id.btn_sort) {
                                btnSort.setBackgroundResource(R.drawable.store_filter_btn_sel_bg);
                                tvSort.setTextColor(twBlue);
                                iconSort.setImageResource(R.drawable.icon_store_filter_expand_blue);
                            } else if (currSelId == R.id.btn_location) {
                                btnLocation.setBackgroundResource(R.drawable.store_filter_btn_sel_bg);
                                tvLocation.setTextColor(twBlue);
                                iconLocation.setImageResource(R.drawable.icon_store_filter_expand_blue);
                            } else if (currSelId == R.id.btn_biz_circle) {
                                btnBizCircle.setBackgroundResource(R.drawable.store_filter_btn_sel_bg);
                                tvBizCircle.setTextColor(twBlue);
                                iconBizCircle.setImageResource(R.drawable.icon_store_filter_expand_blue);
                            }
                        }
                        @Override
                        public void onDismiss() {
                            SLog.info("关闭了");

                            btnSort.setBackground(null);
                            tvSort.setTextColor(twBlack);
                            iconSort.setImageResource(R.drawable.icon_store_filter_expand_black);

                            btnLocation.setBackground(null);
                            tvLocation.setTextColor(twBlack);
                            iconLocation.setImageResource(R.drawable.icon_store_filter_expand_black);

                            btnBizCircle.setBackground(null);
                            tvBizCircle.setTextColor(twBlack);
                            iconBizCircle.setImageResource(R.drawable.icon_store_filter_expand_black);
                        }
                    })
                    .asCustom(new StoreFilterPopup(_mActivity, popupType, generalItemSelectedId,
                            popupType == PopupType.STORE_FILTER_LOCATION ? locationItemList : bizCircleItemList, this));
            storeFilterPopup.show();
        } else {
            storeFilterPopup.dismiss();
            storeFilterPopup = null;
        }
    }


    /**
     * 切換當前產品排序的指示
     * @param id 點擊到哪個按鈕
     */
    private void switchGoodsSortIndicator(int id) {
        goodsSortButtons[goodsSortButtonIndex].setTextColor(twBlack);
        // 如果原來是用價格排序，則隱藏價格升序、降序圖標
        if (goodsSortButtonIndex == GOODS_SEARCH_SORT_PRICE) {
            iconPriceOrder.setVisibility(View.GONE);
        }
        if (id == R.id.btn_sort_goods_general) {
            goodsSortButtonIndex = GOODS_SEARCH_SORT_GENERAL;
        } else if (id == R.id.btn_sort_goods_sale) {
            goodsSortButtonIndex = GOODS_SEARCH_SORT_SALE;
        } else {
            goodsSortButtonIndex = GOODS_SEARCH_SORT_PRICE;
        }
        goodsSortButtons[goodsSortButtonIndex].setTextColor(twBlue);
        // 如果現在是用價格排序，則顯示價格升序、降序圖標
        if (goodsSortButtonIndex == GOODS_SEARCH_SORT_PRICE) {
            if (sortPriceAsc) {
                iconPriceOrder.setImageResource(R.drawable.icon_price_sort_asc);
            } else {
                iconPriceOrder.setImageResource(R.drawable.icon_price_sort_desc);
            }
            iconPriceOrder.setVisibility(View.VISIBLE);
        }
    }


    private void showGoodsFilterPopup() {
        new XPopup.Builder(_mActivity)
                //右边
                .popupPosition(PopupPosition.Right)
                //启用状态栏阴影
                .hasStatusBarShadow(true)
                .asCustom(new GoodsFilterDrawerPopupView(_mActivity, filterCategoryGroupList, this))
                .show();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        try {
            if (type == PopupType.STORE_SORT_TYPE) {
                generalItemSelectedId = id;
                tvSort.setText((String) extra);

                currPage = 0;
                currFilter = EasyJSONObject.generate();
                if (generalItemSelectedId == 1) {
                    // 不需要傳參數
                } else if (generalItemSelectedId == 2) {
                    currFilter.set("sort", STORE_SORT_FOLLOW);
                } else if (generalItemSelectedId == 3) {
                    currFilter.set("sort", STORE_SORT_OPEN);
                }

                doSearch(searchType, currPage + 1, keyword, currFilter);
            } else if (type == PopupType.STORE_FILTER_LOCATION || type == PopupType.STORE_FILTER_BIZ_CIRCLE) {
                bizCircleId = (BizCircleId) extra;

                if (type == PopupType.STORE_FILTER_LOCATION) {
                    tvLocation.setTextColor(twBlue);
                    iconLocation.setImageResource(R.drawable.icon_store_filter_expand_blue);

                    tvBizCircle.setTextColor(twBlack);
                    iconBizCircle.setImageResource(R.drawable.icon_store_filter_expand_black);
                } else {
                    tvLocation.setTextColor(twBlack);
                    iconLocation.setImageResource(R.drawable.icon_store_filter_expand_black);

                    tvBizCircle.setTextColor(twBlue);
                    iconBizCircle.setImageResource(R.drawable.icon_store_filter_expand_blue);
                }

                currPage = 0;
                currFilter = EasyJSONObject.generate();
                if (bizCircleId.idType == BizCircleId.ID_TYPE_AREA_ID) {
                    currFilter.set("area", bizCircleId.id);
                } else if (bizCircleId.idType == BizCircleId.ID_TYPE_CIRCLE_ID) {
                    currFilter.set("districtId", bizCircleId.id);
                } else if (bizCircleId.idType == BizCircleId.ID_TYPE_CIRCLE_AREA_ID) {
                    currFilter.set("districtAreaId", bizCircleId.id);
                }

                if (followStatus) {
                    currFilter.set("follow", 1);
                }
                doSearch(searchType, currPage + 1, keyword, currFilter);
            } else if (type == PopupType.DEFAULT) {
                currPage = 0;
                currFilter = (EasyJSONObject) extra;
                doSearch(searchType, currPage + 1, keyword, currFilter);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested, hasMore[%s]", hasMore);

        if (!hasMore) {
            if (searchType == SearchType.GOODS) {
                mGoodsAdapter.setEnableLoadMore(false);
            } else {
                mStoreAdapter.setEnableLoadMore(false);
            }

            return;
        }
        doSearch(searchType, currPage + 1, keyword, currFilter);
    }


    private void showFloatButton() {
        if (floatButtonShown){
            return;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
        layoutParams.rightMargin = Util.dip2px(_mActivity, 0);
        llFloatButtonContainer.setLayoutParams(layoutParams);
        floatButtonShown = true;
    }

    private void hideFloatButton() {
        if (!floatButtonShown) {
            return;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
        layoutParams.rightMargin = Util.dip2px(_mActivity,  -30.25f);
        llFloatButtonContainer.setLayoutParams(layoutParams);
        floatButtonShown = false;
    }
}
