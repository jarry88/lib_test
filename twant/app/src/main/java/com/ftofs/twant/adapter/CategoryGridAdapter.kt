package com.ftofs.twant.adapter

import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.twant.R
import com.ftofs.twant.constant.SearchType
import com.ftofs.twant.databinding.SimpleGoodDisplayItemBinding
import com.ftofs.twant.fragment.SearchResultFragment
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.Util
import com.ftofs.lib_net.model.CategoryNavVo

class CategoryGridAdapter:DataBoundAdapter<CategoryNavVo,SimpleGoodDisplayItemBinding>() {
    override val layoutId: Int
        get() = R.layout.simple_good_display_item

    override fun initView(binding: SimpleGoodDisplayItemBinding, item: CategoryNavVo) {
        binding.vo=item
        binding.root.setOnClickListener {
            Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name,
                    EasyJSONObject.generate("cat", item.categoryId.toString()).toString())) }
    }
}