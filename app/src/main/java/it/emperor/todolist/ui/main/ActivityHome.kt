package it.emperor.todolist.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.emperor.todolist.R
import it.emperor.todolist.databinding.HomeActivityBinding
import it.emperor.todolist.ui.base.BaseActivity
import it.emperor.todolist.ui.main.adapter.RecyclerViewAdapter
import it.emperor.todolist.ui.main.event.TodoCheckedEvent
import it.emperor.todolist.ui.todo.ActivityTodoCreate
import kotlinx.android.synthetic.main.home_activity.*
import org.greenrobot.eventbus.Subscribe

class ActivityHome : BaseActivity<HomeActivityBinding, ViewModelHome>() {

    override fun layoutRes(): Int {
        return R.layout.home_activity
    }

    override fun viewModelClass(): Class<ViewModelHome> {
        return ViewModelHome::class.java
    }

    override fun bind() {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useBus = true

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(this, viewModel.todoList())

        viewModel.navigateTo.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                when (it) {
                    ViewModelHome.ACTIVITY_TODO_CREATION ->
                        startActivity(Intent(this, ActivityTodoCreate::class.java))
                }
            }
        })
    }

    @Subscribe
    fun onEvent(event: TodoCheckedEvent) {
        viewModel.updateTodo(event.todoId, event.checked)
    }
}
