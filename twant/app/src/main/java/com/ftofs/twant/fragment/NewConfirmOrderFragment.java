package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.ftofs.twant.adapter.ConfirmOrderStoreAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.CommonResult;
import com.ftofs.twant.entity.ConfirmOrderSkuItem;
import com.ftofs.twant.entity.ConfirmOrderStoreItem;
import com.ftofs.twant.entity.ConfirmOrderSummaryItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.ftofs.lib_net.model.MobileZone;
import com.ftofs.twant.entity.PayWayItem;
import com.ftofs.twant.entity.SoldOutGoodsItem;
import com.ftofs.twant.entity.StoreAmount;
import com.ftofs.twant.entity.StoreVoucherVo;
import com.ftofs.twant.entity.VoucherUseStatus;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.HwLoadingPopup;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.ftofs.twant.widget.OrderVoucherPopup;
import com.ftofs.twant.widget.PayWayPopup;
import com.ftofs.twant.widget.RealNamePopup;
import com.ftofs.twant.widget.SoldOutPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
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
 * 調用到的接口
 * A. /member/buy/step1 购买第一步:显示產品信息
 * B. /member/buy/calc/freight 购买第一步:計算運費
 * C. /member/buy/calc 計算總費
 * D. /member/buy/coupon/list 平台券列表
 * E. /member/buy/take/save 生成門店自提訂單
 * F. /member/buy/step2 生成普通訂單
 *
 *
 * 請求流程:
 * 【同步方式】執行接口A、B、C、D
 * 如果沒有地址或門店自提方式，不需要執行步驟B
 * 步驟D只需要首次進入頁面時請求一次
 *
 *
 */
