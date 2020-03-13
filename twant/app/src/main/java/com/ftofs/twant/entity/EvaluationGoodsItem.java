package com.ftofs.twant.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 訂單的每一件產品
 * @author zwm
 */
public class EvaluationGoodsItem {
    public EvaluationGoodsItem(int commonId, String imageSrc, String goodsName, String goodsFullSpecs) {
        this.commonId = commonId;
        this.imageSrc = imageSrc;
        this.goodsName = goodsName;
        this.goodsFullSpecs = goodsFullSpecs;

        this.content = "";
        this.imageList = new ArrayList<>();
    }

    public int commonId;
    public String imageSrc;
    public String goodsName;
    public String goodsFullSpecs;

    public String content;  // 說說內容
    public List<String> imageList; // 評價圖片
}

