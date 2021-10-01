package com.humaturk.newscodecase.ui.article

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humaturk.newscodecase.data.local.ArticleDao
import com.humaturk.newscodecase.data.model.Article
import com.humaturk.newscodecase.data.remote.source.NewsRemoteDataSource
import com.humaturk.newscodecase.data.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val articleDao: ArticleDao
) : ViewModel() {
    private val _viewState = MutableStateFlow(initialCreateViewState())
    val viewState = _viewState.asStateFlow()
    var daoArticleData: List<Article>? = null
    private var job: Job? = null

    init {
        toggleTime()
        getDaoArticle()
    }

    private fun getDaoArticle() {
        articleDao.getAllArticle().observeForever(Observer {
            daoArticleData = it
        })
    }

    private fun getArticle(id: String) {
        viewModelScope.launch {
            newsRemoteDataSource.getAllArticle(id).collect {
                when (it) {
                    is DataState.Success -> {
                        val articleList = daoArticleData?.plus(it.data.articles)?.distinctBy {
                            it.title
                        }
                        articleList?.let {
                            _viewState.value =
                                _viewState.value.copy(articles = it, articleId = id)
                        }
                    }
                    is DataState.Error -> {
                        val error = it.message
                    }
                }
            }
        }
    }

    fun addArticleDao(article: Article) {
        viewModelScope.launch {
            articleDao.insert(article)
        }
    }

    fun deleteArticleDao(articleId: String) {
        viewModelScope.launch {
            articleDao.delete(articleId)
        }
    }

    fun start(id: String) {
        getArticle(id)
    }

    private fun initTimer(totalSeconds: Int = _viewState.value.totalSeconds.toInt()): Flow<ArticleState> =
        (totalSeconds - 1 downTo 0).asFlow()
            .onEach { delay(1000) }
            .onStart { emit(totalSeconds) }
            .conflate()
            .transform { remainingSeconds: Int ->
                emit(ArticleState(remainingSeconds))
            }

    private fun toggleTime() {
        job = if (job == null) {
            viewModelScope.launch {
                initTimer()
                    .collect {
                        _viewState.emit(
                            _viewState.value.copy(
                                secondsRemaining = it.secondsRemaining
                            )
                        )
                        if (it.secondsRemaining == 0) {
                            getArticle(viewState.value.articleId)
                            job = null
                            toggleTime()
                            _viewState.value =
                                _viewState.value.copy(newArticalCheck = true)
                        }
                    }
            }
        } else {
            job?.cancel()
            null
        }
    }

    fun newArticleCheckNot() {
        _viewState.value =
            _viewState.value.copy(newArticalCheck = false)
    }

    private fun initialCreateViewState() = ArticleState(
        totalSeconds = 60,
        secondsRemaining = 60,
        articles = emptyList(),
        articleId = "",
        newArticalCheck = false
    )
}

data class ArticleState(
    val secondsRemaining: Int = 60,
    val totalSeconds: Int = 60,
    val articles: List<Article> = emptyList(),
    val articleId: String = "",
    val newArticalCheck: Boolean = false
)