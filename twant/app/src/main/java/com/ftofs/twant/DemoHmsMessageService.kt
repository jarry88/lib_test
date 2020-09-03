package com.ftofs.twant

import com.ftofs.twant.log.SLog
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class DemoHmsMessageService: HmsMessageService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        SLog.info(p0?.data)
    }
}