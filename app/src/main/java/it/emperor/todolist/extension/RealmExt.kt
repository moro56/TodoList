package it.emperor.todolist.extension

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import it.emperor.todolist.data.converter.LiveRealmObject
import it.emperor.todolist.data.converter.LiveRealmResults
import it.emperor.todolist.data.dao.TodoDao

fun <T : RealmModel> T.asLiveData() = LiveRealmObject(this)

fun <T : RealmModel> RealmResults<T>.asLiveData() = LiveRealmResults(this)

fun Realm.todoDao(): TodoDao = TodoDao(this)