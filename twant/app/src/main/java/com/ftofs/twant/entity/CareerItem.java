package com.ftofs.twant.entity;

import com.ftofs.twant.util.StringUtil;

public class CareerItem {
    public static final int TYPE_EXPERIENCE=1;
    public static final int TYPE_EDUCATION=2;
    public static final int TYPE_CERTIFICATE=3;
    public static final int TYPE_WINNINGEXPERIENCE=4;
    public static final int TYPE_SKILL=5;
    public int Id;
    public String memberName;
    public int itemType=TYPE_EXPERIENCE;
    public String platformName;
    public String major;
    public String Explain;
    public long StartDate;
    public long EndDate;
    public String createTime;
    public String StartDateFormat;//yyyy-mm-dd
    public String EndDateFormat;
    public int acadamicIndex;
    public String educationMajor;
    public String educationAcademic;

    /**
     * 轉型成yyyy.mm字符串
     * @param data
     * @return
     */
    public String toMonth(String data) {
        if (StringUtil.isEmpty(data)) {
            return data;
        } else {
            return String.format("%04d.%02d", Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5, 7)));
        }
    }
    /**
     * 轉型成yyyy年mm月字符串
     * @param data
     * @return
     */
    public String toMonthChina(String data) {
        if (StringUtil.isEmpty(data)) {
            return data;
        } else {
            return String.format("%04d年%02d月", Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5, 7)));
        }
    }
}
