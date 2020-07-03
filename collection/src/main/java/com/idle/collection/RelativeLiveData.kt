package com.idle.collection

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * The RealiveLiveData observes the value of the reference.
 * If there is a change in value, the registered LiveData will be notified of the change.
 * LiveData then creates and returns LiveData to refer to the new value and begins observation.
 */

class RelativeLiveData<T> internal constructor(
    private val reference: LiveData<T>
) : MediatorLiveData<T>() {

    fun <S> relateMap(
        switchFunction: (T) -> LiveData<S?>,
        observer: (S?) -> Unit
    ) = apply {
        val mediator = MediatorLiveData<T>()
        mediator.addSource(reference) { value ->
            var source: LiveData<S?>? = null
            val newLiveData = switchFunction(value)
            source?.run { removeSource(this) }
            source = newLiveData
            mediator.addSource(source, Observer { result ->
                observer(result)
            })
        }
        addSource(mediator) {}
    }

    fun collect(owner: LifecycleOwner, observer: (T) -> Unit) {
        addSource(reference) { setValue(it) }
        observe(owner, Observer { observer(it) })
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        addSource(reference) { setValue(it) }
        super.observe(owner, observer)
    }
}