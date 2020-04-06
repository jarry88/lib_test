package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ConfirmOrderGiftListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.cart.BaseStatus;
import com.ftofs.twant.entity.cart.SpuStatus;
import com.ftofs.twant.entity.cart.StoreStatus;
import com.ftofs.twant.entity.cart.TotalStatus;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AdjustButton;
import com.ftofs.twant.widget.CartAdjustButton;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 購物袋
 * @author zwm
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {
    TextView tvFragmentTitle;
    LinearLayout cartStoreItemContainer;
    String textSettlement;
    TotalStatus totalStatus = new TotalStatus();

    LinearLayout llTotalOperationContainer;

    TextView btnDelete;
    LinearLayout llViewModeButtonGroup;
    TextView btnEdit;
    TextView btnSettlement;
    TextView tvTotalPrice;

    ScaledButton btnBack;

    int totalCartItemCount; // 購物袋中的項數，用于顯示在主頁的底部工具欄中

    int mode = Constant.MODE_VIEW;
    boolean needReloadData = true;

    /**
     * 是否獨立的Fragment，還是依附于MainFragment
     */
    boolean isStandalone;

    ScrollView svItemContainer;
    int svItemContainerHeight = -1;

    public static CartFragment newInstance(boolean isStandalone) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        isStandalone = args.getBoolean("isStandalone");

        textSettlement = getResources().getString(R.string.text_settlement);

        ScaledButton btnSelectAll = view.findViewById(R.id.btn_select_all);
        btnSelectAll.setTag(totalStatus);
        setCheckButtonOnClickListener(btnSelectAll);
        totalStatus.setRadio(btnSelectAll);

        btnEdit = view.findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);
        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        cartStoreItemContainer = view.findViewById(R.id.ll_cart_store_item_container);

        btnDelete = view.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        llViewModeButtonGroup = view.findViewById(R.id.ll_view_mode_button_group);

        btnSettlement = view.findViewById(R.id.btn_settlement);
        btnSettlement.setOnClickListener(this);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);

        svItemContainer = view.findViewById(R.id.sv_item_container);

        llTotalOperationContainer = view.findViewById(R.id.ll_total_operation_container);

        btnBack = view.findViewById(R.id.btn_back);
        if (isStandalone) {
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(this);

            // 調整llTotalOperationContainer的高度為原來一半
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) llTotalOperationContainer.getLayoutParams();
            SLog.info("layoutParams: %s", layoutParams);
            layoutParams.height = Util.dip2px(_mActivity, 49);
            llTotalOperationContainer.setLayoutParams(layoutParams);
        }
    }




    private void displayCartItemCount(int totalUnreadCount) {
        MainFragment mainFragment = MainFragment.getInstance();
        if (mainFragment != null) {
            mainFragment.setCartItemCount(totalCartItemCount);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");

        if (svItemContainerHeight == -1) {
            svItemContainerHeight = svItemContainer.getHeight();
        }

        int userId = User.getUserId();
        if (userId < 1) { // 用戶未登錄，顯示登錄頁面
            Util.showLoginFragment();
            return;
        }

        if (needReloadData) {
            reloadList();
            totalStatus.changeCheckStatus(false, BaseStatus.PHRASE_TARGET);
        }
    }

    /**
     * 重新加載購物袋
     */
    private void reloadList() {
        totalStatus.storeStatusList.clear();
        loadCartData();
        updateTotalData();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();


    }

    private void loadCartData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "clientType", Constant.CLIENT_TYPE_ANDROID);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_CART_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                loadingPopup.dismiss();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();

                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    String cartText = _mActivity.getResources().getString(R.string.text_cart);
                    int cartItemCount = responseObj.getInt("datas.skuCount");
                    cartText = String.format(cartText + "(%d/50)", cartItemCount);
                    tvFragmentTitle.setText(cartText);

                    EasyJSONArray cartStoreVoList = responseObj.getSafeArray("datas.cartStoreVoList");

                    int storeCount = 0;
                    totalCartItemCount = 0;
                    cartStoreItemContainer.removeAllViews();
                    for (Object object : cartStoreVoList) { // store LOOP
                        StoreStatus storeStatus = new StoreStatus();
                        storeStatus.parent = totalStatus;
                        EasyJSONObject cartStoreVo = (EasyJSONObject) object;

                        View cartStoreItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_store_item, null, false);
                        TextView tvStoreName = cartStoreItem.findViewById(R.id.tv_store_name);
                        ScaledButton btnCheckStore = cartStoreItem.findViewById(R.id.btn_check_store);
                        btnCheckStore.setTag(storeStatus);
                        setCheckButtonOnClickListener(btnCheckStore);
                        storeStatus.setRadio(btnCheckStore);

                        // 點擊商店標題，跳轉到具體的商店
                        final int storeId = cartStoreVo.getInt("storeId");
                        cartStoreItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Util.startFragment(ShopMainFragment.newInstance(storeId));
                            }
                        });
                        tvStoreName.setText(cartStoreVo.getSafeString("storeName"));

                        EasyJSONArray cartSpuVoList = cartStoreVo.getSafeArray("cartSpuVoList");
                        int spuCount=0;
                        spuCount++;
                        for (Object object2 : cartSpuVoList) { // spu LOOP
                            EasyJSONObject cartSpuVo = (EasyJSONObject) object2;

                            EasyJSONArray cartItemVoList = cartSpuVo.getSafeArray("cartItemVoList");
                            int skuCount=0;
                            for (Object object3 : cartItemVoList) { // sku LOOP
                                LinearLayout cartSpuItemContainer = cartStoreItem.findViewById(R.id.ll_cart_spu_item_container);
                                SpuStatus spuStatus = new SpuStatus();

                                spuStatus.parent = storeStatus;
                                View cartSpuItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_spu_item, null, false);
                                SLog.info("spu%d spulenth%d,sku%d,skulenth%d,",spuCount,cartSpuVoList.length(),skuCount,cartItemVoList.length());
                                skuCount++;
                                if (spuCount == cartSpuVoList.length()&&skuCount==cartItemVoList.length()) {
                                    cartSpuItem.findViewById(R.id.line).setVisibility(View.GONE);
                                }
                                LinearLayout cartSkuItemContainer = cartSpuItem.findViewById(R.id.ll_cart_sku_item_container);
                                View cartSkuItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_sku_item, cartSkuItemContainer, false);

                                ImageView goodsImage = cartSpuItem.findViewById(R.id.goods_image);
                                ScaledButton btnCheckSpu = cartSpuItem.findViewById(R.id.btn_check_spu);

                                btnCheckSpu.setTag(spuStatus);
                                setCheckButtonOnClickListener(btnCheckSpu);
                                spuStatus.setRadio(btnCheckSpu);

                                // 點擊Spu，跳轉到對應的產品詳情頁面
                                final int commonId = cartSpuVo.getInt("commonId");
                                cartSpuItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                                    }
                                });
                                TextView tvGoodsName = cartSkuItem.findViewById(R.id.tv_goods_name);
                                tvGoodsName.setText(cartSpuVo.getSafeString("goodsName"));
                                Glide.with(CartFragment.this).load(cartSpuVo.getSafeString("imageSrc")).centerCrop().into(goodsImage);

                                ++totalCartItemCount;
