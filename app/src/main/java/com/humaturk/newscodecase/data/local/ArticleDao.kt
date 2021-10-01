package com.humaturk.newscodecase.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.humaturk.newscodecase.data.model.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getAllArticle() : LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Query("DELETE FROM article WHERE id = :articleId")
    suspend fun delete(articleId: String)
}