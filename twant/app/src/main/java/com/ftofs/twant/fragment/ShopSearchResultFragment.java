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
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.entity.GoodsPair;
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
    ShopGoodsGridAdapter shopGoodsGridAdapter;
    List<GoodsPair> goodsPairList = new ArrayList<>();  // 每行顯示兩個產品
    GoodsPair currGoodsPair;  // 當前處理的goodsPair，考慮到分頁時，加載到奇數個產品，所以要預存一下GoodsPair

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
                if (id == R.id.img_left_goods ) {
                    goods = goodsPair.leftGoods;
                    SLog.info("%s",commonId);
                    commonId = goods.id;
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));

                }else if(id==R.id.img_right_goods){
                    goods = goodsPair.rightGoods;
                    SLog.info("%s",commonId);
                    commonId = goods.id;
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                }
                int userId = User.getUserId();

                if (id == R.id.btn_add_to_cart_left || id == R.id.btn_add_to_cart_right) {
                    if (id == R.id.btn_add_to_cart_left) {
                        commonId = goodsPair.leftGoods.commonId;
                    }else {
                        commonId = goodsPair.rightGoods.commonId;

                    }
                    if (userId > 0) {
                        showSpecSelectPopup(commonId);
                    } else {
                        Util.showLoginFragment(requireContext());
                    }
                }
            }
        });
        shopGoodsGridAdapter.setEnableLoadMore(true);
        shopGoodsGridAdapter.setOnLoadMoreListener(this, rvList);
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
                            goods.goodsModal = StringUtil.safeModel(goodsObject);

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

    private void showSpecSelectPopup(int commonId) {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, Constant.ACTION_ADD_TO_CART, commonId, null, null, null, 1, null, null, 0,2, null))
                .show();
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



