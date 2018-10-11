package it.emperor.todolist.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import it.emperor.todolist.App
import it.emperor.todolist.di.module.ActivityBindingModule
import it.emperor.todolist.di.module.AppModule
import it.emperor.todolist.di.module.DataModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AndroidSupportInjectionModule::class, ActivityBindingModule::class, DataModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }
}