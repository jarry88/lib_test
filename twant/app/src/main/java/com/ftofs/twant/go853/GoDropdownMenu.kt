package com.ftofs.twant.go853

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ftofs.twant.R
import com.ftofs.twant.dsl.*
import com.gzp.lib_common.utils.SLog
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.util.XPopupUtils

class GoDropdownMenu @JvmOverloads constructor(private val mContext: Context, val stringList:List<String> = listOf(), var selectText:String="",val fullWidth:Boolean=true,val selectCall:(String)->Unit={s -> SLog.info(s)}) : AttachPopupView(mContext) {
    override fun getImplLayoutId(): Int {
        return R.layout.simple_popup_list
    }

    override fun onCreate() {
        super.onCreate()
        stringList.takeIf { it.isEmpty() }?.let { dismiss() }
        val rvList =findViewById<RecyclerView>(R.id.rv_list)!!
        rvList.layoutManager=LinearLayoutManager(mContext)
        rvList.adapter=object :BaseQuickAdapter<String,BaseViewHolder>( R.layout.wrapt_list_popup_item,stringList){
            override fun convert(helper: BaseViewHolder, s: String?) {
                val textView = helper.getView<TextView>(R.id.tv_text)
                textView.apply {
                    text =s
                    s?.takeIf { it == selectText }?.let {
                        textView.setTextColor(resources.getColor(R.color.tw_blue))
                    }?:textView.setTextColor(resources.getColor(R.color.tw_black))
                }
                helper.itemView.setOnClickListener { s?.let {
                    selectText=it
                    notifyDataSetChanged()
                    selectCall(it) }  }
            }

            override fun setOnItemClick(v: View?, position: Int) {
                selectCall(stringList.get(position))
            }
        }
    }

    override fun getMaxWidth(): Int {
        return XPopupUtils.getWindowWidth(context)
    }
}
