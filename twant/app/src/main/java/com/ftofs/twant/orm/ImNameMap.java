package com.ftofs.twant.orm;

import androidx.annotation.NonNull;

import com.ftofs.twant.log.SLog;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * imName與memberName的映射關系表(只有商店客服才用到這個表)
 * 在點擊客服頭像時，如果沒有記錄關系，就保存記錄關系
 * @author zwm
 */
public class ImNameMap extends LitePalSupport {
    public String imName;
    public String memberName;
    public int storeId; // 對應的商店Id

    public static ImNameMap getByImName(String imName) {
        return LitePal.where("imName = ?", imName).findFirst(ImNameMap.class);
    }

    public static ImNameMap getBymemberName(String memberName) {
        return LitePal.where("memberName = ?", memberName).findFirst(ImNameMap.class);
    }

    /**
     * 保存記錄關系
     * @param imName
     * @param memberName
     * @param storeId
     */
    public static void saveMap(String imName, String memberName, int storeId) {
        SLog.info("saveMap, imName[%s], memberName[%s], storeId[%d]", imName, memberName, storeId);
        ImNameMap map = getByImName(imName);
        if (map != null) { // 已經有映射關系了，返回
            return;
        }

        map = new ImNameMap();
        map.imName = imName;
        map.memberName = memberName;
        map.storeId = storeId;
        map.save();
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("imName[%s], memberName[%s], storeId[%d]",
                imName, memberName, storeId);
    }
}
