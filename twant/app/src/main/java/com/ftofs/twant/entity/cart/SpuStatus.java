package com.ftofs.twant.entity.cart;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class SpuStatus extends BaseStatus {
    public StoreStatus parent;
    public List<SkuStatus> skuStatusList = new ArrayList<>();
}
