package com.ftofs.twant.kotlin

import com.ftofs.twant.databinding.LayoutZoneItemBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter

class ZoneAdapter(override val layoutId: Int) :DataBoundAdapter<ZoneItem,LayoutZoneItemBinding>() {
    override fun initView(binding: LayoutZoneItemBinding, item: ZoneItem) {
        binding.vo=item
    }
}