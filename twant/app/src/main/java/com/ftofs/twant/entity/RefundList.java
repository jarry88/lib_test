package com.ftofs.twant.entity;

import com.ftofs.twant.domain.ApiPageEntity;
import com.ftofs.twant.domain.ApiResultEntity;
import com.ftofs.twant.vo.refund.RefundItemVo;

import java.util.List;

public class RefundList {
    public ApiResultEntity apiResultEntity;
    public ApiPageEntity apiPageEntity;
    public List<RefundItemVo> refundItemVoList;

    public ApiResultEntity getApiResultEntity() {
        return apiResultEntity;
    }

    public void setApiResultEntity(ApiResultEntity apiResultEntity) {
        this.apiResultEntity = apiResultEntity;
    }

    public ApiPageEntity getApiPageEntity() {
        return apiPageEntity;
    }

    public void setApiPageEntity(ApiPageEntity apiPageEntity) {
        this.apiPageEntity = apiPageEntity;
    }

    public List<RefundItemVo> getRefundItemVoList() {
        return refundItemVoList;
    }

    public void setRefundItemVoList(List<RefundItemVo> refundItemVoList) {
        this.refundItemVoList = refundItemVoList;
    }
}
