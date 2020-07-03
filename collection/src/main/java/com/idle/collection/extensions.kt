package com.idle.collection

import android.text.Editable
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

fun Editable?.toSafeLong(defValue: Long = 0): Long =
    if (this.isNullOrBlank() || !this.isDigitsOnly())
        defValue
    else this.toString().toLong()

fun <T, S> LiveData<T>.relateMap(
    switchFunction: (T) -> LiveData<S?>,
    observer: (S?) -> Unit
) = RelativeLiveData(this)
    .relateMap(switchFunction, observer)

fun <T> LiveData<T>.collect(owner: LifecycleOwner, collect: (T) -> Unit) {
    this.observe(owner, Observer { collect(it) })
}

inline fun <T, reified VH : RecyclerView.ViewHolder> LiveData<List<T>>.sync(
    owner: LifecycleOwner,
    recyclerView: RecyclerView,
    adapter: ListAdapter<T, VH>
): Submitter<T> {
    if (recyclerView.adapter == null)
        recyclerView.adapter = adapter
    return this.sync(owner, adapter)
}

inline fun <T, reified VH : RecyclerView.ViewHolder> LiveData<List<T>>.sync(
    owner: LifecycleOwner,
    adapter: ListAdapter<T, VH>
): Submitter<T> {
    val submitter = Submitter<T>()
    this.observe(owner, Observer {
        submitter.list = it
        var list = submitter.beforeSubmission(it)
        list = submitter.comparator?.run {
            list.sortedWith(this)
        } ?: list
        adapter.submitList(list) {
            submitter.afterSubmission(list)
        }
    })
    return submitter
}