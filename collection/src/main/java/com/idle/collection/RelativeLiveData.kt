package com.idle.collection

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class RelativeLiveData<T>(
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