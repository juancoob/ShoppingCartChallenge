package com.juancoob.shoppingcartchallenge.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.shoppingcartchallenge.BuildConfig
import com.juancoob.shoppingcartchallenge.data.server.RemoteDataSourceImpl
import com.juancoob.shoppingcartchallenge.data.server.RemoteService
import com.juancoob.shoppingcartchallenge.di.qualifiers.ApiKey
import com.juancoob.shoppingcartchallenge.di.qualifiers.ApiUrl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@OptIn(ExperimentalSerializationApi::class)
object ProvidesAppModule {

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(): String = BuildConfig.API_LAYER_KEY

    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "https://api.apilayer.com/exchangerates_data"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @Provides
    @Singleton
    fun provideRemoteService(@ApiUrl apiUrl: String, okHttpClient: OkHttpClient): RemoteService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsAppModule {

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

}
