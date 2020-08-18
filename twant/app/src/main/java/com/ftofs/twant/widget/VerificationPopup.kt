package com.ftofs.twant.widget

import android.content.Context
import android.widget.TextView
import com.ftofs.twant.R
import com.lxj.xpopup.core.CenterPopupView

class VerificationPopup(context: Context) :CenterPopupView(context){
    override fun getImplLayoutId(): Int {
        return R.layout.popup_cancel_after_verfication
    }

    override fun onCreate() {
        super.onCreate()
        val btnOk=findViewById<TextView>(R.id.btn_ok)
        btnOk.setOnClickListener{
            dismiss()
        }
    }
}
