package com.ftofs.twant.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by BillyBottle on 2017/10/20.
 */

public class Guid {
    /**
     * 返回guid
     * @return
     */
    public static String get() {
        return UUID.randomUUID().toString();
    }

    /**
     * 将1位62进制数值转换为10进制数值
     * @param digit
     * @return
     *
     * 例如
     * 0 => 0
     * A => 10
     * Z => 35
     * a => 36
     * z => 61
     */
    public static int unit62_unit10_digit_converter(char digit) {
        int charCode = (int) digit;
        int result = 0;
        if (0x30 <= charCode && charCode <= 0x39) {
            result = charCode - 0x30;
        } else if (0x41 <= charCode && charCode <= 0x5a) {
            result = charCode - 0x41 + 10;
        } else if (0x61 <= charCode && charCode <= 0x7a) {
            result = charCode - 0x61 + 10 + 26;
        }
        return result;
    }

    /**
     * 从62进制字符串转换到10进制数值
     * @param val
     * @return
     *
     * 例如
     * 10 => 62
     * 11 => 63
     * z0 => 3782
     */
    public static long unit62_unit10(String val) {
        int len = val.length();
        long result = 0;
        for (int i = 0; i < len; ++i) {
            result = 62 * result + unit62_unit10_digit_converter(val.charAt(i));
        }

        return result;
    }

    /**
     * 10进制数据 => 62进制字符串
     * @param val
     * @return
     */
    public static String unit10_unit62(long val) {
        String[] unit_62_table = {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
        };
        String result = "";
        while (val > 0) {
            result = unit_62_table[(int) (val % 62)] + result;
            val = val / 62;
        }
        if (result.length() == 0) {
            return "0";
        }
        return result;
    }


    /**
     * 返回32位长度的SpUuid
     * @return string
     */
    public static String getSpUuid() {
        String epoch_62 = unit10_unit62(System.currentTimeMillis());
        while (epoch_62.length() < 8) {
            epoch_62 = "0" + epoch_62;
        }
        return epoch_62 + randString(24);
    }

    public static String randString(int len) {
        String[] table = {
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        };
        List<String> tableList = new ArrayList<>();
        for (int i = 0; i < table.length; ++i) {
            tableList.add(table[i]);
        }
        Collections.shuffle(tableList);
        String  str = "";
        for (int i = 0; i < len; ++i) {
            str += tableList.get(getIndex());
        }
        return str;
    }

    public static int getIndex() {
        int index = 0;
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[1];  // byte的取值范围是-128 ~ +127
        boolean ok = false;

        while (!ok) {
            random.nextBytes(bytes);
            int n = bytes[0];
            System.out.println("n = " + n);
            n += 128;  // 使其取值范围为 0 ~ +255
            if (n >= 248) { // 248 = 62 * 4 (最大值取62的整数倍，此处取4)
                continue;
            }
            ok = true;
            index = n % 62;
        }

        return index;
    }
}
