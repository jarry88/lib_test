package com.ftofs.lib_net.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzp.lib_common.constant.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

public class StoreLabel implements Serializable,MultiItemEntity {
    /**
     * 店内產品分类编号
     */
    private int storeLabelId;

    /**
     * 對應分類下的商品數
     */
    private int goodsCount;

    /**
     * 店内產品分类名称
     */
    private String storeLabelName;

    /**
     * 店内產品分类排序
     */
    private int storeLabelSort = 0;

    /**
     * 父级分类编号
     */
    private int parentId = 0;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 是否折叠
     */
    private int isFold;

    /**
     * 图片
     */
    private String image = "";

    /**
     * 图片路径
     */
    private String imageSrc;

    /**
     * 子集实体
     */
    private List<StoreLabel> storeLabelList;
    private int depth =1;
    private int itemType=0;

    public static StoreLabel parse(EasyJSONObject label)throws Exception {
        StoreLabel storeLabel = new StoreLabel();
        storeLabel.setIsFold(label.getInt("isFold"));
        storeLabel.setStoreLabelId(label.getInt("storeLabelId"));
        storeLabel.setParentId(label.getInt("parentId"));
        storeLabel.setImage(label.getSafeString("image"));
        storeLabel.setImageSrc(label.getSafeString("imageSrc"));
        storeLabel.parseStoreLabelList(label.getArray("storeLabelList"),storeLabel.depth+1);
        storeLabel.setStoreLabelName(label.getSafeString("storeLabelName"));
        return storeLabel;
    }

    public int getStoreLabelId() {
        return storeLabelId;
    }

    public void setStoreLabelId(int storeLabelId) {
        this.storeLabelId = storeLabelId;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getStoreLabelName() {
        return storeLabelName;
    }

    public void setStoreLabelName(String storeLabelName) {
        this.storeLabelName = storeLabelName;
    }

    public int getStoreLabelSort() {
        return storeLabelSort;
    }

    public void setStoreLabelSort(int storeLabelSort) {
        this.storeLabelSort = storeLabelSort;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
/**
   1 是 折叠
 */
    public int getIsFold() {
        return isFold;
    }

    public void setIsFold(int isFold) {
        this.isFold = isFold;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public List<StoreLabel> getStoreLabelList() {
        return storeLabelList;
    }

    public void setStoreLabelList(List<StoreLabel> storeLabelList) {
        this.storeLabelList = storeLabelList;
    }

    @Override
    public String toString() {
        return "StoreLabel{" +
                "storeLabelId=" + storeLabelId +
                ", storeLabelName='" + storeLabelName + '\'' +
                ", storeLabelSort=" + storeLabelSort +
                ", parentId=" + parentId +
                ", storeId=" + storeId +
                ", isFold=" + isFold +
                ", image='" + image + '\'' +
                ", storeLabelList=" + storeLabelList +
                '}';
    }

    public void parseStoreLabelList(EasyJSONArray storeLabelList,int depth)throws Exception {
        if (storeLabelList != null && storeLabelList.length() > 0) {
            List<StoreLabel> storeLabels = new ArrayList<>();
            for (Object object2 : storeLabelList) {
                EasyJSONObject easyJSONObject2 = (EasyJSONObject) object2;
                StoreLabel storeLabel2 = new StoreLabel();
                storeLabel2.setStoreLabelId(easyJSONObject2.getInt("storeLabelId"));
                storeLabel2.setStoreLabelName(easyJSONObject2.getSafeString("storeLabelName"));
                storeLabel2.setParentId(easyJSONObject2.getInt("parentId"));
                storeLabel2.setStoreId(easyJSONObject2.getInt("storeId"));
                storeLabel2.depth = depth;
                storeLabel2.setIsFold(Constant.TRUE_INT);

                storeLabels.add(storeLabel2);
            }
            setStoreLabelList(storeLabels);
        }
    }

    public int getAreaDeep() {
        return depth;
    }


    @Override
    public int getItemType() {
        return itemType;
    }
}
