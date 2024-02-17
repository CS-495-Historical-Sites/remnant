package com.ua.historicalsitesapp.authUI

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.LoginDataSource
import com.ua.historicalsitesapp.data.LoginRepositoryProvider
import com.ua.historicalsitesapp.data.Result
import com.ua.historicalsitesapp.data.model.LoggedInUser
import com.ua.historicalsitesapp.data.model.LoginDetails
import kotlinx.coroutines.runBlocking


class LoginViewModel(context: Context) : ViewModel() {
    var email = ""
    var password = ""
    private val loginRepository =
        LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

    fun performLogin(): Result<LoggedInUser> {
        var result: Result<LoggedInUser> = Result.Error(Exception(("Unknown Error")))
        runBlocking {
            result = loginRepository.login(LoginDetails(email, password))
        }

        return result
    }

}
