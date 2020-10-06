package com.ftofs.lib_net.smart.net_utils;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * created 王志强 2020.04.30
 **/
public class RetrofitUtil<T> {
    private Retrofit retrofit;

    private RetrofitUtil() {
       changeBaseApiUrl("https://f2.twant.com/api/");
    }

    public void changeBaseApiUrl(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        if(retrofit!=null){if(retrofit.baseUrl().equals(baseUrl)) return;}
        OkHttpClient okHttpClient = OkHttpUtil.getInstance().okHttpsCacheClient();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonUtil.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

    }
    public  Retrofit getRetrofit(){
        return retrofit;
    }


    public static RetrofitUtil getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final RetrofitUtil instance = new RetrofitUtil();
    }

}
