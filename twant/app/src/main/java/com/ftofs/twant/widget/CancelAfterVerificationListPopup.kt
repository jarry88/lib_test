package com.ftofs.twant.widget

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.twant.R
import com.ftofs.twant.api.Api
import com.ftofs.twant.api.UICallback
import com.ftofs.twant.entity.OrderItem
import com.ftofs.twant.kotlin.OrderGoodsVoListAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.interfaces.XPopupCallback
import okhttp3.Call
import java.io.IOException
import java.util.*

class CancelAfterVerificationListPopup(context: Context):CenterPopupView(context){

    private  var orderList: MutableList<OrdersGoodsVo>?=null
    private  var order: OrderItem?=null
    private  val adapter by lazy {
        OrderGoodsVoListAdapter(this).apply {  }
    }

    companion object{
        fun newInstance(context: Context, datas: MutableList<OrdersGoodsVo>):CancelAfterVerificationListPopup {
            val popupView =CancelAfterVerificationListPopup(context)
            popupView.orderList=datas
            return popupView
        }

    }
    constructor(context: Context, datas: MutableList<OrdersGoodsVo>, order:OrderItem):this(context){
        this.order=order
        this.orderList =datas
    }

    override fun onCreate() {
        super.onCreate()

        val rvList=findViewById<RecyclerView>(R.id.rv_verification_list)
        rvList.adapter=adapter
        orderList?.let{
            adapter.addAll(it,true)
        }
    }


    override fun getImplLayoutId(): Int {
        return R.layout.popup_cancel_after_verfication_list
    }
    fun reloadata() {
//        TODO("Not yet implemented")
        val token = User.getToken()

        val params = EasyJSONObject.generate("token", token, "ordersId", order?.orderId)

        SLog.info("params[%s]", params)
        val loadingPopup = Util.createLoadingPopup(context).show()

        Api.postUI(Api.PATH_IFOODMACAU_GOODS_LIST, params, object : UICallback() {
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

                /*
                all: 全部
                new: 待付款
                pay: 待發貨
                finish: 已完成
                send: 待收貨
                noeval: 待評論
                 */try {
                    val ordersGoodsVoList = responseObj.getSafeArray("datas.ordersGoodsVoList")
                    val list: MutableList<OrdersGoodsVo> = ArrayList<OrdersGoodsVo>()
                    for (`object` in ordersGoodsVoList) {
                        list.add(OrdersGoodsVo.parse(`object` as EasyJSONObject))
                    }
                    if (list.isEmpty()) {
                        //todo異常情況處理
//                       ToastUtil.error(context,);
                        dismiss()
                        return
                    }
                    val filterList= list.filter { it.ifoodmacauCount>0 }
                    if (filterList.isNotEmpty()) {
                        onCreate()
                        adapter.addAll(filterList, true)
                        SLog.info("重新加载数据")
                    } else {
                        dismiss()
                        return
                    }
                } catch (e: Exception) {
                    SLog.info("Error!message[%s], trace[%s]", e.message, Log.getStackTraceString(e))
                    dismiss()
                }
            }
        })
    }

    fun showVerificationPopup(item: OrdersGoodsVo, value: Int) {

        XPopup.Builder(context).
        moveUpToKeyboard(false)
                .setPopupCallback(
                        object :XPopupCallback{
                            override fun onDismiss() {
                                reloadata()
                            }
                            override fun onShow() {
                                dismiss()
                            }

                        }
                )
                .asCustom(VerificationPopup(context,item,value))
                .show()
    }
}