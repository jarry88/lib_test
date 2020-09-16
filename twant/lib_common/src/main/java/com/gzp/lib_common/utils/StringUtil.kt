package com.gzp.lib_common.utils

import cn.snailpad.easyjson.EasyJSONObject

fun isEmpty(str: String?) :Boolean{
    return str.isNullOrEmpty()
}

/**
 * 詢價涉及頁面：首頁banner ，搜索結果列表頁，想要貼發表選擇商品頁，貼文詳情頁,店鋪首頁,店鋪產品頁，我的關注頁,商品詳情頁
 * 商品詳情頁規格
 * @param goods
 * @return
 * @throws Exception
 */
@Throws(Exception::class)
fun safeModel(goods: EasyJSONObject): Int {
    val key = "goodsModal"
    return if (goods.exists(key)) {
        goods.getInt(key)
    } else {
        SLog.info("警告！！！！！ 後端 沒有提供 goodsModel信息")
        if (Util.inDev()) {
            (4.6 + Math.random()).toInt()
        } else 0
    }
}
object StringUtil {

}