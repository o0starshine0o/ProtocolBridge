package com.abelhu.protocol

import android.util.Log
import com.abelhu.protocalbridge.Protocol

class ProtocolFunction {

    companion object {
        @Protocol("no")
        fun no() {
            Log.i("ProtocolFunction", "no params")
        }

        @Protocol("one_params")
        fun one(param: String) {
            Log.i("ProtocolFunction", "one params:$param")
        }

        @Protocol
        fun oneParams(param: String) {
            Log.i("ProtocolFunction", "one params without protocol:$param")
        }
    }
}