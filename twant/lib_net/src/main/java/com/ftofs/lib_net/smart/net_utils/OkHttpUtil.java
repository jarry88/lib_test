package com.ftofs.lib_net.smart.net_utils;



import com.ftofs.lib_net.smart.cookie.CookieJarImpl;
import com.ftofs.lib_net.smart.cookie.MemoryCookieStore;
import com.ftofs.lib_net.smart.https.SslUtils;
import com.ftofs.lib_net.smart.interceptor.HttpCommonInterceptor;
import com.ftofs.lib_net.smart.interceptor.TokenInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * created 王志强 2020.04.30
 **/
public class OkHttpUtil {

    private OkHttpClient okHttpClient;
    private MemoryCookieStore mStore = new MemoryCookieStore();

    public MemoryCookieStore getMemoryCookieStore() {
        return mStore;
    }

    private static class OkHttpClientHelperHolder {
        private static OkHttpUtil instance = new OkHttpUtil();
    }

    public static OkHttpUtil getInstance() {
        return OkHttpClientHelperHolder.instance;
    }

    /**
     * 设置带缓存的OkHttpClient
     */
    OkHttpClient okHttpsCacheClient() {
        if (okHttpClient == null) {
            okHttpClient = okHttpsBuilder().build();
        }
        return okHttpClient;
    }

    private OkHttpClient.Builder okHttpsBuilder() {
        SslUtils.SSLParams sslParams = SslUtils.getSslSocketFactory();// https证书认证,封装了认证方法,可根据自己公司进行调整;
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.cookieJar(new CookieJarImpl(mStore));
        okHttpClientBuilder.connectTimeout(NetConfig.OKHTTP_CONNECTTIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(NetConfig.OKHTTP_READTIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(NetConfig.OKHTTP_READTIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.addInterceptor(new HttpCommonInterceptor());
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager); // https的证书校验
        okHttpClientBuilder.addInterceptor(new TokenInterceptor());
        return okHttpClientBuilder;
    }

}
