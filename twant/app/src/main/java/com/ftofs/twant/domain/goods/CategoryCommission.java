package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class CategoryCommission implements Serializable {
    /**
     * 主键 自增
     */
    private int commissionId;

    /**
     * 商品分类
     */
    private int categoryId;

    /**
     * 佣金比例
     */
    private int commissionRate;

    public int getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(int commissionId) {
        this.commissionId = commissionId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Override
    public String toString() {
        return "CategoryCommission{" +
                "commissionId=" + commissionId +
                ", categoryId=" + categoryId +
                ", commissionRate=" + commissionRate +
                '}';
    }
}
