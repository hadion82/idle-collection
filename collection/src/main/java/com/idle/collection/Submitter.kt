package com.idle.collection

/**
 * This function is for pre and post processing of RecycleView that responds to LiveData.
 * Call 'before' before submitList and 'after' after synchronization is complete.
 */
class Submitter<T> {
    lateinit var list: List<T>

    var beforeSubmission: (list: List<T>) -> List<T> = { list }

    var comparator: Comparator<T>? = null

    var afterSubmission: (list: List<T>) -> Unit = { }

    fun beforeSubmission(method: (list: List<T>) -> List<T>) = apply {
        this.beforeSubmission = method
    }

    fun <R> sortSubmission(selector: (T) -> Comparable<R>) = apply {
        this.comparator = compareBy(selector)
    }

    fun afterSubmission(method: (list: List<T>) -> Unit) = apply {
        afterSubmission = method
    }
}