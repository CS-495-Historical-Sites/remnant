package com.ua.historicalsitesapp.ui.handlers

import android.content.Context
import android.content.Intent
import com.ua.historicalsitesapp.ui.screens.LoginActivity
import com.ua.historicalsitesapp.viewmodels.RemnantUnauthorizedAccessException
import com.ua.historicalsitesapp.viewmodels.UserProfileViewModel

fun <T> withLogoutOnFailure(
    context: Context,
    userProfileViewModel: UserProfileViewModel,
    operation: () -> T,
    onSuccess: (T) -> Unit
) {
  try {
    val result = operation()
    onSuccess(result)
  } catch (e: RemnantUnauthorizedAccessException) {
    userProfileViewModel.logout()

    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
  } catch (e: Exception) {
    throw e
  }
}
