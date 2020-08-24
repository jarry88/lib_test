package com.ftofs.twant.config;

import com.ftofs.twant.constant.SPField;
import com.macau.pay.sdk.MPaySdk;
import com.macau.pay.sdk.base.ConstantBase;
import com.orhanobut.hawk.Hawk;
import com.wzq.mvvmsmart.net.net_utils.MmkvUtils;

/**
 * 配置類
 *
 * @author zwm
 */
public class Config {
    /**
     * 數據庫版本
     * 改變表結構或新增表等操作，都要遞增數據庫版本
     */
    public static final int DB_VERSION = 14;

    /**
     * 登錄有效期(秒)
     */
    public static final int LOGIN_VALID_TIME = 10 * 24 * 3600;

    /**
     * 上傳文件最大為4MB
     */
    public static final int UPLOAD_FILE_SIZE_LIMIT = 4 * 1024 * 1024;

    /**
     * Android 鍵盤最小的高度（單位dp）
     */
    public static final int KEYBOARD_MIN_HEIGHT = 150;
    public static final int ENV_229 = 6;


    public static boolean PROD = true;//true不能進入調試頁面，false，可以進入調試頁面切換環境
    public static boolean DEVELOPER_MODE = true;//線上模式：false,調試模式true

    public static boolean USE_28 = false;   // 開發模式下: true -- 使用28服務器  false -- 使用29服務器
    public static final boolean USE_F2 = true;  // 生產模式下: true -- 使用F2服務器  false -- 使用www服務器
    public static boolean USE_F3 = false;  // 生產模式下: true -- 使用F3服務器  false -- 使用www服務器

    /**
     * 日誌開關
     */

    public static boolean SLOGENABLE = false;

    public static String OSS_BASE_URL = DEVELOPER_MODE ?
            "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com"
            : "https://img.twant.com";

    public static String API_BASE_URL = DEVELOPER_MODE ?
            (USE_28 ? "http://192.168.5.28/api" : "https://192.168.5.29/api")
            : (USE_F2 ? (USE_F3 ? "https://f3.twant.com/api" : "https://f2.twant.com/api") : "https://www.twant.com/api");

    public static String WEB_BASE_URL = DEVELOPER_MODE ?
            (USE_28 ? "http://192.168.5.28/web" : "https://192.168.5.29/web")
            : (USE_F2 ? (USE_F3 ? "https://f3.twant.com/api" : "https://f2.twant.com/api") : "https://www.twant.com/web");
    public static String BASE_URL = DEVELOPER_MODE ?
            (USE_28 ? "http://192.168.5.28" : "https://192.168.5.29")
            : (USE_F2 ? (USE_F3 ? "https://f3.twant.com/api" : "https://f2.twant.com/api") : "https://www.twant.com");

    public static String MOBILE_WEB_BASE_URL = DEVELOPER_MODE ?
            (USE_28 ? "http://192.168.5.28" : "https://192.168.5.29")
            : "https://www.twant.com";


    /**
     * 大豐支付請求的服務器URL
     */
    public static String TAIFUNG_PAY_SERVER_URL = DEVELOPER_MODE ?
            "https://test.taifungbank.com/payment-web/"
            : "https://epay.taifungbank.com/payment-web/";

