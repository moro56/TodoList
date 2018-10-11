package it.emperor.todolist.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    init {
        val realmConfig = RealmConfiguration.Builder()
                .name("database.realm")
                .schemaVersion(1)
                .migration { realm, oldVersion, newVersion -> }
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }
}