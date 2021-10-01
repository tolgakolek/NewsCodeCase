package com.humaturk.newscodecase.data.remote.source

import com.humaturk.newscodecase.data.model.ResponseArticle
import com.humaturk.newscodecase.data.model.ResponseNewsSource
import com.humaturk.newscodecase.data.remote.service.NewsService
import com.humaturk.newscodecase.data.util.DataState
import com.humaturk.newscodecase.data.util.apiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(private val newsService: NewsService) :
    NewsRemoteDataSource {
    override suspend fun getAllNewsSource(): Flow<DataState<ResponseNewsSource>> {
        return apiCall { newsService.getAllNewsSource() }
    }

    override suspend fun getAllArticle(source_id: String): Flow<DataState<ResponseArticle>> {
        return apiCall { newsService.getAllArticle(source_id) }
    }
}