//                                SkuStatus skuStatus = new SkuStatus();
//                                skuStatus.parent = spuStatus;
                                TextView tvGoodsFullSpecs = cartSkuItem.findViewById(R.id.tv_goods_full_specs);
                                TextView tvPriceSum = cartSkuItem.findViewById(R.id.tv_price_sum);
//                                ScaledButton btnCheckSku = cartSkuItem.findViewById(R.id.btn_check_sku);
//                                btnCheckSku.setTag(skuStatus);

                                // 購買數量調節按鈕
                                CartAdjustButton abQuantity = cartSkuItem.findViewById(R.id.ab_quantity);
                                abQuantity.setMinValue(1, null);  // 調節數量不能小于1

                                abQuantity.setSpuStatus(spuStatus);
                                setCheckButtonOnClickListener(btnCheckSpu);
                                spuStatus.setRadio(btnCheckSpu);

                                EasyJSONObject cartSkuVo = (EasyJSONObject) object3;
                                int limitBuy = cartSkuVo.getInt("limitBuy");
                                //如果有限購限制增加數量
                                if (limitBuy < 0) {
                                    abQuantity.setMaxValue(1, () -> {
                                        ToastUtil.error(_mActivity,getString(R.string.out_of_buy_limit));
                                    });
                                }

                                spuStatus.setGoodsId(cartSkuVo.getInt("goodsId"));
                                spuStatus.setCartId(cartSkuVo.getInt("cartId"));
                                tvGoodsFullSpecs.setText(cartSkuVo.getSafeString("goodsFullSpecs"));
                                float goodsPrice = (float) cartSkuVo.getDouble("goodsPrice");
                                int buyNum = cartSkuVo.getInt("buyNum");
                                SLog.info("buyNum %d,limitNum %d ",buyNum,limitBuy);
                                tvPriceSum.setText(StringUtil.formatPrice(_mActivity, goodsPrice, 0));
                                abQuantity.setValue(buyNum);

                                spuStatus.setPrice(goodsPrice);
                                spuStatus.setCount(buyNum);
                                spuStatus.setLimitState(limitBuy);

                                // 贈品列表
                                EasyJSONArray giftVoList = cartSkuVo.getSafeArray("giftVoList");
                                if (giftVoList.length() > 0) {
                                    LinearLayout llGiftListContainer = cartSkuItem.findViewById(R.id.ll_gift_list_container);
                                    ConfirmOrderGiftListAdapter adapter = new ConfirmOrderGiftListAdapter(_mActivity, llGiftListContainer, R.layout.cart_gift_item);
                                    List<GiftItem> giftItemList = new ArrayList<>();
                                    for (Object object4 : giftVoList) {
                                        GiftItem giftItem = (GiftItem) EasyJSONObject.jsonDecode(GiftItem.class, object4.toString());
                                        giftItemList.add(giftItem);
                                    }
                                    adapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(ViewGroupAdapter adapter, View view, int position) {
                                            GiftItem giftItem = giftItemList.get(position);
                                            Util.startFragment(GoodsDetailFragment.newInstance(giftItem.commonId, giftItem.goodsId));
                                        }
                                    });
                                    adapter.setData(giftItemList);

                                    llGiftListContainer.setVisibility(View.VISIBLE);
                                }

