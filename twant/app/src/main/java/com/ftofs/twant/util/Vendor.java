package com.ftofs.twant.util;

import android.os.Build;

import com.ftofs.twant.log.SLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Vendor {
    /**
     * 手機廠商ROM類型
     */
    public static final int VENDOR_OTHER = 0;
    public static final int VENDOR_SONY = 1;
    public static final int VENDOR_HUAWEI = 2;
    public static final int VENDOR_XIAOMI = 3;
    public static final int VENDOR_SAMSUNG = 4;


    public static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    public static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    public static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    public static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    public static final String KEY_VERSION_VIVO = "ro.vivo.os.version";


    public static String getProp(String name) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

    /**
     * 获取厂商信息
     *
     * @return 获取厂商信息
     */
    public static String getManufacturer() {
        return (Build.MANUFACTURER) == null ? "" : (Build.MANUFACTURER).trim();
    }

    public static int getVendorType() {
        String manufacturer = getManufacturer().toLowerCase();
        if (manufacturer.contains("sony")) {
            SLog.info("索尼手機");
            return VENDOR_SONY;
        }

        if (!StringUtil.isEmpty(getProp(KEY_VERSION_EMUI))) {
            SLog.info("華為手機");
            return VENDOR_HUAWEI;
        }

        if (!StringUtil.isEmpty(getProp(KEY_VERSION_MIUI))) {
            SLog.info("小米手機");
            return VENDOR_XIAOMI;
        }

        if (manufacturer.contains("samsung")) {
            SLog.info("三星手機");
            return VENDOR_SAMSUNG;
        }

        return VENDOR_OTHER;
    }
}
