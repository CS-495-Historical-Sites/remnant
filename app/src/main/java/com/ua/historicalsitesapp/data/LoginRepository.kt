package com.ua.historicalsitesapp.data

import com.ua.historicalsitesapp.data.model.LoggedInUser
import com.ua.historicalsitesapp.data.model.LoginDetails
import com.ua.historicalsitesapp.data.model.RegistrationDetails
import com.ua.historicalsitesapp.data.model.RegistrationResult

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(userDetails: LoginDetails): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(userDetails)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    suspend fun register(userDetails: RegistrationDetails): RegistrationResult {
        return dataSource.register(userDetails)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}