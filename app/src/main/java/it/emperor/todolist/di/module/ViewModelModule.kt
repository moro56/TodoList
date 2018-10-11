package it.emperor.todolist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import it.emperor.todolist.di.ViewModelFactory
import it.emperor.todolist.di.annotation.ViewModelKey
import it.emperor.todolist.ui.main.ViewModelHome
import it.emperor.todolist.ui.todo.ViewModelTodoCreate

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelHome::class)
    abstract fun bingHomeViewModel(viewModel: ViewModelHome): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelTodoCreate::class)
    abstract fun bingTodoCreateViewModel(viewModel: ViewModelTodoCreate): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}