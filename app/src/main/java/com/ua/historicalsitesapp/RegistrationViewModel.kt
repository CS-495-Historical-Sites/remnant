package com.ua.historicalsitesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ua.historicalsitesapp.data.LoginDataSource
import com.ua.historicalsitesapp.data.LoginRepository
import com.ua.historicalsitesapp.data.model.RegistrationDetails
import kotlinx.coroutines.launch

class RegistrationViewModel() : ViewModel() {
    var username = ""
    var password = ""
    private val loginRepository = LoginRepository(LoginDataSource())

    fun performLogin() {
        viewModelScope.launch {
            val registrationSuccessful = loginRepository.register(RegistrationDetails(username, password))
        }
    }

}
