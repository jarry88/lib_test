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
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.ConfirmOrderStoreItem;
import com.ftofs.twant.entity.ConfirmOrderSummaryItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.entity.PayWayItem;
import com.ftofs.twant.entity.SoldOutGoodsItem;
import com.ftofs.twant.entity.StoreAmount;
import com.ftofs.twant.entity.StoreVoucherVo;
import com.ftofs.twant.entity.VoucherUseStatus;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.OrderVoucherPopup;
import com.ftofs.twant.widget.PayWayPopup;
import com.ftofs.twant.widget.RealNamePopup;
import com.ftofs.twant.widget.SoldOutPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class NewConfirmOrderFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    /**
     * 各個步驟的常量定義
     */
    public static final int STEP_DISPLAY = 1;  // 顯示產品信息
    public static final int STEP_CALC_FREIGHT = 2; // 計算運費
    public static final int STEP_CALC_TOTAL_AMOUNT = 3; // 計算總金額

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

    List<PayWayItem> payWayItemList = new ArrayList<>(); // 支付方式列表
    List<ListPopupItem> shippingItemList = new ArrayList<>(); // 物流方式列表

    // 當前支付方式
    int payWay = Constant.PAY_WAY_ONLINE;
    // 當前選中的支付方式索引
    int selectedPayWayIndex = 0;

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
    Map<Integer, String> paymentTypeCodeMap = new HashMap<>();

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

    PayWayItem specialItem;  // 只有選擇的收貨人信息是澳門地區才會顯示貨到付款這種交易方式，空地址、香港和內地的地址均不顯示；
    PayWayItem onlineItem;  // 只有選擇的收貨人信息是澳門地區才會顯示貨到付款這種交易方式，空地址、香港和內地的地址均不顯示；

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        // 初始化支付方式數據
        onlineItem = new PayWayItem(Constant.PAY_WAY_ONLINE, "物流配送", "在線付款後物流送貨", true, R.drawable.pay_way_online_selected, R.drawable.pay_way_online_unselected);
        payWayItemList.add(onlineItem);
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
                        summaryItem = getSummaryItem();
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
     * 顯示支付方式彈窗
     */
    private void payWayPopup() {
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
                .asCustom(new ListPopup(_mActivity, TwantApplication.getInstance().getString(R.string.mobile_zone_text),
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
     * 加載數據
     * @param startStep 從哪一個步驟開始
     */
    private void loadData(int startStep) {

    }


    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("PopupType[%s], id[%s], extra[%s]", type, id, extra);
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
            if (soldOutGoodsItemList.size() == totalGoodsCount) { // 如果全部售罄，返回上一面
                SLog.info("all_sold_out");
                hideSoftInputPop();
            } else { // 如果部分售罄，刪除售罄的商品
                if(currPaymentTypeCode.equals(Constant.PAYMENT_TYPE_CODE_CHAIN)){
                    SLog.info("后面要重构，参考ios 到店自提及想要食逻辑");
                    hideSoftInputPop();
                    return;
                }
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
//                            SLog.info("goodsId[%d], soldOutGoodsItem.goodsId[%d]", goodsId, soldOutGoodsItem.goodsId);
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

                    loadData(STEP_DISPLAY);
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
        if (size < 1) {
            return null;
        }
        try {
            return (ConfirmOrderSummaryItem) confirmOrderItemList.get(size - 1);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            return null;
        }

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


