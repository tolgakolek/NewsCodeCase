package com.humaturk.newscodecase.di


import android.content.Context
import com.humaturk.newscodecase.data.local.AppDatabase
import com.humaturk.newscodecase.data.remote.service.NewsService
import com.humaturk.newscodecase.data.remote.source.NewsRemoteDataSource
import com.humaturk.newscodecase.data.remote.source.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModul {
    private const val BASE_URL =
            "https://newsapi.org"

    @Provides
    @Singleton
    fun provideRetrofit(
            okHttpClient: OkHttpClient,
            moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(moshiConverterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }

    @Provides
    @Singleton
    fun provideNewsService(retrofit: Retrofit): NewsService =
            retrofit.create(NewsService::class.java)

    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(newsService: NewsService): NewsRemoteDataSource =
            NewsRemoteDataSourceImpl(newsService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideArticleDao(db: AppDatabase) = db.articleDao()
}