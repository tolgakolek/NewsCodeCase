package com.humaturk.newscodecase.data.remote.source

import com.humaturk.newscodecase.data.model.ResponseArticle
import com.humaturk.newscodecase.data.model.ResponseNewsSource
import com.humaturk.newscodecase.data.util.DataState
import kotlinx.coroutines.flow.Flow

interface NewsRemoteDataSource {
    suspend fun getAllNewsSource() : Flow<DataState<ResponseNewsSource>>
    suspend fun getAllArticle(source_id: String) : Flow<DataState<ResponseArticle>>
}