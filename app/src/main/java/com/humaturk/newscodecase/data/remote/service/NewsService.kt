package com.humaturk.newscodecase.data.remote.service

import com.humaturk.newscodecase.data.model.ResponseArticle
import com.humaturk.newscodecase.data.model.ResponseNewsSource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsService {
    @Headers("Authorization: f04fd93154fa41309aae97a5450f5af3")
    @GET("/v2/sources")
    suspend fun getAllNewsSource(): Response<ResponseNewsSource>

    @Headers("Authorization: f04fd93154fa41309aae97a5450f5af3")
    @GET("/v2/top-headlines")
    suspend fun getAllArticle(
        @Query("sources") source_id: String
    ): Response<ResponseArticle>
}