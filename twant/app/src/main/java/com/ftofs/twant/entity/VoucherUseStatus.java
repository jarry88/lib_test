package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

/**
 * 商店券的使用狀態的數據結構
 * @author zwm
 */
public class VoucherUseStatus {
    public int storeId;
    public int voucherId;
    public boolean isInUse;

    @NonNull
    @Override
    public String toString() {
        return String.format("storeId[%d], voucherId[%d], isInUse[%s]", storeId, voucherId, isInUse);
    }
}
