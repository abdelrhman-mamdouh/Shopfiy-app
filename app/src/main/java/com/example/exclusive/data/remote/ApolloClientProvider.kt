package com.example.exclusive.data.remote

import com.apollographql.apollo3.ApolloClient
import com.example.exclusive.utilities.Constants.BASE_URL

object ApolloClientProvider {
    val apolloClient: ApolloClient by lazy {
        ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .build()
    }
}
