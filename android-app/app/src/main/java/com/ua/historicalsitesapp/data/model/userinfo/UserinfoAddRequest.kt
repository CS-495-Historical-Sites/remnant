package com.ua.historicalsitesapp.data.model.userinfo

import kotlinx.serialization.Serializable

@Serializable data class UserProfileUpdateRequest(var answers: Set<String>?)
