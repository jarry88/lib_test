package com.ftofs.twant.dsl.customer

import androidx.databinding.ViewDataBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.gzp.lib_common.utils.SLog


fun <T,V: ViewDataBinding>factoryAdapter(resId:Int, initAdapter:(V, T)->Unit) =object : DataBoundAdapter<T, V>(){
        override val layoutId: Int
            get() = resId
        override fun initView(binding: V, item: T) =initAdapter(binding,item)
    }
fun factoryParams( vararg args:Any?) =args.takeIf { it.size%2==0 }?.let {
    val params = mutableMapOf<String,Any?>()
    for( i in it.indices step 2 ){
        it[i+1]?.let {o ->
            params.put(it[i] as String , o)//.apply { SLog.info(params.toString()) }
        }
    }
    params.toMap().apply { SLog.info("params: ${toString()}") }
}?: mapOf()

val Double?.toMopString:String
    get() {
        return if(this==null) "-" else{
         "$${String.format("%.2f",this)}MOP"
        }
    }