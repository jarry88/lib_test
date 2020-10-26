package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsSearchResultAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.entity.FilterCategoryGroup;
import com.ftofs.twant.entity.FilterCategoryItem;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UmengAnalytics;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class CrossBorderCategoryFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    int categoryId;
    String categoryName;


    // SearchResultFragment searchResultFragment;

    RecyclerView rvSearchResultList;
    GoodsSearchResultAdapter mGoodsAdapter;
    List<GoodsSearchItemPair> goodsItemPairList = new ArrayList<>();
    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    LinearLayout llFloatButtonContainer;

    public static CrossBorderCategoryFragment newInstance(int categoryId, String categoryName) {
        CrossBorderCategoryFragment fragment = new CrossBorderCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.categoryId = categoryId;
        fragment.categoryName = categoryName;
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cross_border_category, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);
        Util.setOnClickListener(view, R.id.btn_goto_cart, this);
        Util.setOnClickListener(view, R.id.btn_goto_top, this);

        rvSearchResultList = view.findViewById(R.id.rv_goods_list);
        rvSearchResultList.setLayoutManager(new LinearLayoutManager(_mActivity));
        mGoodsAdapter = new GoodsSearchResultAdapter(_mActivity, goodsItemPairList);
        mGoodsAdapter.setEnableLoadMore(true);
        mGoodsAdapter.setOnLoadMoreListener(this, rvSearchResultList);
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
                } else if (id == R.id.icon_publish_want_post) {
                    ApiUtil.addPost(_mActivity,false);
                }
            }
        });
        rvSearchResultList.setAdapter(mGoodsAdapter);
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
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        loadData(currPage + 1);
    }

    private void loadData(int page) {
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        String url = Api.PATH_SEARCH_GOODS;
        EasyJSONObject params = EasyJSONObject.generate(
                "cat", categoryId,
                "page", page,
                "modal", Constant.GOODS_TYPE_CROSS_BORDER
        );

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                ToastUtil.showNetworkError(_mActivity, e);
                loadingPopup.dismiss();
                mGoodsAdapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        mGoodsAdapter.loadMoreFail();

                        return;
                    }
                    hasMore = responseObj.optBoolean("datas.pageEntity.hasMore");

                    SLog.info("page[%d], currPage[%s], goodsItemPairList.size[%d], hasMore[%s]",
                            page, currPage, goodsItemPairList.size(), hasMore);
                    if (!hasMore) {
                        mGoodsAdapter.loadMoreEnd();
                        mGoodsAdapter.setEnableLoadMore(false);
                    }

                    // 如果是加載第一頁的數據，先清除舊數據
                    if (page == 1) {
                        goodsItemPairList.clear();
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
                        double price;
                        int appUsable = goods.getInt("appUsable");
                        if (appUsable > 0) {
                            price =  goods.getDouble("appPrice0");
                        } else {
                            price =  goods.getDouble("batchPrice2");
                        }


                        double extendPrice0 =  goods.getDouble("extendPrice0");
                        double batchPrice0 =  goods.getDouble("batchPrice0");

                        int promotionState = goods.getInt("promotionState");
                        int promotionType = goods.getInt("promotionType");

                                /*
                                    限時折扣條件
                                    promotionState = 1
                                    promotionType = 1
                                    appUsable = 1
                                 */
                        boolean showDiscountLabel = appUsable == 1 && promotionState == 1 && promotionType == 1;

                        String nationalFlag = "";
                        if (goods.exists("adminCountry.nationalFlag")) {
                            nationalFlag = StringUtil.normalizeImageUrl(goods.getSafeString("adminCountry.nationalFlag"));
                        }
                        GoodsSearchItem goodsSearchItem = new GoodsSearchItem(imageSrc, storeAvatarUrl, storeId,
                                storeName, commonId, goodsName, jingle, price, nationalFlag);

                        goodsSearchItem.extendPrice0 = extendPrice0;
                        goodsSearchItem.batchPrice0 = batchPrice0;
                        goodsSearchItem.showDiscountLabel = showDiscountLabel;
                        goodsSearchItem.goodsModel = StringUtil.safeModel(goods);
                        SLog.info("nationalFlag[%s]", goodsSearchItem.nationalFlag);


                        int isPinkage = goods.getInt("isPinkage");
                        int isGift = goods.getInt("isGift");
                        goodsSearchItem.isFreightFree = (isPinkage == 1);
                        goodsSearchItem.hasGift = (isGift == 1);
                        goodsSearchItem.hasDiscount = (appUsable == 1);
                        goodsSearchItem.tariffEnable = goods.optInt("tariffEnable");

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

                    if (currPage == 0 && goodsItemPairList.size() == 0) {
                        goodsItemPairList.add(new GoodsSearchItemPair(Constant.ITEM_TYPE_NO_DATA));
                    }
                    if (!hasMore) {
                        // 如果全部加載完畢，添加加載完畢的提示
                        goodsItemPairList.add(new GoodsSearchItemPair(Constant.ITEM_TYPE_LOAD_END_HINT));
                    }

                    mGoodsAdapter.loadMoreComplete();
                    mGoodsAdapter.setNewData(goodsItemPairList);
                    SLog.info("goodsItemPairList.size[%d]", goodsItemPairList.size());
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested, hasMore[%s]", hasMore);

        if (!hasMore) {
            mGoodsAdapter.setEnableLoadMore(false);
            return;
        }

        loadData(currPage + 1);
    }

    private void showFloatButton() {
        llFloatButtonContainer.setTranslationX(0);
    }
    private void hideFloatButton() {
        llFloatButtonContainer.setTranslationX(Util.dip2px(_mActivity, 30.5f));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_goto_cart) {
            HashMap<String, Object> analyticsDataMap = new HashMap<>();
            UmengAnalytics.onEventObject(UmengAnalyticsActionName.TARIFF_BUY_ADDCART, analyticsDataMap);
            if (User.isLogin()) {
                Util.startFragment(CartFragment.newInstance(true));
            } else {
                Util.showLoginFragment(requireContext());
            }
        } else if (id == R.id.btn_goto_top) {
            rvSearchResultList.scrollToPosition(0);
        }
    }
}

