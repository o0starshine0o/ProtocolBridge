//package com.abelhu.protocol
//
//import android.util.Log
//import com.abelhu.protocalbridge.Protocol
//
///**
// * 应用Protocol注解，参数为协议名称，之后（demo为MainActivity）根据协议名称创建实例
// * 一定要实现JsProtocol接口
// * 可以不继承ProtocolBase，ProtocolBase类只是为了方便json转bean
// */
//@Protocol("js_close")
//class Close : JsProtocol {
//    override fun dealProtocol() {
//        Log.i("Close", "close")
//    }
//}