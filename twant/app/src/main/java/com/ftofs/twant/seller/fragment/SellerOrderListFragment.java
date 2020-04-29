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
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.store.Seller;
import com.ftofs.twant.entity.FeedbackItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.LoginFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.adapter.SellerOrderAdapter;
import com.ftofs.twant.seller.entity.SellerOrderItem;
import com.ftofs.twant.seller.entity.SellerOrderSkuItem;
import com.ftofs.twant.seller.widget.BuyerInfoPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商家訂單列表頁面
 * @author zwm
 */
public class SellerOrderListFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    SellerOrderAdapter[] sellerOrderAdapterArr;
    ArrayList<ArrayList<SellerOrderItem>> dataListArr;
    SimpleTabButton[] tabButtons;

    int currTab = Constant.ORDER_STATUS_ALL;

    public static final int TAB_COUNT = Constant.ORDER_STATUS_CANCELLED + 1;

    // 訂單狀態(default:所有訂單, new:待付款, pay:待發貨, send:已發貨, finish:已完成, cancel:已取消)
    String[] orderStateText = new String[] {"default", "new", "pay", "send", "finish", "cancel"};

    public static SellerOrderListFragment newInstance() {
        Bundle args = new Bundle();

        SellerOrderListFragment fragment = new SellerOrderListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_order_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        sellerOrderAdapterArr = new SellerOrderAdapter[TAB_COUNT];
        tabButtons = new SimpleTabButton[TAB_COUNT];
        dataListArr = new ArrayList<>();
        rvList = view.findViewById(R.id.rv_list);

        tabButtons[Constant.ORDER_STATUS_ALL] = view.findViewById(R.id.tab_all);
        tabButtons[Constant.ORDER_STATUS_TO_BE_PAID] = view.findViewById(R.id.tab_to_be_paid);
        tabButtons[Constant.ORDER_STATUS_TO_BE_SHIPPED] = view.findViewById(R.id.tab_to_be_shipped);
        tabButtons[Constant.ORDER_STATUS_TO_BE_RECEIVED] = view.findViewById(R.id.tab_to_be_received);
        tabButtons[Constant.ORDER_STATUS_TO_BE_COMMENTED] = view.findViewById(R.id.tab_finished);
        tabButtons[Constant.ORDER_STATUS_CANCELLED] = view.findViewById(R.id.tab_cancelled);


        for (int i = 0; i < TAB_COUNT;  i++) {
            ArrayList<SellerOrderItem> sellerOrderItemList = new ArrayList<>();
            dataListArr.add(sellerOrderItemList);

            sellerOrderAdapterArr[i] = new SellerOrderAdapter(_mActivity, R.layout.seller_order_item, sellerOrderItemList);
            sellerOrderAdapterArr[i].setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int id = view.getId();

                    if (id == R.id.tv_buyer) {
                        hideSoftInput();
                        new XPopup.Builder(_mActivity)
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(false)
                                .asCustom(new BuyerInfoPopup(_mActivity, _mActivity))
                                .show();
                    }
                }
            });
            sellerOrderAdapterArr[i].setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    SellerOrderItem item = dataListArr.get(currTab).get(position);
                    Util.startFragment(SellerOrderDetailFragment.newInstance(item.ordersId));
                }
            });
        }

        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        rvList.setAdapter(sellerOrderAdapterArr[Constant.ORDER_STATUS_ALL]);

        SimpleTabManager tabManager = new SimpleTabManager(Constant.ORDER_STATUS_ALL) {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                boolean isRepeat = onSelect(v);
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }
            }
        };

        tabManager.add(view.findViewById(R.id.tab_all));
        tabManager.add(view.findViewById(R.id.tab_to_be_paid));
        tabManager.add(view.findViewById(R.id.tab_to_be_shipped));
        tabManager.add(view.findViewById(R.id.tab_to_be_received));
        tabManager.add(view.findViewById(R.id.tab_finished));
        tabManager.add(view.findViewById(R.id.tab_cancelled));

        loadData(Constant.ORDER_STATUS_ALL);
    }

    /**
     * 加載數據
     * @param tab 哪一種類型的Tab
     */
    private void loadData(int tab) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_ORDERS_LIST + "/" + orderStateText[tab];

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        SLog.info("url[%s], params[%s]", url, params.toString());
        Api.getUI(url, params, new UICallback() {
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

                    EasyJSONArray orderList = responseObj.getArray("datas.ordersList");
                    List<SellerOrderItem> dataList = dataListArr.get(tab);
                    for (Object object : orderList) {
                        EasyJSONObject orderItem = (EasyJSONObject) object;

                        SellerOrderItem item = new SellerOrderItem();

                        item.ordersId = orderItem.getInt("ordersId");
                        item.ordersSnText = orderItem.getSafeString("ordersSnText");
                        item.ordersStateName = orderItem.getSafeString("ordersStateName");
                        item.createTime = orderItem.getSafeString("createTime");
                        item.ordersFrom = orderItem.getString("ordersFrom");
                        item.buyer = orderItem.getSafeString("nickName");
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

                        dataList.add(item);
                    }

                    sellerOrderAdapterArr[tab].setNewData(dataList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_filter) {

        }
    }
}

