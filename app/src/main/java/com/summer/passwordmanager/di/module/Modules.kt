package com.summer.passwordmanager.di.module

import android.app.Application
import androidx.room.Room
import com.summer.passwordmanager.database.AppDatabase
import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.repository.AppRepository
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.viewmodels.PassGeneratorViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "pass_generator_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(database: AppDatabase): AppDao {
        return database.appDao()
    }
    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}

val repositoryModule = module {
    fun provideRepository(dao: AppDao): Repository {
        return AppRepository(dao)
    }
    single { provideRepository(get()) }
}

val viewModelModule = module {
    viewModel {
        PassGeneratorViewModel(repository = get())
    }
}