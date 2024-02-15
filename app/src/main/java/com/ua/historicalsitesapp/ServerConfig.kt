package com.ua.historicalsitesapp

object ServerConfig {
    // server on AWS
    private const val AWS_SERVER_URL = "https://api.uahistoricalsites.com/api"

    // if you want to run the server on your computer
    private const val TEST_SERVER_URL = "http://10.0.2.2:8080/api"

    // use to set the server url
    const val SERVER_URL = AWS_SERVER_URL
}

