package com.humaturk.newscodecase.data.local

import android.content.Context
import androidx.room.*
import com.humaturk.newscodecase.data.model.Article

@Database(entities = [Article::class],version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "article")
                .fallbackToDestructiveMigration()
                .build()
    }
}