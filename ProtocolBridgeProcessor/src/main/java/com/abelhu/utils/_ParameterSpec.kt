package com.abelhu.utils

import com.squareup.kotlinpoet.ParameterSpec
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement

fun ParameterSpec.Companion.constructorParametersOf(method: ExecutableElement) = method.parameters.map { param -> constructorGet(param) }

fun ParameterSpec.Companion.constructorGet(element: VariableElement): ParameterSpec {
    val name = element.simpleName.toString()
    val type = element.asSafeType()
    return builder(name, type).build()
}