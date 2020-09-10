package com.ftofs.twant.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import com.ftofs.twant.api.Api
import com.ftofs.twant.api.UICallback
import com.ftofs.twant.kotlin.bean.TwantResponse
import com.ftofs.twant.util.Util
import com.wzq.mvvmsmart.net.net_utils.GsonUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.ArrayList


@SuppressLint("ViewConstructor")
class VerificationPopup(context: Context, val orderItem: OrdersGoodsVo,var count: Int =1) :CenterPopupView(context), CoroutineScope by MainScope(){
    override fun getImplLayoutId(): Int {
        return R.layout.popup_cancel_after_verfication
    }
    val res by lazy {
        object :BaseRepository(){}
    }
    private val editText by lazy {
        findViewById<EditText>(R.id.et_verification)
    }
    override fun onCreate() {
        super.onCreate()
        val btnOk=findViewById<TextView>(R.id.btn_ok)
        btnOk.setOnClickListener{
            goVerify()
//            res.run {
//                launch {
//                   val  result=simpleGet(api.testPost1(User.getToken()))
//                }
//            }
        }
    }
    private fun goVerify(){
        val token =User.getToken()
        val verification= editText.text.toString()
        val api=object :BaseRepository(){
            suspend fun getIfoodmacauVerify(ordersId: Int, goodsId: Int, count: Int, verificationCode: String?): Result<ZoneInfo> {
                return safeApiCall(call = { executeResponse(api.getIfoodmacauVerify( token,verificationCode,  ordersId,goodsId,count))})

            }
        fun printResult(){
            launch {
                SLog.info("token $token ordersId ${orderItem.ordersId},goodsId${orderItem.goodsId},count$count,verification$verification")

                when (val result =getIfoodmacauVerify(orderItem.ordersId,orderItem.goodsId,count,verification)){
                    is Result.Success ->ToastUtil.success(context,result.datas.message)

                    is Result.DataError -> {//val error= EasyJSONObject.parse<String>()
//                            SLog.info(error.toString())
                        SLog.info(result.datas.error)
                        ToastUtil.error(context,result.datas.error)
                    }
                    is Result.Error -> ToastUtil.error(context,"核销失败")
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
