package com.ftofs.twant.config;

/**
 * 配置類
 * @author zwm
 */
public class Config {
    /**
     * 數據庫版本
     * 改變表結構或新增表等操作，都要遞增數據庫版本
     */
    public static final int DB_VERSION = 3;

    /**
     * 登錄有效期(秒)
     */
    public static final int LOGIN_VALID_TIME = 10 * 24 * 3600;

    /**
     * 開發模式
     */
    public static final boolean DEVELOPER_MODE = false;

    public static String OSS_BASE_URL = DEVELOPER_MODE ?
            "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com"
            : "https://img.twant.com";

    public static String API_BASE_URL = DEVELOPER_MODE ?
            "http://192.168.5.29/api"
            : "https://f2.twant.com/api";

    public static String WEB_BASE_URL = DEVELOPER_MODE ?
            "http://192.168.5.29/web"
            : "http://www.twant.com/web";

    /**
     * Bugly
     */
    public static String BUGLY_KEY = "222fdc9738";

}
