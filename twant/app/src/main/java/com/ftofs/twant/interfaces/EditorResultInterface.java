package com.ftofs.twant.interfaces;

import com.ftofs.twant.seller.entity.SellerGoodsPicVo;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;

import java.util.List;
import java.util.Map;

/**
 * 【賣家】
 * 接收規格編輯結果的接口
 * @author zwm
 */
public interface EditorResultInterface {
    void setEditorResult(Map<String, SellerSpecPermutation> specValueIdStringMap, Map<Integer, List<SellerGoodsPicVo>> colorImageMap);
}

