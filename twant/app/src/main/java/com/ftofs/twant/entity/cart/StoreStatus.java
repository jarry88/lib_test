package com.ftofs.twant.entity.cart;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 店鋪狀態
 * @author zwm
 */
public class StoreStatus extends BaseStatus {
    public TotalStatus parent;
    public List<SpuStatus> spuStatusList = new ArrayList<>();
}
