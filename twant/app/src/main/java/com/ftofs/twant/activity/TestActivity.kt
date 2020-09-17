package com.ftofs.twant.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ftofs.twant.R
import com.ftofs.twant.fragment.LabFragment
import com.gzp.lib_common.base.BaseActivity
import com.umeng.message.UmengNotifyClickActivity

class TestActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)
        loadRootFragment(R.id.container,LabFragment.newInstance())
    }
}