package it.emperor.todolist.ui.todo

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import it.emperor.todolist.R
import it.emperor.todolist.data.model.Todo
import it.emperor.todolist.data.repository.TodoRepository
import it.emperor.todolist.state.ErrorEvent
import it.emperor.todolist.state.ViewState
import it.emperor.todolist.utils.SingleLiveEvent
import java.util.*
import javax.inject.Inject

class ViewModelTodoCreate @Inject constructor(private val todoRepository: TodoRepository) : AndroidViewModel(Application()) {

    val titleField: ObservableField<String> = ObservableField()
    val descriptionField: ObservableField<String> = ObservableField()

    val state: SingleLiveEvent<ViewState<Todo?>> = SingleLiveEvent()

    private fun validateInput(): Boolean {
        titleField.get()?.let {
            if (it.isNotEmpty()) {
                return true
            }
        }
        state.postValue(ViewState.error(ErrorEvent(R.string.todo_create_error_validation_title)))
        return false
    }

    fun createTodo() {
        if (!validateInput()) return

        try {
            val newTodo = todoRepository.insertTodo(Todo().apply {
                this.id = UUID.randomUUID().leastSignificantBits
                this.title = titleField.get().toString().trim()
                this.description = descriptionField.get()?.trim()
            })
            state.postValue(ViewState.success(newTodo))
        } catch (ex: Exception) {
            ex.printStackTrace()
            state.postValue(ViewState.error(ErrorEvent(R.string.todo_create_error_insert)))
        }
    }
}