package it.emperor.todolist.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TodoItem : RealmObject() {

    @PrimaryKey
    var id: Long = 0

    var title: String = ""
    var description: String? = null
    var done: Boolean = false

    var createdAt: Long = 0
    var updatedAt: Long = 0

    companion object {
        const val ID: String = "id"
        const val CREATED_AT: String = "createdAt"
        const val UPDATED_AT: String = "updatedAt"
    }
}