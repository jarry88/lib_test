package com.ftofs.twant.entity;

import java.util.List;

/**
 * 商圈數據結構
 * @author zwm
 */
public class BizCircleItem {
    public int id;
    public String name;
    public List<BizCircleItem> subItemList;

    public BizCircleItem(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
