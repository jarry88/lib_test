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
    fun goVerifyOldType(){
        val token = User.getToken()
        val verification =editText.text?.toString()
        SLog.info("token $token ordersId ${orderItem.ordersId},goodsId${orderItem.goodsId},count$count,verification$verification")

        val params = EasyJSONObject.generate(
                "token", token,
                "ordersId", orderItem.ordersId,
                "goodsId", orderItem.goodsId,
                "verificationCode", verification,
                "count",count)

        SLog.info("params[%s]", params.toString())
        val loadingPopup = Util.createLoadingPopup(context).show()

        Api.postUI(Api.PATH_IFOODMACAU_VERIFY, params, object : UICallback() {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.showNetworkError(context, e)
                loadingPopup.dismiss()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, responseStr: String) {
                loadingPopup.dismiss()
                SLog.info("responseStr[%s]", responseStr)
                val responseObj = EasyJSONObject.parse<EasyJSONObject>(responseStr)
                if (ToastUtil.checkError(context, responseObj)) {
                    dismiss()
                    return
                }
                try {
                    val message= responseObj.getSafeString("datas.message")
                    ToastUtil.success(context,message);
                    dismiss()
                } catch (e: Exception) {
                    SLog.info("Error!message[%s], trace[%s]", e.message, Log.getStackTraceString(e))
                    dismiss()
                }
            }
        })
    }
    fun goVerify(){
        val token =User.getToken()
        val verification= editText.text.toString()
        val api=object :BaseRepository(){
            suspend fun getIfoodmacauVerify(ordersId: Int, goodsId: Int, count: Int, verificationCode: String?): Result<ZoneInfo> {
                return safeApiCall(call = {
                    executeResponse(
                            api.getIfoodtest(
                                RequestBody.create(
                                        MediaType.parse("application/json; charset=utf-8"),
                                        GsonUtil.bean2String(
                                                mapOf(
                                                        "token" to token,
                                                        "ordersId" to ordersId,
                                                        "goodsId" to  goodsId,
                                                        "count" to count,
                                                        "verificationCode" to verificationCode
                                                )
                                        )
                                )
                            )
                    )
                    }
                )
            }


            private suspend fun requestIfoodmacauVerify(ordersId: Int, goodsId: Int, count: Int, verificationCode: String?): Result<ZoneInfo> =
//                    executeResponse(api.getIfoodmacauVerify(token,verificationCode,goodsId,count,ordersId))
                    executeResponse(api.getIfoodtest(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), GsonUtil.bean2String(mapOf(
                            "token" to token,
                            "ordersId" to ordersId,
                            "goodsId" to  goodsId,
                            "count" to count,
                            "verificationCode" to verificationCode

                    )))))
            fun printResult(){
                launch {
                    SLog.info("token $token ordersId ${orderItem.ordersId},goodsId${orderItem.goodsId},count$count,verification$verification")

                    val result =getIfoodmacauVerify(orderItem.ordersId,orderItem.goodsId,count,verification)
                    when (result){
                        is Result.Success -> SLog.info("加載数据完成")
                        is Result.DataError -> {//val error= EasyJSONObject.parse<String>()
//                            SLog.info(error.toString())
                            SLog.info(result.datas.error)
                            ToastUtil.error(context,result.datas.error)
                        }
                        is Result.Error -> SLog.info("加載核銷數據異常")
                    }
                    dismiss()
                }

            }
        }
        api.printResult()
    }
    fun goVerifyTest(){
        val retrofit = Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }

    override fun onDismiss() {
        super.onDismiss()
        cancel()
    }
}
