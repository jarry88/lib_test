package com.ftofs.twant.fragment

import androidx.databinding.ViewDataBinding
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CategoryHeadItemBinding
import com.ftofs.twant.databinding.SimpleGoodDisplayItemBinding
import com.ftofs.twant.entity.CategoryCommodity
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.vo.CategoryNavVo

//SimpleGoodDisplayItemBinding
class CategoryCommodityKotlinAdapter() :DataBoundAdapter<CategoryNavVo,SimpleGoodDisplayItemBinding> (){

    var mHeadText=""

    override fun initView(binding: SimpleGoodDisplayItemBinding, item: CategoryNavVo) {
        binding.vo=item
    }

    override fun initHeadView(binding: ViewDataBinding) {
        val head=binding as CategoryHeadItemBinding
        head.headerTitle.text=mHeadText
    }

    override val layoutId: Int
        get() = R.layout.simple_good_display_item
    override val headId: Int
        get() = R.layout.category_head_item
}