package com.ftofs.twant.widget

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.ftofs.twant.R
import com.ftofs.twant.kotlin.bean.ZoneInfo
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.lxj.xpopup.core.CenterPopupView
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


@SuppressLint("ViewConstructor")
class VerificationPopup(context: Context, val orderItem: OrdersGoodsVo) :CenterPopupView(context), CoroutineScope by MainScope(){
    override fun getImplLayoutId(): Int {
        return R.layout.popup_cancel_after_verfication
    }
    var verificationCode:String?=null
    override fun onCreate() {
        super.onCreate()
        val btnOk=findViewById<TextView>(R.id.btn_ok)
        btnOk.setOnClickListener{
            goVerify()
        }
    }
    fun goVerify(){
        val token =User.getToken()
        val api=object :BaseRepository(){
            suspend fun getIfoodmacauVerify(ordersId: Int, goodsId: Int, count: Int, verificationCode: String?): Result<Any> {
                return safeApiCall(call = {requestIfoodmacauVerify(ordersId,goodsId,count,verificationCode)})
            }


            private suspend fun requestIfoodmacauVerify(ordersId: Int, goodsId: Int, count: Int, verificationCode: String?): Result<Any> =
                    executeResponse(api.getIfoodmacauVerify(token,verificationCode,goodsId,count,ordersId))
            fun printResult(){
                launch {
                    val result =getIfoodmacauVerify(orderItem.ordersId,orderItem.goodsId,1,verificationCode)
                    when (result){
                        is Result.Success -> ToastUtil.success(context,result.datas.toString())
                        is Result.DataError -> SLog.info(result.toString())
                        is Result.Error -> SLog.info("加載核銷數據異常")
                    }
                }

            }
        }
        api.printResult()
    }

    override fun onDismiss() {
        super.onDismiss()
        cancel()
    }
}
