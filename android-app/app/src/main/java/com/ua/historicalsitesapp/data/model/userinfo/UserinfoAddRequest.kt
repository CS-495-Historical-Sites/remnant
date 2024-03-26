package com.ua.historicalsitesapp.data.model.userinfo
import kotlinx.serialization.Serializable

@Serializable
data class QuestionnaireAnswersAddRequest(
    var answers: Map<String, Set<String>>
)
