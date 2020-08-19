package com.ftofs.twant.widget

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
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
import cn.snailpad.easyjson.EasyJSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


@SuppressLint("ViewConstructor")
class VerificationPopup(context: Context, val orderItem: OrdersGoodsVo,var count: Int =1) :CenterPopupView(context), CoroutineScope by MainScope(){
    override fun getImplLayoutId(): Int {
        return R.layout.popup_cancel_after_verfication
    }

    val editText by lazy {
        findViewById<EditText>(R.id.et_verification)
    }
    override fun onCreate() {
        super.onCreate()
        val btnOk=findViewById<TextView>(R.id.btn_ok)
        btnOk.setOnClickListener{
            goVerify()
        }
    }
    fun goVerify(){
        val token =User.getToken()
        val verification= editText.text.toString()
        val api=object :BaseRepository(){
            suspend fun getIfoodmacauVerify(ordersId: Int, goodsId: Int, count: Int, verificationCode: String?): Result<Any> {
                return safeApiCall(call = {requestIfoodmacauVerify(ordersId,goodsId,count,verificationCode)})
            }


            private suspend fun requestIfoodmacauVerify(ordersId: Int, goodsId: Int, count: Int, verificationCode: String?): Result<Any> =
                    executeResponse(api.getIfoodmacauVerify(token,verificationCode,goodsId,count,ordersId))
            fun printResult(){
                launch {
                    SLog.info("ordersId ${orderItem.ordersId},goodsId${orderItem.goodsId},count$count,verification$verification")

                    val result =getIfoodmacauVerify(orderItem.ordersId,orderItem.goodsId,count,verification)
                    when (result){
                        is Result.Success -> SLog.info("加載数据完成")
                        is Result.DataError -> {//val error= EasyJSONObject.parse<String>()
//                            SLog.info(error.toString())
                            SLog.info(result.datas.toString())
                            ToastUtil.error(context,"核销失败")
                        }
                        is Result.Error -> SLog.info("加載核銷數據異常")
                    }
                    dismiss()
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
