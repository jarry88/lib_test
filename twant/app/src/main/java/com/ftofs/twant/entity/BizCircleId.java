package com.ftofs.twant.entity;

/**
 * 商店所在地區或商圈Id
 * @author zwm
 */
public class BizCircleId {
    public static final int ID_TYPE_UNKNOWN = 0;         // 未選擇
    public static final int ID_TYPE_CIRCLE_AREA_ID = 1;  // int  false  普通参数  0  商圈所在地區ID
    public static final int ID_TYPE_CIRCLE_ID = 2;       // int  false  普通参数  0  商圈ID
    public static final int ID_TYPE_AREA_ID = 3;         // int  false  普通参数  0  商店所在地區ID

    /**
     * Id的類型
     */
    public int idType;
    /**
     * Id的值
     */
    public int id;

    public BizCircleId(int idType, int id) {
        this.idType = idType;
        this.id = id;
    }
}
