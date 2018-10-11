package it.emperor.todolist.data.dao

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import it.emperor.todolist.data.model.Todo

class TodoDao(val realm: Realm) {

    fun getTodos(): RealmResults<Todo> {
        return realm.where(Todo::class.java).sort(Todo.CREATED_AT, Sort.DESCENDING).findAllAsync()
    }

    fun getTodo(id: Long): Todo? {
        return realm.where(Todo::class.java).equalTo(Todo.ID, id).findFirst()
    }

    fun updateTodo(todo: Todo, config: (todo: Todo) -> Unit) {
        realm.executeTransaction {
            config(todo)
        }
    }

    fun insertTodo(todo: Todo): Todo? {
        var newTodo: Todo? = null
        realm.executeTransaction {
            newTodo = realm.copyToRealm(todo)
        }
        return newTodo
    }
}