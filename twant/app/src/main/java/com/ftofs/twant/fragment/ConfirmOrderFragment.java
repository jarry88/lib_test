package com.ftofs.twant.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.ConfirmOrderStoreAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.CalcFreightResult;
import com.ftofs.twant.entity.ConfirmOrderSkuItem;
import com.ftofs.twant.entity.ConfirmOrderStoreItem;
import com.ftofs.twant.entity.ConfirmOrderSummaryItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.entity.PayWayItem;
import com.ftofs.twant.entity.RealNameListItem;
import com.ftofs.twant.entity.SoldOutGoodsItem;
import com.ftofs.twant.entity.StoreAmount;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.entity.StoreVoucherVo;
import com.ftofs.twant.entity.VoucherUseStatus;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.SloganView;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.store.StoreVo;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.OrderVoucherPopup;
import com.ftofs.twant.widget.PayWayPopup;
import com.ftofs.twant.widget.RealNamePopup;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.SoldOutPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;


/**
 * 確認訂單Fragment
 * @author zwm
 */
public class ConfirmOrderFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    String buyData;

    int isFromCart;
    int isGroup;
    int goId; // 開團Id
    int bargainOpenId; // 砍價Id

    // 是否不需要再支付（比如，买1元的商品，用1元的代金抵扣）
    int isPayed = Constant.FALSE_INT;

    /*
        /buy/step2
            或
        /buy/take/save
        接口返回的payId
     */
    int finalPayId = 0;

    List<PayWayItem> payWayItemList = new ArrayList<>();
    List<ListPopupItem> shippingItemList = new ArrayList<>();

    // 當前支付方式
    int payWay = Constant.PAY_WAY_ONLINE;
    // 當前選中的支付方式索引
    int selectedPayWayIndex = 0;

    AddrItem mAddrItem;

    TextView tvReceiverName;
    TextView tvMobile;
    TextView tvAddress;
    TextView tvItemCount;  // 共xxx件
    TextView tvTotalPrice;  // 合計:多少錢
    double totalPrice;
    double totalFreightAmount;

    RelativeLayout btnAddShippingAddress;
    LinearLayout btnChangeShippingAddress;
    LinearLayout llSelfFetchInfoContainer;

    ConfirmOrderStoreAdapter adapter;
    List<MultiItemEntity> confirmOrderItemList = new ArrayList<>();

    // 当前选中的支付方式代码
    String currPaymentTypeCode = Constant.PAYMENT_TYPE_CODE_ONLINE;
    String currencyTypeSign;
    int totalItemCount; // 整個訂單的總件數： 如果sku1有2件，sku2有3件，那么總件數就是5
    String textConfirmOrderTotalItemCount;
    Map<Integer, String> paymentTypeCodeMap = new HashMap<>();

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
     * 提交訂單時上去的商店列表
     */
    EasyJSONArray commitStoreList = EasyJSONArray.generate();

    // 商店Id => 商店券列表
    Map<Integer, List<StoreVoucherVo>> voucherMap = new HashMap<>();
    // 商店Id => 運費
    Map<Integer, Double> freightAmountMap = new HashMap<>();
    // 商店Id => 商店優惠
    Map<Integer, StoreAmount> storeAmountMap = new HashMap<>();
    // 商店Id => 商店满优惠列表(conformId，整型)
    Map<Integer, Integer> storeConformIdMap = new HashMap<>();


    int platformCouponIndex = -1; // 當前正在使用的平台券列表Index(-1表示沒有使用)
    List<StoreVoucherVo> platformCouponList = new ArrayList<>();
    private int tariffTotalEnable;

    PayWayItem specialItem;  // 只有選擇的收貨人信息是澳門地區才會顯示貨到付款這種交易方式，空地址、香港和內地的地址均不顯示；

    // 售罄商品列表
    List<SoldOutGoodsItem> soldOutGoodsItemList = new ArrayList<>();
    int totalGoodsCount; // 商品總數
    // Map<Integer, Integer> cartIdToGoodsId = new HashMap<>();
    Map<Integer, Integer> goodsIdToCartId = new HashMap<>();

    /**
     * 創建確認訂單的實例
     * @param isFromCart 1 -- 來源于購物袋 0 -- 直接購買
     * @param buyData
     * @param isGroup 是否為團購  1 -- 是   0 -- 否
     * @return
     */
    public static ConfirmOrderFragment newInstance(int isFromCart, String buyData, int isGroup, int goId, int bargainOpenId) {
        SLog.info("ConfirmOrderFragment.newInstance: isFromCart[%d], buyData[%s], isGroup[%d], goId[%d], bargainOpenId[%d]",
                isFromCart, buyData, isGroup, goId, bargainOpenId);

        Bundle args = new Bundle();

        args.putInt("isFromCart", isFromCart);
        args.putString("buyData", buyData);

        ConfirmOrderFragment fragment = new ConfirmOrderFragment();
        fragment.setArguments(args);
        fragment.isGroup = isGroup;
        fragment.goId = goId;
        fragment.bargainOpenId = bargainOpenId;

        return fragment;
    }


    public static ConfirmOrderFragment newInstance(int isFromCart, String buyData) {
        return newInstance(isFromCart, buyData, Constant.FALSE_INT, Constant.INVALID_GO_ID, Constant.INVALID_BARGAIN_OPEN_ID);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        isFromCart = args.getInt("isFromCart");
        buyData = args.getString("buyData");
        SLog.info("buyData[%s]", buyData);

        currencyTypeSign = getString(R.string.currency_type_sign);
        textConfirmOrderTotalItemCount = getString(R.string.text_confirm_order_total_item_count);

        paymentTypeCodeMap.put(Constant.PAY_WAY_ONLINE, Constant.PAYMENT_TYPE_CODE_ONLINE);
        paymentTypeCodeMap.put(Constant.PAY_WAY_DELIVERY, Constant.PAYMENT_TYPE_CODE_OFFLINE);
        paymentTypeCodeMap.put(Constant.PAY_WAY_FETCH, Constant.PAYMENT_TYPE_CODE_CHAIN);

        // 初始化支付方式數據
        payWayItemList.add(new PayWayItem(Constant.PAY_WAY_ONLINE, "物流配送", "在線付款後物流送貨", true, R.drawable.pay_way_online_selected, R.drawable.pay_way_online_unselected));
        payWayItemList.add(new PayWayItem(Constant.PAY_WAY_FETCH, "到店自提", "在線付款後門店取貨", false, R.drawable.pay_way_fetch_selected, R.drawable.pay_way_fetch_unselected));
        specialItem = new PayWayItem(Constant.PAY_WAY_DELIVERY, "貨到付款", "先送貨再線下付款", false, R.drawable.pay_way_delivery_selected, R.drawable.pay_way_delivery_unselected);


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
                        if (storeItem.voucherCount == 0) {  // 沒有商店券
                            ToastUtil.error(_mActivity,"無可用優惠券");
                            return;
                        }
                        List<StoreVoucherVo> storeVoucherVoList = voucherMap.get(storeItem.storeId);
                        if (storeVoucherVoList == null || storeVoucherVoList.size() == 0) {
                            return;
                        }
                        new XPopup.Builder(_mActivity)
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(false)
                                .asCustom(new OrderVoucherPopup(_mActivity, storeItem.storeId, storeItem.storeName,
                                        Constant.COUPON_TYPE_STORE, storeVoucherVoList, -1, ConfirmOrderFragment.this))
                                .show();
                        SLog.info("HERE");
                        break;
                    case R.id.btn_select_platform_coupon:
                        SLog.info("HERE");
                        new XPopup.Builder(_mActivity)
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(false)
                                .asCustom(new OrderVoucherPopup(_mActivity, 0, "",
                                        Constant.COUPON_TYPE_PLATFORM, platformCouponList, platformCouponIndex, ConfirmOrderFragment.this))
                                .show();
                        break;
                    default:
                        break;
                }
            }
        });
        rvStoreList.setAdapter(adapter);

        loadOrderData();

        getMobileZoneList();

