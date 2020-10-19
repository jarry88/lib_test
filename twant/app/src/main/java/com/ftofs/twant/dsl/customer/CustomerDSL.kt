package com.ftofs.twant.dsl.customer

import androidx.databinding.ViewDataBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter


fun <T,V: ViewDataBinding>factoryAdapter(resId:Int, initAdapter:(V, T)->Unit) =object : DataBoundAdapter<T, V>(){
        override val layoutId: Int
            get() = resId
        override fun initView(binding: V, item: T) =initAdapter(binding,item)
    }