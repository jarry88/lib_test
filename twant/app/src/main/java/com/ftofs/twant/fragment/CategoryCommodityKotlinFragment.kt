package com.ftofs.twant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR

import com.ftofs.twant.databinding.PageCategoryCommodityBinding
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.CategoryCommodityViewModel

class CategoryCommodityKotlinFragment:BaseTwantFragmentMVVM<PageCategoryCommodityBinding, CategoryCommodityViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return  R.layout.page_category_commodity
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
    }

    override fun onSupportVisible() {
        viewModel.getCategoryData()
    }
}