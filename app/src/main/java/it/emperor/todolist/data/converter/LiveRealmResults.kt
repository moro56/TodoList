package it.emperor.todolist.data.converter

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

class LiveRealmResults<T : RealmModel> @MainThread constructor(val objs: RealmResults<T>) : LiveData<RealmChangeSetResults<T>>() {

    private val listener = OrderedRealmCollectionChangeListener<RealmResults<T>> { objs, changeSet ->
        value = RealmChangeSetResults(objs, changeSet)
    }

    init {
        if (!objs.isValid) {
            throw IllegalArgumentException("The provided RealmResults is no longer valid, the Realm instance it belongs to is closed. It can no longer be observed for changes.")
        }
        if (objs.isLoaded) {
            value = RealmChangeSetResults(objs, null)
        }
    }

    override fun onActive() {
        super.onActive()
        if (objs.isValid) {
            objs.addChangeListener(listener)
            value = RealmChangeSetResults(objs, null)
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (objs.isValid) {
            objs.removeChangeListener(listener)
        }
    }
}