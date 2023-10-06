package com.summer.passwordmanager.di.module

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.summer.passwordmanager.database.AppDatabase
import com.summer.passwordmanager.database.DBEncryptor
import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.database.preferences.Preference
import com.summer.passwordmanager.repository.FileRepository
import com.summer.passwordmanager.repository.FileRepositoryImpl
import com.summer.passwordmanager.repository.LocalRepository
import com.summer.passwordmanager.repository.LocalRepositoryImpl
import com.summer.passwordmanager.repository.UserRepository
import com.summer.passwordmanager.repository.UserRepositoryImpl
import com.summer.passwordmanager.ui.dialogs.profile.UserViewModel
import com.summer.passwordmanager.ui.dialogs.tags.CreateTagViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.CreateVaultViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.PassGeneratorViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.ProfileViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.TagViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.VaultViewModel
import com.summer.passwordmanager.ui.screens.pin.viewmodels.PinViewModel
import com.summer.passwordmanager.ui.screens.signup.viewmodels.SetUpPinViewModel
import com.summer.passwordmanager.ui.screens.signup.viewmodels.SignUpViewModel
import com.summer.passwordmanager.ui.screens.splashscreen.viewmodels.SplashScreenViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(
        application: Application,
        sharedPreferences: SharedPreferences
    ): AppDatabase {
        return AppDatabase(context = application, factory = DBEncryptor(sharedPreferences))
    }

    fun provideSharedPreference(application: Application): SharedPreferences {
        return Preference.createdEncryptedPreference(application)
    }

    fun provideDao(database: AppDatabase): AppDao {
        return database.appDao()
    }

    single { provideDao(get()) }
    single { provideSharedPreference(androidApplication()) }
    single { provideDatabase(androidApplication(), provideSharedPreference(androidApplication())) }
}

val repositoryModule = module {
    fun provideFileRepository(): FileRepository = FileRepositoryImpl()

    fun provideLocalRepository(appDao: AppDao): LocalRepository = LocalRepositoryImpl(appDao)

    fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository =
        UserRepositoryImpl(sharedPreferences)

    single { provideFileRepository() }
    single { provideLocalRepository(get()) }
    single { provideUserRepository(get()) }
}

val viewModelModule = module {
    viewModel {
        SplashScreenViewModel(userRepository = get())
    }
    viewModel {
        SignUpViewModel(userRepository = get())
    }
    viewModel {
        PassGeneratorViewModel(localRepository = get())
    }
    viewModel {
        CreateVaultViewModel(localRepository = get())
    }
    viewModel {
        VaultViewModel(localRepository = get())
    }
    viewModel {
        SetUpPinViewModel(userRepository = get())
    }
    viewModel {
        PinViewModel(userRepository = get())
    }
    viewModel {
        ProfileViewModel(userRepository = get(), localRepository = get(), fileRepository = get())
    }
    viewModel {
        TagViewModel(localRepository = get())
    }
    viewModel {
        CreateTagViewModel()
    }
    viewModel {
        UserViewModel(userRepository = get())
    }
}