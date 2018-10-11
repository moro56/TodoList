package it.emperor.todolist.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import it.emperor.todolist.data.converter.RealmChangeSetResults
import it.emperor.todolist.data.model.Todo
import it.emperor.todolist.data.repository.TodoRepository
import it.emperor.todolist.state.ViewState
import it.emperor.todolist.utils.NavigationEvent
import javax.inject.Inject


class ViewModelHome @Inject constructor(private val todoRepository: TodoRepository) : AndroidViewModel(Application()) {

    private val _navigateTo = MutableLiveData<NavigationEvent<Int>>()
    private var _data: LiveData<ViewState<RealmChangeSetResults<Todo>>> = MutableLiveData()

    init {
        loadData()
    }

    private fun loadData() {
        val todos = todoRepository.getLiveTodos()
        _data = Transformations.map(todos) {
            ViewState.success(it)
        }
    }

    fun todoList(): LiveData<RealmChangeSetResults<Todo>> = Transformations.map(_data) {
        _data.value?.data
    }

    val navigateTo: LiveData<NavigationEvent<Int>>
        get() = _navigateTo

    fun updateTodo(todoId: Long?, checked: Boolean) {
        todoId?.let {
            todoRepository.updateTodoCheck(it, checked)
        }
    }

    fun openTodoCreation() {
        _navigateTo.value = NavigationEvent(ACTIVITY_TODO_CREATION)
    }

    companion object {
        const val ACTIVITY_TODO_CREATION: Int = 100
    }
}