package com.itexus.currencyexchanger

import android.app.Application
import com.itexus.data.di.dataModule
import com.itexus.domain.di.domainModule
import com.itexus.logic.presentationModule
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

@FlowPreview
class CurrencyExchangerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@CurrencyExchangerApp)
            modules(dataModule + domainModule + presentationModule)
        }
    }
}
