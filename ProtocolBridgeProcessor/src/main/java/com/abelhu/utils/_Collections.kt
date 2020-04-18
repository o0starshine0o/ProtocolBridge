package com.abelhu.utils

/**
 * Returns a list containing all elements satisfying the given [predicate]
 */
public inline fun <T> Iterable<T>.takeAll(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        if (!predicate(item))
            continue
        list.add(item)
    }
    return list
}

/**
 * Returns one element satisfying the given [predicate]
 */
public inline fun <T> Iterable<T>.takeOnly(predicate: (T) -> Boolean): T? {
    forEach { item -> if (predicate(item)) return item }
    return null
}