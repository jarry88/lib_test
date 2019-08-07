package com.ftofs.twant.orm;

import android.support.annotation.NonNull;

import com.ftofs.twant.log.SLog;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * imName與memberName的映射關系表(只有店鋪客服才用到這個表)
 * @author zwm
 */
public class ImNameMap extends LitePalSupport {
    public String imName;
    public String memberName;
    public int storeId; // 對應的店鋪Id

    public static ImNameMap getByImName(String imName) {
        return LitePal.where("imName = ?", imName).findFirst(ImNameMap.class);
    }

    public static ImNameMap getBymemberName(String memberName) {
        return LitePal.where("memberName = ?", memberName).findFirst(ImNameMap.class);
    }

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
