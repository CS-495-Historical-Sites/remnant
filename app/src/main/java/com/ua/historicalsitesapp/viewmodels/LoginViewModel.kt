package com.ua.historicalsitesapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource
import com.ua.historicalsitesapp.data.repository.auth.LoginRepositoryProvider


class LoginViewModel(context: Context) : ViewModel() {
    var email = ""
    var password = ""
    private val loginRepository =
        LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)


}
