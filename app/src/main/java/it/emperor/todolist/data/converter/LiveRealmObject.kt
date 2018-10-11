package it.emperor.todolist.data.converter

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmObjectChangeListener

class LiveRealmObject<T : RealmModel> @MainThread constructor(obj: T) : LiveData<T>() {

    private val listener = RealmObjectChangeListener<T> { obj, changeSet ->
        changeSet?.let {
            if (!it.isDeleted) {
                value = obj
            } else {
                value = null
            }
        }
    }

    init {
        if (!RealmObject.isManaged(obj)) {
            throw IllegalArgumentException("LiveRealmObject only supports managed RealmModel instances!")
        }
        if (!RealmObject.isValid(obj)) {
            throw IllegalArgumentException("The provided RealmObject is no longer valid, and therefore cannot be observed for changes.")
        }
    }

    override fun onActive() {
        super.onActive()
        value?.let {
            if (RealmObject.isValid(it)) {
                RealmObject.addChangeListener(it, listener)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        value?.let {
            if (RealmObject.isValid(it)) {
                RealmObject.removeChangeListener(it, listener)
            }
        }
    }
}