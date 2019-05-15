package com.ftofs.twant.vo;

import com.ftofs.twant.domain.goods.Brand;
import com.ftofs.twant.domain.goods.BrandLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liusf
 * @create 2018/12/21 19:08
 * @description 品牌標簽VO
 */
public class BrandLabelVo {
    /**
     * 品牌标签编号
     */
    private int brandLabelId;

    /**
     * 品牌标签名称
     */
    private String brandLabelName;

    /**
     * 品牌标签排序
     */
    private int brandLabelSort = 0;

    /**
     * 品牌列表
     */
    private List<Brand> brandList = new ArrayList<>();

    public BrandLabelVo(){
    }

    public BrandLabelVo(BrandLabel brandLabel){
        this.brandLabelId = brandLabel.getBrandLabelId();
        this.brandLabelName = brandLabel.getBrandLabelName();
        this.brandLabelSort = brandLabel.getBrandLabelSort();
    }

    public int getBrandLabelId() {
        return brandLabelId;
    }

    public void setBrandLabelId(int brandLabelId) {
        this.brandLabelId = brandLabelId;
    }

    public String getBrandLabelName() {
        return brandLabelName;
    }

    public void setBrandLabelName(String brandLabelName) {
        this.brandLabelName = brandLabelName;
    }

    public int getBrandLabelSort() {
        return brandLabelSort;
    }

    public void setBrandLabelSort(int brandLabelSort) {
        this.brandLabelSort = brandLabelSort;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }
}
