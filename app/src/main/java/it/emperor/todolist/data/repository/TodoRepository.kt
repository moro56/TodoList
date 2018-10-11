package it.emperor.todolist.data.repository

import io.realm.Realm
import it.emperor.todolist.data.converter.LiveRealmObject
import it.emperor.todolist.data.converter.LiveRealmResults
import it.emperor.todolist.data.dao.TodoDao
import it.emperor.todolist.data.model.Todo
import it.emperor.todolist.extension.todoDao
import it.emperor.todolist.extension.asLiveData
import javax.inject.Inject

class TodoRepository @Inject constructor(val realm: Realm) {

    private val todoDao: TodoDao = realm.todoDao()

    fun getLiveTodos(): LiveRealmResults<Todo> {
        return todoDao.getTodos().asLiveData()
    }

    fun getLiveTodo(id: Long): LiveRealmObject<Todo>? {
        return todoDao.getTodo(id)?.asLiveData()
    }

    @Synchronized
    fun updateTodoCheck(todoId: Long, checked: Boolean) {
        val todo = todoDao.getTodo(todoId)
        todo?.let { it ->
            todoDao.updateTodo(it) {
                it.done = checked
            }
        }
    }

    fun insertTodo(todo: Todo): Todo? {
       return todoDao.insertTodo(todo)
    }
}