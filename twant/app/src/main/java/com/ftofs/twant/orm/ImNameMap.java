package com.ftofs.twant.orm;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * imName與memberName的映射關系表
 * @author zwm
 */
public class ImNameMap extends LitePalSupport {
    public String imName;
    public String memberName;

    public static ImNameMap getByImName(String imName) {
        return LitePal.where("imName = ?", imName).findFirst(ImNameMap.class);
    }

    public static ImNameMap getBymemberName(String memberName) {
        return LitePal.where("memberName = ?", memberName).findFirst(ImNameMap.class);
    }

    public static void saveMap(String imName, String memberName) {
        ImNameMap map = getByImName(imName);
        if (map != null) { // 已經有映射關系了，返回
            return;
        }

        map = new ImNameMap();
        map.imName = imName;
        map.memberName = memberName;
        map.save();
    }
}
