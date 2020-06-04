package com.ftofs.twant.seller.entity;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 各組規格值枚舉項
 * @author zwm
 */
public class SellerSpecPermutation {
    public String specValueIdString;  // 規格值Id用逗號拼接起來 比如 5,12,8
    public String specValueNameString;  // 規格值名稱拼接起來，比如 紅色/xss
    public List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();  // 規格值列表
    public double price;
    public String goodsSN = "";  // 商品編號
    public int storage; // 庫存
    public int reserved; // 預存庫存


    public EasyJSONObject toEasyJSONObject() {
        EasyJSONArray sellerSpecItemListArray = EasyJSONArray.generate();

        for (SellerSpecItem item : sellerSpecItemList) {
            sellerSpecItemListArray.append(item.toEasyJSONObject());
        }

        EasyJSONObject result = EasyJSONObject.generate(
                "specValueIdString", specValueIdString,
                "specValueNameString", specValueNameString,
                "sellerSpecItemList", sellerSpecItemListArray,
                "price", price,
                "goodsSN", goodsSN,
                "storage", storage,
                "reserved", reserved
        );

        return result;
    }
}
