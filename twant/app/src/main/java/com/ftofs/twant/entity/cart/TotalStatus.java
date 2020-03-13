package com.ftofs.twant.entity.cart;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 合計狀態
 * @author zwm
 */
public class TotalStatus extends BaseStatus {
    public List<StoreStatus> storeStatusList = new ArrayList<>();

    @Override
    public void changeCheckStatus(boolean checked, int phrase) {
        super.changeCheckStatus(checked, phrase);

        if (phrase == PHRASE_TARGET) {
            for (StoreStatus storeStatus : storeStatusList) {
                storeStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
            }
        } else if (phrase == PHRASE_BUBBLE) {
            // 如果是冒泡階段，要所有子項目都選中了，才選中自己
            boolean allChecked = true;
            for (StoreStatus storeStatus : storeStatusList) {
                if (!storeStatus.isChecked()) {
                    allChecked = false;
                }
            }

            super.changeCheckStatus(allChecked, PHRASE_BUBBLE);
        }
    }

    /**
     * 獲取合計數據
     * @return
     */
    public Pair<Float, Integer> getTotalData() {
        float totalPrice = 0f;
        int totalCount = 0;
        for (StoreStatus storeStatus : storeStatusList) {
            for (SpuStatus spuStatus : storeStatus.spuStatusList) {
                for (SkuStatus skuStatus : spuStatus.skuStatusList) {
                    if (!skuStatus.isChecked()) {
                        continue;
                    }

//                    totalCount += skuStatus.getCount();
//                    totalPrice += skuStatus.getCount() * skuStatus.getPrice();
                }
            }
        }

        return new Pair<>(totalPrice, totalCount);
    }
    /**
     * 獲取合計數據
     * @return
     */
    public Pair<Float, Integer> getTotalDataNew() {
        float totalPrice = 0f;
        int totalCount = 0;
        for (StoreStatus storeStatus : storeStatusList) {
            for (SpuStatus spuStatus : storeStatus.spuStatusList) {
                if (!spuStatus.isChecked()) {
                    continue;
                }

                totalCount += spuStatus.getCount();
                totalPrice += spuStatus.getCount() * spuStatus.getPrice();
            }
        }

        return new Pair<>(totalPrice, totalCount);
    }


    /**
     * 獲取要購買的Sku的數據，用于提交訂單 或 刪除購物袋
     * @return
     */
    public EasyJSONArray getBuyData() {
        EasyJSONArray buyData = EasyJSONArray.generate();
        for (StoreStatus storeStatus : storeStatusList) {
            for (SpuStatus spuStatus : storeStatus.spuStatusList) {
                for (SkuStatus skuStatus : spuStatus.skuStatusList) {
                    if (!skuStatus.isChecked()) {
                        continue;
                    }
//
//                    buyData.append(EasyJSONObject.generate(
//                            "buyNum", skuStatus.getCount(),
//                            "goodsId", skuStatus.getCartId(),
//                            "cartId", skuStatus.getCartId()));
                }
            }
        }

        return buyData;
    }
    /**
     * 獲取要購買的Sku的數據，用于提交訂單 或 刪除購物袋
     * @return
     */
    public EasyJSONArray getBuyDataNew() {
        EasyJSONArray buyData = EasyJSONArray.generate();
        for (StoreStatus storeStatus : storeStatusList) {
            for (SpuStatus spuStatus : storeStatus.spuStatusList) {

                    if (!spuStatus.isChecked()) {
                        continue;
                    }

                    buyData.append(EasyJSONObject.generate(
                            "buyNum", spuStatus.getCount(),
                            "goodsId", spuStatus.getCartId(),
                            "cartId", spuStatus.getCartId()));

            }
        }

        return buyData;
    }
}
