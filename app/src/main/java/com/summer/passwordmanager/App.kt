package com.summer.passwordmanager

import android.app.Application
import android.os.StrictMode
import com.summer.passwordmanager.di.module.databaseModule
import com.summer.passwordmanager.di.module.repositoryModule
import com.summer.passwordmanager.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        enableStrictMode()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                databaseModule,
                repositoryModule,
                viewModelModule
            )
        }
    }

    private fun enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog()
                    .penaltyDeath().build()
            )
        }
    }
}