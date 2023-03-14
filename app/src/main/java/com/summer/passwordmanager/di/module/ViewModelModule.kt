package com.summer.passwordmanager.di.module

import com.summer.passwordmanager.ui.screens.main.viewmodels.PassGeneratorViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::PassGeneratorViewModel)
}
