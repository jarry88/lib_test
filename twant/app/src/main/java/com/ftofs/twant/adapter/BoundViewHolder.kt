package com.ftofs.twant.adapter

import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseViewHolder

class BoundViewHolder< out T : ViewDataBinding> constructor(val binding: T) :
       BaseViewHolder(binding.root)


//    fun addClickListener(vararg viewIds: Int): BoundViewHolder {
////        return super.addOnClickListener(*viewIds)
//        viewIds.forEach {
//            childClickViewIds.add(it)
//            getView<View>(it)?.apply{
//                if(!isClickable)isClickable=true
//                setOnClickListener {
//                    mAdapter.apply {
//                        var position=absoluteAdapterPosition
//                        if(position== RecyclerView.NO_POSITION){
//                            return@setOnClickListener
//                        }
//                        position -=headerLayoutCount
//                        onItemChildClickListener?.onItemChildClick(this,it,position)
//                    }
//                }
//            }
//        }
//        return this
//    }