    /**
     * 大豐支付私鑰
     */
    public static String TAIFUNG_PAY_SECRET_KEY = DEVELOPER_MODE ?
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/KuuLqkSm6ti6jEmqwbU6HNFMQhfYucxdTBJTr90P+neNOge0dVDLIqnazTAWAwW+d57qFk5uU5p7naP+mejOmOEd7+TLcDfy4wzmtmFFSC1TGa+8UaVhEpoiUltLViAEFgxJ5VP1VcRR2RBWMxKflUfoAvzQBs5x+t7OozYmHbV2AwmjngJy6KIjXe6pqHpWzWYy9WW9rJnLL2obUq7btzghogQy3S+R3axALoGiAGL0Vec3mreTY0RmyEXVeDVBUgd7mq2IIh9W2/x47uAz/61++udb3pD9W1Ue48nQhkhJ9Ow4g7PRXIibHL1lK6SMtFLi/hsa/XWziw3CRyhRAgMBAAECggEAU3WliEA54LW/ERKWRtpzCH/0UFq6ln/nXQQNLEQnOwaakym2m25sa4MirMfQKov/QfxvgLtkWn5df4J/SnRfU3MjNTK6rKa9hmjiBQeyx9CPGSypsInkrdC1Qi66dNWQ/Lezfb+FPCLJpwIhQ8DgbJN75SsIvLl7//8KryRKS7EDMP4mEw25f9F7eKv0HdqMSlO3gR7vkUvz/xRymo+SABFjMUYP5mIJMe3h/c/uJX+3Ege3U+RP5aETvDq+toDpAkEwsBk0F64JeAVpi236Ho7pPcaehnTL9c2pjvOHbwaJsK3HGrU49zS9oBH3TXIrmaqzmpjwiaebUxPBFWex0QKBgQDriHXBTZY9kPsQwXnY3BsWujov28cyV7H5pu0PAG4ijHqG74Q1Llo5AYCqjO008UnHURGhyOuWTYXs8VUWNjwjok1y5kn8+dyGAb+9QU1lCT8+/WfbxPJEleCcKNlvv6qtJoo6M65V9X4a+gmdPOnbyLlZc15V3g+jCr0NdnVwUwKBgQDPx4gjI+1jMzbe2MCA8IfAQxD1hNNclvFhYEYQGYySwTso7CxUHRTX9uhVqhNrEADUYj+ka5wFO4xyJVThzh8w+t55cYt9VWJBfMZWwlFxUfen+jiRSc2tVXaBTa2sG4qJgFSLOBSlE1OqiP4D/xYTh9V94VJLbJlQ3SL/v7vASwKBgAUf+P/1wjkguHXK3+3aDDTYZH+6FoF/6v11pl7XMY5K5Defao8FrSzkXXpYiqjGP0a4+ts8VfP1R965+ZH8KB7WXz0Kyb1ZanT4AMYLb7WtF9U1Cld715GqeTKsqN6Hmx0dY5CUo0x3hQDtQ9xKAQSpP2801W6k4E4545cxZqjFAoGASaohLuw25nuq0XkhTtV4G4brhVAxK1tseqyKSnz0ZLdTRR/uW2fwEt0749snhUaoNKQckiuApi5FjdaEcIYGcvQOWhoSbT4PVs5o9ytvenCoEArbcU8sN27cU915XFJrXHJ+Btm8IAZpHEXzYPFYH1aCL13qMklvVccA+JE+fF8CgYEA1maDuZLKV2wcrx2MNkbSanpjo8Tvnl8XuM/Kec9Aoj2dxg+YmC/5EIZVjRwWhkzv07whxZrHczOYZp/og7WuTtq/9rFWQ9bFkPYhXihlBQjftf6ZhWIQ046SpmwTcbeCQ64ocULHkwQcjSRRI63XpW3Yr2xsWHNAwFPzrEyIW9o="
            : "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDDPgas+09RfZldLzVSy/hNXz1hhNuVogs8IqRz2Z40MjzGEyCqot85ZEQyJkRfmWDwBKrmEp6TA5nq6oXfnLWEtPgpfcVDuKsUzvhxnxrWdDtyLbZxKjsxVfkWJ+7Lhh5NiOElnXtLjhBRhCHDjBF6hE18XRT4IC8x0rlxLvTFU0geBtS7OUj5DkX6lBzmNrexYmiD/6WO+e11Ti8mi9fntEZ7B2T/qxf1jss2x1Q+bp2PqkbNW9jE30F0BEfkkGpAoixS7LolmOGMd5o7jgqNQGjsV5KFppeSMYz+IWX4oY6TtD21Fd8OJlq3Qa2O6q9PTfq6m/T5+icjifzB7optAgMBAAECggEBALzXFWXifMl5DglPS+gVWMidQaU895Y28V7ssttKtPYPUTDT91iTyAyeqkdHNAKzO0treFCn2bCgXxMUWv/5dqgw0YZwG4hw6ShrN2nOVgruyUCabkfubOo/GXkQjqtTwaOErR/QvhxKAAaYIoAczhsONFQBQ/LqTjuGcc7DeAQR5DDzRl4N6UKh+WHikLemMaPma/ecQyQAHjFffCfEmzCpefxgSrphNNaVVtCPLdHbMbkT2PzeMw2CuzYVPilN0u5u0RLmUc0TRqtU5GOSHNvSSIIYcrxQk2HrxSe7wx+NklM+/FQ5ckA0MwAHcAp95WeM5zCXRihwq5/6K1QAdAECgYEA65SX66PkCpn7YstM7jfCtF1hQO6S1ACjC+FvW7Igb49Ix0kyqCpQmZn2XVkg1iGSjamiA8sWaXhcEu+AxUBSkMQKOuZz2fGd8jhgOs4LVrHHc0xdC2uqAL0qXwo8FfeeB/fFXKXHG2Q6S5yv4O2vVCuCssm8Zih2q8gp/a9AMy0CgYEA1CpZo0hDhmLGuBGmpRGU2Y9z5oFYGPEL1bKz2VJlKPdcckN6iN6sxYDCEdvAko9kCTnFTyuZAI+kqsIoSPW1bI5XGdn4PB/dEPe86Kh/IXUnTBkqaABFgwAhsVbOUCsTxQbWkYCPPjR4Gvn1vy52bvB6ZpBS/JbYY7hmwHMPPEECgYApDAQId871Fe1aQmgOHcMcICUg5jDJJa9DE62OgZPrPEEHEDb2XCEdEll93Fi/VqwtaMAk/w7Ro7oPRpHeTJ3WQYIyzxKih9noaXBC7sHFAkbpTQXtRn4hkNRAKQ1dGn77jm1h/jSaDjWmFRAEFgX1mXs1Ybck6an7tm2ymQWLpQKBgBIdu6GxnfJvpMA64ZvesHMTSLY5/SfEDVql861u16xz0rT/BguB2AbVQ7z6oqrhC42uHSbxdhMSMOFTytTQBjnKva07a3LW1a2JOdwz5wcYVc6tp21R+J2C+V2HJ/64buu39cJe09xf5pGHHnDAquQ2LH1tfWiMCNYa/MsaCAcBAoGBANRr7qWIUpDyVMlKic/vrkTjm1mXVzRx0vJUr4/UBAzMb5KswTOhuF5FWHSRYSUsFSFiL7HWSaUnZj5VclF7sxPU2QtN7LvqBPunmo2QnozGfRxXHaLVIJaqVhYYCtLOA4tLmwH9HkFg0e+1fWY/L8dulWlTQ3uGf2FqGNL+umWj";

