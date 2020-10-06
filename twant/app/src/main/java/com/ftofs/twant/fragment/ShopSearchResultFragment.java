package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ShopGoodsGridAdapter;
import com.ftofs.twant.adapter.ShopGoodsSearchResultAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.entity.GoodsPair;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 店鋪內商品搜索結果頁
 * @author zwm
 */
public class ShopSearchResultFragment extends BaseFragment implements View.OnClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    int storeId;
    String keyword;

    RecyclerView rvList;
    ShopGoodsSearchResultAdapter shopGoodsGridAdapter;
    List<GoodsSearchItemPair> goodsPairList = new ArrayList<>();  // 每行顯示兩個產品

    // 當前加載第幾頁
    int currPage = 0;
    boolean hasMore;

    EditText etKeyword;
    View btnClearKeyWord;

    TextView tvTextGeneral;
    TextView tvTextSale;
    TextView tvTextNewest;
    TextView tvTextPrice;

    ImageView iconPriceOrder;

    int twBlack;
    int twBlue;

    public static final int SORT_DEFAULT = 0;
    public static final int SORT_PRICE_DESC = 1;
    public static final int SORT_PRICE_ASC = 2;
    public static final int SORT_SALE_DESC = 3;
    public static final int SORT_NEW_DESC = 4;

    private int sortCriteriaIndex = SORT_DEFAULT;

    // 排序標準
    private String[] sortCriteriaArr = new String[] {
            "default_desc", // 默認
            "price_desc",   // 價格降序
            "price_asc",    // 價格升序
            "sale_desc",    // 销量降序
            "new_desc",     // 上新時間降序
    };

    public static ShopSearchResultFragment newInstance(int storeId, String keyword) {
        Bundle args = new Bundle();

        ShopSearchResultFragment fragment = new ShopSearchResultFragment();
        fragment.setArguments(args);
        fragment.storeId = storeId;
        fragment.keyword = keyword;

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_search_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etKeyword = view.findViewById(R.id.et_keyword);
        Util.setOnClickListener(view, R.id.btn_back, this);

        twBlack = getResources().getColor(R.color.tw_black, null);
        twBlue = getResources().getColor(R.color.tw_blue, null);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        shopGoodsGridAdapter = new ShopGoodsSearchResultAdapter(_mActivity, goodsPairList);
        shopGoodsGridAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsSearchItemPair pair = goodsPairList.get(position);
                // 點擊加載完成提示，忽略
                if (pair.getItemType() == Constant.ITEM_TYPE_FOOTER) {
                    return;
                }

                int id = view.getId();
                if (id == R.id.btn_goto_store_left || id == R.id.btn_goto_store_right) {
                    int storeId;
                    if (id == R.id.btn_goto_store_left) {
                        storeId = pair.left.storeId;
                    } else {
                        storeId = pair.right.storeId;
                    }

                    Util.startFragment(ShopMainFragment.newInstance(storeId));
                } else if (id == R.id.cl_container_left || id == R.id.cl_container_right) {
                    int commonId;
                    if (id == R.id.cl_container_left) {
                        commonId = pair.left.commonId;
                    } else {
                        commonId = pair.right.commonId;
                    }

                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                }
            }
        });
        shopGoodsGridAdapter.setEnableLoadMore(true);
        shopGoodsGridAdapter.setOnLoadMoreListener(this, rvList);

        // 設置空頁面
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.layout_placeholder_no_data, null, false);
        // 設置空頁面的提示語
        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
        tvEmptyHint.setText(R.string.no_data_hint);
        shopGoodsGridAdapter.setEmptyView(emptyView);

        rvList.setAdapter(shopGoodsGridAdapter);


        btnClearKeyWord = view.findViewById(R.id.btn_clear_all);
        if (!StringUtil.isEmpty(keyword)) {
            etKeyword.setText(keyword);
            btnClearKeyWord.setVisibility(View.VISIBLE);
        } else {
            btnClearKeyWord.setVisibility(View.GONE);
        }
        btnClearKeyWord.setOnClickListener(this);

        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnClearKeyWord.setVisibility(View.VISIBLE);
                } else {
                    btnClearKeyWord.setVisibility(View.GONE);
                }
            }
        });
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = v.getText().toString().trim();
                    /*
                    if (StringUtil.isEmpty(text)) {
                        ToastUtil.error(_mActivity, getString(R.string.input_search_keyword_hint));
                        return true;
                    }
                     */

                    keyword = text;

                    reloadData();
                    hideSoftInput();
                    return true;
                }

                return false;
            }
        });

        tvTextGeneral = view.findViewById(R.id.tv_text_general);
        tvTextSale = view.findViewById(R.id.tv_text_sale);
        tvTextNewest = view.findViewById(R.id.tv_text_newest);
        tvTextPrice = view.findViewById(R.id.tv_text_price);

        iconPriceOrder = view.findViewById(R.id.icon_price_order);


        view.findViewById(R.id.btn_sort_goods_general).setOnClickListener(this);
        view.findViewById(R.id.btn_sort_goods_sale).setOnClickListener(this);
        view.findViewById(R.id.btn_sort_newest_goods).setOnClickListener(this);
        view.findViewById(R.id.btn_sort_goods_price).setOnClickListener(this);

        updateFilterView();

        loadStoreGoods(currPage + 1);
    }

    /**
     * 加載商店產品
     * @param page 第幾頁
     */
    private void loadStoreGoods(int page) {
        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "storeId", storeId,
                    "page", page
            );

            if (!StringUtil.isEmpty(keyword)) {
                params.set("keyword", keyword);
            }

            if (sortCriteriaIndex != SORT_DEFAULT) { // 如果不是默認排序，則加上排序條件
                params.set("sort", sortCriteriaArr[sortCriteriaIndex]);
            }

            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
            SLog.info("商店內產品搜索,params[%s]", params.toString());
            Api.getUI(Api.PATH_SEARCH_GOODS_IN_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    loadingPopup.dismiss();

                    ToastUtil.showNetworkError(_mActivity, e);
                    shopGoodsGridAdapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    SLog.info("responseStr[%s]", responseStr);
                    try {
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            shopGoodsGridAdapter.loadMoreFail();
                            return;
                        }

                        hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                        SLog.info("hasMore[%s]", hasMore);
                        if (!hasMore) {
                            shopGoodsGridAdapter.loadMoreEnd();
                            shopGoodsGridAdapter.setEnableLoadMore(false);
                        }

                        if (page == 1) {
                            goodsPairList.clear();
                        }
                        EasyJSONArray goodsArray = responseObj.getSafeArray("datas.goodsCommonList");
                        GoodsSearchItemPair pair = null;
                        for (Object object : goodsArray) {
                            EasyJSONObject goodsObject = (EasyJSONObject) object;

                            int commonId = goodsObject.getInt("commonId");
                            // 產品圖片
                            String goodsImageUrl = goodsObject.getSafeString("imageSrc");
                            String storeAvatarUrl = ""; // 本店的不需要店鋪頭像
                            String storeName = ""; // 本店的不需要店鋪名
                            // 產品名稱
                            String goodsName = goodsObject.getSafeString("goodsName");
                            // 賣點
                            String jingle = goodsObject.getSafeString("jingle");
                            // 獲取價格
                            double price = Util.getSpuPrice(goodsObject);
                            String nationalFlag = "";
                            if (goodsObject.exists("adminCountry.nationalFlag")) {
                                nationalFlag = StringUtil.normalizeImageUrl(goodsObject.getSafeString("adminCountry.nationalFlag"));
                            }

                            GoodsSearchItem goods = new GoodsSearchItem(goodsImageUrl, storeAvatarUrl, storeId,
                                    storeName, commonId, goodsName, jingle, price, nationalFlag);
                            goods.goodsModel = StringUtil.safeModel(goodsObject);

                            int appUsable = goodsObject.optInt("appUsable");
                            int isPinkage = goodsObject.optInt("isPinkage");
                            int isGift = goodsObject.optInt("isGift");
                            goods.isFreightFree = (isPinkage == 1);
                            goods.hasGift = (isGift == 1);
                            goods.hasDiscount = (appUsable == 1);
                            goods.tariffEnable = goodsObject.optInt("tariffEnable");

                            if (pair == null) {
                                pair = new GoodsSearchItemPair(Constant.ITEM_TYPE_NORMAL);
                                pair.left = goods;
                                goodsPairList.add(pair);
                            } else {
                                pair.right = goods;
                                pair = null;
                            }
                        }

                        if (!hasMore && goodsPairList.size() > 0) {
                            // 如果全部加載完畢，添加加載完畢的提示
                            goodsPairList.add(new GoodsSearchItemPair(Constant.ITEM_TYPE_FOOTER));
                        }

                        shopGoodsGridAdapter.setNewData(goodsPairList);
                        shopGoodsGridAdapter.loadMoreComplete();

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
     * 上滑加載更多
     */
    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            shopGoodsGridAdapter.setEnableLoadMore(false);
            return;
        }
        loadStoreGoods(currPage + 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_clear_all) {
            keyword = "";
            btnClearKeyWord.setVisibility(View.GONE);
            etKeyword.setText(keyword);
        } else if (id == R.id.btn_sort_goods_general) {
            if (sortCriteriaIndex == SORT_DEFAULT) {
                return;
            }
            sortCriteriaIndex = SORT_DEFAULT;
            reloadData();
            updateFilterView();
        } else if (id == R.id.btn_sort_goods_sale) {
            if (sortCriteriaIndex == SORT_SALE_DESC) {
                return;
            }
            sortCriteriaIndex = SORT_SALE_DESC;
            reloadData();
            updateFilterView();
        } else if (id == R.id.btn_sort_newest_goods) {
            if (sortCriteriaIndex == SORT_NEW_DESC) {
                return;
            }
            sortCriteriaIndex = SORT_NEW_DESC;
            reloadData();
            updateFilterView();
        } else if (id == R.id.btn_sort_goods_price) {
            if (sortCriteriaIndex == SORT_PRICE_ASC) {
                sortCriteriaIndex = SORT_PRICE_DESC;
            } else {
                sortCriteriaIndex = SORT_PRICE_ASC;
            }
            reloadData();
            updateFilterView();
        }
    }

    private void updateFilterView() {
        tvTextGeneral.setTextColor(twBlack);
        tvTextSale.setTextColor(twBlack);
        tvTextNewest.setTextColor(twBlack);
        tvTextPrice.setTextColor(twBlack);

        iconPriceOrder.setVisibility(View.GONE);

        if (sortCriteriaIndex == SORT_DEFAULT) {
            tvTextGeneral.setTextColor(twBlue);
        } else if (sortCriteriaIndex == SORT_SALE_DESC) {
            tvTextSale.setTextColor(twBlue);
        } else if (sortCriteriaIndex == SORT_NEW_DESC) {
            tvTextNewest.setTextColor(twBlue);
        } else if (sortCriteriaIndex == SORT_PRICE_ASC) {
            tvTextPrice.setTextColor(twBlue);
            iconPriceOrder.setImageResource(R.drawable.ic_sort_asc);
            iconPriceOrder.setVisibility(View.VISIBLE);
        } else if (sortCriteriaIndex == SORT_PRICE_DESC) {
            tvTextPrice.setTextColor(twBlue);
            iconPriceOrder.setImageResource(R.drawable.ic_sort_off);
            iconPriceOrder.setVisibility(View.VISIBLE);
        }
    }

    private void reloadData() {
        currPage = 0;
        loadStoreGoods(currPage + 1);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}



