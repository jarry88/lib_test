package com.ftofs.lib_net;

import android.annotation.SuppressLint;

import com.wzq.mvvmsmart.net.net_utils.RetrofitUtil;
import com.wzq.mvvmsmart.utils.KLog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Retrofit;

/**
 * author :谷志鹏
 * date   : 2019/11/12 11:10
 */
public class MRequest {
    protected Retrofit retrofit;

    public DemoApiService service;

    public static MRequest getInstance() {
        return Holder.INSTANCE;
    }

    @NotNull
    public Object doScope(@Nullable String bean2String) {
        return null;
    }


    private static class Holder {

        @SuppressLint("StaticFieldLeak")
        static MRequest INSTANCE = new MRequest();
    }

    private MRequest() {
        this.retrofit = RetrofitUtil.getInstance().getRetrofit();
        this.service = retrofit.create(DemoApiService.class);
        KLog.INSTANCE.e(retrofit.baseUrl());
//        KLog.retrofit.baseUrl();
    }
    public void updateRetrofit(){
        this.retrofit = RetrofitUtil.getInstance().getRetrofit();

    }
    //下方為參考代碼，後面會逐步移除-----------------------
    //--------------------------------
    //-------------------------
//    // 获取列表条目
//    public Observable<BaseResponse<SellerGoodsItem>> demoGet(int what, int num) {
//        Observable<BaseResponse<SellerGoodsItem>> observable = service.demoGet(num);
//        return observable;
//    }
//
//    // 获取新闻列表
//    public Observable<BaseResponse<ArrayList<SellerGoodsItem>>> doGetServerNews(int pageNum) {
//        Observable<BaseResponse<ArrayList<SellerGoodsItem>>> observable = service.doGetServerNews(pageNum);
//        return observable;
//    }

    // post请求
//    public Observable<BaseResponse<ArrayList<NewsData>>> doPostServerNews(String jsonParams) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
//        Observable<BaseResponse<ArrayList<NewsData>>> observable = service.doPostServerNews(requestBody);
//        return observable;
//    }
//    // get 商品列表请求
//    public Observable<BaseResponse<SellerPageVO<SellerGoodsItem>>> doSellerGoodsList(Map<String,Object> params) {
//
//        Observable<BaseResponse<SellerPageVO<SellerGoodsItem>>> observable = service.doSellerGoodsList(params);
//        return observable;
//    }
}
