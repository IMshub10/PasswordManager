package com.summer.passwordmanager.ui.screens.signup

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.databinding.ActivitySignUpBinding

class SignUpActivity
    : BaseActivity<ActivitySignUpBinding>() {

    private val navController: NavController by lazy { findNavController(R.id.fcv_signUp_container) }

    override val layoutResId: Int
        get() = R.layout.activity_sign_up

    override fun onActivityReady(savedInstanceState: Bundle?) {

    }
}