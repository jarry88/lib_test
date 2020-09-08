package com.ftofs.twant.kotlin.net;

import android.annotation.SuppressLint;

import com.ftofs.twant.entity.SellerGoodsItem;
import com.ftofs.twant.kotlin.bean.NewsData;
import com.ftofs.twant.kotlin.vo.SellerPageVO;
import com.wzq.mvvmsmart.net.base.BaseRequest;
import com.wzq.mvvmsmart.net.base.BaseResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * author :王志强
 * date   : 2019/11/12 11:10
 */
public class MRequest extends BaseRequest {

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
        super();
        this.service = retrofit.create(DemoApiService.class);
        retrofit.baseUrl();
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
    public Observable<BaseResponse<ArrayList<NewsData>>> doPostServerNews(String jsonParams) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Observable<BaseResponse<ArrayList<NewsData>>> observable = service.doPostServerNews(requestBody);
        return observable;
    }
    // get 商品列表请求
    public Observable<BaseResponse<SellerPageVO<SellerGoodsItem>>> doSellerGoodsList(Map<String,Object> params) {

        Observable<BaseResponse<SellerPageVO<SellerGoodsItem>>> observable = service.doSellerGoodsList(params);
        return observable;
    }
}
