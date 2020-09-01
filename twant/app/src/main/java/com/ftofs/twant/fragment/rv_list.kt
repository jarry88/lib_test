package com.ftofs.twant.fragment

import androidx.databinding.ViewDataBinding
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CategoryHeadItemBinding
import com.ftofs.twant.databinding.SimpleGoodDisplayItemBinding
import com.ftofs.twant.entity.CategoryCommodity
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter

class TestBindAdapter():DataBoundAdapter<CategoryCommodity,SimpleGoodDisplayItemBinding>() {
    override val layoutId: Int
        get() = R.layout.simple_good_display_item

    override fun initView(binding: SimpleGoodDisplayItemBinding, item: CategoryCommodity) {
        binding.vo=item
    }

    override fun initHeadView(binding: ViewDataBinding) {
        val head=binding as CategoryHeadItemBinding
        head.headerTitle.text="mHeadText"
    }
}