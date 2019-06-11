package com.ftofs.twant.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 分類商品數據結構
 * @author zwm
 */
public class CategoryCommodityList {
    public CategoryCommodity head;
    public List<CategoryCommodityRow> list = new ArrayList<>();
}