    /**
     * 大豐支付商戶客戶號
     */
    public static String TAIFUNG_PAY_MER_CUST_NO = "Tank Want1";


    /**
     * Bugly
     */
    public static String BUGLY_KEY = "222fdc9738";
    // public static String BUGLY_KEY = "";

    public static final String YOUTUBE_DEVELOPER_KEY = "takewant";


    public static final int ENV_28 = 1;
    public static final int ENV_29 = 2;
    public static final int ENV_PROD = 3;
    public static final int ENV_F3 = 4;
    public static int currEnv = ENV_PROD;

    /**
     * 定位数据有效时间(毫秒)
     */
    public static final int LOCATION_EXPIRE = 300 * 1000;
    public static boolean USE_DEVELOPER_TEST_DATA =false;


    public static void changeEnvironment(int env) {
        currEnv = env;
        Hawk.put(SPField.FIELD_CURRENT_ENV, currEnv);
        //將環境值存入mmkv中，供mvvm架構的網絡調用
        MmkvUtils.putIntValue("env",env);
        if (env == ENV_28 || env == ENV_229 || env == ENV_29) {
            PROD = false;
            DEVELOPER_MODE = true;
            USE_28 = env == ENV_28;
            SLOGENABLE = true;

        } else {
            //User.logout();
            PROD = true;
            DEVELOPER_MODE = false;
            USE_F3 = env == ENV_F3;
        }
        // 設置MPay SDK環境, 默認 UAT 環境 // 0 ：生產，1：測試環境，2 :UAT
        ConstantBase.setMPayPackageName(Config.DEVELOPER_MODE ?
                ConstantBase.ConnectUrl_UAT
                : ConstantBase.ConnectUrl_PRD);
        MPaySdk.setEnvironmentType(Config.DEVELOPER_MODE ?
                ConstantBase.ConnectUrl_UAT
                : ConstantBase.ConnectUrl_PRD);


        BASE_URL = DEVELOPER_MODE ?
                (USE_28 ? "http://192.168.5.28" : env == ENV_29 ? "https://192.168.5.29/api" : "https://192.168.5.229/api")
                : (USE_F2 ? "https://www.twant.com" : "https://www.twant.com");
//        BASE_URL = DEVELOPER_MODE ?
//                (USE_28 ? "http://192.168.5.28" : "http://120.196.113.116:8001/api")
//                : (USE_F2 ? "https://www.twant.com" : "https://www.twant.com");
        OSS_BASE_URL = DEVELOPER_MODE ?
                "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com"
                : "https://img.twant.com";

        API_BASE_URL = DEVELOPER_MODE ?
                (USE_28 ? "http://192.168.5.28/api" : env == ENV_29 ? "https://192.168.5.29/api" : "https://192.168.5.229/api")
                : (USE_F2 ? (USE_F3 ? "https://f1.twant.com/api" : "https://f2.twant.com/api") : "https://www.twant.com/api");
//        API_BASE_URL = DEVELOPER_MODE ?
//               (USE_28 ? "http://192.168.5.28/api" : "http://120.196.113.116:8001/api")
//                : (USE_F2 ? "https://f2.twant.com/api" : "https://www.twant.com/api");

        WEB_BASE_URL = DEVELOPER_MODE ?
                (USE_28 ? "http://192.168.5.28/web" : env == ENV_29 ? "https://192.168.5.29/web" : "https://192.168.5.229/web")
                : (USE_F2 ? "https://www.twant.com/web" : "https://www.twant.com/web");

        MOBILE_WEB_BASE_URL = DEVELOPER_MODE ?
                (USE_28 ? "http://192.168.5.28" : "https://192.168.5.29")
                : "https://www.twant.com";


        TAIFUNG_PAY_SERVER_URL = DEVELOPER_MODE ?
                "https://test.taifungbank.com/payment-web/"
                : "https://epay.taifungbank.com/payment-web/";


        TAIFUNG_PAY_SECRET_KEY = DEVELOPER_MODE ?
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/KuuLqkSm6ti6jEmqwbU6HNFMQhfYucxdTBJTr90P+neNOge0dVDLIqnazTAWAwW+d57qFk5uU5p7naP+mejOmOEd7+TLcDfy4wzmtmFFSC1TGa+8UaVhEpoiUltLViAEFgxJ5VP1VcRR2RBWMxKflUfoAvzQBs5x+t7OozYmHbV2AwmjngJy6KIjXe6pqHpWzWYy9WW9rJnLL2obUq7btzghogQy3S+R3axALoGiAGL0Vec3mreTY0RmyEXVeDVBUgd7mq2IIh9W2/x47uAz/61++udb3pD9W1Ue48nQhkhJ9Ow4g7PRXIibHL1lK6SMtFLi/hsa/XWziw3CRyhRAgMBAAECggEAU3WliEA54LW/ERKWRtpzCH/0UFq6ln/nXQQNLEQnOwaakym2m25sa4MirMfQKov/QfxvgLtkWn5df4J/SnRfU3MjNTK6rKa9hmjiBQeyx9CPGSypsInkrdC1Qi66dNWQ/Lezfb+FPCLJpwIhQ8DgbJN75SsIvLl7//8KryRKS7EDMP4mEw25f9F7eKv0HdqMSlO3gR7vkUvz/xRymo+SABFjMUYP5mIJMe3h/c/uJX+3Ege3U+RP5aETvDq+toDpAkEwsBk0F64JeAVpi236Ho7pPcaehnTL9c2pjvOHbwaJsK3HGrU49zS9oBH3TXIrmaqzmpjwiaebUxPBFWex0QKBgQDriHXBTZY9kPsQwXnY3BsWujov28cyV7H5pu0PAG4ijHqG74Q1Llo5AYCqjO008UnHURGhyOuWTYXs8VUWNjwjok1y5kn8+dyGAb+9QU1lCT8+/WfbxPJEleCcKNlvv6qtJoo6M65V9X4a+gmdPOnbyLlZc15V3g+jCr0NdnVwUwKBgQDPx4gjI+1jMzbe2MCA8IfAQxD1hNNclvFhYEYQGYySwTso7CxUHRTX9uhVqhNrEADUYj+ka5wFO4xyJVThzh8w+t55cYt9VWJBfMZWwlFxUfen+jiRSc2tVXaBTa2sG4qJgFSLOBSlE1OqiP4D/xYTh9V94VJLbJlQ3SL/v7vASwKBgAUf+P/1wjkguHXK3+3aDDTYZH+6FoF/6v11pl7XMY5K5Defao8FrSzkXXpYiqjGP0a4+ts8VfP1R965+ZH8KB7WXz0Kyb1ZanT4AMYLb7WtF9U1Cld715GqeTKsqN6Hmx0dY5CUo0x3hQDtQ9xKAQSpP2801W6k4E4545cxZqjFAoGASaohLuw25nuq0XkhTtV4G4brhVAxK1tseqyKSnz0ZLdTRR/uW2fwEt0749snhUaoNKQckiuApi5FjdaEcIYGcvQOWhoSbT4PVs5o9ytvenCoEArbcU8sN27cU915XFJrXHJ+Btm8IAZpHEXzYPFYH1aCL13qMklvVccA+JE+fF8CgYEA1maDuZLKV2wcrx2MNkbSanpjo8Tvnl8XuM/Kec9Aoj2dxg+YmC/5EIZVjRwWhkzv07whxZrHczOYZp/og7WuTtq/9rFWQ9bFkPYhXihlBQjftf6ZhWIQ046SpmwTcbeCQ64ocULHkwQcjSRRI63XpW3Yr2xsWHNAwFPzrEyIW9o="
                : "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDDPgas+09RfZldLzVSy/hNXz1hhNuVogs8IqRz2Z40MjzGEyCqot85ZEQyJkRfmWDwBKrmEp6TA5nq6oXfnLWEtPgpfcVDuKsUzvhxnxrWdDtyLbZxKjsxVfkWJ+7Lhh5NiOElnXtLjhBRhCHDjBF6hE18XRT4IC8x0rlxLvTFU0geBtS7OUj5DkX6lBzmNrexYmiD/6WO+e11Ti8mi9fntEZ7B2T/qxf1jss2x1Q+bp2PqkbNW9jE30F0BEfkkGpAoixS7LolmOGMd5o7jgqNQGjsV5KFppeSMYz+IWX4oY6TtD21Fd8OJlq3Qa2O6q9PTfq6m/T5+icjifzB7optAgMBAAECggEBALzXFWXifMl5DglPS+gVWMidQaU895Y28V7ssttKtPYPUTDT91iTyAyeqkdHNAKzO0treFCn2bCgXxMUWv/5dqgw0YZwG4hw6ShrN2nOVgruyUCabkfubOo/GXkQjqtTwaOErR/QvhxKAAaYIoAczhsONFQBQ/LqTjuGcc7DeAQR5DDzRl4N6UKh+WHikLemMaPma/ecQyQAHjFffCfEmzCpefxgSrphNNaVVtCPLdHbMbkT2PzeMw2CuzYVPilN0u5u0RLmUc0TRqtU5GOSHNvSSIIYcrxQk2HrxSe7wx+NklM+/FQ5ckA0MwAHcAp95WeM5zCXRihwq5/6K1QAdAECgYEA65SX66PkCpn7YstM7jfCtF1hQO6S1ACjC+FvW7Igb49Ix0kyqCpQmZn2XVkg1iGSjamiA8sWaXhcEu+AxUBSkMQKOuZz2fGd8jhgOs4LVrHHc0xdC2uqAL0qXwo8FfeeB/fFXKXHG2Q6S5yv4O2vVCuCssm8Zih2q8gp/a9AMy0CgYEA1CpZo0hDhmLGuBGmpRGU2Y9z5oFYGPEL1bKz2VJlKPdcckN6iN6sxYDCEdvAko9kCTnFTyuZAI+kqsIoSPW1bI5XGdn4PB/dEPe86Kh/IXUnTBkqaABFgwAhsVbOUCsTxQbWkYCPPjR4Gvn1vy52bvB6ZpBS/JbYY7hmwHMPPEECgYApDAQId871Fe1aQmgOHcMcICUg5jDJJa9DE62OgZPrPEEHEDb2XCEdEll93Fi/VqwtaMAk/w7Ro7oPRpHeTJ3WQYIyzxKih9noaXBC7sHFAkbpTQXtRn4hkNRAKQ1dGn77jm1h/jSaDjWmFRAEFgX1mXs1Ybck6an7tm2ymQWLpQKBgBIdu6GxnfJvpMA64ZvesHMTSLY5/SfEDVql861u16xz0rT/BguB2AbVQ7z6oqrhC42uHSbxdhMSMOFTytTQBjnKva07a3LW1a2JOdwz5wcYVc6tp21R+J2C+V2HJ/64buu39cJe09xf5pGHHnDAquQ2LH1tfWiMCNYa/MsaCAcBAoGBANRr7qWIUpDyVMlKic/vrkTjm1mXVzRx0vJUr4/UBAzMb5KswTOhuF5FWHSRYSUsFSFiL7HWSaUnZj5VclF7sxPU2QtN7LvqBPunmo2QnozGfRxXHaLVIJaqVhYYCtLOA4tLmwH9HkFg0e+1fWY/L8dulWlTQ3uGf2FqGNL+umWj";
    }
}
