package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.entity.SellerGoodsItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.adapter.SellerGoodsAdapter;
import com.ftofs.twant.seller.widget.SellerOperationPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SellerGoodsListPageFragment extends BaseFragment implements View.OnClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, SimpleCallback {
    int currTab;
    SimpleCallback simpleCallback;

    RecyclerView rvList;
    SellerGoodsAdapter sellerGoodsAdapter;
    ArrayList<SellerGoodsItem> sellerGoodsItemList = new ArrayList<>();

    int currPage = 0; // 当前加载到第几页
    boolean hasMore;

    String keyword;  // 商品搜索關鍵詞

    public static SellerGoodsListPageFragment newInstance(int currTab, SimpleCallback simpleCallback) {
        Bundle args = new Bundle();

        SellerGoodsListPageFragment fragment = new SellerGoodsListPageFragment();
        fragment.setArguments(args);
        fragment.currTab = currTab;
        fragment.simpleCallback = simpleCallback;

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_list_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvList = view.findViewById(R.id.rv_list);
        sellerGoodsAdapter = new SellerGoodsAdapter(_mActivity, currTab, R.layout.seller_goods_item, sellerGoodsItemList);
        sellerGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SellerGoodsItem item = sellerGoodsItemList.get(position);

                if (id == R.id.btn_switch_status) {
                    switchSellerGoodsStatus(item.commonId);
                } else if (id == R.id.btn_view_all_sku) {
                    SLog.info("SKU商品列表");
                    Util.startFragment(SellerGoodsSkuListFragment.newInstance(item.commonId));
                } else if (id == R.id.btn_more) {
                    SLog.info("查看更多");
                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new SellerOperationPopup(_mActivity, item.commonId, item.goodsState, SellerGoodsListPageFragment.this))
                            .show();
                } else if (id == R.id.ll_swipe_content) {
                    Util.startFragment(SellerGoodsDetailFragment.newInstance(item.commonId, item.imageName));
                }
            }
        });

        sellerGoodsAdapter.setEnableLoadMore(true);
        sellerGoodsAdapter.setOnLoadMoreListener(this, rvList);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        rvList.setAdapter(sellerGoodsAdapter);

        loadData(currPage + 1);
    }

    private void switchSellerGoodsStatus(int commonId) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url;
        if (currTab == SellerGoodsListFragment.TAB_GOODS_IN_SALE) {
            url = Api.PATH_SELLER_GOODS_BATCH_OFF_SHELF;
        } else {
            url = Api.PATH_SELLER_GOODS_BATCH_ON_SHELF;
        }


        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "commonIdList", String.valueOf(commonId)
        );

        SLog.info("url[%s], params[%s]", url, params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.success(_mActivity, currTab == SellerGoodsListFragment.TAB_GOODS_IN_SALE ? "下架成功" : "上架成功");
                    if (simpleCallback != null) {
                        simpleCallback.onSimpleCall(EasyJSONObject.generate(
                                "action", CustomAction.CUSTOM_ACTION_RELOAD_DATA
                        ));
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        reloadData();
    }

    public void reloadData() {
        currPage = 0;
        loadData(currPage + 1);
    }

    /**
     * 加載數據
     */
    private void loadData(int page) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        try {
            String url = Api.PATH_SELLER_GOODS_LIST;

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "page", page);

            if (!StringUtil.isEmpty(keyword)) {
                params.set("goodsName", keyword);
            }

            int goodsState;  // 查詢哪種狀態的商品
            if (currTab == SellerGoodsListFragment.TAB_GOODS_IN_SALE) {
                goodsState = 1; // 查詢出售中的商品
            } else {
                goodsState = 0; // 查詢庫存中的商品
            }

            params.set("goodsState", goodsState);

            SLog.info("url[%s], params[%s]", url, params.toString());
            Api.getUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    sellerGoodsAdapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            sellerGoodsAdapter.loadMoreFail();
                            return;
                        }

                        hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                        SLog.info("hasMore[%s]", hasMore);
                        if (!hasMore) {
                            sellerGoodsAdapter.loadMoreEnd();
                            sellerGoodsAdapter.setEnableLoadMore(false);
                        }

                        if (page == 1) {
                            sellerGoodsItemList.clear();
                        }
                        EasyJSONArray goodsList = responseObj.getArray("datas.goodsList");
                        for (Object object : goodsList) {
                            SellerGoodsItem item = (SellerGoodsItem) EasyJSONBase.jsonDecode(SellerGoodsItem.class, object.toString());

                            sellerGoodsItemList.add(item);
                        }
                        sellerGoodsAdapter.loadMoreComplete();
                        sellerGoodsAdapter.setNewData(sellerGoodsItemList);
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


    private void deleteGoods(int commonId) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "commonId", commonId
        );

        SLog.info("url[%s], params[%s]", Api.PATH_SELLER_DELETE_GOODS, params);
        Api.postUI(Api.PATH_SELLER_DELETE_GOODS, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.success(_mActivity, "刪除成功");

                    if (simpleCallback != null) {
                        simpleCallback.onSimpleCall(EasyJSONObject.generate(
                                "action", CustomAction.CUSTOM_ACTION_RELOAD_DATA
                        ));
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            sellerGoodsAdapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage + 1);
    }

    @Override
    public void onSimpleCall(Object data) {
        EasyJSONObject dataObj = (EasyJSONObject) data;
        try {
            String action = dataObj.getSafeString("action");
            if (CustomAction.CUSTOM_ACTION_SELLER_DELETE_GOODS.equals(action)) {
                int commonId = dataObj.getInt("commonId");
                new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                        // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                        .setPopupCallback(new XPopupCallback() {
                            @Override
                            public void onShow() {
                            }
                            @Override
                            public void onDismiss() {
                            }
                        }).asCustom(new TwConfirmPopup(_mActivity, "確定要刪除嗎？", null, new OnConfirmCallback() {
                    @Override
                    public void onYes() {
                        SLog.info("onYes");
                        deleteGoods(commonId);
                    }

                    @Override
                    public void onNo() {
                        SLog.info("onNo");
                    }
                })).show();
            } else if (CustomAction.CUSTOM_ACTION_SELLER_SWITCH_GOODS_SHELF_STATUS.equals(action)) {
                if (simpleCallback != null) {
                    simpleCallback.onSimpleCall(EasyJSONObject.generate(
                            "action", CustomAction.CUSTOM_ACTION_RELOAD_DATA
                    ));
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}
