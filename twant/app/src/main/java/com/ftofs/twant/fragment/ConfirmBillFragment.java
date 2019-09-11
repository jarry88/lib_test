package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.ConfirmOrderStoreAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.ConfirmOrderSkuItem;
import com.ftofs.twant.entity.ConfirmOrderStoreItem;
import com.ftofs.twant.entity.ConfirmOrderSummaryItem;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.entity.StoreVoucherVo;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.OrderVoucherPopup;
import com.ftofs.twant.widget.PayPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 確認訂單Fragment
 * @author zwm
 */
public class ConfirmBillFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    String buyData;

    int isFromCart;

    List<ListPopupItem> payWayItemList = new ArrayList<>();
    List<ListPopupItem> shippingItemList = new ArrayList<>();

    // 當前支付方式
    int payWayIndex = 0;

    AddrItem mAddrItem;
    int isExistTrys;

    TextView tvReceiverName;
    TextView tvMobile;
    TextView tvAddress;
    TextView tvItemCount;  // 共xxx件
    TextView tvTotalPrice;  // 合計:多少錢

    RelativeLayout btnAddShippingAddress;
    LinearLayout btnChangeShippingAddress;
    LinearLayout llSelfFetchInfoContainer;

    ConfirmOrderStoreAdapter adapter;
    List<MultiItemEntity> confirmOrderItemList = new ArrayList<>();

    String currencyTypeSign;
    String textConfirmOrderTotalItemCount;
    String[] paymentTypeCodeArr = new String[] {Constant.PAYMENT_TYPE_CODE_ONLINE, Constant.PAYMENT_TYPE_CODE_OFFLINE,
            Constant.PAYMENT_TYPE_CODE_CHAIN};

    boolean isFirstShowSelfFetchInfo = true; // 是否首次顯示門店自提信息，如果是，則自動填充默認地址信息
    EditText etSelfFetchNickname;
    EditText etSelfFetchMobile;
    LinearLayout btnChangeSelfFetchMobileZone;
    TextView tvSelfFetchMobileZone;

    List<MobileZone> mobileZoneList = new ArrayList<>();
    /**
     * 當前選中的區號索引
     */
    private int selectedMobileZoneIndex = 0;

    /**
     * 提交訂單時上去的店鋪列表
     */
    EasyJSONArray commitStoreList = EasyJSONArray.generate();

    // 店鋪Id => 店鋪券列表
    Map<Integer, List<StoreVoucherVo>> voucherMap = new HashMap<>();

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

        tvReceiverName = view.findViewById(R.id.tv_receiver_name);
        tvMobile = view.findViewById(R.id.tv_mobile);
        tvAddress = view.findViewById(R.id.tv_address);
        tvItemCount = view.findViewById(R.id.tv_item_count);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);

        btnAddShippingAddress = view.findViewById(R.id.btn_add_shipping_address);
        btnAddShippingAddress.setOnClickListener(this);
        btnChangeShippingAddress = view.findViewById(R.id.btn_change_shipping_address);
        btnChangeShippingAddress.setOnClickListener(this);
        llSelfFetchInfoContainer = view.findViewById(R.id.ll_self_take_info_container);

        etSelfFetchNickname = view.findViewById(R.id.et_self_take_nickname);
        etSelfFetchMobile = view.findViewById(R.id.et_self_take_mobile);
        btnChangeSelfFetchMobileZone = view.findViewById(R.id.btn_change_self_take_mobile_zone);
        btnChangeSelfFetchMobileZone.setOnClickListener(this);
        tvSelfFetchMobileZone = view.findViewById(R.id.tv_self_take_mobile_zone);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);

        RecyclerView rvStoreList = view.findViewById(R.id.rv_store_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvStoreList.setLayoutManager(layoutManager);
        adapter = new ConfirmOrderStoreAdapter(_mActivity, shippingItemList, confirmOrderItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                ConfirmOrderStoreItem storeItem;
                ConfirmOrderSummaryItem summaryItem;
                switch (id) {
                    case R.id.btn_receipt:
                        summaryItem = (ConfirmOrderSummaryItem) confirmOrderItemList.get(position);
                        startForResult(ReceiptInfoFragment.newInstance(position, summaryItem.receipt), RequestCode.EDIT_RECEIPT.ordinal());
                        break;
                    case R.id.btn_change_shipping_time:
                        shippingTimePopup(position);
                        break;
                    case R.id.btn_change_pay_way:
                        payWayPopup();
                        break;
                    case R.id.ll_store_info_container:
                        storeItem = (ConfirmOrderStoreItem) confirmOrderItemList.get(position);
                        start(ShopMainFragment.newInstance(storeItem.storeId));
                        break;
                    case R.id.btn_use_voucher:
                        storeItem = (ConfirmOrderStoreItem) confirmOrderItemList.get(position);
                        if (storeItem.voucherCount == 0) {  // 沒有店鋪券
                            return;
                        }
                        List<StoreVoucherVo> storeVoucherVoList = voucherMap.get(storeItem.storeId);
                        if (storeVoucherVoList == null || storeVoucherVoList.size() == 0) {
                            return;
                        }
                        new XPopup.Builder(_mActivity)
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(false)
                                .asCustom(new OrderVoucherPopup(_mActivity, storeVoucherVoList, ConfirmBillFragment.this))
                                .show();
                        SLog.info("HERE");
                        break;
                    default:
                        break;
                }
            }
        });
        rvStoreList.setAdapter(adapter);

        loadBillData();

        getMobileZoneList();
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
                .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_pay_way),
                        PopupType.PAY_WAY, payWayItemList, payWayIndex, this))
                .show();
    }

    private void shippingTimePopup(int position) {
        ConfirmOrderSummaryItem summaryItem = (ConfirmOrderSummaryItem) confirmOrderItemList.get(position);
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_shipping_time),
                        PopupType.SHIPPING_TIME, shippingItemList, summaryItem.shipTimeType, this, position))
                .show();
    }

    /**
     * 收集表單參數
     * @param forCommit 參數是否用于提交訂單
     * @return
     */
    private EasyJSONObject collectParams(boolean forCommit) {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return null;
            }

            // 收集表單信息
            EasyJSONObject commitBuyData = EasyJSONObject.generate(
                    // "paymentTypeCode", "online",
                    "paymentTypeCode", Constant.PAYMENT_TYPE_CODE_OFFLINE,
                    "isCart", isFromCart,
                    "isExistTrys", isExistTrys,
                    "storeList", commitStoreList);

            SLog.info("forCommit[%s]", forCommit);
            if (forCommit) {
                ConfirmOrderSummaryItem summaryItem = getSummaryItem();

                // 如果是用于提交訂單，需要從新收集最新的數據
                EasyJSONArray storeList = EasyJSONArray.generate();
                for (int i = 0; i < confirmOrderItemList.size() - 1; i++) {
                    ConfirmOrderStoreItem storeItem = (ConfirmOrderStoreItem) confirmOrderItemList.get(i);
                    /*
                    storeId int 店铺Id,必填
                    receiverMessage string 购买留言，可以为空

                    goodsList 购买商品列表,必填
                        buyNum int 购买数量，必填
                        goodsId int 商品sku Id 当cartId=0时必填
                        cartId int 购物车Id 当cartId=1时必填
                    invoiceTitle string 发票抬头
                    invoiceContent string 发票内容
                    invoiceCode string 纳税人识别号
                    shipTimeType int 配送时间Id
                     */
                    EasyJSONObject store = EasyJSONObject.generate(
                            "storeId", storeItem.storeId,
                            "storeName", storeItem.storeId,
                            "shipTimeType", summaryItem.shipTimeType);

                    // 留言
                    if (!StringUtil.isEmpty(storeItem.leaveMessage)) {
                        store.set("receiverMessage", storeItem.leaveMessage);
                    }


                    // goodsList 购买商品列表
                    EasyJSONArray goodsList = EasyJSONArray.generate();
                    String keyName = "cartId";
                    if (isFromCart == Constant.ZERO) {
                        keyName = "goodsId";
                    }
                    for (ConfirmOrderSkuItem skuItem : storeItem.confirmOrderSkuItemList) {
                        goodsList.append(EasyJSONObject.generate(
                             "buyNum", skuItem.buyNum,
                             keyName, skuItem.goodsId));
                    }
                    store.set("goodsList", goodsList);


                    // 單據信息
                    if (summaryItem.receipt != null) {
                        store.set("invoiceTitle", summaryItem.receipt.header);
                        store.set("invoiceContent", summaryItem.receipt.content);
                        store.set("invoiceCode", summaryItem.receipt.taxPayerId);
                    }

                    storeList.append(store);
                }


                commitBuyData.set("paymentTypeCode", summaryItem.paymentTypeCode);
                commitBuyData.set("storeList", storeList);

                // 如果是門店自提的話，還要自提手機號和買家姓名
                if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(summaryItem.paymentTypeCode)) {
                    SLog.info("是門店自提");

                    String realName = etSelfFetchNickname.getText().toString().trim();
                    if (StringUtil.isEmpty(realName)) {
                        ToastUtil.error(_mActivity, getString(R.string.input_self_take_nickname_hint));
                        return null;
                    }

                    if (mobileZoneList == null || mobileZoneList.size() < 1) {
                        ToastUtil.error(_mActivity, getString(R.string.select_mobile_zone_hint));
                        return null;
                    }

                    String mobile = etSelfFetchMobile.getText().toString().trim();
                    if (StringUtil.isEmpty(mobile)) {
                        ToastUtil.error(_mActivity, getString(R.string.input_self_take_mobile_hint));
                        return null;
                    }

                    MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);
                    if (!StringUtil.isMobileValid(mobile, mobileZone.areaId)) {
                        String[] areaArray = new String[] {
                                "",
                                getString(R.string.text_hongkong),
                                getString(R.string.text_mainland),
                                getString(R.string.text_macao)
                        };

                        String msg = String.format(getString(R.string.text_invalid_mobile), areaArray[mobileZone.areaId]);
                        ToastUtil.error(_mActivity, msg);
                        return null;
                    }

                    String fullMobile = mobileZone.areaCode + mobile;

                    commitBuyData.set("realName", realName);
                    commitBuyData.set("mobile", fullMobile);
                }
            }

            // 收貨地址
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
            SLog.info("Error!%s", e.getMessage());
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
            try {
                EasyJSONObject params = collectParams(true);
                SLog.info("params[%s]", params);
                if (params == null) {
                    // ToastUtil.error(_mActivity, "數據無效");
                    return;
                }

                String buyData = params.getString("buyData");
                EasyJSONObject buyDataObj = (EasyJSONObject) EasyJSONObject.parse(buyData);
                final String paymentTypeCode = buyDataObj.getString("paymentTypeCode");
                SLog.info("paymentTypeCode[%s]", paymentTypeCode);

                // 如果不是門店自提的話，一定要有收貨地址信息
                if (!Constant.PAYMENT_TYPE_CODE_CHAIN.equals(paymentTypeCode) && mAddrItem == null) {
                    SLog.info("Error!地址信息無效");
                    ToastUtil.error(_mActivity, "地址信息無效");
                    return;
                }

                String path;
                if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(paymentTypeCode)) {
                    path = Api.PATH_SELF_TAKE;
                } else {
                    path = Api.PATH_COMMIT_BILL_DATA;
                }

                Api.postUI(path, params, new UICallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showNetworkError(_mActivity, e);
                    }

                    @Override
                    public void onResponse(Call call, String responseStr) throws IOException {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        pop();

                        SLog.info("paymentTypeCode[%s]", paymentTypeCode);
                        if (Constant.PAYMENT_TYPE_CODE_ONLINE.equals(paymentTypeCode) || Constant.PAYMENT_TYPE_CODE_CHAIN.equals(paymentTypeCode)) {
                            // 在線支付或門店自提都需要先付款
                            try {
                                int payId = responseObj.getInt("datas.payId");
                                new XPopup.Builder(_mActivity)
                                        // 如果不加这个，评论弹窗会移动到软键盘上面
                                        .moveUpToKeyboard(false)
                                        .asCustom(new PayPopup(_mActivity, (MainActivity) _mActivity, payId))
                                        .show();
                            } catch (EasyJSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Util.startFragment(PaySuccessFragment.newInstance(EasyJSONObject.generate().toString()));
                            ToastUtil.success(_mActivity, "提交訂單成功");
                        }
                    }
                });
            } catch (Exception e) {
                SLog.info("Error!%s", e.getMessage());
            }
        } else if (id == R.id.btn_add_shipping_address) {
            startForResult(AddAddressFragment.newInstance(Constant.ACTION_ADD, null), RequestCode.ADD_ADDRESS.ordinal());
        } else if (id == R.id.btn_change_shipping_address) {
            startForResult(AddrManageFragment.newInstance(), RequestCode.CHANGE_ADDRESS.ordinal());
        } else if (id == R.id.btn_change_self_take_mobile_zone) {
            List<ListPopupItem> itemList = new ArrayList<>();
            for (MobileZone mobileZone : mobileZoneList) {
                ListPopupItem item = new ListPopupItem(mobileZone.areaId, mobileZone.areaName, null);
                itemList.add(item);
            }

            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.mobile_zone_text),
                            PopupType.MOBILE_ZONE, itemList, selectedMobileZoneIndex, this))
                    .show();
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
            int position = data.getInt("position");
            int action = data.getInt("action");

            if (action == ReceiptInfoFragment.ACTION_NO_CHANGE) {
                return;
            }

            ConfirmOrderSummaryItem summaryItem = (ConfirmOrderSummaryItem) confirmOrderItemList.get(position);
            if (action == ReceiptInfoFragment.ACTION_NO_RECEIPT) { // 不開單據
                summaryItem.receipt = null;
            } else if (action == ReceiptInfoFragment.ACTION_SAVE_AND_USE) {
                summaryItem.receipt = data.getParcelable("receipt");
            }

            adapter.notifyItemChanged(position);
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

    /**
     * 顯示自提信息
     */
    private void showSelfFetchInfo() {
        btnAddShippingAddress.setVisibility(View.GONE);
        btnChangeShippingAddress.setVisibility(View.GONE);

        if (isFirstShowSelfFetchInfo) {  // 首次顯示自提信息
            if (mAddrItem != null) {
                etSelfFetchNickname.setText(mAddrItem.realName);

                // 設置不帶區號的手機號
                String shortMobile = mAddrItem.mobPhone;
                if (shortMobile.startsWith(Constant.AREA_CODE_HONGKONG) || shortMobile.startsWith(Constant.AREA_CODE_MACAO)) {
                    shortMobile = shortMobile.substring(5);
                } else if (shortMobile.startsWith(Constant.AREA_CODE_MAINLAND)) {
                    shortMobile = shortMobile.substring(4);
                }

                etSelfFetchMobile.setText(shortMobile);

                SLog.info("mAddrItem[%s]", mAddrItem.toString());
            }

            isFirstShowSelfFetchInfo = false;
        }
        llSelfFetchInfoContainer.setVisibility(View.VISIBLE);
    }

    private void loadBillData() {
        final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                .asLoading("正在生成訂單")
                .show();
        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                loadingPopup.dismiss();
                String result = (String) message;
                if (!"success".equals(result)) {
                    ToastUtil.error(_mActivity, "生成訂單失敗");
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

                    // 第1步: 獲取配送時間列表 和 店鋪券列表
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

                    // 獲取配送時間列表
                    shippingItemList.clear();
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.shipTimeTypeList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        shippingItemList.add(new ListPopupItem(easyJSONObject.getInt("id"), easyJSONObject.getString("name"), null));
                    }
                    isExistTrys = responseObj.getInt("datas.isExistTrys");

                    SLog.info("HERE");
                    // 獲取店鋪券
                    EasyJSONArray buyStoreVoList = responseObj.getArray("datas.buyStoreVoList");
                    for (Object object : buyStoreVoList) {  // 遍歷每家店鋪
                        List<StoreVoucherVo> storeVoucherVoList = new ArrayList<>();
                        EasyJSONObject buyStoreVo = (EasyJSONObject) object;
                        int storeId = buyStoreVo.getInt("storeId");
                        EasyJSONArray voucherVoList = buyStoreVo.getArray("voucherVoList");
                        for (Object object2 : voucherVoList) {
                            StoreVoucherVo storeVoucherVo = (StoreVoucherVo) EasyJSONBase.jsonDecode(StoreVoucherVo.class, object2.toString());
                            storeVoucherVoList.add(storeVoucherVo);
                            SLog.info("storeVoucherVo[%s]", storeVoucherVo);
                        }

                        voucherMap.put(storeId, storeVoucherVoList);


                        String storeName = buyStoreVo.getString("storeName");
                        int itemCount = buyStoreVo.getInt("itemCount");
                        float freightAmount = (float) buyStoreVo.getDouble("freightAmount");
                        float buyItemAmount = (float) buyStoreVo.getDouble("buyItemAmount");

                        int shipTimeType = 0;

                        EasyJSONArray goodsList = EasyJSONArray.generate();
                        EasyJSONArray buyGoodsItemVoList = buyStoreVo.getArray("buyGoodsItemVoList");
                        List<ConfirmOrderSkuItem> confirmOrderSkuItemList = new ArrayList<>();
                        for (Object object2 : buyGoodsItemVoList) {
                            EasyJSONObject buyGoodsItem = (EasyJSONObject) object2;
                            int goodsId;
                            if (isFromCart == 1) {
                                goodsId = buyGoodsItem.getInt("cartId");
                            } else {
                                goodsId = buyGoodsItem.getInt("goodsId");
                            }

                            int buyNum = buyGoodsItem.getInt("buyNum");

                            String imageSrc = buyGoodsItem.getString("imageSrc");
                            String goodsName = buyGoodsItem.getString("goodsName");
                            String goodsFullSpecs = buyGoodsItem.getString("goodsFullSpecs");
                            float goodsPrice = (float) buyGoodsItem.getDouble("goodsPrice");


                            ConfirmOrderSkuItem confirmOrderSkuItem = new ConfirmOrderSkuItem(imageSrc, goodsId, goodsName, goodsFullSpecs, buyNum, goodsPrice);
                            confirmOrderSkuItemList.add(confirmOrderSkuItem);

                            String keyName = "cartId";
                            if (isFromCart == Constant.ZERO) {
                                keyName = "goodsId";
                            }
                            goodsList.append(EasyJSONObject.generate(keyName, goodsId, "buyNum", buyNum));

                            totalItemCount++;
                        }

                        confirmOrderItemList.add(new ConfirmOrderStoreItem(storeId, storeName, buyItemAmount,
                                freightAmount, itemCount, storeVoucherVoList.size(), confirmOrderSkuItemList));

                        commitStoreList.append(EasyJSONObject.generate(
                                "storeId", storeId,
                                "storeName", storeName,
                                "goodsList", goodsList,
                                "shipTimeType", shipTimeType));
                    }
                    SLog.info("HERE");

                    // 添加上汇总项目
                    confirmOrderItemList.add(new ConfirmOrderSummaryItem());

                    // 第2步：計算運費
                    params = collectParams(false);
                    // 收集地址信息
                    EasyJSONObject address = responseObj.getObject("datas.address");
                    if (address != null) { // TODO: 2019/9/11 如果没有地址信息或【门店自提】方式，都不用做第2步
                        mAddrItem = new AddrItem(address);
                        SLog.info("mAddrItem[%s]", mAddrItem);

                        params = collectParams(false);
                        SLog.info("params[%s]", params.toString());
                        responseStr = Api.syncPost(Api.PATH_CALC_FREIGHT, params);

                        SLog.info("responseStr[%s]", responseStr);
                        responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.isError(responseObj)) {
                            return null;
                        }
                    }

                    // 第3步(請求參數與第2步相同) 計算最終結果
                    SLog.info("params[%s]", params.toString());
                    responseStr = Api.syncPost(Api.PATH_CALC_TOTAL, params);

                    SLog.info("responseStr[%s]", responseStr);
                    responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        return null;
                    }

                    ConfirmOrderSummaryItem summaryItem = getSummaryItem();
                    summaryItem.totalItemCount = totalItemCount;
                    summaryItem.totalAmount = (float) responseObj.getDouble("datas.buyGoodsItemAmount");
                    summaryItem.storeDiscount = (float) responseObj.getDouble("datas.storeTotalDiscountAmount");

                    return "success";
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }

                return null;
            }
        };

        TwantApplication.getThreadPool().execute(taskObservable);
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.PAY_WAY) {
            payWayIndex = id;
            SLog.info("payWayIndex[%d]", payWayIndex);
            ConfirmOrderSummaryItem summaryItem = getSummaryItem();
            if (summaryItem == null) {
                return;
            }

            summaryItem.paymentTypeCode = getPaymentTypeCode(payWayIndex);
            SLog.info("paymentTypeCode[%s], position[%d]", summaryItem.paymentTypeCode, confirmOrderItemList.size() - 1);
            adapter.notifyItemChanged(confirmOrderItemList.size() - 1);

            if (payWayIndex == 2) { // 門店自提
                showSelfFetchInfo();
            } else { // 在線支付 或 貨到付款
                llSelfFetchInfoContainer.setVisibility(View.GONE);
                updateAddrView();
            }
        } else if (type == PopupType.SHIPPING_TIME) {
            int position = (int) extra;
            ConfirmOrderSummaryItem summaryItem = (ConfirmOrderSummaryItem) confirmOrderItemList.get(position);
            summaryItem.shipTimeType = id;
            adapter.notifyItemChanged(position);
        } else if (type == PopupType.MOBILE_ZONE) {
            SLog.info("selectedMobileZoneIndex[%d], id[%d]", selectedMobileZoneIndex, id);
            if (this.selectedMobileZoneIndex == id) {
                return;
            }

            this.selectedMobileZoneIndex = id;
            String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
            tvSelfFetchMobileZone.setText(areaName);
        }
    }

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

    /**
     * 获取区号列表
     */
    private void getMobileZoneList() {
        Api.getMobileZoneList(new TaskObserver() {
            @Override
            public void onMessage() {
                mobileZoneList = (List<MobileZone>) message;
                if (mobileZoneList == null) {
                    return;
                }
                SLog.info("mobileZoneList.size[%d]", mobileZoneList.size());
                if (mobileZoneList.size() > 0) {
                    SLog.info("areaName[%s]", mobileZoneList.get(0).areaName);
                    tvSelfFetchMobileZone.setText(mobileZoneList.get(0).areaName);
                }
            }
        });
    }
}
