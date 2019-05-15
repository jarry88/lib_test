package com.ftofs.twant.repository;

import com.ftofs.twant.api.HttpHelper;
import com.ftofs.twant.constant.Uri;
import com.ftofs.twant.domain.ApiResultEntity;

import cn.snailpad.easyjson.EasyJSONObject;

public class Repository {
    private static ApiResultEntity getApiResultEntity(String json) throws Exception {
        ApiResultEntity apiResultEntity = (ApiResultEntity) EasyJSONObject.jsonDecode(ApiResultEntity.class, json);
        return apiResultEntity;
    }

    public static ApiResultEntity getShoppingCartList() throws Exception {
        ApiResultEntity apiResultEntity = getApiResultEntity(HttpHelper.get(Uri.API_SHOPPING_CART_LIST));
        return apiResultEntity;
    }
}
