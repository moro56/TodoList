package it.emperor.todolist.data.converter

import io.realm.OrderedCollectionChangeSet
import io.realm.RealmModel
import io.realm.RealmResults

class RealmChangeSetResults<T : RealmModel>(val realmResults: RealmResults<T>, val changeSet: OrderedCollectionChangeSet?)