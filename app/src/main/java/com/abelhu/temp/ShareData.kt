package com.abelhu.temp

import android.webkit.WebView
import com.abelhu.bean.JsData
import com.abelhu.protocol.JsProtocol
import com.google.gson.Gson

/**
 * 根据JsProtocol.openUrl自动创建
 *
 * 只能接受string类型的数据，然后根据自己的需要在转成对应的数据结构
 */
class ShareData(private val string: String, private val webView: WebView) : JsProtocol.Stub {
    override fun run() {
        val data = Gson().fromJson(string, JsData::class.java)
        JsProtocol.shareData(data, webView)
    }
}