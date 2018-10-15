package it.emperor.todolist.ui.todo

import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import it.emperor.todolist.R
import it.emperor.todolist.databinding.TodoCreateActivityBinding
import it.emperor.todolist.extension.closeKeyboard
import it.emperor.todolist.extension.errorAlert
import it.emperor.todolist.state.Status
import it.emperor.todolist.ui.base.BaseActivity
import kotlinx.android.synthetic.main.todo_create_activity.*

class ActivityTodoCreate : BaseActivity<TodoCreateActivityBinding, ViewModelTodoCreate>() {

    override fun layoutRes(): Int {
        return R.layout.todo_create_activity
    }

    override fun viewModelClass(): Class<ViewModelTodoCreate> {
        return ViewModelTodoCreate::class.java
    }

    override fun bind() {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        description.imeOptions = EditorInfo.IME_ACTION_DONE
        description.setRawInputType(InputType.TYPE_CLASS_TEXT)

        container.setOnClickListener { closeKeyboard() }

        viewModel.state.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> finish()
                Status.ERROR -> errorAlert(getString(it.errorMessage?.message!!))
                Status.LOADING -> {
                }
                Status.NONE -> {
                }
            }
        })
    }
}