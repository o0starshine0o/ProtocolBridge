package com.abelhu.protocolbridgedemo

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import com.abelhu.temp.JsProtocolProxy
import com.google.gson.JsonSyntaxException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        // 模拟在shouldOverrideUrlLoading中调用
        JsProtocolProxy.Instance.getProtocol("js_close")?.run()
        // 模拟在shouldOverrideUrlLoading中调用
        JsProtocolProxy.Instance.getProtocol("js_open_url", "{\"js\":\"http://www.qq.com/\"}")?.run()
        // 模拟在shouldOverrideUrlLoading中调用
        // 用在项目里的时候统一这么使用，只要规定好JsProtocol里面的函数的参数必须是以下某一种
        // 1、空参数
        // 2、只有一个用于json转换得到的数据类型的参数
        // 3、params里面的数据类型的参数
        // 4、一个用于json转换得到的数据类型的参数 + params里面的数据类型的参数
        val params = arrayOf(WebView(baseContext))
        val types = params.map { it::class.java as Class<*> }.toTypedArray()
        JsProtocolProxy.Instance.getProtocol("js_share_data", "{\"js\":\"http://www.qq.com/\"}", types, params)?.run()


        // 以下模拟错误的情况：
        try {
            JsProtocolProxy.Instance.getProtocol("js_close", "填写了额外的参数，但是没有声明对应的方法")?.run()
        } catch (e: NoSuchMethodException) {
            Log.e("MainActivity", "填写了额外的参数，但是没有声明对应的方法: $e")
        }
        try {
            JsProtocolProxy.Instance.getProtocol("js_open_url", "json错误")?.run()
        }catch (e: JsonSyntaxException){
            Log.e("MainActivity", "json错误: $e")
        }
        // 这个错误导致进入JsProtocol.openUrl时，data.js为null
        JsProtocolProxy.Instance.getProtocol("js_open_url", "{\"error\":\"json无法正常解析为需要的数据\"}")?.run()
        try {
            JsProtocolProxy.Instance.getProtocol("js_share_data", "参数的数量不足")?.run()
        } catch (e: NoSuchMethodException) {
            Log.e("MainActivity", "参数的数量不足: $e")
        }
        try {
            val errorParams = arrayOf(Fragment())
            val errorTypes = errorParams.map { it::class.java as Class<*> }.toTypedArray()
            JsProtocolProxy.Instance.getProtocol("js_share_data", "{\"js\":\"http://www.qq.com/\"}", errorTypes, errorParams)?.run()
        } catch (e: NoSuchMethodException) {
            Log.e("MainActivity", "固定的参数类型错误: $e")
        }
    }
}