public class NewConfirmOrderFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    /**
     * 各個步驟的常量定義
     */
    public static final int STEP_DISPLAY = 1;  // 顯示產品信息
    public static final int STEP_CALC_FREIGHT = 2; // 計算運費
    public static final int STEP_CALC_TOTAL_AMOUNT = 3; // 計算總金額


    /**
     * 參數類型，分三種類型類型：
     * 第1種是：接口A用到的，叫做DISPLAY
     * 第2種是：接口B、C、D、F用到的，叫做CALCULATE
     * 第3種是：接口E、F用到的，叫做COMMIT，與第2種類似，只不過是接口E在buyData結構裏多了提貨人的（mobile和realName字段）
     */
    public static final int PARAMS_TYPE_DISPLAY = 1;
    public static final int PARAMS_TYPE_CALCULATE = 2;
    public static final int PARAMS_TYPE_COMMIT = 3;

    HwLoadingPopup loadingPopup;

    String buyData;

    int isFromCart;
    int isGroup;
    int goId; // 開團Id
    int bargainOpenId; // 砍價Id

    boolean isFirstLoadData = true;  // 是否是第一次加載數據

    // 是否不需要再支付（比如，买1元的商品，用1元的代金抵扣）
    int isPayed = Constant.FALSE_INT;

    /*
        /buy/step2
            或
        /buy/take/save
        接口返回的payId
     */
    int finalPayId = 0;

    List<PayWayItem> payWayItemList = new ArrayList<>(); // 支付方式列表
    List<ListPopupItem> shippingItemList = new ArrayList<>(); // 物流方式列表

    AddrItem mAddrItem;  // 當前選中的地址

    TextView tvReceiverName; // 收貨人姓名
    TextView tvMobile; // 收貨人電話
    TextView tvAddress; // 收貨人地址
    TextView tvItemCount;  // 共xxx件
    TextView tvTotalPrice;  // 合計:多少錢
    double totalPrice; // 合計
    double totalFreightAmount; // 總運費

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

    // 整數類型的payWay與字符串類型的PaymentTypeCode之間的映射關係
    Map<Integer, String> paymentTypeCodeMap = new HashMap<>();
    // 字符串類型的PaymentTypeCode與整數類型的payWay之間的映射關係
    Map<String, Integer> reversePaymentTypeCodeMap = new HashMap<>();


    boolean isFirstShowSelfFetchInfo = true; // 是否首次顯示門店自提信息，如果是，則自動填充默認地址信息
    EditText etSelfFetchNickname; // 門店自提姓名
    EditText etSelfFetchMobile; // 門店自提手機號
    LinearLayout btnChangeSelfFetchMobileZone; // 切換門店自提區號
    TextView tvSelfFetchMobileZone; // 顯示門店自提區號的TextView


    List<MobileZone> mobileZoneList = new ArrayList<>();  // 區號列表
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
    private int tariffTotalEnable; // 是否需要加上跨城購稅費的總價

    PayWayItem onlineItem;    // 在線支付
    PayWayItem fetchItem;     // 到店自提，想要食業務，只能選擇到店自提
    PayWayItem specialItem;   // 只有選擇的收貨人信息是澳門地區才會顯示貨到付款這種交易方式，空地址、香港和內地的地址均不顯示；


    // 售罄商品列表
    List<SoldOutGoodsItem> soldOutGoodsItemList = new ArrayList<>();
    int totalGoodsCount; // 商品總數
    // Map<Integer, Integer> cartIdToGoodsId = new HashMap<>();
    Map<Integer, Integer> goodsIdToCartId = new HashMap<>(); // goodsId => cartId 之間的映射關係
    private boolean onlyFetch; // 想要食業務，只能使用門店自提方式

    /**
     * 創建確認訂單的實例
     * @param isFromCart 1 -- 來源于購物袋 0 -- 直接購買
     * @param buyData
     * @param isGroup 是否為團購  1 -- 是   0 -- 否
     * @return
     */
    public static NewConfirmOrderFragment newInstance(int isFromCart, String buyData, int isGroup, int goId, int bargainOpenId) {
        SLog.info("ConfirmOrderFragment.newInstance: isFromCart[%d], buyData[%s], isGroup[%d], goId[%d], bargainOpenId[%d]",
                isFromCart, buyData, isGroup, goId, bargainOpenId);

        Bundle args = new Bundle();

        NewConfirmOrderFragment fragment = new NewConfirmOrderFragment();
        fragment.setArguments(args);
        fragment.isFromCart = isFromCart;
        fragment.buyData = buyData;
        fragment.isGroup = isGroup;
        fragment.goId = goId;
        fragment.bargainOpenId = bargainOpenId;

        return fragment;
    }


    public static NewConfirmOrderFragment newInstance(int isFromCart, String buyData) {
        return newInstance(isFromCart, buyData, Constant.FALSE_INT, Constant.INVALID_GO_ID, Constant.INVALID_BARGAIN_OPEN_ID);
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_confirm_order, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currencyTypeSign = "$";
        textConfirmOrderTotalItemCount = getString(R.string.text_confirm_order_total_item_count);

        paymentTypeCodeMap.put(Constant.PAY_WAY_ONLINE, Constant.PAYMENT_TYPE_CODE_ONLINE);
        paymentTypeCodeMap.put(Constant.PAY_WAY_DELIVERY, Constant.PAYMENT_TYPE_CODE_OFFLINE);
        paymentTypeCodeMap.put(Constant.PAY_WAY_FETCH, Constant.PAYMENT_TYPE_CODE_CHAIN);
        reversePaymentTypeCodeMap.put(Constant.PAYMENT_TYPE_CODE_ONLINE, Constant.PAY_WAY_ONLINE);
        reversePaymentTypeCodeMap.put(Constant.PAYMENT_TYPE_CODE_OFFLINE, Constant.PAY_WAY_DELIVERY);
        reversePaymentTypeCodeMap.put(Constant.PAYMENT_TYPE_CODE_CHAIN, Constant.PAY_WAY_FETCH);

        // 初始化支付方式數據
        onlineItem = new PayWayItem(Constant.PAY_WAY_ONLINE, "物流配送", "在線付款後物流送貨", true, R.drawable.pay_way_online_selected, R.drawable.pay_way_online_unselected);
        fetchItem = new PayWayItem(Constant.PAY_WAY_FETCH, "到店自提", "在線付款後門店取貨", false, R.drawable.pay_way_fetch_selected, R.drawable.pay_way_fetch_unselected);
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
                        summaryItem = getSummaryItem();
                        startForResult(ReceiptInfoFragment.newInstance(position, summaryItem.receipt), RequestCode.EDIT_RECEIPT.ordinal());
                        break;
                    case R.id.btn_change_shipping_time:
                        shippingTimePopup(position);
                        break;
                    case R.id.btn_change_pay_way:
                        if (onlyFetch) {
                            // 如有只有【到店自提】，不讓選擇
                            break;
                        }
                        showPayWayPopup();
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
                                        Constant.COUPON_TYPE_STORE, storeVoucherVoList, -1, NewConfirmOrderFragment.this))
                                .show();
                        SLog.info("HERE");
                        break;
                    case R.id.btn_select_platform_coupon:
                        SLog.info("HERE");
                        new XPopup.Builder(_mActivity)
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(false)
                                .asCustom(new OrderVoucherPopup(_mActivity, 0, "",
                                        Constant.COUPON_TYPE_PLATFORM, platformCouponList, platformCouponIndex, NewConfirmOrderFragment.this))
                                .show();
                        break;
                    default:
                        break;
                }
            }
        });
        rvStoreList.setAdapter(adapter);

        loadData(STEP_DISPLAY);

        getMobileZoneList();
    }

    @Override
    public boolean onBackPressedSupport() {
        popWithOutRefresh();
        return true;
    }


    /**
     * 獲取可顯示的支付方式的列表
     * @return
     */
    private List<PayWayItem> getPayWayList() {
        List<PayWayItem> list = new ArrayList<>();

        SLog.info("onlyFetch[%s]", onlyFetch);
        if (onlyFetch) { // 想食業務，只支持門店自提，不支持切換收貨方式
            return list;
        }


        list.add(onlineItem);

        // 當用戶下單地址為中國內陸地址時，跨城購產品訂單物流配送方式隱藏【到店自提】方式，僅保留物流配送方式。
        int districtId1 = Constant.DISTRICT_ID_MACAO;
        if (mAddrItem != null && mAddrItem.areaIdList.size() > 0) {
            districtId1 = mAddrItem.areaIdList.get(0);
        }
        if (tariffTotalEnable == Constant.TRUE_INT && districtId1 != Constant.DISTRICT_ID_MACAO && districtId1 != Constant.DISTRICT_ID_HONGKONG) {
            return list;
        }


        list.add(fetchItem);

        // 只有選擇的收貨人信息是澳門地區才會顯示貨到付款這種交易方式，空地址、香港和內地的地址均不顯示
        boolean isMacao = false;
        if (mAddrItem != null && mAddrItem.areaIdList != null &&
            mAddrItem.areaIdList.size() > 0 && mAddrItem.areaIdList.get(0) == Constant.DISTRICT_ID_MACAO) {
            isMacao = true;
        }
        if (isMacao && isGroup == Constant.FALSE_INT) { // 在澳門收貨，並且不是團購，才顯示【貨到付款】
            list.add(specialItem);
        }

        return list;
    }


    /**
     * 顯示支付方式彈窗
     */
    private void showPayWayPopup() {
        if (onlyFetch) {
            return;
        }

        payWayItemList = getPayWayList();
        int selectedPayWayIndex = 0;

        // 當前選中的支付方式
        Integer payWay = reversePaymentTypeCodeMap.get(currPaymentTypeCode);
        if (payWay == null) {
            return;
        }

        for (int i = 0; i < payWayItemList.size(); i++) {
            PayWayItem payWayItem = payWayItemList.get(i);
            if (payWayItem.payWay == payWay) {
                selectedPayWayIndex = i;
                payWayItem.isSelected = true;
            } else {
                payWayItem.isSelected = false;
            }
        }

        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new PayWayPopup(_mActivity, payWayItemList, selectedPayWayIndex, this))
                .show();
    }


    /**
     * 顯示發貨時間彈窗，如工作日可送、休息日可送等
     * @param position
     */
    private void shippingTimePopup(int position) {
        ConfirmOrderSummaryItem summaryItem = (ConfirmOrderSummaryItem) confirmOrderItemList.get(position);
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new ListPopup(_mActivity, TwantApplication.Companion.get().getString(R.string.text_shipping_time),
                        PopupType.SHIPPING_TIME, shippingItemList, summaryItem.shipTimeType, this, position))
                .show();
    }


    /**
     * 收集表單參數
     * @param paramsType 參數類型
     * @return
     */
    private EasyJSONObject collectParams(int paramsType) {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return null;
            }

            // 收集表單信息
            EasyJSONObject commitBuyData = EasyJSONObject.generate(
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

            SLog.info("paramsType[%d]", paramsType);
            if (paramsType == PARAMS_TYPE_CALCULATE || paramsType == PARAMS_TYPE_COMMIT) {
                ConfirmOrderSummaryItem summaryItem = getSummaryItem();

                // 如果是用于提交訂單，需要從新收集最新的數據
                EasyJSONArray storeList = EasyJSONArray.generate();
                for (MultiItemEntity multiItemEntity : confirmOrderItemList) {
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
                            "storeName", storeItem.storeName,
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
                    if (isFromCart == Constant.FALSE_INT) {
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


                commitBuyData.set("paymentTypeCode", currPaymentTypeCode);
                commitBuyData.set("storeList", storeList);

                // 如果是門店自提的話，還要自提手機號和買家姓名
                if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(currPaymentTypeCode) &&
                        paramsType == PARAMS_TYPE_COMMIT // 只有在提交訂單時，才需要校驗自提人姓名和手機號
                ) {
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

            // 門店自提必須addrId設置為0，防止後臺誤判為跨城購
            if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(currPaymentTypeCode)) {
                commitBuyData.set("addressId", "0");
            } else if (!commitBuyData.exists("addressId")) { // 沒有addressId也默認設為0
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
            doCommit();
        } else if (id == R.id.btn_add_shipping_address) {
            startForResult(AddAddressFragment.newInstance(Constant.ACTION_ADD, null), RequestCode.ADD_ADDRESS.ordinal());
        } else if (id == R.id.btn_change_shipping_address) {
            startForResult(AddrManageFragment.newInstance(), RequestCode.CHANGE_ADDRESS.ordinal());
        } else if (id == R.id.btn_change_self_take_mobile_zone) {
            changeSelfTakeMobileZone();
        }
    }

    /**
     * 提交訂單
     */
    private void doCommit() {
        if (soldOutGoodsItemList.size() > 0) { // 如果有售罄商品，則顯示售罄商品的彈窗
            showSoldOutPopup();
            return;
        }

        ConfirmOrderSummaryItem summaryItem = getSummaryItem();
        if (summaryItem == null) {
            return;
        }

        // 檢測交易總金額是否超過$20,000
        double totalPrice = summaryItem.calcTotalPrice();
        SLog.info("summaryItem.totalPrice[%s]", totalPrice);
        if (summaryItem.totalAmount >= 20000) {
            new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                   .asCustom(new TwConfirmPopup(_mActivity, "每次交易總金額不得超過$20,000，請調整購物數量再提交",null, new OnConfirmCallback() {
                @Override
                public void onYes() {
                    SLog.info("onYes");
                }

                @Override
                public void onNo() {
                    SLog.info("onNo");
                }
            })).show();
            return;
        }

        hideSoftInput();
        try {
            EasyJSONObject params = collectParams(PARAMS_TYPE_COMMIT);
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

            showLoadingPopup("正在提交訂單，請稍候...");
            Api.postUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    dismissLoadingPopup();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    dismissLoadingPopup();
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
    }

    /**
     * 切換門店自提手機號
     */
    private void changeSelfTakeMobileZone() {
        List<ListPopupItem> itemList = new ArrayList<>();
        for (MobileZone mobileZone : mobileZoneList) {
            ListPopupItem item = new ListPopupItem(mobileZone.areaId, mobileZone.areaName, null);
            itemList.add(item);
        }

        hideSoftInput();
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new ListPopup(_mActivity, TwantApplication.Companion.get().getString(R.string.mobile_zone_text),
                        PopupType.MOBILE_ZONE, itemList, selectedMobileZoneIndex, this))
                .show();
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
            SLog.info("from[%s]", from);
            // 從地址管理Fragment返回 或 從地址添加Fragment返回
            boolean isNoAddress = data.getBoolean("isNoAddress", false); // 標記是否刪除了所有地址
            if (isNoAddress) {
                mAddrItem = null;
                updateAddrView();
                loadData(STEP_CALC_FREIGHT);
                return;
            }

            // 上一級Fragment返回的地址項
            AddrItem addrItem = data.getParcelable("addrItem");
            SLog.info("addrItem[%s]", addrItem);
            if (addrItem == null) {
                if (mAddrItem != null) {
                    // 重新獲取地址信息（在這種場合下：用戶點擊了收貨地址，編輯了收貨人姓名等信息，但沒重新選擇，就需要重新刷新一下地址信息顯示）
                    // TODO 2020-09-03
                }
                return;
            }
            SLog.info("addrItem: %s", addrItem);
            mAddrItem = addrItem;
            updateAddrView();
            loadData(STEP_CALC_FREIGHT);
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
     * 顯示售罄商品的優惠券
     */
    private void showSoldOutPopup() {
        new XPopup.Builder(_mActivity)
                .dismissOnBackPressed(false) // 按返回键是否关闭弹窗，默认为true
                .dismissOnTouchOutside(false) // 点击外部是否关闭弹窗，默认为true
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
        if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(currPaymentTypeCode)) { // 門店自提
            btnChangeShippingAddress.setVisibility(View.GONE);
            btnChangeShippingAddress.setVisibility(View.GONE);
            llSelfFetchInfoContainer.setVisibility(View.VISIBLE);
        } else if (mAddrItem == null) {
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
//                etSelfFetchNickname.addTextChangedListener();

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
        for (int i = 0; i < confirmOrderItemList.size(); i++) {
            MultiItemEntity entity = confirmOrderItemList.get(i);
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
     * 加載數據
     * @param startStep 從哪一個步驟開始
     */
    private void loadData(int startStep) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        SLog.info("loadData, startStep[%d]", startStep);

        showLoadingPopup("正在加載，請稍候...");

        Observable<CommonResult> observable = Observable.create(new ObservableOnSubscribe<CommonResult>() {
            @Override
            public void subscribe(ObservableEmitter<CommonResult> emitter) throws Exception {
                SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                if (startStep == STEP_DISPLAY) {
                    CommonResult result = stepDisplayData();
                    if (result.code != CommonResult.CODE_SUCCESS) {
                        emitter.onError(result);
                    }
                }

                // 如果是沒地址或門店自提時，則不需要計算運費
                boolean needCalcFreight = (mAddrItem != null && !Constant.PAYMENT_TYPE_CODE_CHAIN.equals(currPaymentTypeCode));
                SLog.info("needCalcFreight[%s]", needCalcFreight);
                if (needCalcFreight && startStep <= STEP_CALC_FREIGHT) {
                    stepCalcFreight(null);
                }

                if (startStep <= STEP_CALC_TOTAL_AMOUNT) {
                    stepCalcTotalAmount();
                }

                if (isFirstLoadData) {
                    isFirstLoadData = false;

                    getPlatformCoupon(); // 首次進入頁面才加載平臺券

                    ConfirmOrderSummaryItem confirmOrderSummaryItem = getSummaryItem();
                    if (confirmOrderSummaryItem != null) {
                        confirmOrderSummaryItem.platformCouponCount = platformCouponList.size();
                        SLog.info("confirmOrderSummaryItem.platformCouponCount[%d]", confirmOrderSummaryItem.platformCouponCount);
                        confirmOrderSummaryItem.platformCouponStatus = Util.getAvailableCouponCountDesc(confirmOrderSummaryItem.platformCouponCount);
                    }
                }

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<CommonResult> observer = new Observer<CommonResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                SLog.info("onSubscribe, threadId[%s]", Thread.currentThread().getId());
            }
            @Override
            public void onNext(CommonResult s) {
                SLog.info("onNext[%s], threadId[%s]", s, Thread.currentThread().getId());
            }
            @Override
            public void onError(Throwable e) {
                SLog.info("onError[%s], threadId[%s], stackTrace[%s]", e.getMessage(), Thread.currentThread().getId(), Util.getStackTraceString(e));
                dismissLoadingPopup();
                CommonResult result = (CommonResult) e;
                ToastUtil.error(_mActivity, result.message);
                hideSoftInputPop();
            }
            @Override
            public void onComplete() {
                SLog.info("onComplete, threadId[%s], soldOutGoodsItemList.size[%d]", Thread.currentThread().getId(), soldOutGoodsItemList.size());
                dismissLoadingPopup();

                if (Constant.PAYMENT_TYPE_CODE_CHAIN.equals(currPaymentTypeCode) || onlyFetch) { // 門店自提
                    showSelfFetchInfo();
                }

                if (startStep == STEP_DISPLAY) { // 如果是從第1步開始加載，更新收貨地址的顯示
                    updateAddrView();
                }

                if (startStep <= STEP_CALC_FREIGHT) {
                    updateStoreFreightAmount();
                }

                if (startStep <= STEP_CALC_TOTAL_AMOUNT) {
                    updateStoreAmount();
                }

                if (soldOutGoodsItemList.size() > 0) {
                    showSoldOutPopup();
                }
                TwantApplication twantApplication = TwantApplication.Companion.get();
                if (twantApplication == null) {
                    ToastUtil.error(_mActivity,"空的");
                }
                String template = TwantApplication.Companion.get().getString(R.string.text_confirm_order_total_item_count);
                if (tariffTotalEnable == Constant.TRUE_INT) {
                    template =TwantApplication.Companion.get().getString(R.string.text_confirm_order_total_with_tax_item_count);
                }

                adapter.setNewData(confirmOrderItemList);

                tvItemCount.setText(String.format(template, totalItemCount));
                tvTotalPrice.setText(StringUtil.formatPrice(_mActivity, totalPrice, 0,2));
            }
        };

        observable.subscribe(observer);
    }

    /**
     * Step 1.查詢顯示數據
     */
    private CommonResult stepDisplayData() {
        SLog.info("XXYY");
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return new CommonResult(CommonResult.CODE_SUCCESS);
        }

        try {
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

            if (Config.USE_DEVELOPER_TEST_DATA) {
                // responseStr = "{\"code\":400,\"datas\":{\"error\":\"xxyy\"}}";
            }

            SLog.info("responseStr[%s]", responseStr);
            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
            if (ToastUtil.isError(responseObj)) {
                String errMsg = "";
                if (responseObj != null) {
                    errMsg = responseObj.optString("datas.error");
                }
                return new CommonResult(CommonResult.CODE_API_FAILED, errMsg);
            }

            // 獲取收貨地址
            if (responseObj.exists("datas.address")) {
                EasyJSONObject address = responseObj.getSafeObject("datas.address");
                if (!Util.isJsonObjectEmpty(address)) {
                    mAddrItem = new AddrItem(address);
                }
            }

            SLog.info("xxyy");
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
            if (onlyFetch) {
                currPaymentTypeCode = Constant.PAYMENT_TYPE_CODE_CHAIN;
            }
            confirmOrderSummaryItem.paymentTypeCode = currPaymentTypeCode;
            SLog.info("__TEST_onlyFetch[%s], currPaymentTypeCode[%s]", onlyFetch, currPaymentTypeCode);
            confirmOrderItemList.add(confirmOrderSummaryItem);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            return new CommonResult(CommonResult.CODE_OTHER_ERROR, e.getMessage());
        }

        return CommonResult.success();
    }

    /**
     * Step 2.計算運費
     * @param address 收貨地址， 如果為null，使用目前的地址
     */
    private void stepCalcFreight(EasyJSONObject address) {
        SLog.info("___calcFreight");
        if (address == null && mAddrItem == null) {  // 收貨地址為空
            return;
        }

        if (address != null) { // 使用傳入的新地址
            mAddrItem = new AddrItem(address);
        }
        SLog.info("mAddrItem[%s]", mAddrItem);


        try {
            EasyJSONObject params = collectParams(PARAMS_TYPE_CALCULATE);
            SLog.info("params[%s]", params.toString());
            String responseStr = Api.syncPost(Api.PATH_CALC_FREIGHT, params);
            // responseStr = "{\"code\":200,\"datas\":{\"isAuth\":0,\"address\":{\"addressId\":695,\"memberId\":247,\"realName\":\"周伟明\",\"areaId1\":19,\"areaId2\":292,\"areaId3\":3066,\"areaId4\":0,\"areaId\":3066,\"areaInfo\":\"广东 珠海市 香洲区\",\"address\":\"Test\",\"mobPhone\":\"13425038750\",\"mobileAreaCode\":\"0086\",\"telPhone\":\"\",\"isDefault\":0},\"freightAmount\":6.00,\"storeList\":[{\"buyGoodsItemVoList\":[{\"cartId\":3552,\"goodsId\":5197,\"commonId\":3728,\"goodsName\":\"Y&I's\",\"goodsFullSpecs\":\"顔色：白色\",\"goodsPrice\":11.90,\"imageName\":\"image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"buyNum\":1,\"itemAmount\":11.90,\"variableItemAmount\":0,\"goodsFreight\":0.00,\"goodsStorage\":8,\"categoryId\":276,\"goodsStatus\":1,\"storeId\":303,\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storageStatus\":1,\"freightTemplateId\":0,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"allowSend\":0,\"freightWeight\":1.00,\"freightVolume\":1.00,\"categoryId1\":256,\"categoryId2\":259,\"categoryId3\":276,\"isOwnShop\":0,\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":11.90,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":11.90,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":11.90,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionType\":0,\"promotionTypeText\":null,\"promotionTitle\":\"\",\"goodsPrice0\":11.90,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"basePrice\":11.90,\"savePrice\":0.00,\"payAmount\":0,\"book\":null,\"isGift\":0,\"giftVoList\":[],\"buyBundlingItemVoList\":null,\"bundlingId\":0,\"groupPrice\":null,\"goodsSerial\":\"111\",\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":1,\"chainId\":0,\"chainName\":null,\"virtualOverdueRefund\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"bargainOpenId\":0,\"couponAmount\":0,\"shopCommitmentAmount\":0,\"shopCommitmentRate\":0.0,\"downAmount\":0,\"finalAmount\":0,\"foreignTaxRate\":0.00,\"isForeign\":0,\"foreignTaxAmount\":0,\"reserveStorage\":1,\"promotionDiscountRate\":0.0,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":0,\"tariffRate\":0,\"tariffAmount\":0,\"groupId\":0},{\"cartId\":3553,\"goodsId\":7190,\"commonId\":3837,\"goodsName\":\"測試_編輯商品自動加空格\",\"goodsFullSpecs\":null,\"goodsPrice\":9.00,\"imageName\":\"image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"buyNum\":1,\"itemAmount\":9.00,\"variableItemAmount\":0,\"goodsFreight\":6.00,\"goodsStorage\":8,\"categoryId\":281,\"goodsStatus\":1,\"storeId\":303,\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storageStatus\":0,\"freightTemplateId\":0,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"allowSend\":1,\"freightWeight\":1.00,\"freightVolume\":1.00,\"categoryId1\":256,\"categoryId2\":259,\"categoryId3\":281,\"isOwnShop\":0,\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":9.00,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":9.00,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":9.00,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionType\":0,\"promotionTypeText\":null,\"promotionTitle\":\"\",\"goodsPrice0\":9.00,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"basePrice\":9.00,\"savePrice\":0.00,\"payAmount\":0,\"book\":null,\"isGift\":0,\"giftVoList\":[],\"buyBundlingItemVoList\":null,\"bundlingId\":0,\"groupPrice\":null,\"goodsSerial\":\"111\",\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"virtualOverdueRefund\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"bargainOpenId\":0,\"couponAmount\":0,\"shopCommitmentAmount\":0,\"shopCommitmentRate\":0.0,\"downAmount\":0,\"finalAmount\":0,\"foreignTaxRate\":0.00,\"isForeign\":0,\"foreignTaxAmount\":0,\"reserveStorage\":7,\"promotionDiscountRate\":0.0,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":0,\"tariffRate\":0,\"tariffAmount\":0,\"groupId\":0}],\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storeId\":303,\"paymentTypeCode\":\"online\",\"isOwnShop\":0,\"receiverMessage\":null,\"invoiceTitle\":null,\"invoiceContent\":null,\"invoiceCode\":null,\"shipTimeType\":0,\"buyItemAmount\":20.90,\"buyItemExcludejoinBigSaleAmount\":20.90,\"freightAmount\":6.00,\"conform\":null,\"voucher\":null,\"couponAmount\":0.00,\"shopCommitmentAmount\":0.00,\"buyAmount0\":20.90,\"buyAmount1\":26.90,\"buyAmount2\":6.00,\"buyAmount3\":20.90,\"buyAmount4\":20.90,\"buyAmount5\":20.90,\"buyAmount6\":0,\"ordersType\":0,\"taxAmount\":0.00,\"tariffTotalAmount\":0.00,\"storeDiscountAmount\":0}]}}";
            // responseStr = "{\"code\":200,\"datas\":{\"isAuth\":0,\"address\":{\"addressId\":695,\"memberId\":247,\"realName\":\"周伟明\",\"areaId1\":19,\"areaId2\":292,\"areaId3\":3066,\"areaId4\":0,\"areaId\":3066,\"areaInfo\":\"广东 珠海市 香洲区\",\"address\":\"Test\",\"mobPhone\":\"13425038750\",\"mobileAreaCode\":\"0086\",\"telPhone\":\"\",\"isDefault\":0},\"freightAmount\":6.00,\"storeList\":[{\"buyGoodsItemVoList\":[{\"cartId\":3552,\"goodsId\":5197,\"commonId\":3728,\"goodsName\":\"Y&I's\",\"goodsFullSpecs\":\"顔色：白色\",\"goodsPrice\":11.90,\"imageName\":\"image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"buyNum\":1,\"itemAmount\":11.90,\"variableItemAmount\":0,\"goodsFreight\":0.00,\"goodsStorage\":7,\"categoryId\":276,\"goodsStatus\":1,\"storeId\":303,\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storageStatus\":0,\"freightTemplateId\":0,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"allowSend\":1,\"freightWeight\":1.00,\"freightVolume\":1.00,\"categoryId1\":256,\"categoryId2\":259,\"categoryId3\":276,\"isOwnShop\":0,\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":11.90,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":11.90,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":11.90,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionType\":0,\"promotionTypeText\":null,\"promotionTitle\":\"\",\"goodsPrice0\":11.90,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"basePrice\":11.90,\"savePrice\":0.00,\"payAmount\":0,\"book\":null,\"isGift\":0,\"giftVoList\":[],\"buyBundlingItemVoList\":null,\"bundlingId\":0,\"groupPrice\":null,\"goodsSerial\":\"111\",\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":1,\"chainId\":0,\"chainName\":null,\"virtualOverdueRefund\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"bargainOpenId\":0,\"couponAmount\":0,\"shopCommitmentAmount\":0,\"shopCommitmentRate\":0.0,\"downAmount\":0,\"finalAmount\":0,\"foreignTaxRate\":0.00,\"isForeign\":0,\"foreignTaxAmount\":0,\"reserveStorage\":1,\"promotionDiscountRate\":0.0,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":0,\"tariffRate\":0,\"tariffAmount\":0,\"groupId\":0},{\"cartId\":3553,\"goodsId\":7190,\"commonId\":3837,\"goodsName\":\"測試_編輯商品自動加空格\",\"goodsFullSpecs\":null,\"goodsPrice\":9.00,\"imageName\":\"image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"buyNum\":1,\"itemAmount\":9.00,\"variableItemAmount\":0,\"goodsFreight\":6.00,\"goodsStorage\":8,\"categoryId\":281,\"goodsStatus\":1,\"storeId\":303,\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storageStatus\":1,\"freightTemplateId\":0,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"allowSend\":1,\"freightWeight\":1.00,\"freightVolume\":1.00,\"categoryId1\":256,\"categoryId2\":259,\"categoryId3\":281,\"isOwnShop\":0,\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":9.00,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":9.00,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":9.00,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/67/4b/674bf566b1ac28f32475ab0b866f5822.png\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionType\":0,\"promotionTypeText\":null,\"promotionTitle\":\"\",\"goodsPrice0\":9.00,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"basePrice\":9.00,\"savePrice\":0.00,\"payAmount\":0,\"book\":null,\"isGift\":0,\"giftVoList\":[],\"buyBundlingItemVoList\":null,\"bundlingId\":0,\"groupPrice\":null,\"goodsSerial\":\"111\",\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"virtualOverdueRefund\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"bargainOpenId\":0,\"couponAmount\":0,\"shopCommitmentAmount\":0,\"shopCommitmentRate\":0.0,\"downAmount\":0,\"finalAmount\":0,\"foreignTaxRate\":0.00,\"isForeign\":0,\"foreignTaxAmount\":0,\"reserveStorage\":7,\"promotionDiscountRate\":0.0,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":0,\"tariffRate\":0,\"tariffAmount\":0,\"groupId\":0}],\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"storeId\":303,\"paymentTypeCode\":\"online\",\"isOwnShop\":0,\"receiverMessage\":null,\"invoiceTitle\":null,\"invoiceContent\":null,\"invoiceCode\":null,\"shipTimeType\":0,\"buyItemAmount\":20.90,\"buyItemExcludejoinBigSaleAmount\":20.90,\"freightAmount\":6.00,\"conform\":null,\"voucher\":null,\"couponAmount\":0.00,\"shopCommitmentAmount\":0.00,\"buyAmount0\":20.90,\"buyAmount1\":26.90,\"buyAmount2\":6.00,\"buyAmount3\":20.90,\"buyAmount4\":20.90,\"buyAmount5\":20.90,\"buyAmount6\":0,\"ordersType\":0,\"taxAmount\":0.00,\"tariffTotalAmount\":0.00,\"storeDiscountAmount\":0}]},\"mapDatas\":null}";
            SLog.info("responseStr[%s]", responseStr);
            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
            if (ToastUtil.isError(responseObj)) {
                return;
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
                for (int i = 0; i < confirmOrderItemList.size(); i++) {
                    MultiItemEntity multiItemEntity = confirmOrderItemList.get(i);
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

            totalFreightAmount = responseObj.getDouble("datas.freightAmount");
            this.soldOutGoodsItemList = soldOutGoodsItemList;
            this.totalGoodsCount = totalGoodsCount;
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    /**
     * Step 3.計算總價
     */
    private void stepCalcTotalAmount() {
        EasyJSONObject params = collectParams(PARAMS_TYPE_CALCULATE);
        if (params == null) {
            return;
        }
        SLog.info("params[%s]", params.toString());

        try {
            String responseStr = Api.syncPost(Api.PATH_CALC_TOTAL, params);
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
                            if (currPaymentTypeCode.equals(Constant.PAYMENT_TYPE_CODE_ONLINE)) {
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
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    /**
     * 【同步】獲取平臺券數據
     */
    private void getPlatformCoupon() {
        EasyJSONObject params = collectParams(PARAMS_TYPE_CALCULATE);
        SLog.info("params[%s]", params.toString());

        String responseStr = Api.syncPost(Api.PATH_BUY_COUPON_LIST, params);
        SLog.info("responseStr[%s]", responseStr);
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
                storeVoucherVo.limitAmount = coupon.getDouble("coupon.limitAmount");
                storeVoucherVo.limitText = coupon.getSafeString("coupon.limitText");
                storeVoucherVo.price = coupon.getDouble("coupon.couponPrice");

                platformCouponList.add(storeVoucherVo);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    /**
     * 遍歷並添加每家店鋪到列表中去
     * @param responseObj
     */
    private void traverseStore(EasyJSONObject responseObj) {
        try {
            // 獲取商店券
            EasyJSONArray buyStoreVoList = responseObj.getSafeArray("datas.buyStoreVoList");
            SLog.info("storeSize[%d]", buyStoreVoList.length());
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
                    double goodsPrice = buyGoodsItem.getDouble("goodsPrice");
                    int goodsModel = buyGoodsItem.optInt("goodsModal");


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
                    confirmOrderSkuItem.goodsModel = goodsModel;
                    confirmOrderSkuItem.tariffEnable = tariffEnable;
                    confirmOrderSkuItemList.add(confirmOrderSkuItem);

                    String keyName = "cartId";
                    if (isFromCart == Constant.FALSE_INT) {
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
                //判斷店鋪id為  使用核銷功能  ，只能允許自提取貨（由前端寫死）,
                onlyFetch = Util.showWriteOffsReceiveButton(storeId);

                commitStoreList.append(EasyJSONObject.generate(
                        "storeId", storeId,
                        "storeName", storeName,
                        "goodsList", goodsList,
                        "shipTimeType", shipTimeType,
                        // 沒有活動是傳空字符串
                        "conformId", conformId >=0 ? conformId : ""));
            }  // END OF 遍歷每家商店
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("PopupType[%s], id[%s], extra[%s]", type, id, extra);
        if (type == PopupType.PAY_WAY) {
            int payWay = (int) extra;
            SLog.info("payWay[%d]", payWay);
            ConfirmOrderSummaryItem summaryItem = getSummaryItem();
            if (summaryItem == null) {
                return;
            }

            currPaymentTypeCode = paymentTypeCodeMap.get(payWay);
            summaryItem.paymentTypeCode = currPaymentTypeCode;
            SLog.info("currPaymentTypeCode[%s], position[%d]", currPaymentTypeCode, confirmOrderItemList.size() - 1);
            adapter.notifyItemChanged(confirmOrderItemList.size() - 1);
            totalPrice = summaryItem.calcTotalPrice();
            tvTotalPrice.setText(StringUtil.formatPrice(_mActivity, totalPrice, 0));
            if (payWay == Constant.PAY_WAY_FETCH || onlyFetch) { // 門店自提
                showSelfFetchInfo();
            } else { // 在線支付 或 貨到付款
                llSelfFetchInfoContainer.setVisibility(View.GONE);
                updateAddrView();
            }
            adapter.setPayWay(payWay);
            adapter.notifyDataSetChanged();

            if (payWay != Constant.PAY_WAY_FETCH) {
                loadData(STEP_CALC_FREIGHT);
            } else { // 【門店自提】不用計算運費
                loadData(STEP_CALC_TOTAL_AMOUNT);
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

            loadData(STEP_CALC_TOTAL_AMOUNT);
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
            loadData(STEP_CALC_TOTAL_AMOUNT);
        } else if (type == PopupType.HANDLE_SOLD_OUT_GOODS) { // 處理售罄的商品
            if (id == 1) { // 重新選擇地址
                startForResult(AddrManageFragment.newInstance(), RequestCode.CHANGE_ADDRESS.ordinal());
            } else if (id == 2) { // 返回
                hideSoftInputPop();
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
//        SLog.bt();
        int size = confirmOrderItemList.size();
        if (size < 1) {
            return null;
        }
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

    private void showLoadingPopup(String info) {
        if (loadingPopup == null) {
            loadingPopup = (HwLoadingPopup) new XPopup.Builder(_mActivity)
                    .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
                    .dismissOnTouchOutside(false) // 点击外部是否关闭弹窗，默认为true
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new HwLoadingPopup(_mActivity, info));
        }

        loadingPopup.setInfo(info);
        loadingPopup.show();
    }

    private void dismissLoadingPopup() {
        if (loadingPopup != null) {
            loadingPopup.dismiss();
        }
    }
}

