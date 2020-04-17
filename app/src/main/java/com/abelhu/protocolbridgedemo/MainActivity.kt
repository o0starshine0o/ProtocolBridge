package com.abelhu.protocolbridgedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.abelhu.protocol.JsProtocol
import com.abelhu.temp.JsProtocolProxy

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        // 模拟在shouldOverrideUrlLoading中调用，其中传过来的值为"js_open_url"
        JsProtocolProxy.Instance.getProtocol("js_open_url", "mock data")?.run()
    }
}
