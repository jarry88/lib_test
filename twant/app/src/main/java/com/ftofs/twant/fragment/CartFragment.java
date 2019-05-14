package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
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
import com.ftofs.twant.widget.AdjustButton;

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
    TotalStatus totalStatus = new TotalStatus();

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

        currencyTypeSign = getResources().getString(R.string.currency_type_sign);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        cartStoreItemContainer = view.findViewById(R.id.ll_cart_store_item_container);

        loadCartData();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
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
                    for (Object object : cartStoreVoList) { // store LOOP
                        StoreStatus storeStatus = new StoreStatus();
                        storeStatus.parent = totalStatus;
                        EasyJSONObject cartStoreVo = (EasyJSONObject) object;

                        View cartStoreItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_store_item, null, false);
                        TextView tvStoreName = cartStoreItem.findViewById(R.id.tv_store_name);
                        ImageView btnCheckStore = cartStoreItem.findViewById(R.id.btn_check_store);
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
                            ImageView btnCheckSpu = cartSpuItem.findViewById(R.id.btn_check_spu);
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
                                ImageView btnCheckSku = cartSkuItem.findViewById(R.id.btn_check_sku);
                                btnCheckSku.setTag(skuStatus);

                                // 購買數量調節按鈕
                                AdjustButton abQuantity = cartSkuItem.findViewById(R.id.ab_quantity);
                                setCheckButtonOnClickListener(btnCheckSku);
                                skuStatus.setRadio(btnCheckSku);


                                EasyJSONObject cartSkuVo = (EasyJSONObject) object3;
                                tvGoodsFullSpecs.setText(cartSkuVo.getString("goodsFullSpecs"));
                                tvPriceSum.setText(currencyTypeSign + String.valueOf(cartSkuVo.getDouble("goodsPrice")));
                                abQuantity.setValue(cartSkuVo.getInt("buyNum"));

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

    }

    private void setCheckButtonOnClickListener(ImageView checkButton) {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView btnCheck = (ImageView) v;
                BaseStatus status = (BaseStatus) btnCheck.getTag();
                if (status.isChecked()) {
                    status.setChecked(false);
                    btnCheck.setImageResource(R.drawable.icon_cart_item_unchecked);
                } else {
                    status.setChecked(true);
                    btnCheck.setImageResource(R.drawable.icon_cart_item_checked);
                }
            }
        });
    }
}

