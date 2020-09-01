package com.ftofs.twant.kotlin

import android.view.View
import com.ftofs.twant.databinding.LayoutZoneItemBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.tangram.NewShoppingSpecialFragment
import com.ftofs.twant.util.Util

class ZoneAdapter(override val layoutId: Int, override val headId: Int) :DataBoundAdapter<ZoneItem,LayoutZoneItemBinding>() {
    override fun initView(binding: LayoutZoneItemBinding, item: ZoneItem) {
        binding.vo=item
        binding.btnGoZone.setOnClickListener { Util.startFragment(NewShoppingSpecialFragment.newInstance(item.zoneId)) }
    }
}