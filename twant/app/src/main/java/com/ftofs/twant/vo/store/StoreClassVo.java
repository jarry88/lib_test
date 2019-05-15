package com.ftofs.twant.vo.store;

import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.domain.store.StoreClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @function StoreClassVo
 * @description 主營業務二級分類VO
 * @param
 * @return
 * @author Nick.Chung
 * @create 2018/7/30 17:31
 */
public class StoreClassVo extends StoreClass {
    private String parentCnName = ""; //父類名稱
    private String parentEnName = ""; //英文名稱
    private int deep = 1; //目前級別（1,2)
    private long storeListCount;//該分類店鋪總數量
    //Modify By liusf 2018/12/24 14:38 該分類商品總數量
    private long goodsListCount;
    private List<StoreClassVo> children = new ArrayList<>();//下級菜單
    private List<Store> storeList = new ArrayList<>();//店鋪列表

    public String getParentCnName() {
        return parentCnName;
    }

    public void setParentCnName(String parentCnName) {
        this.parentCnName = parentCnName;
    }

    public String getParentEnName() {
        return parentEnName;
    }

    public void setParentEnName(String parentEnName) {
        this.parentEnName = parentEnName;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public List<StoreClassVo> getChildren() {
        return children;
    }

    public void setChildren(List<StoreClassVo> children) {
        this.children = children;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

    public long getStoreListCount() {
        return storeListCount;
    }

    public void setStoreListCount(long storeListCount) {
        this.storeListCount = storeListCount;
    }

    public long getGoodsListCount() {
        return goodsListCount;
    }

    public void setGoodsListCount(long goodsListCount) {
        this.goodsListCount = goodsListCount;
    }

    @Override
    public String toString() {
        return "StoreClassVo{" +
                "parentCnName='" + parentCnName + '\'' +
                ", parentEnName='" + parentEnName + '\'' +
                ", deep=" + deep +
                ", storeListCount=" + storeListCount +
                ", children=" + children +
                ", storeList=" + storeList +
                '}';
    }
}
