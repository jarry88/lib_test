package com.ftofs.twant.widget

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.ftofs.twant.R
import com.ftofs.twant.databinding.PopupCancelAfterVerficationListBinding
import com.ftofs.twant.databinding.VerificationGoodsItemBinding
import com.ftofs.twant.entity.OrderItem
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.lxj.xpopup.core.CenterPopupView

class TestCenterPopup(context: Context):CenterPopupView(context), View.OnClickListener{

    private  var orderList: MutableList<OrdersGoodsVo>?=null
    private  var order: OrderItem?=null
    private val net by lazy {
        object : BaseRepository(){}
    }
    private  val adapter by lazy {
        object :DataBoundAdapter<OrdersGoodsVo,VerificationGoodsItemBinding>(){
            override fun initView(binding: VerificationGoodsItemBinding, item: OrdersGoodsVo) {
                binding.vo = item

            }

            override val layoutId =R.layout.verification_goods_item

        }
    }

    override fun onCreate() {
        super.onCreate()
//        val binding = DataBindingUtil.bind<PopupCancelAfterVerficationListBinding>(this)
//
        val rvList=findViewById<RecyclerView>(R.id.rv_verification_list)
        val binding =PopupCancelAfterVerficationListBinding.bind(this)
        binding.rvVerificationList.adapter=adapter
//        orderList = ArrayList<OrdersGoodsVo>()
//        orderList?.add(OrdersGoodsVo())
//        adapter.addAll(orderList as ArrayList<OrdersGoodsVo>,true)
        val fllayout=findViewById<FrameLayout>(R.id.fl_my_container)
        rvList.visibility=View.GONE
        fllayout.visibility=View.VISIBLE
//        vg.addView(object :View(context){
//            var mDrawable: Drawable? = null
//
//            constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
//                val typedArray = context!!.theme.obtainStyledAttributes(attrs,
//                        R.styleable.MyCustomView, 0, 0)
//                mDrawable = typedArray.getDrawable(R.styleable.MyCustomView_img)
//                typedArray.recycle()
//            }
//
//            override fun onDraw(canvas: Canvas?) {
//                mDrawable?.run {
//                    setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
//                    draw(canvas!!)
//                }
//            }
//        })

    }


    override fun getImplLayoutId(): Int {
        return R.layout.popup_cancel_after_verfication_list
    }

    override fun onClick(v: View?) {
//        TODO("Not yet implemented")
    }
}