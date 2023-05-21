package com.summer.passwordmanager.di.module

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.summer.passwordmanager.database.AppDatabase
import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.database.preferences.Preference
import com.summer.passwordmanager.repository.AppRepository
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.viewmodels.CreateVaultViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.PassGeneratorViewModel
import com.summer.passwordmanager.ui.screens.signup.viewmodels.SignUpViewModel
import com.summer.passwordmanager.ui.screens.splashscreen.viewmodels.SplashScreenViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "pass_generator_db")
            .fallbackToDestructiveMigration()
            .createFromAsset("db/init.db")
            .build()
    }

    fun provideSharedPreference(application: Application): SharedPreferences {
        return Preference.createdEncryptedPreference(application)
    }

    fun provideDao(database: AppDatabase): AppDao {
        return database.appDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { provideSharedPreference(androidApplication()) }
}

val repositoryModule = module {
    fun provideRepository(dao: AppDao, sharedPreferences: SharedPreferences): Repository {
        return AppRepository(dao, sharedPreferences)
    }
    single { provideRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel {
        SplashScreenViewModel(repository = get())
    }
    viewModel {
        SignUpViewModel(repository = get())
    }
    viewModel {
        PassGeneratorViewModel(repository = get())
    }
    viewModel {
        CreateVaultViewModel(repository = get())
    }
}