package com.abelhu.protocol

import android.util.Log
import android.webkit.WebView
import com.abelhu.bean.JsData

//@protocol
object JsProtocol {
    //@protocol
    interface Stub {
        fun run()
    }

    //@protocol("js_close")
    fun close() {
        Log.i("JsProtocol", "close")
    }

    //@protocol("js_open_url")
    fun openUrl(data: JsData) {
        Log.i("JsProtocol", "openUrl:${data.js}")
    }

    /**
     * 模拟更多的参数
     */
    //@protocol("js_share_date")
    fun shareData(data: JsData, webView: WebView) {
        Log.i("JsProtocol", "shareData[$webView]:${data.js}")
    }

}