package com.summer.passwordmanager.ui.screens.main.models

import androidx.databinding.BaseObservable

class PassGeneratorModel(
    var length: Int = 4,
    var hasLowerAlphas: Boolean = true,
    var hasUpperAlphas: Boolean = true,
    var hasNumbers: Boolean = true,
    var hasSpecialCharacters: Boolean = true,
) : BaseObservable()