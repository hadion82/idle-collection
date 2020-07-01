package com.idle.collection

class Mediator<T> {
    lateinit var list: List<T>

    var before: (list: List<T>) -> List<T> = { list }

    var comparator: Comparator<T>? = null

    var after: (list: List<T>) -> Unit = { }

    fun before(method: (list: List<T>) -> List<T>) = apply {
        this.before = method
    }

    fun <R> sort(selector: (T) -> Comparable<R>) = apply {
        this.comparator = compareBy(selector)
    }

    fun after(method: (list: List<T>) -> Unit) = apply {
        after = method
    }
}