package com.humaturk.newscodecase.ui.source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humaturk.newscodecase.data.model.NewsSource
import com.humaturk.newscodecase.data.remote.source.NewsRemoteDataSource
import com.humaturk.newscodecase.data.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsSourceViewModel @Inject constructor(private val newsRemoteDataSource: NewsRemoteDataSource) :
    ViewModel() {
    private val _viewState = MutableStateFlow(initialCreateViewState())
    val viewState = _viewState.asStateFlow()

    init {
        getNewsSource()
    }

    private fun getNewsSource() {
        viewModelScope.launch {
            newsRemoteDataSource.getAllNewsSource().collect {
                when (it) {
                    is DataState.Success -> {
                        _viewState.value =
                            _viewState.value.copy(newsSource = it.data.sources)
                    }
                    is DataState.Error -> {
                        val error = it.message
                    }
                }
            }
        }
    }

    private fun initialCreateViewState() = NewsSourceState(
        totalSeconds = 6000,
        secondsRemaining = 6000,
        newsSource = emptyList()
    )
}

data class NewsSourceState(
    val secondsRemaining: Long = 20,
    val totalSeconds: Long = 20,
    val newsSource: List<NewsSource> = emptyList()
)