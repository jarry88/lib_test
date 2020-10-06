package com.ftofs.twant.kotlin

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ftofs.twant.R
import com.ftofs.lib_net.model.SellerGoodsItem
import com.wzq.mvvmsmart.utils.GlideLoadUtils

/**
 * author : 谷志鹏
 * date   : 2020/07/09 11:34
 */
class SellerGoodsListAdapter0(layoutResId: Int, data: List<SellerGoodsItem>?) : BaseQuickAdapter<SellerGoodsItem, BaseViewHolder>(layoutResId, data as MutableList<SellerGoodsItem>?) {
    override fun convert(helper: BaseViewHolder, item: SellerGoodsItem) {
//        helper.setText(R.id.iv2, item.news_title)
//        GlideLoadUtils.loadRoundCornerImg(helper.getView(R.id.image_view), item.imageName, R.mipmap.ic_launcher, 10)
        helper.addOnClickListener(R.id.image_view)
    }

}