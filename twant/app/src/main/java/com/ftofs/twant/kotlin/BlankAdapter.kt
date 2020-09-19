package com.ftofs.twant.kotlin

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.fastloan.app.ui.adapter.DataBoundViewHolder
import com.ftofs.twant.R
import com.ftofs.twant.databinding.VerificationGoodsItemBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.gzp.lib_common.utils.SLog
import com.ftofs.twant.vo.orders.OrdersGoodsVo

class BlankAdapter : DataBoundAdapter<OrdersGoodsVo, VerificationGoodsItemBinding>(){
    override val layoutId: Int
        get() = R.layout.verification_goods_item

    override fun onBindViewHolder(holder: DataBoundViewHolder<VerificationGoodsItemBinding>, position: Int) {
        SLog.info("newItem.goodsName $position")

        initView(holder.binding, mDiffer.currentList[position])
//            onItemClickListener?.let { holder.binding.root.apply {setOnClickListener { _->it.onClick(realPosition,this) }  } }


        holder.binding.executePendingBindings()//必须调用，否则闪屏
    }

    private val mDiffer by  lazy { AsyncListDiffer<OrdersGoodsVo>(this,object : DiffUtil.ItemCallback<OrdersGoodsVo>(){
        override fun areItemsTheSame(oldItem: OrdersGoodsVo, newItem: OrdersGoodsVo): Boolean {
            SLog.info(newItem.goodsName)
            return oldItem.goodsName == newItem.goodsName
        }

        override fun areContentsTheSame(oldItem: OrdersGoodsVo, newItem: OrdersGoodsVo): Boolean {
            SLog.info(newItem.goodsFullSpecs)

            return oldItem.goodsFullSpecs ==oldItem.goodsFullSpecs
        }

    }) }
    override fun initView(binding: VerificationGoodsItemBinding, item: OrdersGoodsVo) {
        binding.vo=item
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }
    fun submitList(list: List<OrdersGoodsVo>) {
        mDiffer.submitList(list)
    }
}