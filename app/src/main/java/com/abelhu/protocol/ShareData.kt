package com.abelhu.protocol

import android.util.Log
import android.webkit.WebView
import com.abelhu.bean.JsData
import com.abelhu.protocalbridge.Protocol
import com.google.gson.Gson

/**
 * 应用Protocol注解，参数为协议名称，之后（demo为MainActivity）根据协议名称创建实例
 * 一定要实现JsProtocol接口
 * 可以不继承ProtocolBase，ProtocolBase类只是为了方便json转bean
 */
@Protocol("js_share_data")
class ShareData(string: String, private val webView: WebView)  :ProtocolBase<JsData>(string), JsProtocol {
    override fun dealProtocol() {
        Log.i("ShareData", "shareData[$webView]:${data?.js}")
    }
}