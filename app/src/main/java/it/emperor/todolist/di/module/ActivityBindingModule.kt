package it.emperor.todolist.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import it.emperor.todolist.ui.main.ActivityHome
import it.emperor.todolist.ui.todo.ActivityTodoCreate

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [])
    abstract fun bindHomeActivity(): ActivityHome

    @ContributesAndroidInjector(modules = [])
    abstract fun bindTodoCreateActivity(): ActivityTodoCreate
}