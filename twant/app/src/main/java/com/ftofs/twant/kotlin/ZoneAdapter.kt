package com.ftofs.twant.kotlin

import android.view.View
import com.ftofs.twant.databinding.LayoutZoneItemBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.tangram.NewShoppingSpecialFragment
import com.ftofs.twant.util.Util

class ZoneAdapter(override val layoutId: Int) :DataBoundAdapter<ZoneItem,LayoutZoneItemBinding>() {
    override fun initView(binding: LayoutZoneItemBinding, item: ZoneItem) {
        binding.vo=item
        binding.btnGoZone.setOnClickListener { Util.startFragment(NewShoppingSpecialFragment.newInstance(item.zoneId)) }
        val id = item.zoneId
        if (id < 0) {
            binding.llContainer.visibility = View.GONE
            binding.imageZone.visibility = View.INVISIBLE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.llContainer.visibility = View.VISIBLE
            binding.imageZone.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }
}