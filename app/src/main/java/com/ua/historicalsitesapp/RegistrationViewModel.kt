package com.ua.historicalsitesapp


import android.content.Context
import androidx.lifecycle.ViewModel
import com.ua.historicalsitesapp.data.LoginDataSource
import com.ua.historicalsitesapp.data.LoginRepositoryProvider
import com.ua.historicalsitesapp.data.model.RegistrationDetails
import com.ua.historicalsitesapp.data.model.RegistrationResult
import kotlinx.coroutines.runBlocking


class RegistrationViewModel(context: Context) : ViewModel() {
    var username = ""
    var password = ""
    var registrationStatusText = ""

    private val loginRepository =
        LoginRepositoryProvider.provideLoginRepository(LoginDataSource(), context)

    fun performRegistration(): RegistrationResult? {
        var result: RegistrationResult? = null
        runBlocking {
            result = loginRepository.register(RegistrationDetails(username, password))
        }

        return result
    }

    fun isLoggedIn(): Boolean {
        return loginRepository.isLoggedIn
    }


}
