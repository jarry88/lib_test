package com.ftofs.twant.kotlin.adapter

import com.ftofs.twant.R
//import com.ftofs.twant.databinding.SellerGoodsItemBinding
import com.ftofs.twant.databinding.SellerGoodsItemUnswipeBinding
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.util.RouterUtil

class FeatureGoodAdapter : DataBoundAdapter<SellerGoodsItem, SellerGoodsItemUnswipeBinding>() {
//    private val dao = AppDataBase.get().historyDao()
//    var username = SP.getString(SP.KEY_USER_NAME)

    override val layoutId: Int
        get() = R.layout.seller_goods_item_unswipe

    override fun initView(binding: SellerGoodsItemUnswipeBinding, item: SellerGoodsItem) {
        binding.vo = item
        binding.root.setOnClickListener {
            RouterUtil.navWeb(item, it.context)
        }
    }
}