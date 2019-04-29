package com.ftofs.twant.data;

/**
 * 區號結構體
 * @author zwm
 */
public class MobileZone {
    public int areaId;
    public String areaName;
    public String areaCode;

    @Override
    public String toString() {
        return String.format("areaId[%d], areaName[%s], areaCode[%s]", areaId, areaName, areaCode);
    }
}
