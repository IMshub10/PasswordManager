package com.summer.passwordmanager.ui.screens.signup.frags

import android.os.Bundle
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragSignUpBinding
import com.summer.passwordmanager.ui.screens.signup.viewmodels.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFrag : BaseFragment<FragSignUpBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_sign_up

    private val viewModel: SignUpViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {

    }
}