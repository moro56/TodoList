package it.emperor.todolist.di.module

import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import it.emperor.todolist.data.repository.TodoRepository

@Module
class DataModule {

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
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

    @Provides
    fun provideTodoRepository(realm: Realm): TodoRepository {
        return TodoRepository(realm)
    }
}