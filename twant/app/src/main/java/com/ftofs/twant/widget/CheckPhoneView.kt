package com.ftofs.twant.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.set
import androidx.core.widget.doAfterTextChanged
import com.ftofs.twant.R
import com.ftofs.twant.entity.MobileZone
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.StringUtil
import java.util.regex.Pattern

const val text_invalid_mobile="你輸入的%s手機號碼有誤，請重新輸入"
const val errorValidTip="此號碼無法用於進行註冊或登入"
const val LandIndex=2//内地
val regex= listOf(
        "",
        "^[569][0-9]{0,7}$", // 香港
        "^1[0-9]{0,10}$",    // 大陸
        "^6[0-9]{0,7}$"   // 澳門
)
val isValidRegex ="^1(([7][0,1])|([6][2,5,7])|[3][4][9])[0-9]{0,10}$"
//3、虛擬運營商號段：
//
//電信：1700、1701、1702、162
//
//移動：1703、1705、1706 、165
//聯通：1704、1707、1708、1709、167、171
//衛星通訊：1349
/**
 * 1、在xml、fragment中声明并绑定控件
 * 2、调用接口获取 mobilelist 数据 赋值给控件
 * 3、onselect时传递序号进来
 * 4、调取checkerror检查状态
 * 5、返回true时 取值使用
 * 6、返回false时 查看msg
 */
@SuppressLint("CustomViewStyleable")
class CheckPhoneView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var mobileList= listOf<MobileZone>()
    private val mContext:Context=context
    private val areaArray = arrayOf(
            "",
            mContext.getString(R.string.text_hongkong),
            mContext.getString(R.string.text_mainland),
            mContext.getString(R.string.text_macao)
    )

    private var zoneIndex:Int=0
    val etMobile:EditText by lazy {
        rootView.findViewById<EditText>(R.id.et_mobile)
    }
    private var errorText:TextView?=null
    private var llErrorContainer:LinearLayout?=null
    var isError = false
    var msg = context.getString(R.string.input_mobile_hint)


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingCheckPhoneView)
        initView()
        typedArray.recycle()
    }

    private fun initView() {
        View.inflate(context,R.layout.wdget_check_phone,this)
        errorText=rootView.findViewById(R.id.tv_mobile_error)
        llErrorContainer=rootView.findViewById(R.id.ll_container_mobile_error)
        initTextChangedListener()
    }
    fun setZoneIndex(index:Int){
        if( mobileList.size<=index)return
        zoneIndex=mobileList[index].areaId
        etMobile.text=etMobile.text
    }
    fun checkError():Pair<Boolean,String>{
        return Pair(isError,msg)
    }
    fun getPhone():String{
        etMobile.text?:return ""
        return etMobile.text.toString()
    }
    fun setPhone(phone:String?){
        etMobile.setText(phone)
    }
    private fun initTextChangedListener(){
        etMobile.doAfterTextChanged { text->
            SLog.info(regex[zoneIndex]+areaArray[zoneIndex]+text)

            if(text==null||text.isEmpty()){
                llErrorContainer?.visibility= View.GONE
                isError=false
                msg=context.getString(R.string.input_mobile_hint)

                return@doAfterTextChanged
            }else if(mobileList==null||mobileList.size<zoneIndex){
                llErrorContainer?.visibility= View.GONE
                return@doAfterTextChanged
            }else{
                isError=!Pattern.compile(regex[zoneIndex]).matcher(text).matches()
                if (isError) {
                    msg = text_invalid_mobile.format(areaArray[zoneIndex])
                }else if(zoneIndex==LandIndex){
                    isError= Pattern.compile(isValidRegex).matcher(text).matches()
                    if(isError){
                        msg= errorValidTip
                    }
                }

                if (isError) {
                    errorText?.text=msg
                    llErrorContainer?.visibility = View.VISIBLE
                } else {
                    llErrorContainer?.visibility = View.GONE
                    isError=true
                }
             }

        }
    }

}