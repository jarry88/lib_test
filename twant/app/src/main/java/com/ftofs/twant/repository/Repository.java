package com.ftofs.twant.repository;

import com.alibaba.fastjson.JSON;
import com.ftofs.twant.api.HttpHelper;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.Uri;
import com.ftofs.twant.domain.ApiResultEntity;
import com.ftofs.twant.util.User;


public class Repository {
    private static String Token = User.getToken();

    private static String UrlWrap(String url) {
        return url.concat(String.format("?token=%s&clientType=%s", Token, Constant.CLIENT_TYPE_ANDROID));
    }

    private static ApiResultEntity getApiResultEntity(String json) throws Exception {
        ApiResultEntity apiResultEntity = JSON.parseObject(json, ApiResultEntity.class);
        return apiResultEntity;
    }

    public static ApiResultEntity getShoppingCartList() throws Exception {
        ApiResultEntity apiResultEntity = getApiResultEntity(HttpHelper.post(UrlWrap(Uri.API_SHOPPING_CART_LIST)));
        return apiResultEntity;
    }
}
