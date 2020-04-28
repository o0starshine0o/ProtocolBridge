package com.abelhu.generate

import com.abelhu.generate.PrefixFileGenerate.Companion.FunctionName
import com.abelhu.protocalbridge.Protocol
import com.abelhu.utils.asSafeType
import com.abelhu.utils.constructorParametersOf
import com.abelhu.utils.protocolName
import com.squareup.kotlinpoet.*
import javax.annotation.processing.Filer
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement

class ClassFileGenerate(private val filer: Filer, private val function: ExecutableElement, file: FileSpec) {
    private val interfaceName = ClassName(file.packageName, file.name)
    private val className = ClassName(file.packageName, function.simpleName.toString().capitalize())

    fun generateFile() = FileSpec.builder(className.packageName, className.simpleName).addType(generateClass()).build().writeTo(filer).let { className }

    private fun generateClass() = TypeSpec.classBuilder(className.simpleName).addSuperinterface(interfaceName)
        .primaryConstructor(generateConstructor())
        .addFunction(generateFunction())
        .addProperties(function.parameters.map { param -> generateProperty(param) })
        .addAnnotation(generateAnnotation())
        .build()

    private fun generateAnnotation() =
        AnnotationSpec.builder(Protocol::class.java).addMember("\"${function.protocolName()}\", \"${getProtocolPrefix()}\"").build()

    private fun generateProperty(param: VariableElement) = PropertySpec.builder(param.toString(), param.asSafeType()).initializer(param.toString()).build()

    private fun generateConstructor() = FunSpec.constructorBuilder().addParameters(ParameterSpec.constructorParametersOf(function)).build()

    private fun generateFunction() = FunSpec.builder(FunctionName).addModifiers(KModifier.OVERRIDE)
        .addStatement("${function.enclosingElement.enclosingElement.simpleName}.${function.simpleName}(${function.parameters})")
        .build()

    private fun getProtocolPrefix() = function.getAnnotation(Protocol::class.java).prefix
}