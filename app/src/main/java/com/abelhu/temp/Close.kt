package com.abelhu.temp

import com.abelhu.protocol.JsProtocol

class Close: JsProtocol.Stub {
    override fun run() {
        JsProtocol.close()
    }
}