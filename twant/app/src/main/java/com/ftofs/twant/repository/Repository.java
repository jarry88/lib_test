package com.ftofs.twant.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ftofs.twant.api.HttpHelper;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.Uri;
import com.ftofs.twant.domain.ApiPageEntity;
import com.ftofs.twant.entity.RefundList;
import com.ftofs.twant.entity.ReturnList;
import com.ftofs.twant.util.User;
import com.ftofs.twant.vo.refund.RefundItemVo;

import java.util.List;

public class Repository {
    /**
     * 注入Token參數
     * @param url
     * @return
     */
    private static String UrlWrap(String url) {
        return url.concat(String.format("?token=%s&clientType=%s", User.getToken(), Constant.CLIENT_TYPE_ANDROID));
    }

    /**
     * 獲取返回JSON根部對象
     * @param json
     * @return
     */
    private static JSONObject getJSONRoot(String json){
        return JSON.parseObject(json).getJSONObject("datas");
    }

    /**
     * 退款列表
     */
    public static RefundList getRefundList() throws Exception {
        String json = HttpHelper.post(UrlWrap(Uri.API_REFUND_LIST));

        RefundList refundList = new RefundList();
        List<RefundItemVo> refundItemVoList = getJSONRoot(json).getJSONArray("refundItemVoList").toJavaList(RefundItemVo.class);
        ApiPageEntity apiPageEntity = JSON.parseObject(getJSONRoot(json).getString("pageEntity"), ApiPageEntity.class);
        refundList.setRefundItemVoList(refundItemVoList);
        refundList.setApiPageEntity(apiPageEntity);

        return refundList;
    }

    /**
     * 退貨列表
     */

    public static ReturnList getReturnList() throws Exception {
        String json = HttpHelper.post(UrlWrap(Uri.API_RETURN_LIST));

        ReturnList returnList = new ReturnList();
        List<RefundItemVo> refundItemVoList = getJSONRoot(json).getJSONArray("refundItemVoList").toJavaList(RefundItemVo.class);
        ApiPageEntity apiPageEntity = JSON.parseObject(getJSONRoot(json).getString("pageEntity"), ApiPageEntity.class);
        returnList.setRefundItemVoList(refundItemVoList);
        returnList.setApiPageEntity(apiPageEntity);

        return returnList;
    }
}
