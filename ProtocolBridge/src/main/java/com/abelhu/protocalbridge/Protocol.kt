package com.abelhu.protocalbridge

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
/**
 * 根据{prefix} 生成"${prefix}Protocol"接口
 *
 * 如果注解在Class上，Class必须实现"${prefix}Protocol"接口
 * 如果注解在Function上，生成"${prefix}${protocol}"类，并且实现"${prefix}Protocol"接口
 *
 * @param protocol 协议名称
 * @param prefix 协议前缀
 */
annotation class Protocol(val protocol: String = "", val prefix: String = "js")