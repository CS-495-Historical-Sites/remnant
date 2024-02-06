package com.ua.historicalsitesapp.data

object LoginRepositoryProvider {
    private var loginRepository: LoginRepository? = null

    fun provideLoginRepository(dataSource: LoginDataSource): LoginRepository {
        synchronized(this) {
            if (loginRepository == null) {
                loginRepository = LoginRepository(dataSource)
            }
            return loginRepository!!
        }
    }
}