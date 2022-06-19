package com.juancoob.shoppingcartchallenge.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.juancoob.data.datasource.LocalCurrencyDataSource
import com.juancoob.data.datasource.LocalDormDataSource
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.shoppingcartchallenge.BuildConfig
import com.juancoob.shoppingcartchallenge.data.database.ChallengeDatabase
import com.juancoob.shoppingcartchallenge.data.database.DormDao
import com.juancoob.shoppingcartchallenge.data.database.LocalCurrencyDataSourceImpl
import com.juancoob.shoppingcartchallenge.data.database.LocalDormDataSourceImpl
import com.juancoob.shoppingcartchallenge.data.database.SymbolDao
import com.juancoob.shoppingcartchallenge.data.server.RemoteDataSourceImpl
import com.juancoob.shoppingcartchallenge.data.server.RemoteService
import com.juancoob.shoppingcartchallenge.di.qualifiers.ApiKey
import com.juancoob.shoppingcartchallenge.di.qualifiers.ApiUrl
import com.juancoob.shoppingcartchallenge.di.qualifiers.DormId
import com.juancoob.shoppingcartchallenge.ui.detail.DetailFragmentArgs
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


// Provides

@Module
@InstallIn(SingletonComponent::class)
@OptIn(ExperimentalSerializationApi::class)
object AppModuleProviderForRetrofit {
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
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForApiKey {
    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(): String = BuildConfig.API_LAYER_KEY
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForEndPoint {
    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "https://api.apilayer.com/exchangerates_data/"
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForRoom {
    @Provides
    @Singleton
    fun provideRoomDataBase(app: Application): ChallengeDatabase =
        Room.databaseBuilder(
            app,
            ChallengeDatabase::class.java,
            "challenge-database"
        ).build()
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForSymbolDao {
    @Provides
    @Singleton
    fun provideSymbolDao(challengeDatabase: ChallengeDatabase): SymbolDao =
        challengeDatabase.symbolDao()
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProviderForDormDao {
    @Provides
    @Singleton
    fun provideSymbolDao(challengeDatabase: ChallengeDatabase): DormDao =
        challengeDatabase.dormDao()
}

@Module
@InstallIn(ViewModelComponent::class)
object AppModuleProviderForDormId {
    @Provides
    @ViewModelScoped
    @DormId
    fun provideDormId(savedStateHandle: SavedStateHandle) =
        DetailFragmentArgs.fromSavedStateHandle(savedStateHandle).dormId
}


// Binds

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinderForRemoteDataSource {
    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinderForLocalCurrencyDataSource {
    @Binds
    abstract fun bindLocalCurrencyDataSource(
        localCurrencyDataSourceImpl: LocalCurrencyDataSourceImpl
    ): LocalCurrencyDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinderForLocalDormDataSource {
    @Binds
    abstract fun bindLocalDormDataSource(
        localDormDataSourceImpl: LocalDormDataSourceImpl
    ): LocalDormDataSource
}
