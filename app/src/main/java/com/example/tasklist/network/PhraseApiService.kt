package com.example.tasklist.network

import retrofit2.Response
import retrofit2.http.GET

interface PhraseApiService {
    @GET("api/phrase")
    suspend fun getPhrase(): Response<PhraseResponse>
}

