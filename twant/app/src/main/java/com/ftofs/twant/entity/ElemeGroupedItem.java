package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

import com.kunminx.linkage.bean.BaseGroupedItem;

public class ElemeGroupedItem extends BaseGroupedItem<ElemeGroupedItem.ItemInfo> {

    public ElemeGroupedItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ElemeGroupedItem(ItemInfo item) {
        super(item);
    }

    public static class ItemInfo extends BaseGroupedItem.ItemInfo {
        public boolean show=true;
        private float discount;
        private float original;
        private String content;
        private String imgUrl;
        private String cost;
        public int commonId;

        public ItemInfo(String title, String group, String content) {
            super(title, group);
            this.content = content;
        }

        public ItemInfo(String title, String group, String content, String imgUrl) {
            this(title, group, content);
            this.imgUrl = imgUrl;
        }

        public ItemInfo(String title, String group, String content, String imgUrl, String cost) {
            this(title, group, content, imgUrl);
            this.cost = cost;
        }
        public ItemInfo(String title, String group, String content, String imgUrl, String cost,float discount,float original,int commonId,boolean showDiscount) {
            this(title, group, content, imgUrl,cost);
            this.discount = discount;
            this.original = original;
            this.commonId = commonId;
            this.show = showDiscount;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public float getDoscount() {
            return discount;
        }

        public float getOriginal() {
            return original;
        }
    }

    @NonNull
    @Override
    public String toString() {
        if (info == null) {
            return String.format("header[%s]",header);
        }
        return String.format("header[%s],content[%s],imgUrl[%s],cost[%s]",header,info.content,info.imgUrl,info.cost);
    }
}