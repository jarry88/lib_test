package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.OrderListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.OrderSkuItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 訂單搜索Fragment
 * @author zwm
 */
public class OrderSearchFragment extends BaseFragment implements View.OnClickListener {
    List<OrderItem> orderItemList = new ArrayList<>();
    OrderListAdapter adapter;

    public static OrderSearchFragment newInstance() {
        Bundle args = new Bundle();

        OrderSearchFragment fragment = new OrderSearchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        EditText etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = v.getText().toString().trim();
                    doSearch(keyword);
                    return true;
                }

                return false;
            }
        });

        RecyclerView rvOrderList = view.findViewById(R.id.rv_order_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOrderList.setLayoutManager(layoutManager);
        adapter = new OrderListAdapter(_mActivity, R.layout.order_item, orderItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("onItemClick");

                OrderItem orderItem = orderItemList.get(position);

                Util.startFragment(OrderDetailFragment.newInstance(orderItem.orderId));
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SLog.info("id[%d]", id);
                if (id == R.id.btn_pay_order) {
                    int payId = (int) view.getTag();
                    SLog.info("payId[%d]", payId);
                    Util.startFragment(ICBCFragment.newInstance(String.valueOf(payId)));
                }
            }
        });
        rvOrderList.setAdapter(adapter);
    }


    private void doSearch(String keyword) {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "keyword", keyword);

        final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                .asLoading(getString(R.string.text_loading))
                .show();

        Api.postUI(Api.PATH_ORDER_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONArray ordersPayVoList = responseObj.getArray("datas.ordersPayVoList");
                    orderItemList.clear();
                    for (Object object : ordersPayVoList) { // PayObject
                        EasyJSONObject ordersPayVo = (EasyJSONObject) object;

                        int payId = ordersPayVo.getInt("payId");
                        EasyJSONArray ordersVoList = ordersPayVo.getArray("ordersVoList");
                        int len = ordersVoList.length();
                        int index = 0;
                        for (Object object2 : ordersVoList) { // OrderVo
                            EasyJSONObject ordersVo = (EasyJSONObject) object2;

                            int ordersId = ordersVo.getInt("ordersId");
                            String ordersStateName = ordersVo.getString("ordersStateName");
                            String storeName = ordersVo.getString("storeName");
                            float freightAmount = (float) ordersVo.getDouble("freightAmount");
                            float ordersAmount = (float) ordersVo.getDouble("ordersAmount");

                            List<OrderSkuItem> orderSkuItemList = new ArrayList<>();

                            // 獲取Sku列表
                            EasyJSONArray ordersGoodsVoList = ordersVo.getArray("ordersGoodsVoList");
                            for (Object object3 : ordersGoodsVoList) { // Sku
                                EasyJSONObject ordersGoodsVo = (EasyJSONObject) object3;

                                String goodsName = ordersGoodsVo.getString("goodsName");
                                String imageSrc = ordersGoodsVo.getString("imageSrc");
                                float goodsPrice = (float) ordersGoodsVo.getDouble("goodsPrice");
                                String goodsFullSpecs = ordersGoodsVo.getString("goodsFullSpecs");
                                int buyNum = ordersGoodsVo.getInt("buyNum");

                                orderSkuItemList.add(new OrderSkuItem(goodsName, imageSrc, goodsPrice, goodsFullSpecs, buyNum));
                            }  // END OF Sku

                            OrderItem orderItem = new OrderItem(ordersId, storeName, ordersStateName, freightAmount, ordersAmount, orderSkuItemList);
                            // 最后一個顯示【支付訂單】按鈕
                            if (index == len -1) {
                                orderItem.setShowPayButton(true);
                                orderItem.setPayId(payId);
                            }

                            orderItemList.add(orderItem);
                            ++index;
                        } // END OF Order Object
                    } // END OF Pay Object
                    SLog.info("orderItemList:count[%d]", orderItemList.size());
                    adapter.setNewData(orderItemList);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                    SLog.info("Error!loadBillData failed");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
