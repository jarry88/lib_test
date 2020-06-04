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
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.adapter.SellerOrderAdapter;
import com.ftofs.twant.seller.entity.SellerOrderFilterParams;
import com.ftofs.twant.seller.entity.SellerOrderItem;
import com.ftofs.twant.seller.entity.SellerOrderSkuItem;
import com.ftofs.twant.seller.widget.BuyerInfoPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabButton;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 【商家】各个订单列表页面
 * @author zwm
 */
public class SellerOrderListPageFragment extends BaseFragment implements View.OnClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    int tab; // 哪个Tab的页面

    RecyclerView rvList;
    SellerOrderAdapter sellerOrderAdapter;
    ArrayList<SellerOrderItem> sellerOrderItemList = new ArrayList<>();

    int currPage = 0; // 当前加载到第几页
    SellerOrderFilterParams filterParams; // 搜索過濾參數

    // 訂單狀態(default:所有訂單, new:待付款, pay:待發貨, send:已發貨, finish:已完成, cancel:已取消)
    String[] orderStateText = new String[] {"default", "new", "pay", "send", "finish", "cancel"};

    boolean hasMore;

    public static SellerOrderListPageFragment newInstance(int tab) {
        Bundle args = new Bundle();

        SellerOrderListPageFragment fragment = new SellerOrderListPageFragment();
        fragment.setArguments(args);
        fragment.setParams(tab);

        return fragment;
    }

    public void setParams(int tab) {
        this.tab = tab;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_order_list_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);
        rvList = view.findViewById(R.id.rv_list);


        sellerOrderAdapter = new SellerOrderAdapter(_mActivity, R.layout.seller_order_item, sellerOrderItemList);
        sellerOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                SellerOrderItem sellerOrderItem = sellerOrderItemList.get(position);

                if (id == R.id.tv_buyer) {
                    hideSoftInput();
                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new BuyerInfoPopup(_mActivity, _mActivity, sellerOrderItem.ordersId, sellerOrderItem.buyerMemberName))
                            .show();
                } else if (id == R.id.btn_ship) {
                    Util.startFragment(SellerOrderShipFragment.newInstance(sellerOrderItem.ordersId, sellerOrderItem.ordersSnText));
                }
            }
        });
        sellerOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SellerOrderItem item = sellerOrderItemList.get(position);
                Util.startFragment(SellerOrderDetailFragment.newInstance(item.ordersId));
            }
        });

        sellerOrderAdapter.setEnableLoadMore(true);
        sellerOrderAdapter.setOnLoadMoreListener(this, rvList);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        rvList.setAdapter(sellerOrderAdapter);

        loadData(currPage + 1);
    }

    public void reloadDataWithFilter(SellerOrderFilterParams filterParams) {
        this.filterParams = filterParams;

        reloadData();
    }

    private void reloadData() {
        currPage = 0;
        loadData(currPage + 1);
    }

    /**
     * 加載數據
     * @param page 第幾頁
     */
    private void loadData(int page) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        try {
            String url = Api.PATH_SELLER_ORDERS_LIST + "/" + orderStateText[tab];

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "page", page);

            if (filterParams != null) {
                if (!StringUtil.isEmpty(filterParams.buyerNickname)) {
                    params.set("searchName", filterParams.buyerNickname);
                }

                if (!StringUtil.isEmpty(filterParams.goodsName)) {
                    params.set("goodsName", filterParams.goodsName);
                }

                if (!StringUtil.isEmpty(filterParams.orderSN)) {
                    params.set("ordersSn", filterParams.orderSN);
                }

                params.set("createTimeStart", filterParams.beginDate.toString() + " 00:00:00");
                params.set("createTimeEnd", filterParams.endDate.toString() + " 23:59:59");

                if (!StringUtil.isEmpty(filterParams.orderSource)) {
                    params.set("ordersFrom", filterParams.orderSource);
                }

                if (!StringUtil.isEmpty(filterParams.orderType)) {
                    params.set("ordersType", filterParams.orderType);
                }

                if (!StringUtil.isEmpty(filterParams.receiverMobile)) {
                    params.set("receiverPhone", filterParams.receiverMobile);
                }

                if (!StringUtil.isEmpty(filterParams.receiverAddress)) {
                    params.set("receiverAddress", filterParams.receiverAddress);
                }

                if (!StringUtil.isEmpty(filterParams.buyerMobile)) {
                    params.set("buyerPhone", filterParams.buyerMobile);
                }
            }

            SLog.info("url[%s], params[%s]", url, params.toString());
            Api.getUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    sellerOrderAdapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            sellerOrderAdapter.loadMoreFail();
                            return;
                        }

                        hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                        SLog.info("hasMore[%s]", hasMore);
                        if (!hasMore) {
                            sellerOrderAdapter.loadMoreEnd();
                            sellerOrderAdapter.setEnableLoadMore(false);
                        }

                        if (page == 1) {
                            sellerOrderItemList.clear();
                        }
                        EasyJSONArray orderList = responseObj.getArray("datas.ordersList");
                        for (Object object : orderList) {
                            EasyJSONObject orderItem = (EasyJSONObject) object;

                            SellerOrderItem item = new SellerOrderItem();

                            item.ordersId = orderItem.getInt("ordersId");
                            item.ordersState = orderItem.getInt("ordersState");
                            item.ordersSnText = orderItem.getSafeString("ordersSnText");
                            item.ordersStateName = orderItem.getSafeString("ordersStateName");
                            item.createTime = orderItem.getSafeString("createTime");
                            item.ordersFrom = orderItem.getString("ordersFrom");
                            item.buyer = orderItem.getSafeString("nickName");
                            item.buyerMemberName = orderItem.getSafeString("memberName");
                            item.paymentName = orderItem.getSafeString("paymentName");
                            item.ordersAmount = orderItem.getDouble("ordersAmount");
                            item.freightAmount = orderItem.getDouble("freightAmount");

                            // 獲取各個商品列表
                            EasyJSONArray goodsList = orderItem.getArray("ordersGoodsList");
                            for (Object object2 : goodsList) {
                                EasyJSONObject goodsItem = (EasyJSONObject) object2;

                                SellerOrderSkuItem skuItem = new SellerOrderSkuItem();
                                skuItem.goodsId = goodsItem.getInt("ordersGoodsId");
                                skuItem.goodsName = goodsItem.getSafeString("goodsName");
                                skuItem.goodsImage = goodsItem.getSafeString("goodsImage");
                                skuItem.goodsPrice = goodsItem.getDouble("goodsPrice");
                                skuItem.buyNum = goodsItem.getInt("buyNum");
                                skuItem.goodsFullSpecs = goodsItem.getSafeString("goodsFullSpecs");
                                skuItem.hasGift = (goodsItem.getInt("hasGift") == Constant.TRUE_INT);

                                item.goodsList.add(skuItem);
                            }

                            sellerOrderItemList.add(item);
                        }
                        sellerOrderAdapter.loadMoreComplete();
                        sellerOrderAdapter.setNewData(sellerOrderItemList);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_SELLER_RELOAD_ORDER_LIST) {
            if (tab == Constant.ORDER_STATUS_TO_BE_SHIPPED || tab == Constant.ORDER_STATUS_TO_BE_RECEIVED) {
                reloadData();
            }
        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            sellerOrderAdapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage + 1);
    }
}
