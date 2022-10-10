package com.itexus.data.di

import androidx.room.Room
import com.itexus.data.BuildConfig
import com.google.gson.GsonBuilder
import com.itexus.data.di.util.provideApi
import com.itexus.data.local.CurrenciesDatabase
import com.itexus.data.remote.CurrenciesApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext

private val local = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CurrenciesDatabase::class.java, "currencies-db"
        ).build().currenciesDao()
    }
}

private val network = module {
    single {
        val gson = GsonBuilder().create()
        Retrofit.Builder()
            .baseUrl(BuildConfig.API)
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

val dataModule = local + network