//        determineShowRealNamePopup();
    }

    @Override
    public boolean onBackPressedSupport() {
        popWithOutRefresh();
        return true;
    }

    private void determineShowRealNamePopup() {
        SLog.info("determineShowRealNamePopup");
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        SLog.info("path[%s], params[%s]", Api.PATH_DETERMINE_SHOW_REAL_NAME_POPUP, params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        Api.getUI(Api.PATH_DETERMINE_SHOW_REAL_NAME_POPUP, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    int isShowAuth = responseObj.getInt("datas.isShowAuth");
                    if (isShowAuth == Constant.TRUE_INT) {
                        SLog.info("determineShowRealNamePopup");
                        new XPopup.Builder(_mActivity)
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(true)
                                .asCustom(new RealNamePopup(_mActivity, mAddrItem.realName))
                                .show();
                    } else {

                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    private void payWayPopup() {
        checkPayWay();
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new PayWayPopup(_mActivity, payWayItemList, selectedPayWayIndex, this))
                .show();
    }

    private void shippingTimePopup(int position) {
        ConfirmOrderSummaryItem summaryItem = (ConfirmOrderSummaryItem) confirmOrderItemList.get(position);
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new ListPopup(_mActivity, TwantApplication.getInstance().getString(R.string.text_shipping_time),
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
                    "storeList", commitStoreList);

            if (isGroup == Constant.TRUE_INT) {
                commitBuyData.set("isGroup", 1);
            }

            if (goId != Constant.INVALID_GO_ID) {
                commitBuyData.set("goId", goId);
            }

            if (bargainOpenId != Constant.INVALID_BARGAIN_OPEN_ID) {
                commitBuyData.set("bargainOpenId", bargainOpenId);
            }

            if (platformCouponIndex != -1) { // 如果有選擇平台券
                StoreVoucherVo platformCoupon = platformCouponList.get(platformCouponIndex);
                EasyJSONArray couponIdList = EasyJSONArray.generate(String.valueOf(platformCoupon.voucherId));
                commitBuyData.set("couponIdList", couponIdList);
            }

            SLog.info("forCommit[%s]", forCommit);
            if (forCommit) {
                ConfirmOrderSummaryItem summaryItem = getSummaryItem();

                // 如果是用于提交訂單，需要從新收集最新的數據
                EasyJSONArray storeList = EasyJSONArray.generate();
                for (MultiItemEntity multiItemEntity:confirmOrderItemList) {
                    if (multiItemEntity.getItemType() != Constant.ITEM_VIEW_TYPE_COMMON) {
                        //防止强制轉換失敗
                        continue;
                    }
                    ConfirmOrderStoreItem storeItem = (ConfirmOrderStoreItem) multiItemEntity;
                    /*
                    storeId int 店铺Id,必填
                    receiverMessage string 购买留言，可以为空
                    voucherId int 店铺券Id,可为空
                    goodsList 购买產品列表,必填
                        buyNum int 购买数量，必填
                        goodsId int 產品sku Id 当cartId=0时必填
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

                    // 看是否要conformId
                    Integer conformId = storeConformIdMap.get(storeItem.storeId);
                    if (conformId != null) {
                        store.set("conformId", conformId.toString());
                    }
//                    store.set("conformId", "196");
                    // 留言
                    if (!StringUtil.isEmpty(storeItem.leaveMessage)) {
                        store.set("receiverMessage", storeItem.leaveMessage);
                    }

                    if (storeItem.voucherId > 0) {
                        SLog.info(">>>______________________________________<<<");
                        store.set("voucherId", storeItem.voucherId);
                    }


                    // goodsList 购买產品列表
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
            //門店自提必須addId設置為0，防止後臺誤判為跨城購
            if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(getSummaryItem().paymentTypeCode)) {
                commitBuyData.set("addressId", "0");
            } else if (!commitBuyData.exists("addressId")) {
                commitBuyData.set("addressId","0");
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "clientType", Constant.CLIENT_TYPE_ANDROID,
                    "buyData", commitBuyData.toString());

            SLog.info("collectParams.params[%s]", params.toString());
            return params;
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        return null;
    }

    /**
     * 彈出，并且父Fragment不刷新
     */
    private void popWithOutRefresh() {
        // TODO: 2019/5/20 通知上一級不刷新數據
        hideSoftInputPop();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            popWithOutRefresh();
        } else if (id == R.id.btn_commit) {
            if (soldOutGoodsItemList.size() > 0) {
                showSoldOutPopup();
                return;
            }

            ConfirmOrderSummaryItem summaryItem = getSummaryItem();
            if (summaryItem != null) {
                double totalPrice = summaryItem.calcTotalPrice();
                SLog.info("summaryItem.totalPrice[%s]", totalPrice);
                if (summaryItem.totalAmount >= 20000) {
//                    ToastUtil.error(_mActivity, "每次交易總金額不得超過 $20,000，請調整購物數量再提交");
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
                            }).asCustom(new TwConfirmPopup(_mActivity, "每次交易總金額不得超過$20,000，請調整購物數量再提交",null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    }))
                            .show();
                    return;
                }
            }

            hideSoftInput();
            try {
                EasyJSONObject params = collectParams(true);
                SLog.info("params[%s]", params);
                if (params == null) {
                    // ToastUtil.error(_mActivity, "數據無效");
                    return;
                }

                String buyData = params.getSafeString("buyData");
                EasyJSONObject buyDataObj = EasyJSONObject.parse(buyData);
                final String paymentTypeCode = buyDataObj.getSafeString("paymentTypeCode");
                SLog.info("paymentTypeCode[%s]", paymentTypeCode);

                // 如果不是門店自提的話，一定要有收貨地址信息
                if (!Constant.PAYMENT_TYPE_CODE_CHAIN.equals(paymentTypeCode) && mAddrItem == null) {
                    SLog.info("Error!請添加收貨地址");
                    ToastUtil.error(_mActivity, "請添加收貨地址");
                    return;
                }

                String path;
                if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(paymentTypeCode)) { // 门店自提
                    path = Api.PATH_SELF_TAKE;
                } else { // 直接购买
                    path = Api.PATH_COMMIT_BILL_DATA;
                }
                SLog.info("url[%s],params[%s]",path,params.toString());
                Api.postUI(path, params, new UICallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showNetworkError(_mActivity, e);
                    }

                    @Override
                    public void onResponse(Call call, String responseStr) throws IOException {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        hideSoftInput();

                        SLog.info("paymentTypeCode[%s]", paymentTypeCode);
                        if (Constant.PAYMENT_TYPE_CODE_ONLINE.equals(paymentTypeCode) || Constant.PAYMENT_TYPE_CODE_CHAIN.equals(paymentTypeCode)) {
                            // 在線支付或門店自提都需要先付款
                            try {
                                int isAuth = Constant.FALSE_INT;
                                if (responseObj.exists("datas.isAuth")) {
                                   isAuth= responseObj.getInt("datas.isAuth");
                                }
                                SLog.info("__isAuth[%d]", isAuth);
                                if (isAuth == Constant.TRUE_INT) {
                                    new XPopup.Builder(_mActivity)
                                            // 如果不加这个，评论弹窗会移动到软键盘上面
                                            .moveUpToKeyboard(true)
                                            .asCustom(new RealNamePopup(_mActivity, mAddrItem.realName))
                                            .show();
                                    return;
                                } else {
                                    hideSoftInputPop();
                                }
                                finalPayId = responseObj.getInt("datas.payId");
                                isPayed = responseObj.getInt("datas.isPayed");

                                SLog.info("isPayed[%d], finalPayId[%d]", isPayed, finalPayId);
                                if (isPayed == Constant.TRUE_INT && finalPayId > 0) { // 不需要再支付，直接显示支付成功页面
                                    SLog.info("不需要再支付，直接显示支付成功页面");
                                    hideSoftInputPop();
                                    Util.startFragment(PaySuccessFragment.newInstance(finalPayId));

                                    return;
                                }

                                start(PayVendorFragment.newInstance(finalPayId, totalPrice, 0));
                            } catch (Exception e) {
                                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                            }
                        } else { // 货到付款
                            try {
                                finalPayId = responseObj.getInt("datas.payId");
                            } catch (Exception e) {
                                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                            }
                            SLog.info("finalPayId[%d]", finalPayId);
                            pop();
                            Util.startFragment(PaySuccessFragment.newInstance(finalPayId, true));

                            ToastUtil.success(_mActivity, "訂單提交成功");

                            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_GOODS_DETAIL, null);
                        }
                    }
                });
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
                    .asCustom(new ListPopup(_mActivity, TwantApplication.getInstance().getString(R.string.mobile_zone_text),
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
        if (requestCode == RequestCode.REAL_NAME_INFO.ordinal()) {
            SLog.info("data[%s]",data.toString());
            boolean reloadData = data.getBoolean("reloadData");
          SLog.info("執行realname result了回調");
            return;
        }
        if (AddrManageFragment.class.getName().equals(from) || AddAddressFragment.class.getName().equals(from)) {
            SLog.info("from[%s]", from);
            // 從地址管理Fragment返回 或 從地址添加Fragment返回
            boolean isNoAddress = data.getBoolean("isNoAddress", false); // 標記是否刪除了所有地址
            if (isNoAddress) {
                mAddrItem = null;
                updateAddrView();
                checkPayWay();
                return;
            }

            // 上一級Fragment返回的地址項
            AddrItem addrItem = data.getParcelable("addrItem");
            SLog.info("addrItem[%s]", addrItem);
            if (addrItem == null) {
                if (mAddrItem != null) {
                    // 重新獲取地址信息（在這種場合下：用戶點擊了收貨地址，編輯了收貨人姓名等信息，但沒重新選擇，就需要重新刷新一下地址信息顯示）
                    updateAddrData();
                    checkPayWay();
                }
                return;
            }
            SLog.info("addrItem: %s", addrItem);
            mAddrItem = addrItem;
            checkPayWay();
//            loadOrderData();
            updateTotalOrderData();
            updateFreightTotalAmount();
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
     * 主要用來在更換地址時更新商品庫存情況
     */
    private void updateTotalOrderData() {
        SLog.info("重新加載商品庫存數據");

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                Pair<Boolean, String> result = loadOrder(null);

                if (result.first) {
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable(result.second));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                SLog.info("onSubscribe, threadId[%s]", Thread.currentThread().getId());
            }
            @Override
            public void onNext(String s) {
                SLog.info("onNext[%s], threadId[%s]", s, Thread.currentThread().getId());
            }
            @Override
            public void onError(Throwable e) {
//                ToastUtil.error();
                SLog.info("onError[%s], threadId[%s]", e.getMessage(), Thread.currentThread().getId());
            }
            @Override
            public void onComplete() {
                SLog.info("onComplete, threadId[%s]", Thread.currentThread().getId());
                updateOrderView();
                calcAmount();
            }
        };

        observable.subscribe(observer);
    }

    /**
     * 同步加載商品信息
     * @param o
     * @return
     */
    private Pair<Boolean, String> loadOrder(Object o) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return new Pair<>(false,"用戶未登錄");
        }
        // 整個訂單的總件數
        totalItemCount = 0;

        // 第1步: 獲取配送時間列表 和 商店券列表
        EasyJSONArray currBuyData =EasyJSONArray.generate();
        for (MultiItemEntity multiItemEntity : confirmOrderItemList) {
            if (multiItemEntity.getItemType() == Constant.ITEM_VIEW_TYPE_COMMON) {
                ConfirmOrderStoreItem storeItem = (ConfirmOrderStoreItem) multiItemEntity;
                for (ConfirmOrderSkuItem skuItem : storeItem.confirmOrderSkuItemList) {
                    EasyJSONObject buyItem = EasyJSONObject.generate("buyNum", skuItem.buyNum, "goodsId", skuItem.goodsId);

                    try {
                        if (goId != Constant.INVALID_GO_ID) {
                            buyItem.set("goId", goId);
                        }
                        if (bargainOpenId != Constant.INVALID_BARGAIN_OPEN_ID) {
                            buyItem.set("bargainOpenId", bargainOpenId);
                        }
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                    currBuyData.append(buyItem);
                }
            }
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "buyData", currBuyData.toString(),
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "isCart", isFromCart);
        SLog.info("params[%s]", params.toString());
        String responseStr = Api.syncPost(Api.PATH_DISPLAY_BILL_DATA, params);

        SLog.info("responseStr[%s]", responseStr);
        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
        try {
            if (ToastUtil.isError(responseObj)) {
                if (responseObj.exists("datas.error")) {
                        return new Pair<>(false,responseObj.getSafeString("datas.error"));
                }
                return new Pair<>(false,"null");
            }

            // 獲取配送時間列表
            shippingItemList.clear();
            EasyJSONArray easyJSONArray = responseObj.getSafeArray("datas.shipTimeTypeList");
            for (Object object : easyJSONArray) {
                EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                shippingItemList.add(new ListPopupItem(easyJSONObject.getInt("id"), easyJSONObject.getSafeString("name"), null));
            }

            confirmOrderItemList.clear();
            traverseStore(responseObj);

            // 添加上汇总项目
            ConfirmOrderSummaryItem confirmOrderSummaryItem = new ConfirmOrderSummaryItem();
            confirmOrderItemList.add(confirmOrderSummaryItem);
            _mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setNewData(confirmOrderItemList);
                }
            });
            return new Pair<>(true, "");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            return new Pair<>(false, "");
        }
    }

    private void traverseStore(EasyJSONObject responseObj) {
        try {
            // 獲取商店券
            EasyJSONArray buyStoreVoList = responseObj.getSafeArray("datas.buyStoreVoList");
            SLog.info("HERE storeSize[%d]",buyStoreVoList.length());
            for (Object object : buyStoreVoList) {  // 遍歷每家商店
                int storeTariff = Constant.FALSE_INT;
                List<StoreVoucherVo> storeVoucherVoList = new ArrayList<>();
                EasyJSONObject buyStoreVo = (EasyJSONObject) object;
                int storeId = buyStoreVo.getInt("storeId");

                int maxAmountVoucherId = 0; // 最大面额的优惠券Id
                double maxVoucherAmount = 0; // 最大面额的优惠券
                StoreVoucherVo maxAmountStoreVoucherVo = null;  // 最大面额的优惠券对象
                EasyJSONArray voucherVoList = buyStoreVo.getSafeArray("voucherVoList");
                for (Object object2 : voucherVoList) {
                    StoreVoucherVo storeVoucherVo = (StoreVoucherVo) EasyJSONBase.jsonDecode(StoreVoucherVo.class, object2.toString());
                    if (storeVoucherVo.useEnable == Constant.FALSE_INT) {
                        continue;
                    }

                    if (storeVoucherVo.price > maxVoucherAmount) {
                        maxAmountVoucherId = storeVoucherVo.voucherId;
                        maxVoucherAmount = storeVoucherVo.price;
                        maxAmountStoreVoucherVo = storeVoucherVo;
                    }

                    storeVoucherVoList.add(storeVoucherVo);
                    SLog.info("storeVoucherVo[%s]", storeVoucherVo);
                }

                if (maxAmountStoreVoucherVo != null) {
                    maxAmountStoreVoucherVo.isInUse = true;
                }

                voucherMap.put(storeId, storeVoucherVoList);
                int conformId = -1;
                // 获取满减优惠
                SLog.info("buyStoreVo[%s]", buyStoreVo);
                if (buyStoreVo.exists("conform.conformId")) {
                    conformId = buyStoreVo.getInt("conform.conformId");
                    storeConformIdMap.put(storeId, conformId);
                }

                String storeName = buyStoreVo.getSafeString("storeName");
                // int itemCount = buyStoreVo.getInt("itemCount");
                // float freightAmount = (float) buyStoreVo.getDouble("freightAmount"); 在第2步中獲取運費
                // float buyItemAmount = (float) buyStoreVo.getDouble("buyItemAmount"); 在第3步中獲取金額

                int shipTimeType = 0;

                EasyJSONArray goodsList = EasyJSONArray.generate();
                EasyJSONArray buyGoodsItemVoList = buyStoreVo.getSafeArray("buyGoodsItemVoList");
                List<ConfirmOrderSkuItem> confirmOrderSkuItemList = new ArrayList<>();
                int storeItemCount = 0;
                for (Object object2 : buyGoodsItemVoList) { // 遍歷每個Sku
                    EasyJSONObject buyGoodsItem = (EasyJSONObject) object2;
                    int goodsId;
                    int cartId=-1;
                    if (isFromCart == Constant.TRUE_INT) {
                        goodsId = buyGoodsItem.getInt("cartId");
                        cartId = buyGoodsItem.getInt("cartId");

                        int realGoodsId = buyGoodsItem.getInt("goodsId");  // 真正的GoodsId, 而不是購物車Id
                        goodsIdToCartId.put(realGoodsId, cartId);
                    } else {
                        goodsId = buyGoodsItem.getInt("goodsId");
                    }

                    int buyNum = buyGoodsItem.getInt("buyNum");

                    String imageSrc = buyGoodsItem.getSafeString("imageSrc");
                    String goodsName = buyGoodsItem.getSafeString("goodsName");
                    String goodsFullSpecs = buyGoodsItem.getSafeString("goodsFullSpecs");
                    int storageStatus =buyGoodsItem.getInt("storageStatus");
                    int allowSend =buyGoodsItem.getInt("allowSend");
                    int tariffEnable = buyGoodsItem.getInt("tariffEnable");
                    int joinBigSale = buyGoodsItem.getInt("joinBigSale");
                    if (tariffEnable == Constant.TRUE_INT) {
                        storeTariff = Constant.TRUE_INT;
                        tariffTotalEnable = Constant.TRUE_INT;
                    }
                    float goodsPrice = (float) buyGoodsItem.getDouble("goodsPrice");

                    // 處理SKU贈品信息
                    List<GiftItem> giftItemList = new ArrayList<>();
                    EasyJSONArray giftVoList = buyGoodsItem.getSafeArray("giftVoList");
                    if (giftVoList != null && giftVoList.length() > 0) {
                        for (Object object3 : giftVoList) {
                            GiftItem giftItem = (GiftItem) EasyJSONBase.jsonDecode(GiftItem.class, object3.toString());
                            giftItemList.add(giftItem);
                        }
                    }

                    ConfirmOrderSkuItem confirmOrderSkuItem = new ConfirmOrderSkuItem(imageSrc, goodsId, goodsName,
                            goodsFullSpecs, buyNum, goodsPrice, giftItemList);
                    confirmOrderSkuItem.cartId = cartId;
                    confirmOrderSkuItem.storageStatus = storageStatus;
                    confirmOrderSkuItem.allowSend = allowSend;
                    confirmOrderSkuItem.joinBigSale = joinBigSale;
                    confirmOrderSkuItemList.add(confirmOrderSkuItem);

                    String keyName = "cartId";
                    if (isFromCart == Constant.ZERO) {
                        keyName = "goodsId";
                    }
                    goodsList.append(EasyJSONObject.generate(keyName, goodsId, "buyNum", buyNum));

                    storeItemCount += buyNum;
                    totalItemCount += buyNum;
                } // END OF 遍歷每個Sku

                // 確認訂單時，店鋪滿減券
                float conformTemplatePrice = 0;
                if (buyStoreVo.exists("conform.templatePrice")) {
                    conformTemplatePrice = (float) buyStoreVo.getDouble("conform.templatePrice");
                    SLog.info("conformTemplatePrice[%s]", conformTemplatePrice);
                    // conformTemplatePrice = 999;
                }
                ConfirmOrderStoreItem storeItem;
                if (storeTariff == Constant.TRUE_INT) {
                    SLog.info("跨城購店鋪數據[%s]",buyStoreVo.toString());
                    storeItem = new ConfirmOrderStoreItem(storeId, storeName, 0,
                            0, storeItemCount, storeVoucherVoList.size(), maxAmountVoucherId, confirmOrderSkuItemList, conformTemplatePrice,0);
                } else {
                    storeItem = new ConfirmOrderStoreItem(storeId, storeName, 0,
                            0, storeItemCount, storeVoucherVoList.size(), maxAmountVoucherId, confirmOrderSkuItemList, conformTemplatePrice);
                }

                if (maxAmountStoreVoucherVo != null) { // 如果有优惠券，则使用最大面额的优惠券
                    storeItem.voucherName = formatVoucherName(maxAmountStoreVoucherVo);
                }
                confirmOrderItemList.add(storeItem);

                commitStoreList.append(EasyJSONObject.generate(
                        "storeId", storeId,
                        "storeName", storeName,
                        "goodsList", goodsList,
                        "shipTimeType", shipTimeType,
                        //沒有活動是傳空字符串
                        "conformId",conformId>=0?conformId:""));
            }  // END OF 遍歷每家商店
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void updateFreightTotalAmount() {
        SLog.info("重新計算整個頁面運費");
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                CalcFreightResult result = calcFreight(null);

                if (result.success) {
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable(result.errorMessage));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                SLog.info("onSubscribe, threadId[%s]", Thread.currentThread().getId());
            }
            @Override
            public void onNext(String s) {
                SLog.info("onNext[%s], threadId[%s]", s, Thread.currentThread().getId());
            }
            @Override
            public void onError(Throwable e) {
//                ToastUtil.error();
                SLog.info("onError[%s], threadId[%s]", e.getMessage(), Thread.currentThread().getId());
            }
            @Override
            public void onComplete() {
                SLog.info("onComplete, threadId[%s], soldOutGoodsItemList.size[%d]", Thread.currentThread().getId(), soldOutGoodsItemList.size());

                updateOrderView();
                calcAmount();

                if (soldOutGoodsItemList.size() > 0) {
                    showSoldOutPopup();
                }
            }
        };

        observable.subscribe(observer);
    }

    private void showSoldOutPopup() {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SoldOutPopup(_mActivity, soldOutGoodsItemList, totalGoodsCount > soldOutGoodsItemList.size(), this))
                .show();
    }

    /**
     * 更新地址信息的顯示
     */
    private void updateAddrView() {
        SLog.info("currPaymentTypeCode[%s]", currPaymentTypeCode);
        if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(currPaymentTypeCode)) {
            btnChangeShippingAddress.setVisibility(View.GONE);
            btnChangeShippingAddress.setVisibility(View.GONE);
            llSelfFetchInfoContainer.setVisibility(View.VISIBLE);
        }else if (mAddrItem == null) {
            // 用戶沒有收貨地址，顯示【新增收貨地址】按鈕
            btnAddShippingAddress.setVisibility(View.VISIBLE);
            btnChangeShippingAddress.setVisibility(View.GONE);
        } else {
            btnAddShippingAddress.setVisibility(View.GONE);
            btnChangeShippingAddress.setVisibility(View.VISIBLE);

            tvReceiverName.setText(_mActivity.getString(R.string.text_receiver) + ": " + mAddrItem.realName);
            tvMobile.setText(mAddrItem.mobPhone);
            tvAddress.setText(mAddrItem.areaInfo + " " + mAddrItem.address);
        }
        //門店自提時隱藏地址欄顯示 自提欄

    }

    /**
     * 顯示自提信息
     */
    private void showSelfFetchInfo() {
        SLog.info("顯示自提信息");
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

    /**
     * 更新商店優惠數據和商店購買金額
     */
    private void updateStoreAmount() {
        for (MultiItemEntity entity : confirmOrderItemList) {
            if (!(entity instanceof ConfirmOrderStoreItem)) {
                continue;
            }
            ConfirmOrderStoreItem item = (ConfirmOrderStoreItem) entity;

            StoreAmount storeAmount = storeAmountMap.get(item.storeId);
            if (storeAmount == null) {
                continue;
            }
            item.discountAmount = storeAmount.storeDiscountAmount;
            item.buyItemAmount = storeAmount.storeBuyAmount;
            SLog.info("item.discountAmount[%s]", item.discountAmount);
        }
    }

    /**
     * 更新商店運費數據
     */
    private void updateStoreFreightAmount() {
        for (MultiItemEntity entity : confirmOrderItemList) {
            if (!(entity instanceof ConfirmOrderStoreItem)) {
                continue;
            }
            ConfirmOrderStoreItem item = (ConfirmOrderStoreItem) entity;

            Double storeFreight = freightAmountMap.get(item.storeId);
            if (storeFreight == null) {
                continue;
            }
            item.freightAmount = storeFreight;
        }
    }

    /**
     * 更新收貨地址顯示
     */
    private void updateAddrData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        if (mAddrItem == null) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);
        Api.postUI(Api.PATH_LIST_ADDRESS, params, new UICallback() {
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

                    EasyJSONArray addressList = responseObj.getSafeArray("datas.addressList");
                    for (Object object : addressList) {
                        EasyJSONObject item = (EasyJSONObject) object;

                        int addressId = item.getInt("addressId");
                        String realName = item.getSafeString("realName");
                        List<Integer> areaIdList = new ArrayList<>();
                        for (int i = 1; i <= 4; ++i) {
                            areaIdList.add(item.getInt("areaId" + i));
                        }
                        int areaId = item.getInt("areaId");
                        String areaInfo = item.getSafeString("areaInfo");
                        String address = item.getSafeString("address");
                        String mobileAreaCode = item.getSafeString("mobileAreaCode");
                        String mobPhone = item.getSafeString("mobPhone");
                        int isDefault = item.getInt("isDefault");

                        // 已經加載到最新的地址信息
                        if (addressId == mAddrItem.addressId) {
                            mAddrItem = new AddrItem(addressId, realName, areaIdList, areaId, areaInfo, address, mobileAreaCode, mobPhone, isDefault);
                            updateAddrView();
                            break;
                            }
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void checkPayWay() {
        // 只有選擇的收貨人信息是澳門地區才會顯示貨到付款這種交易方式，空地址、香港和內地的地址均不顯示；
        if (mAddrItem != null && mAddrItem.areaIdList!=null
                && mAddrItem.areaIdList.size() > 0
                && mAddrItem.areaIdList.get(0) == Constant.DISTRICT_ID_MACAO
                && isGroup == Constant.FALSE_INT // 團購不顯示貨到付款
        ) {
            SLog.info("顯示貨到付款");
            if (payWayItemList.size() < 3) {
                payWayItemList.add(specialItem);
            }
        } else {
            SLog.info("不顯示貨到付款");
            if (payWayItemList.size() == 3) {
                payWayItemList.remove(2);
            }

            // 如果原先選的是【貨到付款】，則默認選中【在線支付】
            if (payWay == Constant.PAY_WAY_DELIVERY) {
                ConfirmOrderSummaryItem summaryItem = getSummaryItem();
                if (summaryItem == null) {
                    return;
                }
                specialItem.isSelected = false;
                payWayItemList.get(0).isSelected = true;
                payWay = Constant.PAY_WAY_ONLINE;
                selectedPayWayIndex = 0;

                summaryItem.paymentTypeCode = paymentTypeCodeMap.get(payWay);
                summaryItem.payWayIndex = payWay;
                SLog.info("paymentTypeCode[%s], position[%d]", summaryItem.paymentTypeCode, confirmOrderItemList.size() - 1);
                adapter.notifyItemChanged(confirmOrderItemList.size() - 1);

                updateFreightTotalAmount();
            }
        }
    }

    /**
     * 首次加载订单数据
     */
    private void loadOrderData() {
        SLog.info("xxyy");
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                loadingPopup.dismiss();
                String result = (String) message;
                if (!"success".equals(result)) {  // 如果沒有具體的錯誤消息，則顯示 生成訂單失敗
                    if (StringUtil.isEmpty(result)) {
                        ToastUtil.error(_mActivity, "生成訂單失敗");
                    } else { // 否則，顯示具體的錯誤消息
                        ToastUtil.error(_mActivity, result);
                    }

                    hideSoftInputPop();
                    return;
                }

                // 下面顯示訂單數據
                updateOrderView();

                if (soldOutGoodsItemList.size() > 0) {
                    showSoldOutPopup();
                }
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
                        return "用戶未登錄";
                    }
                    SLog.info("xxyy");
                    // 整個訂單的總件數
                    totalItemCount = 0;

                    // 第1步: 獲取配送時間列表 和 商店券列表
                    EasyJSONObject params = EasyJSONObject.generate(
                            "token", token,
                            "buyData", buyData,
                            "clientType", Constant.CLIENT_TYPE_ANDROID,
                            "isCart", isFromCart);
                    if (isGroup == Constant.TRUE_INT) {
                        params.set("isGroup", isGroup);
                    }
                    SLog.info("params[%s]", params.toString());
                    String responseStr = Api.syncPost(Api.PATH_DISPLAY_BILL_DATA, params);

                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        if (responseObj.exists("datas.error")) {
                            return responseObj.getSafeString("datas.error");
                        }
                        return null;
                    }
                    SLog.info("xxyy");
                    // 獲取配送時間列表
                    shippingItemList.clear();
//                    tariffEnable = responseObj.getInt("datas.tariffEnable");
//                    if (tariffEnable == Constant.TRUE_INT) {
//
//                    }
                    EasyJSONArray easyJSONArray = responseObj.getSafeArray("datas.shipTimeTypeList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        shippingItemList.add(new ListPopupItem(easyJSONObject.getInt("id"), easyJSONObject.getSafeString("name"), null));
                    }

                    confirmOrderItemList.clear();
                    traverseStore(responseObj);

                    // 添加上汇总项目
                    ConfirmOrderSummaryItem confirmOrderSummaryItem = new ConfirmOrderSummaryItem();
                    confirmOrderItemList.add(confirmOrderSummaryItem);
                    SLog.info("xxyy");
                    // 第2步：計算運費
                    // 收集地址信息
                    EasyJSONObject address = responseObj.getObject("datas.address");
                    if (address != null) { // 如果没有地址信息或【门店自提】方式，都不用做第2步
                        CalcFreightResult result = calcFreight(address);
                        if (!result.success) {
                            // 如果計算運費失敗，返回錯誤消息
                            if (!StringUtil.isEmpty(result.errorMessage)) {
                                return result.errorMessage;
                            } else { // 如果錯誤消息為空，顯示【計算運費失敗】
                                return "計算運費失敗";
                            }
                        }
                    }
                    SLog.info("xxyy");
                    // 第3步(請求參數與第2步相同) 計算最終結果
                    calcAmount();

                    // 請求平台券列表(請求參數與第2步相同)
                    getPlatformCoupon();
                    SLog.info("xxyy");
                    confirmOrderSummaryItem.platformCouponCount = platformCouponList.size();
                    confirmOrderSummaryItem.platformCouponStatus = Util.getAvailableCouponCountDesc(confirmOrderSummaryItem.platformCouponCount);
                    SLog.info("confirmOrderSummaryItem.platformCouponCount[%d]", confirmOrderSummaryItem.platformCouponCount);

                    return "success";
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

                return null;
            }
        };

        TwantApplication.getThreadPool().execute(taskObservable);
    }

    /**
     * 更新訂單的數據顯示
     */
    private void updateOrderView() {
        updateAddrView();

        ConfirmOrderSummaryItem summaryItem = getSummaryItem();
        String template = TwantApplication.getInstance().getString(R.string.text_confirm_order_total_item_count);
        if (tariffTotalEnable == Constant.TRUE_INT) {
            template = TwantApplication.getInstance().getString(R.string.text_confirm_order_total_with_tax_item_count);
        }
        tvItemCount.setText(String.format(template, totalItemCount));
        if (summaryItem != null) {
            double totalPrice = summaryItem.calcTotalPrice();
            tvTotalPrice.setText(StringUtil.formatPrice(_mActivity,totalPrice,0,2));
            Hawk.put(SPField.FIELD_TOTAL_ORDER_AMOUNT, totalPrice);
        }

        // 更新每家商店的優惠額
        updateStoreAmount();
        // 更新每家商店的運費
        updateStoreFreightAmount();

        adapter.setNewData(confirmOrderItemList);
    }

    /**
     * 【同步方式】計算運費
     * @param address 收貨地址， 如果為null，使用目前的地址
     * 返回一個Pair
     *          first -- true: 計算成功  false: 計算失敗
     *          second -- 失敗時的錯誤消息
     */
    private CalcFreightResult calcFreight(EasyJSONObject address) {
        if (address == null && mAddrItem == null) {
            return new CalcFreightResult(false, "收貨地址不能為空");
        }

        if (address != null) { // 使用傳入的新地址
            mAddrItem = new AddrItem(address);
        }
        SLog.info("mAddrItem[%s]", mAddrItem);


        try {
            EasyJSONObject params = collectParams(false);
            SLog.info("params[%s]", params.toString());
            String responseStr = Api.syncPost(Api.PATH_CALC_FREIGHT, params);
            // responseStr = "{\"code\":200,\"datas\":{\"isAuth\":0,\"address\":{\"addressId\":695,\"memberId\":247,\"realName\":\"周伟明\",\"areaId1\":19,\"areaId2\":292,\"areaId3\":3066,\"areaId4\":0,\"areaId\":3066,\"areaInfo\":\"广东 珠海市 香洲区\",\"address\":\"Test\",\"mobPhone\":\"13425038750\",\"mobileAreaCode\":\"0086\",\"telPhone\":\"\",\"isDefault\":0},\"freightAmount\":6.00,\"storeList\":[{\"buyGoodsItemVoList\":[{\"cartId\":3552,\"goodsId\":5197,\"commonId\":3728,\"goodsName\":\"Y&I's\",\"goodsFullSpecs\":\"顔色：白色\",\"goodsPrice\":11.90,\"imageName\":\"image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"buyNum\":1,\"itemAmount\":11.90,\"variableItemAmount\":0,\"goodsFreight\":0.00,\"goodsStorage\":8,\"categoryId\":276,\"goodsStatus\":1,\"storeId\":303,\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storageStatus\":1,\"freightTemplateId\":0,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"allowSend\":0,\"freightWeight\":1.00,\"freightVolume\":1.00,\"categoryId1\":256,\"categoryId2\":259,\"categoryId3\":276,\"isOwnShop\":0,\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":11.90,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":11.90,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":11.90,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionType\":0,\"promotionTypeText\":null,\"promotionTitle\":\"\",\"goodsPrice0\":11.90,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"basePrice\":11.90,\"savePrice\":0.00,\"payAmount\":0,\"book\":null,\"isGift\":0,\"giftVoList\":[],\"buyBundlingItemVoList\":null,\"bundlingId\":0,\"groupPrice\":null,\"goodsSerial\":\"111\",\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":1,\"chainId\":0,\"chainName\":null,\"virtualOverdueRefund\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"bargainOpenId\":0,\"couponAmount\":0,\"shopCommitmentAmount\":0,\"shopCommitmentRate\":0.0,\"downAmount\":0,\"finalAmount\":0,\"foreignTaxRate\":0.00,\"isForeign\":0,\"foreignTaxAmount\":0,\"reserveStorage\":1,\"promotionDiscountRate\":0.0,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":0,\"tariffRate\":0,\"tariffAmount\":0,\"groupId\":0},{\"cartId\":3553,\"goodsId\":7190,\"commonId\":3837,\"goodsName\":\"測試_編輯商品自動加空格\",\"goodsFullSpecs\":null,\"goodsPrice\":9.00,\"imageName\":\"image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"buyNum\":1,\"itemAmount\":9.00,\"variableItemAmount\":0,\"goodsFreight\":6.00,\"goodsStorage\":8,\"categoryId\":281,\"goodsStatus\":1,\"storeId\":303,\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storageStatus\":0,\"freightTemplateId\":0,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"allowSend\":1,\"freightWeight\":1.00,\"freightVolume\":1.00,\"categoryId1\":256,\"categoryId2\":259,\"categoryId3\":281,\"isOwnShop\":0,\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":9.00,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":9.00,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":9.00,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionType\":0,\"promotionTypeText\":null,\"promotionTitle\":\"\",\"goodsPrice0\":9.00,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"basePrice\":9.00,\"savePrice\":0.00,\"payAmount\":0,\"book\":null,\"isGift\":0,\"giftVoList\":[],\"buyBundlingItemVoList\":null,\"bundlingId\":0,\"groupPrice\":null,\"goodsSerial\":\"111\",\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"virtualOverdueRefund\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"bargainOpenId\":0,\"couponAmount\":0,\"shopCommitmentAmount\":0,\"shopCommitmentRate\":0.0,\"downAmount\":0,\"finalAmount\":0,\"foreignTaxRate\":0.00,\"isForeign\":0,\"foreignTaxAmount\":0,\"reserveStorage\":7,\"promotionDiscountRate\":0.0,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":0,\"tariffRate\":0,\"tariffAmount\":0,\"groupId\":0}],\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storeId\":303,\"paymentTypeCode\":\"online\",\"isOwnShop\":0,\"receiverMessage\":null,\"invoiceTitle\":null,\"invoiceContent\":null,\"invoiceCode\":null,\"shipTimeType\":0,\"buyItemAmount\":20.90,\"buyItemExcludejoinBigSaleAmount\":20.90,\"freightAmount\":6.00,\"conform\":null,\"voucher\":null,\"couponAmount\":0.00,\"shopCommitmentAmount\":0.00,\"buyAmount0\":20.90,\"buyAmount1\":26.90,\"buyAmount2\":6.00,\"buyAmount3\":20.90,\"buyAmount4\":20.90,\"buyAmount5\":20.90,\"buyAmount6\":0,\"ordersType\":0,\"taxAmount\":0.00,\"tariffTotalAmount\":0.00,\"storeDiscountAmount\":0}]}}";
            // responseStr = "{\"code\":200,\"datas\":{\"isAuth\":0,\"address\":{\"addressId\":695,\"memberId\":247,\"realName\":\"周伟明\",\"areaId1\":19,\"areaId2\":292,\"areaId3\":3066,\"areaId4\":0,\"areaId\":3066,\"areaInfo\":\"广东 珠海市 香洲区\",\"address\":\"Test\",\"mobPhone\":\"13425038750\",\"mobileAreaCode\":\"0086\",\"telPhone\":\"\",\"isDefault\":0},\"freightAmount\":6.00,\"storeList\":[{\"buyGoodsItemVoList\":[{\"cartId\":3552,\"goodsId\":5197,\"commonId\":3728,\"goodsName\":\"Y&I's\",\"goodsFullSpecs\":\"顔色：白色\",\"goodsPrice\":11.90,\"imageName\":\"image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"buyNum\":1,\"itemAmount\":11.90,\"variableItemAmount\":0,\"goodsFreight\":0.00,\"goodsStorage\":7,\"categoryId\":276,\"goodsStatus\":1,\"storeId\":303,\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storageStatus\":0,\"freightTemplateId\":0,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"allowSend\":1,\"freightWeight\":1.00,\"freightVolume\":1.00,\"categoryId1\":256,\"categoryId2\":259,\"categoryId3\":276,\"isOwnShop\":0,\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":11.90,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":11.90,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":11.90,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionType\":0,\"promotionTypeText\":null,\"promotionTitle\":\"\",\"goodsPrice0\":11.90,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"basePrice\":11.90,\"savePrice\":0.00,\"payAmount\":0,\"book\":null,\"isGift\":0,\"giftVoList\":[],\"buyBundlingItemVoList\":null,\"bundlingId\":0,\"groupPrice\":null,\"goodsSerial\":\"111\",\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":1,\"chainId\":0,\"chainName\":null,\"virtualOverdueRefund\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"bargainOpenId\":0,\"couponAmount\":0,\"shopCommitmentAmount\":0,\"shopCommitmentRate\":0.0,\"downAmount\":0,\"finalAmount\":0,\"foreignTaxRate\":0.00,\"isForeign\":0,\"foreignTaxAmount\":0,\"reserveStorage\":1,\"promotionDiscountRate\":0.0,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":0,\"tariffRate\":0,\"tariffAmount\":0,\"groupId\":0},{\"cartId\":3553,\"goodsId\":7190,\"commonId\":3837,\"goodsName\":\"測試_編輯商品自動加空格\",\"goodsFullSpecs\":null,\"goodsPrice\":9.00,\"imageName\":\"image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"buyNum\":1,\"itemAmount\":9.00,\"variableItemAmount\":0,\"goodsFreight\":6.00,\"goodsStorage\":8,\"categoryId\":281,\"goodsStatus\":1,\"storeId\":303,\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storageStatus\":1,\"freightTemplateId\":0,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"allowSend\":1,\"freightWeight\":1.00,\"freightVolume\":1.00,\"categoryId1\":256,\"categoryId2\":259,\"categoryId3\":281,\"isOwnShop\":0,\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":9.00,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":9.00,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":9.00,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionType\":0,\"promotionTypeText\":null,\"promotionTitle\":\"\",\"goodsPrice0\":9.00,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"basePrice\":9.00,\"savePrice\":0.00,\"payAmount\":0,\"book\":null,\"isGift\":0,\"giftVoList\":[],\"buyBundlingItemVoList\":null,\"bundlingId\":0,\"groupPrice\":null,\"goodsSerial\":\"111\",\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"virtualOverdueRefund\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"bargainOpenId\":0,\"couponAmount\":0,\"shopCommitmentAmount\":0,\"shopCommitmentRate\":0.0,\"downAmount\":0,\"finalAmount\":0,\"foreignTaxRate\":0.00,\"isForeign\":0,\"foreignTaxAmount\":0,\"reserveStorage\":7,\"promotionDiscountRate\":0.0,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":0,\"tariffRate\":0,\"tariffAmount\":0,\"groupId\":0}],\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storeId\":303,\"paymentTypeCode\":\"online\",\"isOwnShop\":0,\"receiverMessage\":null,\"invoiceTitle\":null,\"invoiceContent\":null,\"invoiceCode\":null,\"shipTimeType\":0,\"buyItemAmount\":20.90,\"buyItemExcludejoinBigSaleAmount\":20.90,\"freightAmount\":6.00,\"conform\":null,\"voucher\":null,\"couponAmount\":0.00,\"shopCommitmentAmount\":0.00,\"buyAmount0\":20.90,\"buyAmount1\":26.90,\"buyAmount2\":6.00,\"buyAmount3\":20.90,\"buyAmount4\":20.90,\"buyAmount5\":20.90,\"buyAmount6\":0,\"ordersType\":0,\"taxAmount\":0.00,\"tariffTotalAmount\":0.00,\"storeDiscountAmount\":0}]},\"mapDatas\":null}";
            SLog.info("responseStr[%s]", responseStr);
            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
            if (ToastUtil.isError(responseObj)) {
                String errMsg = "請求錯誤";
                if (responseObj.exists("datas.error")) {
                    errMsg = responseObj.getSafeString("datas.error");
                }
                return new CalcFreightResult(false, errMsg);
            }

            int totalGoodsCount = 0; // 商品总数
            List<SoldOutGoodsItem> soldOutGoodsItemList = new ArrayList<>(); // 售罄的商品列表
            // 獲取商店Id對應的運費數據
            EasyJSONArray storeList = responseObj.getSafeArray("datas.storeList");
            SLog.info("conformOrderItemList size 为【%d】",confirmOrderItemList.size());
            for (Object object : storeList) {
                EasyJSONObject store = (EasyJSONObject) object;

                int storeId = store.getInt("storeId");
                double freightAmount =  store.getDouble("freightAmount");
                EasyJSONArray buyGoodsItemVoList= store.getSafeArray("buyGoodsItemVoList");
                for (MultiItemEntity multiItemEntity : confirmOrderItemList) {
                    if (multiItemEntity.getItemType() == Constant.ITEM_VIEW_TYPE_COMMON) {
                        ConfirmOrderStoreItem storeItem = (ConfirmOrderStoreItem) multiItemEntity;
                        if (storeItem.storeId == storeId) {
                            SLog.info("进入店铺[%s]数据",storeItem.storeName);
                            for (Object object1 : buyGoodsItemVoList) {
                                totalGoodsCount++;
                                EasyJSONObject goodsItem = (EasyJSONObject) object1;

                                int goodsId = goodsItem.getInt("goodsId");
                                int storageStatus = goodsItem.getInt("storageStatus");
                                int allowSend = goodsItem.getInt("allowSend");
                                String goodsName = goodsItem.getSafeString("goodsName");
                                String goodsImage = goodsItem.getSafeString("imageName");
                                int buyNum = goodsItem.getInt("buyNum");

                                for (ConfirmOrderSkuItem skuItem : storeItem.confirmOrderSkuItemList) {
                                    SLog.info("skuItem Id[%s]数据，\ngoodsItem为[%s]",skuItem.toString(),goodsItem.toString());
                                    if (skuItem.goodsId == goodsId||skuItem.cartId == goodsItem.getInt("cartId")) {
                                        skuItem.storageStatus = storageStatus;
                                        skuItem.allowSend = allowSend;
                                        SLog.info("更新了[%s]数据，allowsend为【%d】",skuItem.goodsName,skuItem.allowSend);
                                    }
                                }

                                if (storageStatus == 0) { // 售罄
                                    SoldOutGoodsItem soldOutGoodsItem = new SoldOutGoodsItem(
                                            goodsId, goodsImage, goodsName, buyNum, SoldOutGoodsItem.REASON_SOLD_OUT, null);
                                    soldOutGoodsItemList.add(soldOutGoodsItem);
                                } else if (allowSend == 0) { // 该地区不支持配送
                                    SoldOutGoodsItem soldOutGoodsItem = new SoldOutGoodsItem(
                                            goodsId, goodsImage, goodsName, buyNum, SoldOutGoodsItem.REASON_NOT_AVAILABLE, null);
                                    soldOutGoodsItemList.add(soldOutGoodsItem);
                                }
                            }
                        }
                    }
                }

                freightAmountMap.put(storeId, freightAmount);
            }

            totalFreightAmount = (float) responseObj.getDouble("datas.freightAmount");
            this.soldOutGoodsItemList = soldOutGoodsItemList;
            this.totalGoodsCount = totalGoodsCount;
            return new CalcFreightResult(true, "");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            return new CalcFreightResult(false, e.getMessage());
        }
    }

    /**
     * 計算金額
     */
    private void calcAmount() {
        EasyJSONObject params = collectParams(true);
        if (params == null) {
            return;
        }
        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_CALC_TOTAL, params, new UICallback() {
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

                    // 獲取每家商店的優惠金額
                    EasyJSONArray storeList = responseObj.getSafeArray("datas.storeList");
                    for (Object object : storeList) {
                        EasyJSONObject store = (EasyJSONObject) object;
                        int storeId = store.getInt("storeId");
                        double storeDiscountAmount = store.getDouble("storeDiscountAmount");
                        double tariffTotalAmount = store.getDouble("tariffTotalAmount");
                        double buyAmount2 = store.getDouble("buyAmount2");
                        for (MultiItemEntity item : confirmOrderItemList){
                            if (item.getItemType() == Constant.ITEM_VIEW_TYPE_COMMON) {
                                ConfirmOrderStoreItem storeItem = (ConfirmOrderStoreItem) item;
                                if (storeItem.storeId == storeId) {
                                    if (payWay == Constant.PAY_WAY_ONLINE) {
                                        storeItem.taxAmount = tariffTotalAmount;
                                    } else { // 門店自提和貨到付款，稅費設置為0
                                        storeItem.taxAmount = 0;
                                    }

                                    SLog.info("object[%s]",storeItem.toString());

                                }
                            }

                        };

                        StoreAmount storeAmount = new StoreAmount(storeDiscountAmount, buyAmount2);
                        storeAmountMap.put(storeId, storeAmount);
                    }
                    updateStoreAmount();
                    adapter.setNewData(confirmOrderItemList);

                    ConfirmOrderSummaryItem summaryItem = getSummaryItem();
                    summaryItem.totalItemCount = totalItemCount;
                    summaryItem.totalAmount = responseObj.getDouble("datas.buyGoodsItemAmount");
                    summaryItem.storeDiscount = responseObj.getDouble("datas.storeTotalDiscountAmount");
                    summaryItem.platformDiscount = responseObj.getDouble("datas.platTotalDiscountAmount");
                    summaryItem.totalTaxAmount = responseObj.getDouble("datas.taxAmount");
                    summaryItem.totalFreight = totalFreightAmount;
                    SLog.info("summaryItem, summaryItem.totalFreight【%s】totalItemCount[%d], totalAmount[%s], storeDiscount[%s]",
                            summaryItem.totalFreight,summaryItem.totalItemCount, summaryItem.totalAmount, summaryItem.storeDiscount);

                    totalPrice = summaryItem.calcTotalPrice();
                    String template = TwantApplication.getInstance().getString(R.string.text_confirm_order_total_item_count);
                    if (tariffTotalEnable == Constant.TRUE_INT) {
                        template = TwantApplication.getInstance().getString(R.string.text_confirm_order_total_with_tax_item_count);
                    }
                    tvItemCount.setText(String.format(template, totalItemCount));
                    tvTotalPrice.setText(StringUtil.formatPrice(_mActivity, totalPrice, 0,2));
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void getPlatformCoupon() {
        EasyJSONObject params = collectParams(true);
        SLog.info("params[%s]", params.toString());

        String responseStr = Api.syncPost(Api.PATH_BUY_COUPON_LIST, params);
        SLog.info("__responseStr[%s]", responseStr);
        if (!EasyJSONBase.isJSONString(responseStr)) {
            return;
        }

        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
        if (ToastUtil.isError(responseObj)) {
            return;
        }

        try {
            platformCouponList.clear();
            EasyJSONArray couponList = responseObj.getSafeArray("datas.couponList");
            for (Object object : couponList) {
                EasyJSONObject coupon = (EasyJSONObject) object;

                boolean available = coupon.getBoolean("couponIsAble");
                if (!available) {
                    continue;
                }
                StoreVoucherVo storeVoucherVo = new StoreVoucherVo();

                storeVoucherVo.storeId = 0;
                storeVoucherVo.voucherId = coupon.getInt("coupon.couponId");
                storeVoucherVo.voucherTitle = coupon.getSafeString("coupon.useGoodsRangeExplain");
                storeVoucherVo.startTime = coupon.getSafeString("coupon.useStartTimeText");
                storeVoucherVo.endTime = coupon.getSafeString("coupon.useEndTimeText");
                storeVoucherVo.limitAmount = (float) coupon.getDouble("coupon.limitAmount");
                storeVoucherVo.limitText = coupon.getSafeString("coupon.limitText");
                storeVoucherVo.price = (float) coupon.getDouble("coupon.couponPrice");

                platformCouponList.add(storeVoucherVo);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.PAY_WAY) {
            selectedPayWayIndex = id;
            payWay = (int) extra;
            SLog.info("selectedPayWayIndex[%d], payWay[%d]", selectedPayWayIndex, payWay);
            ConfirmOrderSummaryItem summaryItem = getSummaryItem();
            if (summaryItem == null) {
                return;
            }

            summaryItem.paymentTypeCode = paymentTypeCodeMap.get(payWay);
            currPaymentTypeCode = summaryItem.paymentTypeCode;
            SLog.info("currPaymentTypeCode[%s]", currPaymentTypeCode);
            summaryItem.payWayIndex = payWay;
            SLog.info("paymentTypeCode[%s], position[%d]", summaryItem.paymentTypeCode, confirmOrderItemList.size() - 1);
            adapter.notifyItemChanged(confirmOrderItemList.size() - 1);
            totalPrice = summaryItem.calcTotalPrice();
            tvTotalPrice.setText(StringUtil.formatPrice(_mActivity, totalPrice, 0));
            if (payWay == Constant.PAY_WAY_FETCH) { // 門店自提
                showSelfFetchInfo();
            } else { // 在線支付 或 貨到付款
                llSelfFetchInfoContainer.setVisibility(View.GONE);
                updateAddrView();
            }
            adapter.setPayWayIndex(payWay);
            adapter.notifyDataSetChanged();
//            loadOrderData();
            if (payWay != Constant.PAY_WAY_FETCH) { // 【門店自提】不用計算運費
                updateFreightTotalAmount();
            } else {
                calcAmount();
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
        } else if (type == PopupType.SELECT_VOUCHER) { // 選擇店铺券
            VoucherUseStatus voucherUseStatus = (VoucherUseStatus) extra;
            SLog.info("voucherUseStatus: %s", voucherUseStatus);
            updateStoreVoucherStatus(voucherUseStatus);

            calcAmount();
        } else if (type == PopupType.SELECT_PLATFORM_COUPON) { // 選擇平台券
            SLog.info("platformCouponIndex[%d], id[%d]", platformCouponIndex, id);
            ConfirmOrderSummaryItem confirmOrderSummaryItem = getSummaryItem();
            if (platformCouponIndex == id) { // 再次點擊，表示取消選擇
                platformCouponIndex = -1;
                // 沒選中任何平台券，顯示平台券數量
                confirmOrderSummaryItem.platformCouponStatus = Util.getAvailableCouponCountDesc(confirmOrderSummaryItem.platformCouponCount);
            } else {
                // 顯示當前選中的平台券信息
                platformCouponIndex = id;
                StoreVoucherVo platformCoupon = platformCouponList.get(platformCouponIndex);
                String statusText = StringUtil.formatPrice(_mActivity, platformCoupon.price, 0) + platformCoupon.limitText;
                confirmOrderSummaryItem.platformCouponStatus = statusText;
            }

            SLog.info("platformCouponIndex[%d]", platformCouponIndex);
            calcAmount();
        } else if (type == PopupType.HANDLE_SOLD_OUT_GOODS) { // 處理售罄的商品
            if (soldOutGoodsItemList.size() == totalGoodsCount) { // 如果全部售罄，返回上一面
                SLog.info("all_sold_out");
                hideSoftInputPop();
            } else { // 如果部分售罄，刪除售罄的商品
                SLog.info("partial_sold_out");
                try {
                    // 過濾售罄的商品
                    EasyJSONArray newBuyDataArr = EasyJSONArray.generate();
                    EasyJSONArray buyDataArr = EasyJSONArray.parse(buyData);
                    SLog.info("soldOutGoodsItemList.size[%d]", soldOutGoodsItemList.size());
                    for (Object object : buyDataArr) {
                        EasyJSONObject buyDataItem = (EasyJSONObject) object;

                        // 遍歷缺貨列表，看該商品是否缺貨
                        boolean isSoldOut = false;
                        int goodsId = buyDataItem.getInt("goodsId");
                        for (SoldOutGoodsItem soldOutGoodsItem : soldOutGoodsItemList) {
                            SLog.info("goodsId[%d], soldOutGoodsItem.goodsId[%d]", goodsId, soldOutGoodsItem.goodsId);
                            int cartId;
                            if (isFromCart == Constant.TRUE_INT) {
                                Integer result = goodsIdToCartId.get(soldOutGoodsItem.goodsId);
                                if (result == null) {
                                    SLog.info("Error!根據goodsId找不到cartId");
                                    continue;
                                }
                                cartId = result;
                            } else {
                                cartId = soldOutGoodsItem.goodsId;
                            }
                            if (goodsId == cartId) {
                                isSoldOut = true;
                                break;
                            }
                        }

                        if (isSoldOut) {
                            continue;
                        }

                        newBuyDataArr.append(buyDataItem);
                    }

                    buyData = newBuyDataArr.toString();
                    SLog.info("NewBuyData[%s]", buyData);

                    loadOrderData();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        }
    }

    /**
     * 更新商店券狀態
     * @param voucherUseStatus
     */
    private void updateStoreVoucherStatus(VoucherUseStatus voucherUseStatus) {
        int storeId = voucherUseStatus.storeId;
        int position = 0;
        for (MultiItemEntity entity : confirmOrderItemList) {
            if (entity instanceof ConfirmOrderStoreItem) {
                ConfirmOrderStoreItem item = (ConfirmOrderStoreItem) entity;
                if (item.storeId == storeId) {
                    List<StoreVoucherVo> storeVoucherVoList = voucherMap.get(storeId);

                    for (StoreVoucherVo storeVoucherVo : storeVoucherVoList) {
                        if (storeVoucherVo.voucherId == voucherUseStatus.voucherId) {
                            storeVoucherVo.isInUse = voucherUseStatus.isInUse; // 更新券的狀態

                            if (voucherUseStatus.isInUse) { // 使用商店券
                                // 獲取要使用的商店券名稱
                                item.voucherName = formatVoucherName(storeVoucherVo);
                            }
                        }
                    }

                    if (voucherUseStatus.isInUse) { // 使用商店券，將當前使用使用的商店券Id賦值到商店數據中
                        item.voucherId = voucherUseStatus.voucherId;
                    } else {  // 不使用商店券，將voucherId賦值為0
                        item.voucherId = 0;
                    }

                    adapter.notifyItemChanged(position);
                    break;
                }
            }

            position++;
        }
    }

    private String formatVoucherName(StoreVoucherVo storeVoucherVo) {
        return StringUtil.formatPrice(_mActivity, storeVoucherVo.price, 0) + " " + storeVoucherVo.voucherTitle;
    }

    /**
     * 獲取匯總數據項
     * @return
     */
    private ConfirmOrderSummaryItem getSummaryItem() {
        int size = confirmOrderItemList.size();
        SLog.info("__size[%d]", size);
        if (size < 1) {
            return null;
        }
        SLog.info("__size[%d]", size);
        return (ConfirmOrderSummaryItem) confirmOrderItemList.get(size - 1);
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

