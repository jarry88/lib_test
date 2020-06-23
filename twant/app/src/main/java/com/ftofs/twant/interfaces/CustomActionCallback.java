package com.ftofs.twant.interfaces;

import com.ftofs.twant.entity.CustomActionData;


/**
 * 比SimpleCallback更規範的回調接口
 * @author zwm
 */
public interface CustomActionCallback {
    void onCustomActionCall(CustomActionData customActionData);
}
