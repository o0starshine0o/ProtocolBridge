package com.abelhu.utils

import com.abelhu.protocalbridge.Protocol
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName


fun Element.asSafeType(): TypeName {
    val className = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(this.asType().asTypeName().toString()))?.asSingleFqName()?.asString()
    return if (className == null) asType().asTypeName() else ClassName.bestGuess(className)
}

fun Element.protocolName():String{
    val name = getAnnotation(Protocol::class.java).protocol
    return if (name.isEmpty()) simpleName.toString() else name
}