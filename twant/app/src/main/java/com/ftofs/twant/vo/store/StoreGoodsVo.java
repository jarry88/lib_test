package com.ftofs.twant.vo.store;

import com.ftofs.twant.vo.goods.GoodsVo;

import java.util.List;

/**
 * @Description: 首頁店鋪模塊視圖對象
 * @Auther: yangjian
 * @Date: 2018/10/25 11:26
 */
public class StoreGoodsVo {
    /**
     * 店鋪
     */
    private StoreVo storeVo;
    /**
     * 商品
     */
    private List<GoodsVo> goodsList;

    public StoreGoodsVo(){}

    public StoreGoodsVo(StoreVo storeVo, List<GoodsVo> goodsList) {
        this.storeVo = storeVo;
        this.goodsList = goodsList;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    public List<GoodsVo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsVo> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "StoreGoodsVo{" +
                "storeVo=" + storeVo +
                ", goodsList=" + goodsList +
                '}';
    }
}
