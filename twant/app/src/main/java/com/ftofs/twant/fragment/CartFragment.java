package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.cart.BaseStatus;
import com.ftofs.twant.entity.cart.SkuStatus;
import com.ftofs.twant.entity.cart.SpuStatus;
import com.ftofs.twant.entity.cart.StoreStatus;
import com.ftofs.twant.entity.cart.TotalStatus;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.CartAdjustButton;
import com.ftofs.twant.widget.ScaledButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 購物車
 * @author zwm
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {
    TextView tvFragmentTitle;
    LinearLayout cartStoreItemContainer;
    String currencyTypeSign;
    String textSettlement;
    TotalStatus totalStatus = new TotalStatus();

    TextView btnSettlement;
    TextView tvTotalPrice;

    public static CartFragment newInstance() {
        Bundle args = new Bundle();

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

        currencyTypeSign = getResources().getString(R.string.currency_type_sign);
        textSettlement = getResources().getString(R.string.text_settlement);

        ScaledButton btnSelectAll = view.findViewById(R.id.btn_select_all);
        btnSelectAll.setTag(totalStatus);
        setCheckButtonOnClickListener(btnSelectAll);
        totalStatus.setRadio(btnSelectAll);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        cartStoreItemContainer = view.findViewById(R.id.ll_cart_store_item_container);

        btnSettlement = view.findViewById(R.id.btn_settlement);
        btnSettlement.setOnClickListener(this);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

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

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "clientType", Constant.CLIENT_TYPE_ANDROID);

        Api.postUI(Api.PATH_CART_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    String cartText = getResources().getString(R.string.text_cart);
                    int cartItemCount = responseObj.getInt("datas.skuCount");
                    cartText = String.format(cartText + "(%d)", cartItemCount);
                    tvFragmentTitle.setText(cartText);

                    EasyJSONArray cartStoreVoList = responseObj.getArray("datas.cartStoreVoList");
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

                        tvStoreName.setText(cartStoreVo.getString("storeName"));

                        EasyJSONArray cartSpuVoList = cartStoreVo.getArray("cartSpuVoList");
                        LinearLayout cartSpuItemContainer = cartStoreItem.findViewById(R.id.ll_cart_spu_item_container);
                        for (Object object2 : cartSpuVoList) { // spu LOOP
                            SpuStatus spuStatus = new SpuStatus();
                            spuStatus.parent = storeStatus;
                            View cartSpuItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_spu_item, null, false);

                            TextView tvGoodsName = cartSpuItem.findViewById(R.id.tv_goods_name);
                            ImageView goodsImage = cartSpuItem.findViewById(R.id.goods_image);
                            ScaledButton btnCheckSpu = cartSpuItem.findViewById(R.id.btn_check_spu);
                            btnCheckSpu.setTag(spuStatus);
                            setCheckButtonOnClickListener(btnCheckSpu);
                            spuStatus.setRadio(btnCheckSpu);

                            EasyJSONObject cartSpuVo = (EasyJSONObject) object2;
                            tvGoodsName.setText(cartSpuVo.getString("goodsName"));
                            Glide.with(CartFragment.this).load(cartSpuVo.getString("imageSrc")).into(goodsImage);

                            EasyJSONArray cartItemVoList = cartSpuVo.getArray("cartItemVoList");
                            LinearLayout cartSkuItemContainer = cartSpuItem.findViewById(R.id.ll_cart_sku_item_container);
                            for (Object object3 : cartItemVoList) { // sku LOOP
                                SkuStatus skuStatus = new SkuStatus();
                                skuStatus.parent = spuStatus;
                                View cartSkuItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_sku_item, null, false);
                                TextView tvGoodsFullSpecs = cartSkuItem.findViewById(R.id.tv_goods_full_specs);
                                TextView tvPriceSum = cartSkuItem.findViewById(R.id.tv_price_sum);
                                ScaledButton btnCheckSku = cartSkuItem.findViewById(R.id.btn_check_sku);
                                btnCheckSku.setTag(skuStatus);

                                // 購買數量調節按鈕
                                CartAdjustButton abQuantity = cartSkuItem.findViewById(R.id.ab_quantity);
                                abQuantity.setMinValue(1);  // 調節數量不能小于1
                                abQuantity.setSkuStatus(skuStatus);
                                setCheckButtonOnClickListener(btnCheckSku);
                                skuStatus.setRadio(btnCheckSku);

                                EasyJSONObject cartSkuVo = (EasyJSONObject) object3;

                                skuStatus.setGoodsId(cartSkuVo.getInt("goodsId"));
                                skuStatus.setCartId(cartSkuVo.getInt("cartId"));
                                tvGoodsFullSpecs.setText(cartSkuVo.getString("goodsFullSpecs"));
                                float goodsPrice = (float) cartSkuVo.getDouble("goodsPrice");
                                int buyNum = cartSkuVo.getInt("buyNum");
                                tvPriceSum.setText(currencyTypeSign + goodsPrice);
                                abQuantity.setValue(buyNum);

                                skuStatus.setPrice(goodsPrice);
                                skuStatus.setCount(buyNum);

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(0, Util.dip2px(_mActivity, 15), 0, 0);

                                spuStatus.skuStatusList.add(skuStatus);
                                cartSkuItemContainer.addView(cartSkuItem, layoutParams);
                            } // END OF sku LOOP

                            storeStatus.spuStatusList.add(spuStatus);
                            cartSpuItemContainer.addView(cartSpuItem);
                        } // END OF spu LOOP

                        totalStatus.storeStatusList.add(storeStatus);
                        cartStoreItemContainer.addView(cartStoreItem);
                    } // END OF store LOOP
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_settlement) {
            String buyData = totalStatus.getBuyData();
            if (buyData == null) {
                // 如果沒有勾選什么數據，返回
                return;
            }

            MainFragment mainFragment = (MainFragment) getParentFragment();
            mainFragment.start(ConfirmBillFragment.newInstance(buyData));
        }
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
        Pair<Float, Integer> totalData = totalStatus.getTotalData();

        float totalPrice = totalData.first;  // 總價錢
        int totalCount = totalData.second;  // 總件數
        tvTotalPrice.setText(currencyTypeSign + totalPrice);
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
        }
    }
}
