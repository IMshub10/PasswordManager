package com.summer.passwordmanager.ui.screens.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.databinding.ActivitySplashScreenBinding
import com.summer.passwordmanager.ui.screens.main.MainActivity
import com.summer.passwordmanager.ui.screens.signup.SignUpActivity
import com.summer.passwordmanager.ui.screens.splashscreen.viewmodels.SplashScreenViewModel
import com.summer.passwordmanager.utils.LauncherUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity
    : BaseActivity<ActivitySplashScreenBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_splash_screen

    private val viewModel: SplashScreenViewModel by viewModel()

    override fun onActivityReady(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val checkAccExists = viewModel.checkIfAccountExists()
            withContext(Dispatchers.Main) {
                LauncherUtils.startActivityWithClearTop(
                    this@SplashScreenActivity,
                    if (checkAccExists) MainActivity::class.java else SignUpActivity::class.java
                )
            }
        }
    }
}