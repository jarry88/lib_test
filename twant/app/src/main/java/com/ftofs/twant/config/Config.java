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
    public static final int DB_VERSION = 11;

    /**
     * 登錄有效期(秒)
     */
    public static final int LOGIN_VALID_TIME = 10 * 24 * 3600;

    /**
     * 開發模式
     */
    public static final boolean DEVELOPER_MODE = true;

    public static final String OSS_BASE_URL = DEVELOPER_MODE ?
            "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com"
            : "https://img.twant.com";

    public static final String API_BASE_URL = DEVELOPER_MODE ?
            "http://192.168.5.28/api"
            : "https://www.twant.com/api";

    public static final String WEB_BASE_URL = DEVELOPER_MODE ?
            "http://192.168.5.28/web"
            : "http://www.twant.com/web";


    /**
     * 大豐支付請求的服務器URL
     */
    public static final String TAIFUNG_PAY_SERVER_URL = DEVELOPER_MODE ?
            "https://test.taifungbank.com/payment-web/"
            : "https://epay.taifungbank.com/payment-web/";

    /**
     * 大豐支付私鑰
     */
    public static final String TAIFUNG_PAY_SECRET_KEY = DEVELOPER_MODE ?
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/KuuLqkSm6ti6jEmqwbU6HNFMQhfYucxdTBJTr90P+neNOge0dVDLIqnazTAWAwW+d57qFk5uU5p7naP+mejOmOEd7+TLcDfy4wzmtmFFSC1TGa+8UaVhEpoiUltLViAEFgxJ5VP1VcRR2RBWMxKflUfoAvzQBs5x+t7OozYmHbV2AwmjngJy6KIjXe6pqHpWzWYy9WW9rJnLL2obUq7btzghogQy3S+R3axALoGiAGL0Vec3mreTY0RmyEXVeDVBUgd7mq2IIh9W2/x47uAz/61++udb3pD9W1Ue48nQhkhJ9Ow4g7PRXIibHL1lK6SMtFLi/hsa/XWziw3CRyhRAgMBAAECggEAU3WliEA54LW/ERKWRtpzCH/0UFq6ln/nXQQNLEQnOwaakym2m25sa4MirMfQKov/QfxvgLtkWn5df4J/SnRfU3MjNTK6rKa9hmjiBQeyx9CPGSypsInkrdC1Qi66dNWQ/Lezfb+FPCLJpwIhQ8DgbJN75SsIvLl7//8KryRKS7EDMP4mEw25f9F7eKv0HdqMSlO3gR7vkUvz/xRymo+SABFjMUYP5mIJMe3h/c/uJX+3Ege3U+RP5aETvDq+toDpAkEwsBk0F64JeAVpi236Ho7pPcaehnTL9c2pjvOHbwaJsK3HGrU49zS9oBH3TXIrmaqzmpjwiaebUxPBFWex0QKBgQDriHXBTZY9kPsQwXnY3BsWujov28cyV7H5pu0PAG4ijHqG74Q1Llo5AYCqjO008UnHURGhyOuWTYXs8VUWNjwjok1y5kn8+dyGAb+9QU1lCT8+/WfbxPJEleCcKNlvv6qtJoo6M65V9X4a+gmdPOnbyLlZc15V3g+jCr0NdnVwUwKBgQDPx4gjI+1jMzbe2MCA8IfAQxD1hNNclvFhYEYQGYySwTso7CxUHRTX9uhVqhNrEADUYj+ka5wFO4xyJVThzh8w+t55cYt9VWJBfMZWwlFxUfen+jiRSc2tVXaBTa2sG4qJgFSLOBSlE1OqiP4D/xYTh9V94VJLbJlQ3SL/v7vASwKBgAUf+P/1wjkguHXK3+3aDDTYZH+6FoF/6v11pl7XMY5K5Defao8FrSzkXXpYiqjGP0a4+ts8VfP1R965+ZH8KB7WXz0Kyb1ZanT4AMYLb7WtF9U1Cld715GqeTKsqN6Hmx0dY5CUo0x3hQDtQ9xKAQSpP2801W6k4E4545cxZqjFAoGASaohLuw25nuq0XkhTtV4G4brhVAxK1tseqyKSnz0ZLdTRR/uW2fwEt0749snhUaoNKQckiuApi5FjdaEcIYGcvQOWhoSbT4PVs5o9ytvenCoEArbcU8sN27cU915XFJrXHJ+Btm8IAZpHEXzYPFYH1aCL13qMklvVccA+JE+fF8CgYEA1maDuZLKV2wcrx2MNkbSanpjo8Tvnl8XuM/Kec9Aoj2dxg+YmC/5EIZVjRwWhkzv07whxZrHczOYZp/og7WuTtq/9rFWQ9bFkPYhXihlBQjftf6ZhWIQ046SpmwTcbeCQ64ocULHkwQcjSRRI63XpW3Yr2xsWHNAwFPzrEyIW9o="
            : "";

    /**
     * 大豐支付商戶客戶號
     */
    public static final String TAIFUNG_PAY_MER_CUST_NO = "Tank Want1";


    /**
     * Bugly
     */
    public static String BUGLY_KEY = "222fdc9738";

}
