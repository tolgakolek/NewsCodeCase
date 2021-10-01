package com.humaturk.newscodecase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class Article(
    @PrimaryKey
    var id: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val content: String?,
    val publishedAt: String,
    var readListStatus: Boolean = false
)
