package com.ftofs.twant.entity;

import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.lib_net.model.Goods;
import com.gzp.lib_common.utils.SLog;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * @author gzp
 */
public class LinkageGoodsItem extends Goods implements MultiItemEntity {
    public static LinkageGoodsItem parse(EasyJSONObject object,int type){
        LinkageGoodsItem item=null;
        try {
            item = (LinkageGoodsItem) Goods.parse(object);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        return item;

    }
}
