package com.abelhu.protocol

import android.util.Log
import com.abelhu.bean.JsData
import com.abelhu.bean.ShareBean

//@protocol
object JsProtocol {
    //@protocol
    interface Stub {
        fun run()
    }

    //@protocol("js_open_url")
    fun openUrl(data: JsData) {
        Log.i("JsProtocol", "openUrl:${data.js}")
    }

    /**
     * 模拟更多的参数
     */
    //@protocol("js_share_date")
    fun shareData(data: ShareBean, more: JsData) {
        Log.i("JsProtocol", "openUrl:${data.js}， ${more.js}")
    }

}