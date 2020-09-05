package com.ftofs.twant.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ftofs.twant.R
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.databinding.MoonVoucherListItemBinding
import com.ftofs.twant.entity.StoreVoucher
import com.ftofs.twant.interfaces.OnConfirmCallback
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.ftofs.twant.tangram.NewShoppingSpecialFragment
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.TwConfirmPopup
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.util.XPopupUtils
import kotlinx.coroutines.*

@SuppressLint("ViewConstructor")
class MoonVoucherListPopup(context: Context, private val voucherList:List<StoreVoucher>):CenterPopupView(context),CoroutineScope by MainScope(){
    val repository by lazy { BaseRepository() }
    val adapter =object :DataBoundAdapter<StoreVoucher,MoonVoucherListItemBinding>(){
        override val layoutId: Int
            get() = R.layout.moon_voucher_list_item

        override fun initView(binding: MoonVoucherListItemBinding, item: StoreVoucher) {
            binding.vo=item
            binding.root.setOnClickListener {
                if (item.memberIsReceive == Constant.TRUE_INT) {
                    XPopup.Builder(context).asCustom(TwConfirmPopup(context,"領取成功","前往【想要中秋】月餅優惠專場",object:OnConfirmCallback{
                        override fun onYes() {
                            dismiss()
                            Util.startFragment(NewShoppingSpecialFragment.newInstance(20))
                        }

                        override fun onNo() {
                            dismiss()
//                            TODO("Not yet implemented")
                        }

                    })).show()
                } else {
                    launch {
                        try {
                           when(val result= repository.run {
                                simpleGet(api.getVoucherCommandReceive(User.getToken(),item.templateId.toString()))
                            }){
                               is Result.Success -> {
                                   ToastUtil.success(context,result.datas.success)
                                   getData()?.indexOf(item)?.let {
                                       item.memberIsReceive = 1
                                       notifyItemChanged(it) }
                               }
                               is Result.DataError -> ToastUtil.error(context,result.datas.error)
                               is Result.Error -> ToastUtil.error(context,"領取失敗")
                           }
                        }   catch (e:Exception){
                            SLog.info(e.printStackTrace().toString())
                        }
                    }

                }
            }
        }
    }
    override fun onCreate() {
        rvList.adapter=adapter
        voucherList.takeUnless { it.isNullOrEmpty() }?.let {
            if (it.size <= 3) {

            } else {
                rvList.apply {
                    layoutParams=layoutParams.apply {
                        height=Util.dip2px(context, (3.5*70).toFloat())
                    }
                }
            }
            adapter.addAll(it,true) }

    }
    val rvList: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rv_list) }

    override fun getImplLayoutId(): Int {
        return R.layout.popup_moon_voucher_list
    }
    override fun getMaxWidth(): Int {
        return (XPopupUtils.getWindowWidth(context) * 0.85f).toInt()
    }

    override fun onDismiss() {
        super.onDismiss()
        cancel()
    }
    //
//    override fun getPaddingTop(): Int {
//        return (XPopupUtils.getWindowWidth(context) * 0.25f).toInt()
//    }
}