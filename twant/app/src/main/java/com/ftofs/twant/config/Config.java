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
            "http://192.168.5.29/api"
            : "https://www.twant.com/api";

    public static final String WEB_BASE_URL = DEVELOPER_MODE ?
            "http://192.168.5.29/web"
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
            : "-----BEGIN RSA PRIVATE KEY-----\n" +
            "Proc-Type: 4,ENCRYPTED\n" +
            "DEK-Info: DES-EDE3-CBC,6119E5F83D9BC69A\n" +
            "\n" +
            "8KChu/8udBabMxK61xTCrGcf0oxXR9B3Gcf+CeSkDrkPqzXJUZwK4+6uOEjSmUdo\n" +
            "B8lmXWcKd0JlOHhlzYXr+qeBltiwdkt9m/Bmw1Cjy64J8lGt/76Uo82C2N7G0zkq\n" +
            "Ips+PSTVoL4mygZ9jiBzo2ykFfe5wgqpUq0nGAN6rSsprbmNlx1Kpfsq/hhx8Vxr\n" +
            "sDqwHqc6k3ljxJyGP0Dt+ug3gxHDClqIxlykKe75NdCCa+I0C6SqnfVj+WxL8XkI\n" +
            "jAoFQCKwUmhoMAZFS6rrEF6vchDRZI5WhVuq4M7xML6u8uLiAVAmLu7lKOqykX01\n" +
            "4U+e0bkIdQnsEmMzGAk21A1/JNRuDlUEUJyFXd7HIPZDW8YcXudbLuQN072hBUo+\n" +
            "HDMSF+zOvYUdzEJ3P8TCeqblh4Cw08t1/ljHO/xK1fSSkZjVg1c7wo92BQU4+ipm\n" +
            "tpuBZNpLz542moR2OrjAkqvI5b/rHhQUhVT8YRXM03jrgKLlQ/MOtNngHmvwfUD+\n" +
            "wsxAo6ATS6xO8TjZUSHMfoG0bv+K9fWZjQc7wJGjFeptjYlqM9gI6s6XbsmhvKtg\n" +
            "JFHN7UY2VrXsnVBtbKfXRP1sMbx+x7RvqbB4fkBKrXwoqgkGR+qo1Ia73ocqb84e\n" +
            "CagnSHAv3JvIIeSTfuLtnw0kpV8HCm9Sc3d+CiFwWFRLrcNsTxqEBEoomAt7Zp/T\n" +
            "v8zKBaUj8OsIJzWS5vwEZmWIah5a/BerGZRCKHmKskYV6MiavYZxiUBPQeF/fmi7\n" +
            "bH/QtuO0vLZAUuWvAU+c690z+GzxfnmEz+dn9AUxTHGNouaAe/fdPqlaDjaetXON\n" +
            "VfDkV5l0JEjsW1+98h7aQoTfdvEMLiwX8r0r5MQfNXJ9u8C8i4HM3tq1VJ8Gz1ky\n" +
            "A6leK9rg2a0/+pNMLva62YMm4MldEmO9In8yTAiuklT2oyufEr14gpor1CMMMYFi\n" +
            "uZSZv/paPzSYHk9782wqfT4Vo8RiUjvaPorDgIGgyqDio50BAOZUJWTlPTHWlntN\n" +
            "N2FYv/xd314Z8mSMdLLomMXmqNXcS8K5n3wkGHpq9hCHu9yj6yeHGwThIDqHe0v7\n" +
            "I5EfyNVpKcWvglRq/Chca6WxfSIPL77cGnsqCoiKrj0mS5athOIMWgKMrTMiVU0w\n" +
            "4iyCqesSQoUn64bH+nJXvYwCiVIva1FOskhatuRAlZRMw3Lxu3MXohGPx1GcTcAg\n" +
            "C+HIX0+tNYslmbYOjAV4eEMG01WD82D5EVLQvdRmYkaTx25gxcfYe66oBCAmcJh6\n" +
            "1SwB6HHDNSwJOAPaaDza2TOfY4oijKVPTI344iqaeTm+XYmg9WOy9V9oAbuv76wK\n" +
            "5G4Xf9JnCjI+5Lr2VdCz4gCh4HLkqvF2Cp2t09f6rtYOLliwC20LwZnZx/Sp1oDI\n" +
            "dy5mFv33CQQjHFfPoYrIjh1m28DdZTNYPJGwYEmKd9rZD78BwCy5IV5p+sdtEp7y\n" +
            "S8V7j8eJZLeYHJM4Gn/yTmNpW1I4VPDluas5J0qMTZmtBFwW3hoCOHJFG9JwHfvS\n" +
            "gfmF6JortWk2l0C1IjgjFy/U2ls5ScjwWq+WLwtM+ZonpKBbDD4tFGEQrdocWWPI\n" +
            "-----END RSA PRIVATE KEY-----";

    /**
     * 大豐支付商戶客戶號
     */
    public static final String TAIFUNG_PAY_MER_CUST_NO = "Tank Want1";


    /**
     * Bugly
     */
    public static String BUGLY_KEY = "222fdc9738";

}
