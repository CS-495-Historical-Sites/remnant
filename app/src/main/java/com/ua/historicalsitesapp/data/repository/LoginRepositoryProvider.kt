package com.ua.historicalsitesapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object LoginRepositoryProvider {
    private var loginRepository: LoginRepository? = null

    fun provideLoginRepository(dataSource: LoginDataSource, context: Context): LoginRepository {
        synchronized(this) {
            if (loginRepository == null) {
                loginRepository = LoginRepository(dataSource, context.dataStore)
            }
            return loginRepository!!
        }
    }
}