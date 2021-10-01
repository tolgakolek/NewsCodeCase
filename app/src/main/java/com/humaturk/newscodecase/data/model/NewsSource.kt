package com.humaturk.newscodecase.data.model

data class NewsSource(
        val id: String,
        val name: String,
        val description: String,
        val url: String,
        val category: String,
        val language: String,
        val country: String
)
