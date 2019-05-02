package com.ftofs.twant.util;


/**
 * 二進制相關工具類
 * @author zwm
 */
public class Binary {
    /**
     * 将二进制数据转为16进制字符串（小写）
     * @param data
     * @return
     */
    public static String toHexString(byte[] data) {
        StringBuffer hexString = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int value;
        for (int i = 0; i < data.length; i++) {
            value = data[i];

            if (value < 0) {
                value += 256;
            }
            if (value < 16) {
                hexString.append("0");
            }
            hexString.append(Integer.toHexString(value));
        }
        return hexString.toString().toLowerCase();
    }
}

