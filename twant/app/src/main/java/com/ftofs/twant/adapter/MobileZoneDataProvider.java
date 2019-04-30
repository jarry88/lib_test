package com.ftofs.twant.adapter;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.entity.MobileZoneReadyMsg;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;

public class MobileZoneDataProvider {
    private static List<MobileZone> mobileZoneList = null;

    public List<MobileZone> getMobileZoneList() {
        if (mobileZoneList != null && mobileZoneList.size() > 0) {
            return mobileZoneList;
        }

        TwantApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseStr = Api.syncGet(Api.PATH_MOBILE_ZONE, null);
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (responseObj == null) {
                        return;
                    }

                    EasyJSONArray adminMobileAreaList = responseObj.getArray("datas.adminMobileAreaList");

                    for (Object object : adminMobileAreaList) {
                        final MobileZone mobileZone = (MobileZone) EasyJSONBase.jsonDecode(MobileZone.class, object.toString());
                        SLog.info("mobileZone: %s", mobileZone);
                        mobileZoneList.add(mobileZone);
                    }

                    SLog.info("获取MobileZone数据成功");
                    EventBus.getDefault().post(new MobileZoneReadyMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }
}
