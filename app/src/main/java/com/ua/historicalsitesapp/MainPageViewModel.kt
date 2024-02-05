package com.ua.historicalsitesapp

import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.LoginDataSource
import com.ua.historicalsitesapp.data.LoginRepository

class MainPageViewModel() : ViewModel() {
    private val loginRepository = LoginRepository(LoginDataSource())

}
