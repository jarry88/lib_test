package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.ConfirmOrderStoreAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.ConfirmOrderSkuItem;
import com.ftofs.twant.entity.ConfirmOrderStoreItem;
import com.ftofs.twant.entity.ConfirmOrderSummaryItem;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 確認訂單Fragment
 * @author zwm
 */
public class ConfirmBillFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    String buyData;

    /**
     * 是否來源于購物車
     * todo 暫時寫死
     */
    int isFromCart;

    List<ListPopupItem> payWayItemList = new ArrayList<>();
    List<ListPopupItem> shippingItemList = new ArrayList<>();

    private static final int LIST_POPUP_TYPE_PAY_WAY = 1;
    private static final int LIST_POPUP_TYPE_SHIPPING_TIME = 2;

    // 當前支付方式
    int payWayIndex = 0;
    // 當前配送時間索引
    int shippingTimeIndex = 0;

    AddrItem mAddrItem;
    int isExistTrys;

    TextView tvShippingTime;
    TextView tvReceiverName;
    TextView tvMobile;
    TextView tvAddress;
    TextView tvItemCount;  // 共xxx件
    TextView tvTotalPrice;  // 合計:多少錢

    RelativeLayout btnAddShippingAddress;
    LinearLayout btnChangeShippingAddress;

    BaseQuickAdapter adapter;
    List<MultiItemEntity> confirmOrderItemList = new ArrayList<>();

    String currencyTypeSign;
    String textConfirmOrderTotalItemCount;
    String[] paymentTypeCodeArr = new String[] {Constant.PAYMENT_TYPE_CODE_OFFLINE, Constant.PAYMENT_TYPE_CODE_OFFLINE,
            Constant.PAYMENT_TYPE_CODE_CHAIN};

    /**
     * 提交訂單時上去的店鋪列表
     */
    EasyJSONArray commitStoreList = EasyJSONArray.generate();

    /**
     * 創建確認訂單的實例
     * @param isFromCart 1 -- 來源于購物車 0 -- 直接購買
     * @param buyData
     * @return
     */
    public static ConfirmBillFragment newInstance(int isFromCart, String buyData) {
        Bundle args = new Bundle();

        args.putInt("isFromCart", isFromCart);
        args.putString("buyData", buyData);

        ConfirmBillFragment fragment = new ConfirmBillFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_bill, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        isFromCart = args.getInt("isFromCart");
        buyData = args.getString("buyData");
        SLog.info("buyData[%s]", buyData);

        currencyTypeSign = getResources().getString(R.string.currency_type_sign);
        textConfirmOrderTotalItemCount = getResources().getString(R.string.text_confirm_order_total_item_count);

        // 初始化支付方式數據
        payWayItemList.add(new ListPopupItem(0, getResources().getString(R.string.text_pay_online), null));
        payWayItemList.add(new ListPopupItem(1, getResources().getString(R.string.text_pay_delivery), null));
        payWayItemList.add(new ListPopupItem(2, getResources().getString(R.string.text_pay_fetch), null));

        tvShippingTime = view.findViewById(R.id.tv_shipping_time);

        tvReceiverName = view.findViewById(R.id.tv_receiver_name);
        tvMobile = view.findViewById(R.id.tv_mobile);
        tvAddress = view.findViewById(R.id.tv_address);
        tvItemCount = view.findViewById(R.id.tv_item_count);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);

        btnAddShippingAddress = view.findViewById(R.id.btn_add_shipping_address);
        btnChangeShippingAddress = view.findViewById(R.id.btn_change_shipping_address);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);
        Util.setOnClickListener(view, R.id.btn_add_shipping_address, this);
        Util.setOnClickListener(view, R.id.btn_change_shipping_address, this);

        RecyclerView rvStoreList = view.findViewById(R.id.rv_store_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvStoreList.setLayoutManager(layoutManager);
        adapter = new ConfirmOrderStoreAdapter(_mActivity, confirmOrderItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                switch (id) {
                    case R.id.btn_change_pay_way:
                        payWayPopup();
                        break;
                    case R.id.btn_change_shipping_time:
                        shippingTimePopup();
                        break;
                    default:
                        break;
                }
            }
        });
        rvStoreList.setAdapter(adapter);

        loadBillData();
    }

    @Override
    public boolean onBackPressedSupport() {
        popWithOutRefresh();
        return true;
    }


    private void payWayPopup() {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_pay_fetch),
                        LIST_POPUP_TYPE_PAY_WAY, payWayItemList, payWayIndex, this))
                .show();
    }

    private void shippingTimePopup() {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_shipping_time),
                        LIST_POPUP_TYPE_SHIPPING_TIME, shippingItemList, shippingTimeIndex, this))
                .show();
    }

    /**
     * 收集表單參數
     * @return
     */
    private EasyJSONObject collectParams() {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return null;
            }

            // 收集表單信息
            EasyJSONObject commitBuyData = EasyJSONObject.generate(
                    "paymentTypeCode", "online",
                    "isCart", isFromCart,
                    "isExistTrys", isExistTrys,
                    "storeList", commitStoreList);
            if (mAddrItem != null) {
                commitBuyData.set("addressId", mAddrItem.addressId);
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "clientType", Constant.CLIENT_TYPE_ANDROID,
                    "buyData", commitBuyData.toString());

            SLog.info("params[%s]", params.toString());
            return params;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 彈出，并且父Fragment不刷新
     */
    private void popWithOutRefresh() {
        // TODO: 2019/5/20 通知上一級不刷新數據
        pop();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            popWithOutRefresh();
        } else if (id == R.id.btn_commit) {
            if (mAddrItem == null) {
                SLog.info("Error!地址信息無效");
                ToastUtil.show(_mActivity, "地址信息無效");
                return;
            }

            EasyJSONObject params = collectParams();
            if (params == null) {
                ToastUtil.show(_mActivity, "數據無效");
                return;
            }
            Api.postUI(Api.PATH_COMMIT_BILL_DATA, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(getContext(), responseObj)) {
                        return;
                    }
                    ToastUtil.show(_mActivity, "提交訂單成功");
                    pop();
                }
            });
        } else if (id == R.id.btn_add_shipping_address) {
            startForResult(AddAddressFragment.newInstance(Constant.ACTION_ADD, null), Constant.REQUEST_CODE_ADD_ADDRESS);
        } else if (id == R.id.btn_change_shipping_address) {
            startForResult(AddrManageFragment.newInstance(), Constant.REQUEST_CODE_CHANGE_ADDRESS);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        // 從哪個Fragment返回
        String from = data.getString("from");
        SLog.info("requestCode[%d], resultCode[%d], from[%s]", requestCode, resultCode, from);
        if (AddrManageFragment.class.getName().equals(from) || AddAddressFragment.class.getName().equals(from)) {
            // 從地址管理Fragment返回 或 從地址添加Fragment返回
            boolean isNoAddress = data.getBoolean("isNoAddress", false); // 標記是否刪除了所有地址
            if (isNoAddress) {
                mAddrItem = null;
                updateAddrView();
                return;
            }

            // 上一級Fragment返回的地址項
            AddrItem addrItem = data.getParcelable("addrItem");
            if (addrItem == null) {
                return;
            }
            SLog.info("addrItem: %s", addrItem);
            mAddrItem = addrItem;
            updateAddrView();
        } else if (ReceiptInfoFragment.class.getName().equals(from)) {
            int action = data.getInt("action");


        }
    }

    /**
     * 更新地址信息的顯示
     */
    private void updateAddrView() {
        if (mAddrItem == null) {
            // 用戶沒有收貨地址，顯示【新增收貨地址】按鈕
            btnAddShippingAddress.setVisibility(View.VISIBLE);
            btnChangeShippingAddress.setVisibility(View.GONE);
        } else {
            btnAddShippingAddress.setVisibility(View.GONE);
            btnChangeShippingAddress.setVisibility(View.VISIBLE);

            tvReceiverName.setText(getResources().getString(R.string.text_receiver) + ": " + mAddrItem.realName);
            tvMobile.setText(mAddrItem.mobPhone);
            tvAddress.setText(mAddrItem.areaInfo + " " + mAddrItem.address);
        }
    }

    private void loadBillData() {
        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                List<EasyJSONObject> resultList = (List<EasyJSONObject>) message;
                if (resultList == null) {
                    ToastUtil.show(_mActivity, "生成訂單失敗");
                    pop();
                    return;
                }

                // 下面顯示訂單數據
                updateAddrView();

                ConfirmOrderSummaryItem summaryItem = getSummaryItem();
                String template = getResources().getString(R.string.text_confirm_order_total_item_count);
                tvItemCount.setText(String.format(template, summaryItem.totalItemCount));
                tvTotalPrice.setText(StringUtil.formatPrice(_mActivity,
                        summaryItem.totalAmount + summaryItem.totalFreight - summaryItem.storeDiscount, 0));

                adapter.setNewData(confirmOrderItemList);
            }
        };

        TaskObservable taskObservable = new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                try {
                    List<EasyJSONObject> resultList = new ArrayList<>();
                /*
                 同步請求這三個接口
                    /member/buy/step1
                    /member/buy/calc/freight
                    /member/buy/calc
                  */
                    String token = User.getToken();
                    if (StringUtil.isEmpty(token)) {
                        return null;
                    }

                    // 總件數
                    int totalItemCount = 0;

                    // 第1步
                    EasyJSONObject params = EasyJSONObject.generate(
                            "token", token,
                            "buyData", buyData,
                            "clientType", Constant.CLIENT_TYPE_ANDROID,
                            "isCart", isFromCart);
                    SLog.info("params[%s]", params.toString());
                    String responseStr = Api.syncPost(Api.PATH_DISPLAY_BILL_DATA, params);

                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        return null;
                    }
                    resultList.add(responseObj);


                    // 獲取配送時間列表
                    shippingItemList.clear();
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.shipTimeTypeList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        shippingItemList.add(new ListPopupItem(easyJSONObject.getInt("id"), easyJSONObject.getString("name"), null));
                    }

                    isExistTrys = responseObj.getInt("datas.isExistTrys");

                    // 店鋪Sku數據
                    easyJSONArray = responseObj.getArray("datas.buyStoreVoList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        int storeId = easyJSONObject.getInt("storeId");
                        String storeName = easyJSONObject.getString("storeName");
                        int itemCount = easyJSONObject.getInt("itemCount");
                        float freightAmount = (float) easyJSONObject.getDouble("freightAmount");
                        float buyItemAmount = (float) easyJSONObject.getDouble("buyItemAmount");

                        int shipTimeType = 0;

                        EasyJSONArray goodsList = EasyJSONArray.generate();
                        EasyJSONArray buyGoodsItemVoList = easyJSONObject.getArray("buyGoodsItemVoList");
                        List<ConfirmOrderSkuItem> confirmOrderSkuItemList = new ArrayList<>();
                        for (Object object2 : buyGoodsItemVoList) {
                            EasyJSONObject buyGoodsItem = (EasyJSONObject) object2;
                            int cartId = buyGoodsItem.getInt("cartId");
                            int buyNum = buyGoodsItem.getInt("buyNum");

                            String imageSrc = buyGoodsItem.getString("imageSrc");
                            String goodsName = buyGoodsItem.getString("goodsName");
                            String goodsFullSpecs = buyGoodsItem.getString("goodsFullSpecs");
                            float goodsPrice = (float) buyGoodsItem.getDouble("goodsPrice");


                            ConfirmOrderSkuItem confirmOrderSkuItem = new ConfirmOrderSkuItem(imageSrc, cartId, goodsName, goodsFullSpecs, buyNum, goodsPrice);
                            confirmOrderSkuItemList.add(confirmOrderSkuItem);
                            goodsList.append(EasyJSONObject.generate("cartId", cartId, "buyNum", buyNum));

                            totalItemCount++;
                        }

                        confirmOrderItemList.add(new ConfirmOrderStoreItem(storeId, storeName, buyItemAmount,
                                freightAmount, itemCount, confirmOrderSkuItemList));

                        commitStoreList.append(EasyJSONObject.generate(
                                "storeId", storeId,
                                "storeName", storeName,
                                "goodsList", goodsList,
                                "shipTimeType", shipTimeType));
                    } // END OF for

                    // 添加上汇总项目
                    confirmOrderItemList.add(new ConfirmOrderSummaryItem());



                    // 第2步
                    params = collectParams();
                    // 收集地址信息
                    EasyJSONObject address = responseObj.getObject("datas.address");
                    if (address != null) {
                        mAddrItem = new AddrItem(address);
                        SLog.info("mAddrItem[%s]", mAddrItem);

                        params = collectParams();
                        SLog.info("params[%s]", params.toString());
                        responseStr = Api.syncPost(Api.PATH_CALC_FREIGHT, params);

                        SLog.info("responseStr[%s]", responseStr);
                        responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.isError(responseObj)) {
                            return null;
                        }
                        resultList.add(responseObj);
                    }

                    // 第3步(請求參數與第2步相同)
                    responseStr = Api.syncPost(Api.PATH_CALC_TOTAL, params);

                    SLog.info("responseStr[%s]", responseStr);
                    responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        return null;
                    }
                    resultList.add(responseObj);

                    ConfirmOrderSummaryItem summaryItem = getSummaryItem();
                    summaryItem.totalItemCount = totalItemCount;
                    summaryItem.totalAmount = (float) responseObj.getDouble("datas.buyGoodsItemAmount");
                    summaryItem.storeDiscount = (float) responseObj.getDouble("datas.storeTotalDiscountAmount");

                    return resultList;
                } catch (Exception e) {

                }

                return null;
            }
        };

        TwantApplication.getThreadPool().execute(taskObservable);
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        if (type == LIST_POPUP_TYPE_PAY_WAY) {
            payWayIndex = id;

            ConfirmOrderSummaryItem summaryItem = getSummaryItem();
            if (summaryItem == null) {
                return;
            }
            summaryItem.paymentTypeCode = getPaymentTypeCode(payWayIndex);
            adapter.notifyItemChanged(confirmOrderItemList.size() - 1);
        } else if (type == LIST_POPUP_TYPE_SHIPPING_TIME) {
            shippingTimeIndex = id;
            tvShippingTime.setText(shippingItemList.get(shippingTimeIndex).title);
        }
    }

    /*
    {"code":200,"datas":{"address":null,
     */

    private ConfirmOrderSummaryItem getSummaryItem() {
        int size = confirmOrderItemList.size();
        if (size < 1) {
            return null;
        }
        return (ConfirmOrderSummaryItem) confirmOrderItemList.get(size - 1);
    }

    private String getPaymentTypeCode(int index) {
        return paymentTypeCodeArr[index];
    }
}
