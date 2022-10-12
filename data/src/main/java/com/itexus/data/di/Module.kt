package com.itexus.data.di

import com.itexus.data.BuildConfig
import com.google.gson.GsonBuilder
import com.itexus.data.di.util.provideApi
import com.itexus.data.local.CurrenciesDao
import com.itexus.data.local.entity.CurrencyEntity
import com.itexus.data.remote.CurrenciesApi
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import okhttp3.logging.HttpLoggingInterceptor

private val persistence = module {
    val realmVersion = 1L
    single {
        val schema = setOf(
            CurrencyEntity::class,
        )
        val config = RealmConfiguration.Builder(schema)
            .schemaVersion(realmVersion)
            .build()
        Realm.open(config)
    }

    factory {
        CurrenciesDao(realm = get())
    }
}

private const val TIMEOUT = 30L
private val network = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single {
        val gson = GsonBuilder().create()
        Retrofit.Builder()
            .baseUrl(BuildConfig.API)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    single<CurrenciesApi> {
        provideApi(retrofit = get())
    }

    factory {
        val logger = HttpLoggingInterceptor.Logger { message -> Timber.d("OkHttp -> $message") }
        HttpLoggingInterceptor(logger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}

val dataModule = persistence + network