//                                spuStatus.skuStatusList.add(skuStatus);
                                cartSkuItemContainer.addView(cartSkuItem);

                                ImageView maskImage = cartSpuItem.findViewById(R.id.mask_image);
                                if (cartSkuVo.getInt("goodsStatus") == 0) {
                                    Glide.with(_mActivity).load(R.drawable.icon_take_off).into(maskImage);
                                } else if(cartSkuVo.getInt("goodsStorage")<=0){
                                    Glide.with(_mActivity).load(R.drawable.icon_no_storage).into(maskImage);
                                }else if(cartSkuVo.getInt("goodsStorage")<=2){
                                    Glide.with(_mActivity).load(R.drawable.icon_less_storage).into(maskImage);
                                }
                                storeStatus.spuStatusList.add(spuStatus);
                                cartSpuItemContainer.addView(cartSpuItem);
                            } // END OF sku LOOP

                        } // END OF spu LOOP


                        totalStatus.storeStatusList.add(storeStatus);
                        cartStoreItemContainer.addView(cartStoreItem);

                        storeCount++;
                    } // END OF store LOOP

                    if (storeCount == 0 && svItemContainerHeight != -1) { // 如果購物車內沒東西，則顯示空白頁面的占位符
                        View placeholderCartEmpty = LayoutInflater.from(_mActivity).inflate(R.layout.layout_placeholder_cart_empty, null, false);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, svItemContainerHeight);
                        cartStoreItemContainer.addView(placeholderCartEmpty, layoutParams);
                    }

                    displayCartItemCount(cartItemCount);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_edit:
                switchMode();
                break;
            case R.id.btn_settlement:
                EasyJSONArray buyData = totalStatus.getBuyDataNew();
                if (buyData.length() < 1) {
                    // 如果沒有勾選什么數據，返回
                    return;
                }

                if (totalStatus.getTotalDataNew().first >= 20000) {
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
                            }).asCustom(new TwConfirmPopup(_mActivity, "每次交易總金額不的超過￥20,000，請調整購物數量再提交", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    })).show();
                } else {
                    Util.startFragmentForResult(ConfirmOrderFragment.newInstance(1, buyData.toString()), RequestCode.CONFIRM_ORDER.ordinal());
                }
                break;
            case R.id.btn_delete:
                buyData = totalStatus.getBuyDataNew();
                if (buyData.length() < 1) {
                    // 如果沒有勾選什么數據，返回
                    SLog.info("如果沒有勾選什么數據，返回");
                    return;
                }


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
                            deleteCartItem(buyData);
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    })).show();
                break;
            default:
                break;
        }
    }

    private void deleteCartItem(EasyJSONArray buyData) {
        try {
            boolean first = true;
            StringBuilder cartId = new StringBuilder();
            for (Object object : buyData) {
                EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                if (!first) {
                    cartId.append(",");
                }
                cartId.append(easyJSONObject.getInt("cartId"));
                first = false;
            }

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("token", token, "cartId", cartId.toString());
            SLog.info("params[%s]", params);
            Api.postUI(Api.PATH_DELETE_CART, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.success(_mActivity, "刪除成功");
                    reloadList();
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    /**
     * 切換購物袋的模式
     * 查看模式 還是 編輯模式
     */
    private void switchMode() {
        if (mode == Constant.MODE_VIEW) {
            btnDelete.setVisibility(View.VISIBLE);
            llViewModeButtonGroup.setVisibility(View.GONE);

            btnEdit.setText(getResources().getString(R.string.text_finish));
            btnEdit.setTextColor(getResources().getColor(R.color.tw_red, null));
            mode = Constant.MODE_EDIT;
        } else {
            btnDelete.setVisibility(View.GONE);
            llViewModeButtonGroup.setVisibility(View.VISIBLE);

            btnEdit.setText(getResources().getString(R.string.text_edit));
            btnEdit.setTextColor(getResources().getColor(R.color.tw_black, null));
            mode = Constant.MODE_VIEW;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (mode == Constant.MODE_EDIT) {
            switchMode();
            return true;
        }

        if (isStandalone) {
            hideSoftInputPop();
            return true;
        }
        return false;
    }

    private void setCheckButtonOnClickListener(View checkButton) {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaledButton btnCheck = (ScaledButton) v;
                BaseStatus status = (BaseStatus) btnCheck.getTag();
                status.changeCheckStatus(!status.isChecked(), BaseStatus.PHRASE_TARGET);
                updateTotalData();
            }
        });
    }

    /**
     * 更新合計數據
     */
    private void updateTotalData() {
        Pair<Float, Integer> totalData = totalStatus.getTotalDataNew();

        float totalPrice = totalData.first;  // 總價錢
        int totalCount = totalData.second;  // 總件數
        tvTotalPrice.setText(StringUtil.formatPrice(_mActivity, totalPrice, 0));
        String btnSettlementText = textSettlement;

        if (totalCount > 0) {
            btnSettlementText += "(" + totalCount + ")";
        }
        btnSettlement.setText(btnSettlementText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_ADD_CART) {
            updateTotalData();
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_UPDATE_TOOLBAR_RED_BUBBLE ||
                    message.messageType == EBMessageType.MESSAGE_TYPE_LOGIN_SUCCESS) {
            reloadList();
        }
    }
}
