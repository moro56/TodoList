package it.emperor.todolist.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Todo : RealmObject() {

    @PrimaryKey
    var id: Long = 0

    var title: String = ""
    var description: String? = null
        get() {
            return if (field?.isEmpty() == true) null else field
        }
    var done: Boolean = false
    var todos: RealmList<TodoItem> = RealmList()
    var notes: RealmList<Note> = RealmList()

    var createdAt: Long = Date().time
    var updatedAt: Long = Date().time

    companion object {
        const val ID: String = "id"
        const val CREATED_AT: String = "createdAt"
        const val UPDATED_AT: String = "updatedAt"
    }
}